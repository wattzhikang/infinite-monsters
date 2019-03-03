package server_core;

import java.io.EOFException;
import java.io.IOException;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import com.mysql.cj.protocol.Message;

public class Client extends Thread {
	private SocketAdapter socket;
	private DBInterface db;
	private Game game;
	
	private BlockingQueue<SocketMessage> queue;
	private ClientListener in;
//	private Game game;
	
	private enum RequestType { REGISTRATION, AUTHENTICATION, SUBSCRIPTION, MODIFICATION, LOGOUT, MALFORMED };
	
	public Client(SocketAdapter socket, DBInterface db, Game game) {
		this.socket = socket;
		this.db = db;
		this.game = game;
		
		queue = new LinkedBlockingQueue<SocketMessage>();
		in = new ClientListener(socket, queue);
	}

	public void run() {
		/*
		 * I bet this could be made a lot less ugly with the Strategy Pattern
		 * and maybe the Abstract Factory pattern
		 */
		in.start();
		
		try {
			
			SocketMessage message;
			
			String response = null;
			
			boolean active = true;
			
			while (active) {
				message = queue.take();
				
				switch (message.getOrigin()) {
					case CLIENT:
						
						switch (getRequestType(message.getMessage())) {
							case AUTHENTICATION:
								response = login(message, db);
								break;
							case MALFORMED:
								break;
							case MODIFICATION:
								break;
							case REGISTRATION:
								response = register(message, db);
								break;
							case SUBSCRIPTION:
								break;
							case LOGOUT:
								//TODO send acknowledgement
								/*
								 * ISSUE: sending an acknowledgement here would mean two places
								 * where writeString() is called, and thus two places for problems
								 * with the socket to occur. How to send the acknowledgement,
								 * then call shutdown()?
								 */
							default:
								break;
						}
						if (response != null) {
							socket.writeString(response);
							System.out.println("Response sent to " + socket.getHost() + ": " + response);
						} else {
							System.out.println("Malformed input from " + socket.getHost());
						}
						break;
					
					case SERVER:
						break;
					case POISON:
						//TODO save player information here
						active = false;
						break;
				}
				
				response = null; //garbage collection is wonderful
			}
			
		} catch (InterruptedException e) {
			//TODO make sure the whole program doesn't break over this
		}
	}
	
	public void shutDown() throws InterruptedException {
		in.shutDown();
		in.join();
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
					message = new SocketMessage(SocketMessage.MessageOrigin.CLIENT, clientMessage);
					queue.offer(message);
				}
			} catch (IOException e) {
				if (e instanceof EOFException) {
					if (!socket.isClosed()) {
						this.shutDown();
					}
					queue.offer(new SocketMessage(SocketMessage.MessageOrigin.POISON, null));
				} else {
					e.printStackTrace();
				}
			}
		}
		
		public void shutDown() {
			socket.close();
		}
	}
	
	private String login(SocketMessage message, DBInterface db) {
		String response = null;
		
		String username = getAuthUser(message.getMessage());
		String password = getAuthPassword(message.getMessage());
		if (db.login(username, password)) {
			response = "{\"loginSuccess\":\"true\"}";
		} else {
			response = "{\"loginSuccess\":\"false\"}";
		}
		
		return response;
	}
	
	private String register(SocketMessage message, DBInterface db) {
		String response = null;
		
		String username = getRegUser(message.getMessage());
		String password = getRegPassword(message.getMessage());
		if (db.register(username, password)) {
			response = "{\"registrationSuccess\":\"true\"}";
		} else {
			response = "{\"registrationSuccess\":\"false\"}";
		}
		
		return response;
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
