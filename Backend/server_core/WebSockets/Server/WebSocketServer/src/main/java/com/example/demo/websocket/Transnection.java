package com.example.demo.websocket;

import javax.websocket.Session;

import java.io.IOException;
import java.net.*;

public class Transnection {
	
	public static final int SERVER_CORE_PORT = 10042;
	
	private SocketAdapter socket;
	
	private Reader reader;
	
	public Transnection(Session session) {
		try {
			socket = new SocketAdapter(new Socket("localhost", SERVER_CORE_PORT));
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
		//TODO
	}
	
	private class Reader extends Thread {
		private SocketAdapter socket;
		private Session session;
		
		public Reader(SocketAdapter socket, Session session) {
			this.socket = socket;
			this.session = session;
		}
		
		public void run() {
			while(true) {
				try {
					String message = socket.readString();
					synchronized (session) {
						session.getBasicRemote().sendText(message);
					}
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}
}
