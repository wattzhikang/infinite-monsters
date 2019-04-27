package server_core;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.BlockingQueue;

import game.DBInterface;
import game.Game;

/**
 * Maintains a ServerSocket listening for clients. When a client
 * connects, it spins off a new Client object. Also maintains a
 * list of Client objects.
 * @author zjwatt
 *
 */
public class ConnectionListener extends Thread{
	private ServerSocket listener;
	
	private List<Client> clientList;
	
	private DBInterface db;
	private Game game;
	
	public ConnectionListener(DBInterface db, Game game) {
		super();
		this.clientList = new LinkedList<Client>();
		this.db = db;
		this.game = game;
		try {
			listener = new ServerSocket(ServerCore.PORT_TCP);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * Listens for connecting clients and spins of a Client
	 * thread to represent them
	 */
	public void run() {
		while(!listener.isClosed()) {
			Socket newConnection = null;
			//TODO expand this try-catch block
			try {
				newConnection = listener.accept();
			} catch (SocketException e) {
				if (listener.isClosed()) {
					break;
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			Client newClient = new Client(new SocketAdapterTCP(newConnection), game);
			clientList.add(newClient);
			newClient.start();
			
			//maybe go through list and cull
		}
	}
	
	/**
	 * Calls shutdown() for every single Client this ConnectionListener has
	 * created
	 * @throws InterruptedException
	 */
	public void shutDown() throws InterruptedException {
		try {
			listener.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		for (Client client : clientList) {
			client.shutDown();
			client.join();
		}
	}
}
