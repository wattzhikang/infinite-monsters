package game;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;

import server_core.Client;

class WatchedLocation {
	private Collection<Watcher> clients;
	private Tile tile;
	
	public WatchedLocation(Tile tile) {
		this.tile = tile;
		clients = new LinkedList<Watcher>();
	}
	
	public void addWatcher(Watcher client) {
		clients.add(client);
		Collection<Tile> tiles = new ArrayList<Tile>();
		tiles.add(tile);
		client.sendDelta(tiles);
	}
	
	public void removeWatcher(Watcher client) {
		clients.remove(client);
	}
	
	private void sendDeltas() {
		Collection<Tile> tiles = new ArrayList<Tile>();
		tiles.add(tile);
		for (Watcher client : clients) {
			client.sendDelta(tiles);
		}
	}

	public Tile getTile() {
		return tile;
	}

	/*
	 * Send delta here
	 */
	public void setTile(Tile tile) {
		this.tile = tile;
		sendDeltas();
	}
}
