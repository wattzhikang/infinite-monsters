package server_core;

import java.util.concurrent.BlockingQueue;

public class GameProcessor extends Thread implements Killable {
	private BlockingQueue<String> messageQueue;
	
	public GameProcessor(BlockingQueue<String> inputQueue) {
		messageQueue = inputQueue;
	}
	
	public void run() {
		String message = null;
		try {
			while (!((message = messageQueue.take()).equals(ServerCore.POISONPILL))) {
				System.out.print(message);
			}
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void shutDown() {
		messageQueue.offer(ServerCore.POISONPILL);
	}
}
