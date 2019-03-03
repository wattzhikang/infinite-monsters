package server_core;

public class Game {
	private DBInterface db;

	public Game(DBInterface db) {
		this.db = db;
	}
	
	public void shutDown() {
		return;
	}
}
