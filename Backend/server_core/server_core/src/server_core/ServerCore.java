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
		
		//wait for user to exit the program
		
		//shut down gracefully
		
		socketListener.shutDown();
		messageQueue.offer("PoisonPill");
	}

}
