package server_core;

import java.util.concurrent.BlockingQueue;

public class GameProcessor extends Thread implements Killable {
	private BlockingQueue<Client> clientQueue;
	
	public GameProcessor(BlockingQueue<Client> clientQueue) {
		this.clientQueue = clientQueue;
	}
	
	public void run() {
		Client newClient = null;
		try {
			while ((newClient = clientQueue.take()) != null) {
				newClient.start();
			}
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void shutDown() {
		clientQueue.offer(ServerCore.POISONPILL);
	}
}
