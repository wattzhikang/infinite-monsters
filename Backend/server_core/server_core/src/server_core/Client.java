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
					
					//{"username":"user1","password":"password"}
					//{ username : user1 , password : password }
					//0 1        2 3     4 5        6 7        8 length=9
					
					String[] JSONParse = message.split("\"");
					
					if (JSONParse.length == 9) {
						username = JSONParse[3];
						password = JSONParse[7];
						if (db.login(username, password)) {
							String success = "{\"loginSuccess\":\"true\"}";
							socket.writeString(success);
							System.out.println("Response sent to " + socket.getHost() + ": " + success);
						} else {
							String fail = "{\"loginSuccess\":\"false\"}";
							socket.writeString(fail);
							System.out.println("Response sent to " + socket.getHost() + ": " + fail);
						}
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
