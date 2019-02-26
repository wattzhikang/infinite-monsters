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
	
	private enum RequestType {REGISTRATION, AUTHENTICATION, SUBSCRIPTION, MODIFICATION, MALFORMED};
	
	public Client(SocketAdapter socket, DBInterface db) {
		this.socket = socket;
		this.db = db;
	}
	
	public void run() {
		assert socket != null;
		while(!socket.isClosed()) {
			String message = socket.readString();
			if (message != null) {
				System.out.println("Message Received From " + socket.getHost() + ":" + message);
				
				String username = null;
				String password = null;
				
				String response = null;
				
				switch (getRequestType(message)) {
					case AUTHENTICATION:
						username = getAuthUser(message);
						password = getAuthPassword(message);
						if (db.login(username, password)) {
							response = "{\"loginSuccess\":\"true\"}";
						} else {
							response = "{\"loginSuccess\":\"false\"}";
						}
						break;
					case MALFORMED:
						break;
					case MODIFICATION:
						break;
					case REGISTRATION:
						username = getRegUser(message);
						password = getRegPassword(message);
						if (db.register(username, password)) {
							response = "{\"registrationSuccess\":\"true\"}";
						} else {
							response = "{\"registrationSuccess\":\"false\"}";
						}
						break;
					case SUBSCRIPTION:
						break;
					default:
						break;
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
	}
	
	//{"requestType":"registration","username":"user1","password":"sunshine","privileges":"player"}
	//{ requestType : registration , username : user1 , password : sunshine , privileges : player }
	//0 1           2 3            4 5        6 7     8 9       10 11      12 13        14 15     16
	
	//{"requestType":"authentication","username":"user1","password":"sunshine"}
	//{ requestType : authentication , username : user1 , password : sunshine }
	//0 1           2 3              4 5        6 7     8 9       10 11       12
	
	private RequestType getRequestType(String JSON) {
		String[] JSONParse = JSON.split("\"");
		
		if (JSONParse[3].equals("registration")) {
			return RequestType.REGISTRATION;
		} else if (JSONParse[3].equals("authentication")) {
			return RequestType.AUTHENTICATION;
		} else if (JSONParse[3].equals("subscription")) {
			return RequestType.SUBSCRIPTION;
		} else if (JSONParse[3].equals("modification")) {
			return RequestType.MODIFICATION;
		} else {
			return RequestType.MALFORMED;
		}
	}
	
	private String getAuthUser(String message) {
		String[] parse = message.split("\"");
		
		if (parse.length == 13) {
			return getUser(parse);
		} else {
			return null;
		}
	}
	
	private String getRegUser(String message) {
		String[] parse = message.split("\"");
		
		if (parse.length == 17) {
			return getUser(parse);
		} else {
			return null;
		}
	}
	
	private String getUser(String[] parse) {
		return parse[5];
	}
	
	private String getAuthPassword(String message) {
		String[] parse = message.split("\"");
		
		if (parse.length == 13) {
			return getPassword(parse);
		} else {
			return null;
		}
	}
	
	private String getRegPassword(String message) {
		String[] parse = message.split("\"");
		
		if (parse.length == 17) {
			return getPassword(parse);
		} else {
			return null;
		}
	}
	
	private String getPassword(String[] parse) {
		return parse[9];
	}
	
	public void shutDown() {
		socket.close();
	}
}
