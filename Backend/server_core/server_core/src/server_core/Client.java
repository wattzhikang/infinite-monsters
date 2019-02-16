package server_core;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.BlockingQueue;

public class Client extends Thread implements Killable {
	private SocketAdapter socket;
	
	
	public Client(SocketAdapter socket) {
		this.socket = socket;
	}
	
	public void run() {
//		try {
			assert socket != null;
			while(!socket.isClosed()) {
				String message = socket.readString();
				socket.writeString(message);
			}
//		} catch (NullPointerException e) {
//			assert (socket != null);
//			e.printStackTrace();
//		}
	}
	
	public void shutDown() {
		socket.close();
	}
}
