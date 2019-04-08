package com.example.demo.websocket;


import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
/**
 * 
 * @author Vamsi Krishna Calpakkam
 * @author Zachariah Watt
 *
 */
@ServerEndpoint("/websocket/{username}")
@Component
public class WebSocketServer {
    
    private static Map<Session, Transnection> serverCoreSockets = new HashMap<Session, Transnection>();
    
    private final Logger logger = LoggerFactory.getLogger(WebSocketServer.class);
    
    @OnOpen
    public void onOpen(
    	      Session session, 
    	      @PathParam("username") String username) throws IOException 
    {
        logger.info("Entered into Open");
		
        serverCoreSockets.put(session, new Transnection(session));
    }
 
    @OnMessage
    public void onMessage(Session session, String message) throws IOException 
    {
        // Handle new messages
    	logger.info("Entered into Message: Got Message:"+message);
    	
    	serverCoreSockets.get(session).onMessage(message);
    }
 
    @OnClose
    public void onClose(Session session) throws IOException
    {
    	logger.info("Closing a socket");
        
        for (Map.Entry<Session, Transnection> transnection : serverCoreSockets.entrySet()) {
        	transnection.getValue().close();
        }
    }
 
    @OnError
    public void onError(Session session, Throwable throwable) 
    {
        // Do error handling here
    	logger.info("Entered into Error");
    	
        for (Map.Entry<Session, Transnection> transnection : serverCoreSockets.entrySet()) {
        	transnection.getValue().close();
        }
    }
}

