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

import game.ClientKey;
import game.Game;

/**
 * Handles and represents a connection to a client
 * @author Zachariah Watt
 *
 */
public class Client extends Thread {
	private SocketAdapter socket;
	private Game game;
	
	private BlockingQueue<SocketMessage> queue;
	private ClientListener in;
	
	private ClientKey key;
	
	boolean active = true;
	
	/**
	 * 
	 * @param socket
	 * @param game
	 */
	public Client(SocketAdapter socket, Game game) {
		this.socket = socket;
		this.game = game;
		
		queue = new LinkedBlockingQueue<SocketMessage>();
		in = (socket != null) ? 
				new ClientListener(socket, queue) : null
		;
	}

	/**
	 * Begin accepting messages from the client
	 */
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
	
	/**
	 * Shuts down the client and closes its socket
	 * @throws InterruptedException
	 */
	public void shutDown() throws InterruptedException {
		active = false;
		in.shutDown();
		in.join(500);
	}
	
	/**
	 * Enqueue a Delta Frame that will be sent to the client
	 * as soon as possible
	 * @param frame
	 */
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

		@Override
		public String toString() {
			return ((active) ? "active " : "inactive ") + "Client at " + socket.toString();
		}
	}
}
