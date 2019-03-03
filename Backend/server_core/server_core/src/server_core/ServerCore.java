package server_core;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;

import java.sql.*;

public class ServerCore {
	public static final int PORT = 10042;
	public static final String DEBUG = "debug";

	public static void main(String[] args) {
		DBInterface db = null;
		if (args.length > 1 && args[1].equals(DEBUG)) {
			db = new DBDummy();
		} else {
			db = new DBAdapter();
		}
		
		Game game = new Game(db);
		
		ConnectionListener socketListener = new ConnectionListener(db, game);
		
		socketListener.start();
		
		Runtime.getRuntime().addShutdownHook(new ShutdownHook(socketListener, db, game));
	}
	
	private static class ShutdownHook extends Thread {
		ConnectionListener socketListener;
		DBInterface db;
		Game game;
		
		public ShutdownHook(ConnectionListener socketListener, DBInterface db, Game game) {
			this.socketListener = socketListener;
			this.db = db;
		}
		
		public void run() {
			System.out.println("Shutting Down...");
			try {
				socketListener.shutDown();
				game.shutDown();
			} catch (InterruptedException e) {
				System.out.println("Unable to shut down gracefully - Interrupted during shutdown process");
			} finally {
				db.close();
			}
			System.out.println("Graceful shutdown complete");
		}
	}
}