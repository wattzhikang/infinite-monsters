package server_core;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.BlockingQueue;

public class ConnectionListener extends Thread{
	private ServerSocket listener;
	List<Client> clientList;
	
	DBInterface db;
	
	public ConnectionListener(DBInterface db) {
		super();
		this.clientList = new LinkedList<Client>();
		this.db = db;
		try {
			listener = new ServerSocket(ServerCore.PORT);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
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
			Client newClient = new Client(new SocketAdapter(newConnection), db);
			clientList.add(newClient);
			newClient.start();
		}
	}
	
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
