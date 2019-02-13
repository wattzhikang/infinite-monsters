package server_core;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;

public class ServerCore {
	
	public static final String POISONPILL = "POISONPILL";

	public static void main(String[] args) {
		BlockingQueue<String> messageQueue = new LinkedBlockingDeque<String>();
		
		ConnectionListener socketListener = new ConnectionListener(messageQueue);
		GameProcessor game = new GameProcessor(messageQueue);
		
		socketListener.start();
		game.start();
		
		Runtime.getRuntime().addShutdownHook(new ShutdownHook(messageQueue, socketListener));
	}
	
	private static class ShutdownHook extends Thread {
		BlockingQueue<String> messageQueue;
		ConnectionListener socketListener;
		
		public ShutdownHook(BlockingQueue<String> messageQueue, ConnectionListener socketListener) {
			this.messageQueue = messageQueue;
			this.socketListener = socketListener;
		}
		
		public void run() {
			messageQueue.offer("PoisonPill");
			socketListener.shutDown();
		}
	}

}
