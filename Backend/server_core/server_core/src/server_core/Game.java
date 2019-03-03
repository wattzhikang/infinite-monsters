package server_core;

import java.util.HashSet;
import java.util.Set;

public class Game {
	private DBInterface db;
	
	/*
	 * Since anyone can construct keys with whatever privileges he likes,
	 * this is the master record of all users who have actually logged in
	 */
	private Set<ClientKey> keys;

	public Game(DBInterface db) {
		this.db = db;
		keys = new HashSet<ClientKey>();
	}
	
	public boolean register(String username, String password) {
		if (db.register(username, password)) {
			return true;
		} else {
			return false;
		}
	}
	
	/*
	 * Returns a unique key to identify the user
	 */
	public ClientKey authenticate(String username, String password) {
		ClientKey key = null;
		if (db.login(username, password)) {
			ClientKey.Privileges privileges = ClientKey.Privileges.PLAYER;
			key = new ClientKey(username, privileges);
			if (!keys.contains(key)) {
				keys.add(key);
			}
		}
		return key;
	}
	
	/*
	 * This method should join all of its threads to ensure complete shutdown
	 */
	public void shutDown() {
		return;
	}
}
