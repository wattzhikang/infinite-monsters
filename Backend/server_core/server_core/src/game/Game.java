package game;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import game.ClientKey.Privileges;
import server_core.Client;

/**
 * This class holds a secondary cache of game information, and handles
 * conflicts between different Subscriptions.
 * 
 * This class is intended to be like Air Traffic Control. It doesn't do
 * any flying, it just makes sure that moving places don't collide.
 * @author Zachariah Watt
 */
public class Game {
	private DBInterface db;
	
	/*
	 * Since anyone can construct keys with whatever privileges he likes,
	 * this is the master record of all users who have actually logged in
	 */
	private Map<ClientKey, Object> keys;
	
	/**
	 * A Map of all dungeons within the game. Each dungeon is itself, of course,
	 * a map unto itself.
	 */
	private Map<Long, Map<Position, Plot>> dungeons;
	

	/**
	 * Constructs a new Game using the given database
	 * @param db
	 */
	public Game(DBInterface db) {
		this.db = db;
		keys = new ConcurrentHashMap<ClientKey, Object>();
		
		dungeons = new HashMap<Long, Map<Position, Plot>>();
	}
	
	/**
	 * Replace outdated tiles with modified ones
	 * @param tiles
	 */
	void flushTiles(Collection<Tile> tiles) {
		//assume that tiles exist in the map
		for (Tile tile : tiles) {
			dungeons.get(new Long(tile.getLocation().getDungeon()))
				.get(tile.getLocation())
				.setPlot(tile)
			;
		}
	}
	
	/**
	 * Alters the Subscription's footprint on the map, placing the SubscriptionLock where it is supposed
	 * to be and removing it from areas where the Subscription no longer has a presence.
	 * @param subscription
	 * @param oldBounds
	 * @param newBounds
	 */
	void adjustBounds(Subscription subscription, RectangleBoundary oldBounds, RectangleBoundary newBounds) {
		Collection<Position> nTiles = null;
		if (oldBounds != null) {
			nTiles = oldBounds.getDifference(newBounds);
		} else {
			nTiles = newBounds.getBetween();
		}
		
		getTiles(nTiles); //load tiles if they aren't already loaded
		
		//plant new stakes
		for (Position position : nTiles) {
			Plot plot = dungeons.get(new Long(position.getDungeon())).get(position);
			synchronized (plot) {
				plot.addSubscriber(subscription);
				subscription.enqueueUpdate(plot.getPlot());
			}
		}
		
		if (oldBounds != null) {
			//remove old stakes
			Collection<Position> oTiles = newBounds.getDifference(oldBounds);
			for (Position position : oTiles) {
				Plot plot = dungeons.get(new Long(position.getDungeon())).get(position);
				synchronized (plot) {
					plot.removeSubscriber(subscription);
				}
			}
		}
		
		subscription.setBounds(newBounds);
		
		subscription.flushUpdates();
	}
	
	public int getSubscription(ClientKey key) {
		int ID = key.addSubscriber(new Subscription(this, key.getUserLink()));
		adjustBounds(key.getSubscriber(ID), null, db.lastSubscriptionBounds(key));
		return ID;
	}
	
	/*
	 * Obsolete since the getLocks() method
	 */
	private void overlapDetected(Subscription sub1, Collection<Subscription> sub2, SubscriptionLock otherLock) {
		for (Subscription subscription : sub2) {
			subscription.updateLock(sub1.getLock());
		}
	}
	
	private void severOverlap(Subscription subscription, SubscriptionLock lock) {
		lock.removeSubscriber(subscription);
		
		SubscriptionLock nLock = new SubscriptionLock();
		subscription.updateLock(nLock);
		nLock.addSubscriber(subscription);
	}
	
	/**
	 * Returns tiles at the desired locations, pulling from the database if necessary. This is a valid way to
	 * load tiles from the database.
	 * @param locations
	 * @return a Collection of tiles retrieved
	 */
	Collection<Tile> getTiles(Collection<Position> locations) {
		
		Collection<Position> dbLocations = new LinkedList<Position>();
		Collection<Tile> tiles = new LinkedList<Tile>();
		for (Position location : locations) {
			if (!dungeons.containsKey(new Long(location.getDungeon()))) {
				dungeons.put(new Long(location.getDungeon()), new HashMap<Position, Plot>());
			}
			if (!dungeons.get(new Long(location.getDungeon())).containsKey(location)) {
				dbLocations.add(location);
			} else {
				tiles.add(dungeons.get(new Long(location.getDungeon())).get(location).getPlot());
			}
		}
		
		Collection<Tile> dbTiles = db.getTiles(dbLocations);
		for (Tile tile : dbTiles) {
			dungeons.get(new Long(tile.getLocation().getDungeon())).put(tile.getLocation(), new Plot(tile));
		}
		tiles.addAll(dbTiles);
		
		
		return tiles;
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
	public ClientKey authenticate(String username, String password, Client client) {
		ClientKey key = null;
		if (db.login(username, password)) {
			ClientKey.Privileges privileges = ClientKey.Privileges.PLAYER;
			key = new ClientKey(username, client, privileges);
			if (!keys.containsKey(key)) {
				keys.put(key, key);
			}
		}
		return key;
	}
	//https://stackoverflow.com/questions/6992608/why-there-is-no-concurrenthashset-against-concurrenthashmap
	/*
	 * This method should join all of its threads to ensure complete shutdown
	 */
	public void shutDown() {
		//TODO
//		List<Tile> tiles = new LinkedList<Tile>();
//		Collection<WatchedLocation> locations = subscriptions.values();
//		for (WatchedLocation location : locations) {
//			tiles.add(location.getTile());
//		}
//		db.updateTiles(tiles);
//		
//		
//		Collection<ClientKey> allKeys = keys.keySet();
//		for (ClientKey key : allKeys) {
//			Collection<Watcher> subscriptions = key.getSubscriptions();
//			for (Watcher subscription : subscriptions) {
//				db.updateSubscriptionBounds(key, subscription.getBounds());
//			}
//		}
//		return;
	}
}
