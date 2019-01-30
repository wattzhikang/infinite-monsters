package server_core;

import java.io.IOException;
import java.net.ServerSocket;

public class ConnectionListener extends Thread implements Killable {
	private ServerSocket listener;
	
	public ConnectionListener() {
		super();
		try {
			listener = new ServerSocket();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void run() {
		while(!listener.isClosed()) {
			
		}
	}
	
	public void shutDown() {
		try {
			listener.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
