package server_experiment;

import java.net.Socket;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;

public class Servlet {
	private static int port = 9234;
	public static void main(String[] args) throws IOException, ClassNotFoundException {
		ServerSocket server = new ServerSocket(port);
		
		while(true) {
			System.out.println("Waiting for client request");
			
			Socket socket = server.accept();
			
			ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
			
			String message = (String) ois.readObject();
			
			System.out.println("Message Received: " + message);
			
			ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
			
			oos.writeObject("Greetings, client " + message);
			
			ois.close();
			oos.close();
			socket.close();
			
			if (message.equalsIgnoreCase("exit")) {
				break;
			}
		}
		
		System.out.println("Shutting down Socket server...");
		server.close();
	}
}
