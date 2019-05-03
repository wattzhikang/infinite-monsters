import React from 'react';
import ReactDOM from 'react-dom';
import './index.css';
import * as serviceWorker from './serviceWorker';
import { reverse } from 'dns';

const cellWidth = 49;

//singleton that establishes a websocket connection to the server
var connection = {
    socket: null,
    queue: [],
    game: null, //it needs the game so it knows where to send
    //the delta frames to
    login: null, //it also needs to know who to notify of login success

    //establishes a connection to the server
    instantiate: () => {
        if (connection.isOpen) {
            connection.socket = new WebSocket('ws://localhost:8080/websocket/zjwatt');
            //connection.socket = new WebSocket('ws://localhost:8080/websocket/zjwatt');

            //this function handles messages from the server
            connection.socket.onmessage = function (event) {
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
                } else if (object.loginSuccess !== undefined) {
                    if (object.loginSuccess) {
                        connection.login.handleLoginSuccess();
                        connection.game.objectTypeRender();
                        connection.game.terrainTypeRender();
                    } else {
                        connection.login.handleLoginFailure();
                    }
                }
            };

            //sends messages that were waiting for the
            //socket to finish connecting
            connection.socket.onopen = function (event) {
                for (let string of connection.queue) {
                    connection.send(string);
                }
            }
        }
    },
    isOpen: () => {
        if (connection.socket && connection.socket.readyState === 1) {
            return true;
        } else {
            return false;
        }
    },
    send: (string) => {
        //if an attempt is made to send a message before the
        //socket is open, this function enqueues the message
        //in a queue and will be sent when it opens
        if (!connection.isOpen()) {
            connection.queue.push(string);
        } else {
            connection.socket.send(string);
        }
    },
    close: () => { //this function is basically reverse instantiate
        if (connection.isOpen) {
            connection.socket.close();

            connection.socket = null;
            connection.queue = [];

            connection.isOpen = false;
        }
    },
}

