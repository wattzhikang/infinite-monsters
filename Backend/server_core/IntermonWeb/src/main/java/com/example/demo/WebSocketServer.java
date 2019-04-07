package com.example.demo;

import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;
import javax.websocket.*;

import org.springframework.stereotype.Component;

@ServerEndpoint("/websocket")
@Component
public class WebSocketServer {

	@OnOpen
	public void onOpen(Session session) {
		System.out.println("Landed a socket");
	}
	
	@OnMessage
	public void onMessage(Session session, String message) {
		
	}
}
