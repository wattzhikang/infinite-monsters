package server_core;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;
import java.util.concurrent.BlockingQueue;

public class SocketListener extends Thread implements Killable {
	private Socket socket;
	private ObjectInputStream inputStream;
	private BlockingQueue<String> messageQueue;
	
	public SocketListener(Socket connectionSocket, BlockingQueue<String> mainQueue) {
		socket = connectionSocket;
		messageQueue = mainQueue;
	}
	
	public void run() {
		try {
			inputStream = new ObjectInputStream(socket.getInputStream());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		while(!socket.isClosed()) {
			String inputString = null;
			try {
				inputString = (String) inputStream.readObject();
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			messageQueue.offer(inputString);
		}
	}
	
	public void shutDown() {
		try {
			socket.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
