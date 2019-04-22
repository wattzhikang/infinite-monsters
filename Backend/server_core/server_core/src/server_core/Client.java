package server_core;

import java.io.EOFException;
import java.io.IOException;
import java.net.SocketException;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import com.mysql.cj.protocol.Message;

import game.ClientKey;
import game.Game;

public class Client extends Thread {
	private SocketAdapter socket;
	private Game game;
	
	private BlockingQueue<SocketMessage> queue;
	private ClientListener in;
	
	private ClientKey key;
	
	boolean active = true;
	
	public Client(SocketAdapter socket, Game game) {
		this.socket = socket;
		this.game = game;
		
		queue = new LinkedBlockingQueue<SocketMessage>();
		in = (socket != null) ? 
				new ClientListener(socket, queue) : null
		;
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
	
	//deauthentication doesn't have to shut down the client, that can be done by shutdown
	public void shutDown() throws InterruptedException {
		in.shutDown();
		in.join(500);
	}
	
	public void enqueueDeltaFrame(DeltaFrame frame) {
		queue.add(new SocketMessage(frame));
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
					message = new SocketMessage(clientMessage);
					queue.offer(message);
				}
			} catch (IOException e) {
				/*
				 * Any time there is an exception, we want to shut down this client.
				 * The only difference between some unexpected exception and the normal
				 * case of a client terminating its connection is that if it is some
				 * unusual exception, we want to print the stack trace. If it isn't,
				 * then we don't.
				 */
				if (!(e instanceof EOFException || e instanceof SocketException)) {
					e.printStackTrace();
				}
				this.shutDown();
				queue.offer(new SocketMessage());
			}
		}
		
		public void shutDown() {
			if (!socket.isClosed()) {
				socket.close();
			}
		}
	}
}
