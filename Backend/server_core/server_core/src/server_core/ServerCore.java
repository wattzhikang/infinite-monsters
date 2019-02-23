package server_core;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;

import java.sql.*;

public class ServerCore {
	
	public static final Client POISONPILL = new Client(null, null); //DO NOT START THIS CLIENT
	public static final int PORT = 10042;

	public static void main(String[] args) {
		DBInterface db = null;
		if (args.length > 1 && args[1].equals("debug")) {
			db = new DBAdapter();
		} else {
			db = new DBDummy();
		}
				
		BlockingQueue<Client> clientQueue = new LinkedBlockingDeque<Client>();
		
		ConnectionListener socketListener = new ConnectionListener(clientQueue, db);
		GameProcessor game = new GameProcessor(clientQueue);
		
		socketListener.start();
		game.start();
		
		Runtime.getRuntime().addShutdownHook(new ShutdownHook(socketListener, db));
	}
	
	private static class ShutdownHook extends Thread {
		ConnectionListener socketListener;
		DBInterface db;
		
		public ShutdownHook(ConnectionListener socketListener, DBInterface db) {
			this.socketListener = socketListener;
			this.db = db;
		}
		
		public void run() {
			System.out.println("Shutting Down...");
			socketListener.shutDown();
			db.close();
			try {
				Thread.sleep(500);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}