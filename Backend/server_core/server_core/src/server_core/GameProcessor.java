package server_core;

import java.util.concurrent.BlockingQueue;

public class GameProcessor extends Thread implements Killable {
	private BlockingQueue<Message> messageQueue;
	
	public GameProcessor(BlockingQueue<Message> inputQueue) {
		messageQueue = inputQueue;
	}
	
	public void run() {
		Message message = null;
		try {
			while (!((message = messageQueue.take()).equals(ServerCore.POISONPILL))) {
				message.getClient().enqueueMessage(message);
			}
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void shutDown() {
		messageQueue.offer(new Message(ServerCore.POISONPILL, null));
	}
}
