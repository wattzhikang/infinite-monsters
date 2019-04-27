package server_core;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;

import game.DBAdapter;
import game.DBDummy;
import game.DBInterface;
import game.Game;

import java.sql.*;

/**
 * Main class of the Intermon Server Core
 * @author zjwatt
 *
 */
public class ServerCore {
	public static final int PORT_TCP = 10042;
	public static final int PORT_WEB = 10043;
	public static final String DEBUG = "debug";

	public static void main(String[] args) {
		for (String string : args) {
			System.out.println(string);
		}
		DBInterface db = null;
		if (args.length > 0 && args[0].equals(DEBUG)) {
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
				if (game != null) {
					game.shutDown();
				} else {
					System.err.println("Game object prematurely culled");
				}
			} catch (InterruptedException e) {
				System.out.println("Unable to shut down gracefully - Interrupted during shutdown process");
			} finally {
				db.close();
			}
			System.out.println("Graceful shutdown complete");
		}
	}
}