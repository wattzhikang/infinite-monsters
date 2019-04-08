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
            connection.socket = new WebSocket('ws://localhost:8080/websocket/zw');
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

function Tile(x, y, walkable, terrainType, object, character) {
    this.x = x;
    this.y = y;
    this.walkable = walkable;
    this.terrainType = terrainType;
    this.object = object;
    this.character = character;
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

class Game extends React.Component {
    constructor(props) {
        super(props);

        connection.game = this;

        this.width = 0;
        this.height = 0;

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
                object.character
            );
            console.log(tmpTile.y - deltaframe.yL);
            this.map
                [tmpTile.y - deltaframe.yL]
                [tmpTile.x - deltaframe.xL]
            = tmpTile;
        }

        this.setState({
            map: this.map.slice()
        }); //rerenders here
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

            console.log(tileRows);
            return (
                <div>
                    <ol>{tileRows}</ol>
                    <button id="leftButton" type="button" onClick={this.left}>Left</button>
                    <button id="upButton" type="button">Up</button>
                    <button id="downButton" type="button">Down</button>
                    <button id="rightButton" type="button">Right</button>
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
