import React from 'react';
import ReactDOM from 'react-dom';
import './index.css';
import * as serviceWorker from './serviceWorker';

//singleton that establishes a websocket connection to the server
var connection = {
    socket: null,
    queue : [],
    game : null, //it needs the game so it knows where to send
                 //the delta frames to
    
    //establishes a connection to the server
    instantiate : () => {
        if (connection.isOpen) {
            connection.socket = new WebSocket('ws://cs309-yt-1.misc.iastate.edu:8080/websocket/zw');
            
            //this function handles messages from the server
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

            //sends messages that were waiting for the
            //socket to finish connecting
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
        //if an attempt is made to send a message before the
        //socket is open, this function enqueues the message
        //in a queue and will be sent when it opens
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

//singleton that loads all the images so they can be displayed.
//To load additional images, just add URLs to the "urls" object.
//Note that this singleton is implemented with different syntax.
//That's just because I learned a different way to do it. Both
//connection and imgLoader are the same thing.
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

//Basically a dummy class for parsing
//the JSON stringfrom the server
function Credentials(name, password) {
    this.requestType = "authentication";
    this.name = name;
    this.password = password;
}

//Dummy class for parsing JSON string from the server
function RequestSubscription() {
    this.requestType = "subscription";
}

//also a dummy class for JSON parsing
function ModMoveSubscription(
    subscriptionID,
    xL, xR,
    yU, yL,
    oldPlayerX,
    oldPlayerY,
    newPlayerX,
    newPlayerY
) {
    this.requestType = "mod_move_subscription";
    this.subscriptionID = subscriptionID;
    this.xL = xL;
    this.xR = xR;
    this.yU = yU;
    this.yL = yL;
    this.oldPlayerX = oldPlayerX;
    this.oldPlayerY = oldPlayerY;
    this.newPlayerX = newPlayerX;
    this.newPlayerY = newPlayerY;
};

//Defines the Login component
class Login extends React.Component {
    constructor(props) {
        super(props);
        this.state = {name: 'username', password: 'password'};

        //I think these statements just ensure that the 'this' reference
        //in these methods refers to this object, and not their own
        //objects. Javascript sure is interesting.
        this.handleNameChange = this.handleNameChange.bind(this);
        this.handlePasswordChange = this.handlePasswordChange.bind(this);
        this.handleSubmit = this.handleSubmit.bind(this);
    }

    //When a different username is entered, the state of this object is changed
    //to reflect that.
    handleNameChange(event) {
        this.setState({name: event.target.value});
    }

    //Same as handleNameChange()
    handlePasswordChange(event) {
        this.setState({password: event.target.value});
    }

    //Sends the credentials to the server and subsequently requests a subscription.
    handleSubmit(event) {
        alert(
            "Credentials were submitted: "
            + this.state.name + " "
            + this.state.password
        );
        event.preventDefault(); //I don't know what this does
        connection.instantiate(); //prior to this, the websocket is not connected
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

//not a dummy class for JSON parsing. This is actually used internally
function Tile(x, y, xAbs, yAbs, walkable, terrainType, object, character) {
    this.x = x;
    this.y = y;
    this.xAbs = xAbs;
    this.yAbs = yAbs;
    this.walkable = walkable;
    this.terrainType = terrainType;
    this.object = object;
    this.character = character;

    this.inBounds = (xAbsL, xAbsR, yAbsL, yAbsU) => {
        return (
            xAbsL <= this.xAbs &&
            xAbsR >= this.xAbs &&
            yAbsL <= this.yAbs &&
            yAbsU >= this.yAbs
        );
    };
}

//holds all the actual game logic
class Game extends React.Component {
    constructor(props) {
        super(props);

        connection.game = this;

        this.width = 0;
        this.height = 0;

        this.board = document.getElementById("board");

        //The state of the game is simply the map array itself.
        //When it changes, the game is re-rendered.
        /*
         * TODO Now that the game is rendered in a canvas, it
         * isn't necessary for this to be a state anymore,
         * causing the entire component to re-render.
        */
        this.state = {
            map: []
        }

        this.map = this.state.map;

        /*
         * These four methods are where the buttons are implemented
        */
        this.left = () => {
            console.log("Moving left");
            let move = new ModMoveSubscription(
                this.subscriptionID,
                this.xAbsL, //TODO move bounds as well
                this.xAbsR, //TODO move bounds as well
                this.yAbsU, //TODO move bounds as well
                this.yAbsL, //TODO move bounds as well
                this.playerX,
                this.playerY,
                this.playerX - 1,
                this.playerY,
            );
    
            connection.send(JSON.stringify(
                move
            ));
        };

        this.right = () => {
            console.log("Moving right");
            let move = new ModMoveSubscription(
                this.subscriptionID,
                this.xAbsL, //TODO move bounds as well
                this.xAbsR, //TODO move bounds as well
                this.yAbsU, //TODO move bounds as well
                this.yAbsL, //TODO move bounds as well
                this.playerX,
                this.playerY,
                this.playerX + 1,
                this.playerY,
            );

            console.log(move);
            console.log(JSON.stringify(move));
    
            connection.send(JSON.stringify(move));
        };

        this.up = () => {
            console.log("Moving up");
            let move = new ModMoveSubscription(
                this.subscriptionID,
                this.xAbsL, //TODO move bounds as well
                this.xAbsR, //TODO move bounds as well
                this.yAbsU, //TODO move bounds as well
                this.yAbsL, //TODO move bounds as well
                this.playerX,
                this.playerY,
                this.playerX,
                this.playerY + 1,
            );
    
            connection.send(JSON.stringify(
                move
            ));
        };

        this.down = () => {
            console.log("Moving down");
            let move = new ModMoveSubscription(
                this.subscriptionID,
                this.xAbsL, //TODO move bounds as well
                this.xAbsR, //TODO move bounds as well
                this.yAbsU, //TODO move bounds as well
                this.yAbsL, //TODO move bounds as well
                this.playerX,
                this.playerY,
                this.playerX,
                this.playerY - 1,
            );
    
            connection.send(JSON.stringify(
                move
            ));
        };
    }

    //unpacks the delta frame and hashes tiles into an array
    receivedDeltaFrame(deltaframe) {
        //record subscriptionID
        this.subscriptionID = deltaframe.subscriptionID;
        
        //the dimensions may have changed
        this.xAbsR = deltaframe.xR;
        this.xAbsL = deltaframe.xL;
        this.yAbsU = deltaframe.yU;
        this.yAbsL = deltaframe.yL;

        this.width = deltaframe.xR - deltaframe.xL + 1;
        this.height = deltaframe.yU - deltaframe.yL + 1;

        //remove tiles that are no longer within bounds
        if (this.map !== null) {
            this.map.forEach( (tile, i) => {
                if (!tile.inBounds(
                    this.xAbsL,
                    this.xAbsR,
                    this.yAbsL,
                    this.yAbsU
                )) {
                    this.state.map.splice(i, 1);
                }
            });
        }

        //then hash tne new ones in
        for (let object of deltaframe.tiles) {
            let tmpTile = new Tile(
                object.x - deltaframe.xL,
                object.y - deltaframe.yL,
                object.x,
                object.y,
                object.walkable,
                object.terrainType,
                object.object,
                object.character
            );

            if (tmpTile.character != null) {
                this.playerX = tmpTile.x;
                this.playerY = tmpTile.y;
            }

            this.map.push(tmpTile);
        }

        this.setState({
            map: this.map.slice()
        }); //rerenders here

        this.canvasRender();
    }

    //actually draws the map on the canvas
    canvasRender() {
        if (imgLoader.allLoaded) {
            var context = document
                .getElementById("board")
                .getContext("2d")
            ;

            this.state.map.forEach( function(element) {
                console.log(element);
                //draw terrain
                const cellWidth = 49;
                //TODO This should be a switch statement as well
                if (element.terrainType == "greenGrass1") {
                    context.rect(element.x * cellWidth, element.y * cellWidth, cellWidth, cellWidth);
                } else {
                    context.arc(element.x * cellWidth, element.y * cellWidth, cellWidth, 2 * Math.PI);
                }
                context.stroke();

                //draw object
                switch (element.object) {
                    case "genericBarrier1" :
                        context.drawImage(imgLoader.getImage("genericBarrier01"), element.x * 50, element.y * 50);
                }

                //draw character
                //TODO Obviously not all characters will be named "lance"
                switch (element.character) {
                    case "lance" :
                        context.drawImage(imgLoader.getImage("character"), element.x * 50, element.y * 50);
                }
            });
        }
    }

    render() {
        //TODO this if statement is no longer necessary
        if (this.state.map !== null) {
            return (
                <div>
                    <div><canvas id="board" width="500" height="500"></canvas></div>
                    <div><button id="upButton" type="button" onClick={this.up}>Up</button></div>
                    <div>
                        <button id="leftButton" type="button" onClick={this.left}>Left</button>
                        <button id="rightButton" type="button" onClick={this.right}>Right</button>
                    </div>
                    <div><button id="downButton" type="button" onClick={this.down}>Down</button></div>
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