//singleton that loads all the images so they can be displayed.
//To load additional images, just add URLs to the "urls" object.
//Note that this singleton is implemented with different syntax.
//That's just because I learned a different way to do it. Both
//connection and imgLoader are the same thing.
var imgLoader = new (function () {
    //new images can be added here. They will load automatically
    var urls = {
        genericBarrier01: (process.env.PUBLIC_URL + "/rec/genericBarrier_01.svg"),
        character: (process.env.PUBLIC_URL + "/rec/character.svg"),
    };

    var that = this;

    var allLoaded = false;

    var images = {};
    function ImageType(url) {

        this.loaded = false;

        this.image = new Image();

        //could have async issues
        this.image.onload = function () {
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

    this.getImages = () => {
        return Object.keys(images);
    }

    this.getImage = (img) => {
        return images[img].image;
    }

    this.allLoaded = () => {
        return allLoaded;
    }

    this.getLength = () => {
        return Object.keys(urls).length;
    }
})();

//Basically a dummy class for parsing
//the JSON stringfrom the server
function Credentials(name, password) {
    this.requestType = "authentication";
    this.username = name;
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

        connection.login = this;

        this.state = {
            name: 'username',
            password: 'password',
            loggedIn: false,
            readOnly: false
        };

        //I think these statements just ensure that the 'this' reference
        //in these methods refers to this object, and not their own
        //objects. Javascript sure is interesting.
        this.handleNameChange = this.handleNameChange.bind(this);
        this.handlePasswordChange = this.handlePasswordChange.bind(this);
        this.handleSubmit = this.handleSubmit.bind(this);
        this.logOut = this.logOut.bind(this);
    }

    //When a different username is entered, the state of this object is changed
    //to reflect that.
    handleNameChange(event) {
        this.setState({ name: event.target.value });
    }

    //Same as handleNameChange()
    handlePasswordChange(event) {
        this.setState({ password: event.target.value });
    }

    //Sends the credentials to the server and subsequently requests a subscription.
    handleSubmit(event) {
        if (!this.state.loggedIn) {
            alert(
                "Credentials were submitted: "
                + this.state.name + " "
                + this.state.password
            );
            this.submittedName = this.state.name;
            event.preventDefault(); //I don't know what this does
            connection.instantiate(); //prior to this, the websocket is not connected
            connection.send(
                JSON.stringify(new Credentials(this.state.name, this.state.password))
            );
            this.setState(
                {
                    loggedIn: true,
                    readOnly: true
                }
            );
        }
    }

    handleLoginSuccess() {
        this.setState(
            {
                name: this.submittedName,
                password: '',
                loggedIn: true,
                readOnly: true
            }
        );
        connection.send(
            JSON.stringify(new RequestSubscription())
        );
    }

    handleLoginFailure() {
        this.setState(
            {
                loggedIn: false,
                readOnly: false
            }
        );
    }

    logOut() {
        connection.send(
            JSON.stringify({ requestType: "deauthentication" })
        );
        //TODO this is bad architecture
        document.getElementById("board").getContext("2d").clearRect(
            0,
            0,
            document.getElementById("board").width,
            document.getElementById("board").height
        );
        this.setState(
            {
                name: 'username',
                password: 'password',
                loggedIn: false,
                readOnly: false
            }
        );
    }

    render() {
        return (
            <div>
                <form onSubmit={this.handleSubmit}>
                    username
                    <input
                        type="text"
                        value={this.state.name}
                        onChange={this.handleNameChange}
                        readOnly={this.state.readOnly}
                    /><br />
                    password
                    <input
                        type="text"
                        value={this.state.password}
                        onChange={this.handlePasswordChange}
                        readOnly={this.state.readOnly}
                    /><br />
                    <input type="submit" value="Log In" />
                </form>
                <div>
                    <button type="button" onClick={this.logOut}>Log Out</button>
                </div>
            </div>
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

//class for JSON parsing. Used to send a deltaFrame to the server when the Game Designer edits the map
function ReverseDeltaFrame(subscriptionID, tiles) {
    this.requestType = "reverseDelta";
    this.subscriptionID = subscriptionID;
    this.tiles = tiles;
}

//holds all the actual game logic
class Game extends React.Component {
    constructor(props) {
        super(props);

        connection.game = this;
        this.width = 0;
        this.height = 0;

        this.board = document.getElementById("board");
        this.tileEditor = document.getElementById("tileEditor");
        var reverseDeltaFrameTiles = [];
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
            console.log(move);
            console.log(JSON.stringify(move));
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
                this.playerY - 1,
            );
            console.log(move);
            console.log(JSON.stringify(move));
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
                this.playerY + 1,
            );
            console.log(move);
            console.log(JSON.stringify(move));
            connection.send(JSON.stringify(
                move
            ));
        };

        this.canvas = (event) => {
            let canvasContext = this.getCanvasContext();
            let rect = document.getElementById("board").getBoundingClientRect();
            var x = event.clientX - rect.left;
            var y = event.clientY - rect.top;
            var canvasCellLocX = Math.floor(x / 50);
            var canvasCellLocY = Math.floor(y / 50);
            let mapSize = this.map.length;
            let newTile = false;
            for (var n = 0; n < mapSize; n++) {
                if (canvasCellLocX === this.map[n].x && canvasCellLocY === this.map[n].y) {
                    canvasContext.globalAlpha = '0.5';
                    canvasContext.fillRect((canvasCellLocX * 49), (canvasCellLocY * 49), 49, 49);
                    reverseDeltaFrameTiles.push(this.map[n]);
                    newTile = true;
                }
            }
            if (newTile === false) {
                if (canvasCellLocX > this.map[mapSize - 1].x) {
                    canvasContext.globalAlpha = '0.5';
                    canvasContext.fillRect((canvasCellLocX * 49), (canvasCellLocY * 49), 49, 49);
                    let tile = new Tile(canvasCellLocX, canvasCellLocY, (canvasCellLocX + this.map[0].xAbs), this.map[0].yAbs, false, null, null, null);
                    reverseDeltaFrameTiles.push(tile);
                    newTile = 1;
                }
                else if (canvasCellLocY > this.map[mapSize - 1].y) {
                    canvasContext.globalAlpha = '0.5';
                    canvasContext.fillRect((canvasCellLocX * 49), (canvasCellLocY * 49), 49, 49);
                    let tile = new Tile(canvasCellLocX, canvasCellLocY, this.map[0].xAbs, (canvasCellLocY + this.map[0].yAbs), false, null, null, null);
                    reverseDeltaFrameTiles.push(tile);
                    newTile = 1;
                }
                else {
                    canvasContext.globalAlpha = '0.5';
                    canvasContext.fillRect((canvasCellLocX * 49), (canvasCellLocY * 49), 49, 49);
                    let tile = new Tile(canvasCellLocX, canvasCellLocY, (canvasCellLocX + this.map[0].xAbs), (canvasCellLocY + this.map[0].yAbs), false, null, null, null);
                    reverseDeltaFrameTiles.push(tile);
                    newTile = 1;
                }
            }
        };

        this.objectEditor = (event) => {
            console.log("object editor");
            let canvasContext = this.getCanvasContext();
            let rect = document.getElementById("objectTypes").getBoundingClientRect();
            var x = event.clientX - rect.left;
            var y = event.clientY - rect.top;
            var cellLocX = Math.floor(x / 50);
            //var cellLocY = Math.floor(y / 50);
            let numReverseTiles = reverseDeltaFrameTiles.length;
            for (var i = 0; i < numReverseTiles; i++) {
                if (cellLocX === 0) {
                    reverseDeltaFrameTiles[i].object = "genericBarrier1";
                    reverseDeltaFrameTiles[i].walkable = "false";
                }
                if (cellLocX === 1) {
                    reverseDeltaFrameTiles[i].character = "lance";
                }
            }
            for (i = 0; i < reverseDeltaFrameTiles.length; i++) {
                console.log("reverseDeltaFrameX: " + reverseDeltaFrameTiles[i].x + ", reverseDeltaFrameY: " + reverseDeltaFrameTiles[i].y);
                canvasContext.clearRect((reverseDeltaFrameTiles[i].x * 49), (reverseDeltaFrameTiles[i].y * 49), 47, 47);
            }
            let reverseDeltaFrame = new ReverseDeltaFrame(this.subscriptionID, reverseDeltaFrameTiles)
            connection.send(JSON.stringify(reverseDeltaFrame));
            reverseDeltaFrameTiles.length = 0;
        };

        this.terrainEditor = (event) => {
            console.log("terrain editor");
            let canvasContext = this.getCanvasContext();
            let rect = document.getElementById("terrainTypes").getBoundingClientRect();
            var x = event.clientX - rect.left;
            var y = event.clientY - rect.top;
            var cellLocX = Math.floor(x / 50);
            //var cellLocY = Math.floor(y / 50);
            let numReverseTiles = reverseDeltaFrameTiles.length;
            for (var i = 0; i < numReverseTiles; i++) {
                if (cellLocX === 0) {
                    reverseDeltaFrameTiles[i].terrainType = "greenGrass1";
                    reverseDeltaFrameTiles[i].object = null;
                    reverseDeltaFrameTiles[i].walkable = "true";
                }
                else if (cellLocX === 1) {
                    reverseDeltaFrameTiles[i].terrainType = "genericPath1";
                    reverseDeltaFrameTiles[i].object = null;
                    reverseDeltaFrameTiles[i].walkable = "true";
                }
            }

            for (i = 0; i < reverseDeltaFrameTiles.length; i++) {
                console.log("reverseDeltaFrameX: " + reverseDeltaFrameTiles[i].x + ", reverseDeltaFrameY: " + reverseDeltaFrameTiles[i].y);
                canvasContext.clearRect((reverseDeltaFrameTiles[i].x * 49), (reverseDeltaFrameTiles[i].y * 49), 47, 47);
            }
            let reverseDeltaFrame = new ReverseDeltaFrame(this.subscriptionID, reverseDeltaFrameTiles)
            connection.send(JSON.stringify(reverseDeltaFrame));
            reverseDeltaFrameTiles.length = 0;
        };
    }

    //returns the context for the game canvas used to select the tiles
    getCanvasContext() {
        return document.getElementById("board").getContext("2d");
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
            this.map.forEach((tile, i) => {
                if (!tile.inBounds(
                    this.xAbsL,
                    this.xAbsR,
                    this.yAbsL,
                    this.yAbsU
                )) {
                    this.map.splice(i, 1);
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

            //TODO this is a band-aid solution
            //to ensure that old tiles are overwritten
            for (let i = 0; i < this.map.length; i++) {
                let tile = this.map[i];
                if (tile.xAbs === tmpTile.xAbs &&
                    tile.yAbs === tmpTile.yAbs
                ) {
                    this.map.splice(i, 1);
                }
            }

            this.map.push(tmpTile);
        }

        this.setState({
            map: this.map.slice()
        });

        this.canvasRender();
    }

    //actually draws the map on the canvas
    canvasRender() {
        if (imgLoader.allLoaded) {
            var context = document
                .getElementById("board")
                .getContext("2d")
                ;

            //clear the canvas before drawing every time
            context.clearRect(
                0,
                0,
                document.getElementById("board").width,
                document.getElementById("board").height
            );

            this.state.map.forEach(function (element) {
                console.log(element);
                //draw terrain
                
                //TODO This should be a switch statement as well
                if (element.terrainType === "greenGrass1") {
                    context.rect(element.x * cellWidth, element.y * cellWidth, cellWidth, cellWidth);
                }
                else if (element.terrainType === null) {
                    context.fillStyle = "#000000";
                    context.fillRect(element.x * cellWidth, element.y * cellWidth, cellWidth, cellWidth);
                }
                else {
                    context.arc(element.x * cellWidth, element.y * cellWidth, cellWidth, 2 * Math.PI);
                }
                context.stroke();

                //draw object
                switch (element.object) {
                    case "genericBarrier1":
                        context.drawImage(imgLoader.getImage("genericBarrier01"), element.x * 50, element.y * 50);
                        break;
                    default:
                        break;
                }

                //draw character
                //TODO Obviously not all characters will be named "lance"
                switch (element.character) {
                    case "lance":
                        context.drawImage(imgLoader.getImage("character"), element.x * 50, element.y * 50);
                        break;
                    default:
                        break;
                }
            });
        }
    }

    //Displays all of the images of every game tile for the Game Designer
    objectTypeRender() {
        if (imgLoader.allLoaded) {
            var tileEditorContext = document.getElementById("objectTypes").getContext("2d");
            var tileEditorImages = imgLoader.getImages();
            var numImages = imgLoader.getImages().length;
            var i;
            for (var i = 0; i < numImages; i++) {
                tileEditorContext.drawImage(imgLoader.getImage(tileEditorImages[i]), i * 50, 0);
            }
            tileEditorContext.rect(i * cellWidth, 0, cellWidth, cellWidth);
            console.log("Why isn't the rectangle being drawn?");
        }
    }

    terrainTypeRender() {
        if (imgLoader.allLoaded) {
            var terrainTypesContext = document.getElementById("terrainTypes").getContext("2d");
            terrainTypesContext.fillStyle = "#00FF00";
            terrainTypesContext.fillRect(0, 0, 49, 49);
            terrainTypesContext.fillStyle = "#795300";
            terrainTypesContext.fillRect(50, 0, 49, 49);
        }
    }

    render() {
        //TODO this if statement is no longer necessary
        if (this.state.map !== null) {

            return (
                <div>
                    <div><canvas id="board" width="500" height="500" onClick={this.canvas}></canvas></div>
                    <div><canvas id="objectTypes" width="500" height="150" onClick={this.objectEditor}></canvas></div>
                    <div><canvas id="terrainTypes" width="500" height="150" onClick={this.terrainEditor}></canvas></div>
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
