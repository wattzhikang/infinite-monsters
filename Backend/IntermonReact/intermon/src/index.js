import React from 'react';
import ReactDOM from 'react-dom';
import './index.css';
import * as serviceWorker from './serviceWorker';

var connection = {
    socket: null,
    queue : [],
    game : null,
    instantiate : () => {
        if (connection.isOpen) {
            connection.socket = new WebSocket('ws://cs309-yt-1.misc.iastate.edu:8080/websocket/zw');
            connection.socket.onmessage = function(event) {
                console.log("Message received: " + event.data);
                let object = JSON.parse(event.data);
                if (object.subscriptionID !== undefined 
                    && object.newDungeon !== undefined
                    && object.xL !== undefined
                    && object.xR !== undefined
                    && object.yU !== undefined
                    && object.yL !== undefined
                    && object.tiles !== undefined
                ) {
                    connection.game.receivedDeltaFrame(object);
                }
            };
            connection.socket.onopen = function(event) {
                for (let string of connection.queue) {
                    connection.send(string);
                }
            }
        }
    },
    isOpen : () => {
        if (connection.socket && connection.socket.readyState === 1) {
            return true;
        } else {
            return false;
        }
    },
    send : (string) => {
        if (!connection.isOpen()) {
            connection.queue.push(string);
        } else {
            connection.socket.send(string);
        }
    },
    close : () => {
        connection.socket.close();
    },
}

// var characterImage = new Image();
// characterImage.onload = function() {
//     document
//     .getElementById("board")
//     .getContext("2d")
//     .drawImage(characterImage, 0, 0);
// }
// characterImage.src = process.env.PUBLIC_URL + "/rec/character.svg";
// console.log("Public URL: " +process.env.PUBLIC_URL);

var imgLoader = new (function() {
    //new images can be added here. They will load automatically
    var urls = {
        genericBarrier01 : (process.env.PUBLIC_URL + "/rec/genericBarrier_01.svg"),
        character : (process.env.PUBLIC_URL + "/rec/character.svg"),
    };

    var allLoaded = false;

    var images = {};
    function ImageType(url) {
        var that = this;
        
        this.loaded = false;

        this.image = new Image();

        //could have async issues
        this.image.onload = function() {
            that.loaded = true;
            for (let img in images) {
                let tmploaded = true;
                if (img.loaded === false) {
                    tmploaded = false;
                }
                allLoaded = tmploaded;
            }
        };
        this.image.src = url;
    };

    for (let url in urls) {
        images[url] = new ImageType(urls[url]);
        console.log(urls[url]);
    }

    this.getImage = (img) => {
        return images[img].image;
    }

    this.allLoaded = () => {
        return allLoaded;
    }
})();

function Credentials(name, password) {
    this.requestType = "authentication";
    this.name = name;
    this.password = password;
}

function RequestSubscription() {
    this.requestType = "subscription";
}

class Login extends React.Component {
    constructor(props) {
        super(props);
        this.state = {name: 'username', password: 'password'};

        this.handleNameChange = this.handleNameChange.bind(this);
        this.handlePasswordChange = this.handlePasswordChange.bind(this);
        this.handleSubmit = this.handleSubmit.bind(this);
    }

    handleNameChange(event) {
        this.setState({name: event.target.value});
    }

    handlePasswordChange(event) {
        this.setState({password: event.target.value});
    }

    handleSubmit(event) {
        alert(
            "Credentials were submitted: "
            + this.state.name + " "
            + this.state.password
        );
        event.preventDefault();
        connection.instantiate();
        connection.send(
            JSON.stringify(new Credentials(this.state.name, this.state.password))
        );
        connection.send(
            JSON.stringify(new RequestSubscription())
        );
    }
    
    render() {
        return (
            <form onSubmit={this.handleSubmit}>
                username
                <input
                    type="text"
                    value={this.state.name}
                    onChange={this.handleNameChange}
                /><br/>
                password
                <input
                    type="text"
                    value={this.state.password}
                    onChange={this.handlePasswordChange}
                /><br/>
                <input type="submit" value="Log In"/>
            </form>
        );
    }
}

class MapRow extends React.Component {
    constructor(props) {
        super(props);
    }

