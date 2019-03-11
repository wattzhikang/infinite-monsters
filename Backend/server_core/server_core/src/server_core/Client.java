package server_core;

import java.io.EOFException;
import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import com.mysql.cj.protocol.Message;

import game.ClientKey;
import game.Game;
import game.Watcher;

public class Client extends Thread {
	private SocketAdapter socket;
	private Game game;
	
	private BlockingQueue<SocketMessage> queue;
	private ClientListener in;
	
	private ClientKey key;
	private Map<Integer, Watcher> subscriptions;
	
	boolean active = true;
	
	public Client(SocketAdapter socket, Game game) {
		this.socket = socket;
		this.game = game;
		
		queue = new LinkedBlockingQueue<SocketMessage>();
		in = (socket != null) ? 
				new ClientListener(socket, queue) : null;
				
		subscriptions = new HashMap<Integer, Watcher>();
	}

	public void run() {
		in.start();
		
		try {
			
			SocketMessage message;
			while (active) {
				message = queue.take();
				message.getStrategy().takeAction(game, socket, this);
			}
			
		} catch (InterruptedException e) {
			//TODO make sure the whole program doesn't break over this
		}
	}
	
	public void setKey(ClientKey key) {
		this.key = key;
	}
	
	public ClientKey getKey() {
		return key;
	}
	
	public void addSubscription(Watcher subscription) {
		subscriptions.put(new Integer(subscription.getId()), subscription);
	}
	
	public Watcher getSubscription(int id) {
		return subscriptions.get(new Integer(id));
	}
	
	public void shutDown() throws InterruptedException {
		in.shutDown();
		in.join(500);
	}
	
	public void enqueueDeltaFrame(DeltaFrame frame) {
		//queue.offer(new SocketMessage(SocketMessage.MessageOrigin.SERVER, frame.toString()));
		queue.add(new SocketMessage(frame));
		//System.out.println(frame);
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
					try {
						message = new SocketMessage(clientMessage);
					} catch (Exception e) {
						System.out.println("Malformed input");
						continue;
					}
					queue.offer(message);
				}
			} catch (IOException e) {
				if (e instanceof EOFException) {
					if (!socket.isClosed()) {
						this.shutDown();
					}
					queue.offer(new SocketMessage());
				} else {
					e.printStackTrace();
				}
			}
		}
		
		public void shutDown() {
			socket.close();
			active = false;
		}
	}

	//{"requestType":"registration","username":"user1","password":"sunshine","privileges":"player"}
	//{ requestType : registration , username : user1 , password : sunshine , privileges : player }
	//0 1           2 3            4 5        6 7     8 9       10 11      12 13        14 15     16
	
	//{"requestType":"authentication","username":"user1","password":"sunshine"}
	//{ requestType : authentication , username : user1 , password : sunshine }
	//0 1           2 3              4 5        6 7     8 9       10 11       12
}
