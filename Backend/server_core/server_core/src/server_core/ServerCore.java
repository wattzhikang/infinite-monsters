package server_core;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;

public class ServerCore {

	public static void main(String[] args) {
		BlockingQueue<String> messageQueue = new LinkedBlockingDeque<String>();
		
		ConnectionListener socketListener = new ConnectionListener();
		GameProcessor game = new GameProcessor(messageQueue);
		
		socketListener.start();
		game.start();
		
		Runtime.getRuntime().addShutdownHook(new ShutdownHook(messageQueue, socketListener));
		
		while(true) {
			try {
				System.out.println(messageQueue.take());
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
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
