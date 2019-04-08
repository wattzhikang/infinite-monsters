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

class Game extends React.Component {
    constructor(props) {
        super(props);
        connection.game = this;
        this.map = null;
    }

    receivedDeltaFrame(deltaframe) {
        if (this.map = null) {
            this.map = Array(
                (deltaframe.xR - deltaframe.xL)
                * (deltaframe.yU - deltaframe.yL)
            ).fill(null);
        }
    }

    testShowHistory() {

    }

    render() {
        return (
            <h1>Here haveth game</h1>
        );
    }
}

ReactDOM.render(<Login />, document.getElementById('root'));

ReactDOM.render(<Game />, document.getElementById('game'));

// If you want your app to work offline and load faster, you can change
// unregister() to register() below. Note this comes with some pitfalls.
// Learn more about service workers: https://bit.ly/CRA-PWA
serviceWorker.unregister();
