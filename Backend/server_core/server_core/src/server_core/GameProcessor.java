package server_core;

import java.util.concurrent.BlockingQueue;

public class GameProcessor extends Thread implements Killable {
	private BlockingQueue<String> messageQueue;
	
	public GameProcessor(BlockingQueue<String> inputQueue) {
		messageQueue = inputQueue;
	}
	
	public void run() {
		//meh
	}
	
	public void shutDown() {
		//meh
	}
}
