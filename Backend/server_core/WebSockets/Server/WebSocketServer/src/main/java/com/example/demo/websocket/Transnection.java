package com.example.demo.websocket;

import javax.websocket.Session;

import java.io.IOException;
import java.io.EOFException;
import java.net.*;

public class Transnection {
	
	public static final String SERVER_CORE_URL = "localhost";
	public static final int SERVER_CORE_PORT = 10042;
	
	private SocketAdapter socket;
	
	private Reader reader;
	
	public Transnection(Session session) {
		try {
			socket = new SocketAdapter(new Socket(SERVER_CORE_URL, SERVER_CORE_PORT));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		reader = new Reader(socket, session);
		reader.start();
	}
	
	public void onMessage(String message) {
		socket.writeString(message);
	}
	
	public void close() {
		reader.shutDown();
	}
	
	private class Reader extends Thread {
		private SocketAdapter socket;
		private Session session;
		
		public Reader(SocketAdapter socket, Session session) {
			this.socket = socket;
			this.session = session;
		}
		
		public void run() {
			try {
				while (true) {
					String message = socket.readString();
					synchronized (session) {
						session.getBasicRemote().sendText(message);
					}
				}
			} catch (IOException e) {
				if (!(e instanceof EOFException || e instanceof SocketException)) {
					e.printStackTrace();
				}
			} finally {
				this.shutDown();
			}
		}

		public void shutDown() {
			if (!socket.isClosed()) {
				socket.close();
			}
		}
	}
}
