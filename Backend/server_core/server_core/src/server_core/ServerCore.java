package server_core;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;

public class ServerCore {
	
	public static final Client POISONPILL = new Client(null); //DO NOT START THIS CLIENT
	public static final int PORT = 10042;

	public static void main(String[] args) {
		BlockingQueue<Client> clientQueue = new LinkedBlockingDeque<Client>();
		
		ConnectionListener socketListener = new ConnectionListener(clientQueue);
		GameProcessor game = new GameProcessor(clientQueue);
		
		socketListener.start();
		game.start();
		
		Runtime.getRuntime().addShutdownHook(new ShutdownHook(socketListener));
	}
	
	private static class ShutdownHook extends Thread {
		ConnectionListener socketListener;
		
		public ShutdownHook(ConnectionListener socketListener) {
			this.socketListener = socketListener;
		}
		
		public void run() {
			System.out.println("Shutting Down...");
			socketListener.shutDown();
		}
	}
}