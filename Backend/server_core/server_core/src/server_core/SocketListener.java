package server_core;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.BlockingQueue;

public class SocketListener extends Thread implements Killable {
	private Socket socket;
	private ObjectInputStream inputStream;
	private ObjectOutputStream outputStream;
	
	private BlockingQueue<Message> messageQueue;
	
	public SocketListener(Socket connectionSocket, BlockingQueue<Message> mainQueue) {
		socket = connectionSocket;
		try {
			inputStream = new ObjectInputStream(socket.getInputStream());
			outputStream = new ObjectOutputStream(socket.getOutputStream());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		messageQueue = mainQueue;
	}
	
	public void run() {
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
			messageQueue.offer(new Message(inputString, this));
		}
	}
	
	public synchronized void enqueueMessage(Message message) {
		try {
			outputStream.writeObject(message.toString());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
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
