package server_core;

import java.io.IOException;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import com.mysql.cj.protocol.Message;

public class Client extends Thread implements Killable {
	private SocketAdapter socket;
	private DBInterface db;
	private BlockingQueue<SocketMessage> queue;
	private ClientListener in;
//	private Game game;
	
	private enum RequestType { REGISTRATION, AUTHENTICATION, SUBSCRIPTION, MODIFICATION, MALFORMED };
	
	public Client(SocketAdapter socket, DBInterface db) {
		this.socket = socket;
		this.db = db;
		queue = new LinkedBlockingQueue<SocketMessage>();
		in = new ClientListener(socket, queue);
//		this.game = game;
	}
	
	public void run() {
		
		in.start();
		
		SocketMessage message;
		
		String username = null;
		String password = null;
		String response = null;
		
		try {
			while (true) {
				message = queue.take();
				
				switch (message.getOrigin()) {
					case CLIENT:
						switch (getRequestType(message.getMessage())) {
							case AUTHENTICATION:
								username = getAuthUser(message.getMessage());
								password = getAuthPassword(message.getMessage());
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
								username = getRegUser(message.getMessage());
								password = getRegPassword(message.getMessage());
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
						break;
					case SERVER:
						break;
				}
				
				if (response != null) {
					socket.writeString(response);
					System.out.println("Response sent to " + socket.getHost() + ": " + response);
				} else {
					System.out.println("Malformed input");
					socket.close();
				}
				
				response = null; //garbage collection is wonderful
			}
		} catch (InterruptedException e) {
			
		} finally {
			
		}
	}
	
	public void shutDown() {
		//TODO
	}
	
	private class ClientListener extends Thread {
		private BlockingQueue<SocketMessage> queue;
		private SocketAdapter socket;
		
		public ClientListener(SocketAdapter socket, BlockingQueue<SocketMessage> queue) {
			this.socket = socket;
			this.queue = queue;
		}
		
		public void run() {
			String clientMessage;
			SocketMessage message;
			try {
			while (true) {
				clientMessage = socket.readString();
				
				System.out.println(clientMessage);
				
				message = new SocketMessage(SocketMessage.MessageOrigin.CLIENT, clientMessage);
				queue.offer(message);
			}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	//{"requestType":"registration","username":"user1","password":"sunshine","privileges":"player"}
	//{ requestType : registration , username : user1 , password : sunshine , privileges : player }
	//0 1           2 3            4 5        6 7     8 9       10 11      12 13        14 15     16
	
	//{"requestType":"authentication","username":"user1","password":"sunshine"}
	//{ requestType : authentication , username : user1 , password : sunshine }
	//0 1           2 3              4 5        6 7     8 9       10 11       12
	
	/*
	 * The following methods are a hacked-together means of reading the client's
	 * JSON strings until such time as Gson is integrated into the project
	 */
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
}