    render() {
        const tiles = this.props.row.map((element, i) => {
            if (element.character !== null) {
                return (
                    <span key={i}>Tile({element.character})</span>
                );
            } else {
                return (
                    <span key={i}>Tile({element.x},{element.y})</span>
                );
            }
        });
        
        return (
            <div>{tiles}</div>
        );
    }
}

function Tile(x, y, walkable, terrainType, object, character) {
    this.x = x;
    this.y = y;
    this.walkable = walkable;
    this.terrainType = terrainType;
    this.object = object;
    this.character = character;
}

class Game extends React.Component {
    constructor(props) {
        super(props);

        connection.game = this;

        this.width = 0;
        this.height = 0;

        this.board = document.getElementById("board");

        this.state = {
            map: []
        }

        this.map = this.state.map;
    }

    receivedDeltaFrame(deltaframe) {
        //the dimensions may have changed and tiles
        //hay have to be shifted
        this.width = deltaframe.xR - deltaframe.xL + 1;
        this.height = deltaframe.yU - deltaframe.yL + 1;

        //take out tile refugees
        let tmpTiles = [];
        if (this.map !== null) {
            for (let row of this.state.map) {
                for (let tile of row) {
                    tmpTiles.push(tile);
                }
            }
        }

        //first adjust the array to the appropriate size
        this.map = Array(this.height).fill(null);
        for (let i = 0; i < this.map.length; i++) {
            this.map[i] = Array(this.width).fill(null);
        }

        //relocate refugees
        for (let refugee of tmpTiles) {
            this.map
                [refugee.y - deltaframe.yL]
                [refugee.x - deltaframe.xL]
            = refugee;
        }

        //then hash tne new ones in
        for (let object of deltaframe.tiles) {
            let tmpTile = new Tile(
                object.x,
                object.y,
                object.walkable,
                object.terrainType,
                object.object,
                object.character
            );

            this.map
                [tmpTile.y - deltaframe.yL]
                [tmpTile.x - deltaframe.xL]
            = tmpTile;
        }

        this.setState({
            map: this.map.slice()
        }); //rerenders here

        this.canvasRender();
    }

    canvasRender() {
        //just display some of the sprites to prove
        //that your loader code works
        if (imgLoader.allLoaded) {
            var context = document
                .getElementById("board")
                .getContext("2d")
            ;

            //console.log(imgLoader.getImage("character"));
            //context.drawImage(imgLoader.getImage("character"), 0, 0);
            //context.rect(0,0,40,40);
            //context.stroke();

            this.state.map.forEach( function(element) {
                element.forEach( function(element) {
                    console.log(element);
                    //draw terrain
                    if (element.terrainType == "greenGrass1") {
                        context.rect(element.x * 50, element.y * 50, 50, 50);
                    } else {
                        context.arc(element.x * 50, element.y * 50, 50, 2 * Math.PI);
                    }
                    context.stroke();

                    //draw object

                    switch (element.object) {
                        case "genericBarrier1" :
                            context.drawImage(imgLoader.getImage("genericBarrier01"), element.x * 50, element.y * 50);
                    }

                    switch (element.character) {
                        case "lance" :
                            context.drawImage(imgLoader.getImage("character"), element.x * 50, element.y * 50);
                    }
                });
            });
        }
    }

    left() {
        console.log("Moving left");
    }

    right() {

    }

    up() {

    }

    down() {

    }

    render() {
        if (this.state.map !== null) {

            const tileRows = this.state.map.reverse().map((element, i) => {
                return (
                    <li key={i}><MapRow row={element}/></li>
                )
            })

            this.state.map.forEach((element) => {
                element.forEach((element) => {
                    console.log(element);
                });
            });


            return (
                <div>
                    <div><canvas id="board" width="500" height="500"></canvas></div>
                    <div><button id="upButton" type="button">Up</button></div>
                    <div>
                        <button id="leftButton" type="button" onClick={this.left}>Left</button>
                        <button id="rightButton" type="button">Right</button>
                    </div>
                    <div><button id="downButton" type="button">Down</button></div>
                </div>
            );
        } else {
            return (
                <h1>Here haveth game</h1>
            );
        }
    }
}

ReactDOM.render(<Login />, document.getElementById('root'));

ReactDOM.render(<Game />, document.getElementById('game'));

// If you want your app to work offline and load faster, you can change
// unregister() to register() below. Note this comes with some pitfalls.
// Learn more about service workers: https://bit.ly/CRA-PWA
serviceWorker.unregister();
