import React from 'react';
import ReactDOM from 'react-dom';
import './index.css';
import * as serviceWorker from './serviceWorker';

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
            connection.socket = new WebSocket('ws://cs309-yt-1.misc.iastate.edu:8080/websocket/zjwatt');

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
            JSON.stringify({requestType: "deauthentication"})
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
                    <button type="button" >Log Out</button>
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

            connection.send(JSON.stringify(
                move
            ));
        };

        this.canvas = (event) => {
            let rect = document.getElementById("board").getBoundingClientRect();
            var x = event.clientX - rect.left;
            var y = event.clientY - rect.top;
            console.log(x + ',' + y);
            return x;
        };

        this.tileEditor = (event) => {
            let tileEditorContext = document
                .getElementById("tileEditor")
                .getContext("2d")
                ;
            let rect = document.getElementById("tileEditor").getBoundingClientRect();
            var x = event.clientX - rect.left;
            var y = event.clientY - rect.top;
            var cellLocX = Math.floor(x / 50);
            var cellLocY = Math.floor(y / 50);
            console.log("pointer location: " + x + ',' + y);
            var tileEditorImages = imgLoader.getImages();
            var numImages = imgLoader.getLength();
            for (var i = 0; i < numImages; i++) {
                tileEditorContext.drawImage(imgLoader.getImage(tileEditorImages[i]), i * 50, 0);
            }

            for (var n = 0; n < numImages; n++) {
                console.log("n: " + n + ", cellLocX: " + cellLocX);
                if (cellLocX === n) {
                    tileEditorContext.globalAlpha = '0.5';
                    console.log("cell location: " + cellLocX + ',' + cellLocY)
                    tileEditorContext.fillRect((cellLocX * 50), (cellLocY * 50), 50, 50);
                }
            }
            connection.send(JSON.stringify(new ReverseDeltaFrame(0, reverseDeltaFrameTiles)));
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
        this.tileEditorRender();
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
                const cellWidth = 49;
                //TODO This should be a switch statement as well
                if (element.terrainType === "greenGrass1") {
                    context.rect(element.x * cellWidth, element.y * cellWidth, cellWidth, cellWidth);
                } else {
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
    tileEditorRender() {
        if (imgLoader.allLoaded) {
            console.log("tile editor");
            var tileEditorContext = document
                .getElementById("tileEditor")
                .getContext("2d")
                ;
            let count = 0;
            console.log(imgLoader.getImages);
            for (let i in imgLoader.getImages) {
                console.log(i);
                tileEditorContext.drawImage(i, count * 50, count * 50);
                count++;
            }
        }
    }

    render() {
        //TODO this if statement is no longer necessary
        if (this.state.map !== null) {
            return (
                <div>
                    <div><canvas id="board" width="500" height="500" onClick={this.canvas}></canvas></div>
                    <div><canvas id="tileEditor" width="500" height="300" onClick={this.tileEditor}></canvas></div>
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
