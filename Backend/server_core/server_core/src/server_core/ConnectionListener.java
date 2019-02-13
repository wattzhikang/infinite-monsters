package server_core;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.BlockingQueue;

public class ConnectionListener extends Thread implements Killable {
	private ServerSocket listener;
	BlockingQueue<String> messageQueue;
	List<SocketListener> clients = new LinkedList<SocketListener>();
	
	public ConnectionListener(BlockingQueue<String> messageQueue) {
		super();
		this.messageQueue = messageQueue;
		try {
			listener = new ServerSocket();
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
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			SocketListener newClient = new SocketListener(newConnection, messageQueue);
			newClient.start();
			clients.add(newClient);
		}
		for (SocketListener client : clients) {
			client.shutDown();
		}
	}
	
	public void shutDown() {
		try {
			listener.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
