package server_core;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.BlockingQueue;

public class ConnectionListener extends Thread implements Killable {
	private ServerSocket listener;
	BlockingQueue<Client> clientQueue;
	
	DBAdapter db;
	
	public ConnectionListener(BlockingQueue<Client> clientQueue, DBAdapter db) {
		super();
		this.clientQueue = clientQueue;
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
			clientQueue.offer(new Client(new SocketAdapter(newConnection), db));
		}
	}
	
	public void shutDown() {
		try {
			listener.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		clientQueue.offer(ServerCore.POISONPILL);
	}
}
