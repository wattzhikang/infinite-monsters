package server_experiment;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;

public class Client {
	public static final int port = 9234;
	public static void main(String[] args) throws IOException, InterruptedException, ClassNotFoundException {
		InetAddress host = InetAddress.getLocalHost();
		
		Socket socket = null;
		ObjectInputStream ois = null;
		ObjectOutputStream oos = null;
		
		for (int i = 0; i < 5; i++) {
			socket = new Socket(host.getHostName(), port);
			
			oos = new ObjectOutputStream(socket.getOutputStream());
			System.out.println("Sending request to socket server");
			if (i == 4) {
				oos.writeObject("exit");
			} else {
				oos.writeObject("" + 1);
			}
			ois = new ObjectInputStream(socket.getInputStream());
			String message = (String) ois.readObject();
			System.out.println("Message: " + message);
			ois.close();
			oos.close();
			Thread.sleep(100);
		}
	}
}
