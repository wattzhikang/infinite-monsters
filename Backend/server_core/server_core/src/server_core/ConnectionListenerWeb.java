package server_core;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import javax.websocket.DeploymentException;
import javax.websocket.server.ServerEndpoint;



import game.DBInterface;
import game.Game;

import org.glassfish.tyrus.server.Server;

@ServerEndpoint(value = "/game")
public class ConnectionListenerWeb{

	private List<Client> clientList;
	
	private DBInterface db;
	private Game game;
	
	Server server;
	
	public ConnectionListenerWeb(DBInterface db, Game game) {
		super();
		this.clientList = new LinkedList<Client>();
		this.db = db;
		this.game = game;
		this.server = new Server("localhost", ServerCore.PORT_WEB, "", new HashMap<String, Object>(), ConnectionListenerWeb.class);
	}
	
	public void start() {
		try {
			server.start();
		} catch (DeploymentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
