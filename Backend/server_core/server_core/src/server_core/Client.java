package server_core;

//import java.io.EOFException;
//import java.io.IOException;
//import java.io.ObjectInputStream;
//import java.io.ObjectOutputStream;
//import java.net.Socket;
//import java.util.LinkedList;
//import java.util.Queue;
//import java.util.concurrent.BlockingQueue;


public class Client extends Thread implements Killable {
	private SocketAdapter socket;
	private DBInterface db;
	
	public Client(SocketAdapter socket, DBInterface db) {
		this.socket = socket;
		this.db = db;
	}
	
	public void run() {
//		try {
			assert socket != null;
			while(!socket.isClosed()) {
				String message = socket.readString();
				if (message != null) {
					System.out.println("Message Received From " + socket.getHost() + ":" + message);
					
					//separate out into user name and password
					String username = null;
					String password = null;
					
					//{"requestType":"registration","username":"user1","password":"sunshine","privileges":"player"}
					//{ requestType : registration , username : user1 , password : sunshine , privileges : player }
					//0 1           2 3            4 5        6 7     8 9       10 11      12 13        14 15     16
					
					//{"requestType":"authentication","username":"user1","password":"sunshine"}
					//{ requestType : authentication , username : user1 , password : sunshine }
					//0 1           2 3              4 5        6 7     8 9       10 11       12
					
					String[] JSONParse = message.split("\"");
					
					String response = null;
					
					if (JSONParse.length >= 12) {
						username = JSONParse[7];
						password = JSONParse[11];
						if (JSONParse[3].equals("registration")) {
							if (db.register(username, password)) {
								response = "{\"registrationSuccess\":\"true\"}";
							} else {
								response = "{\"registrationSuccess\":\"false\"}";
							}
						} else if (JSONParse[3].equals("authentication")) {
							if (db.login(username, password)) {
								response = "{\"loginSuccess\":\"true\"}";
							} else {
								response = "{\"loginSuccess\":\"false\"}";
							}
						} else {
							//malformed input
						}
					}
					
					if (response != null) {
						socket.writeString(response);
						System.out.println("Response sent to " + socket.getHost() + ": " + response);
					} else {
						System.out.println("Malformed input");
						socket.close();
					}
				}
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
