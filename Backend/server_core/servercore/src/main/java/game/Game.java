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
		flushTiles(tiles, null);
	}
	
	/**
	 * Replaces outdated tiles with modified ones, flushing updates on all subscribers
	 * to all modified tiles except the chiefSubscriber.
	 * @param tiles The tiles to be modified
	 * @param chiefSubscriber If null, all affected subscriptions will be flushed
	 */
	void flushTiles(Collection<Tile> tiles, Subscription chiefSubscriber) {
		//this loop places all the tiles on the plots that they go to, then enqueues updates
		Collection<Subscription> subscribers = new LinkedList<Subscription>();
		for (Tile tile : tiles) {
			if (tile != null) {
				Plot plot = dungeons.get(new Long(tile.getLocation().getDungeon()))
					.get(tile.getLocation())
				;
				
				plot.setPlot(tile);
				
				for (Subscription subscriber : plot.getSubscribers()) {
					subscriber.enqueueUpdate(tile);
					if (!subscribers.contains(subscriber)) {
						subscribers.add(subscriber);
					}
				}
			}
		}
		
		//flushes updates to all subscribers except chiefSubscriber, which, if not null,
		//means that the calling subscription intends to flush itself
		for (Subscription subscriber : subscribers) {
			if (subscriber != chiefSubscriber) {
				subscriber.flushUpdates();
			}
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
	
	/**
	 * Gets a subscription for the given client identity
	 * @param key
	 * @return
	 */
	public int getSubscription(ClientKey key) {
		int ID = key.addSubscriber(new Subscription(this, key.getUserLink()));
		adjustBounds(key.getSubscriber(ID), null, db.lastSubscriptionBounds(key));
		return ID;
	}
	
	/**
	 * Returns tiles at the desired locations, pulling from the database if necessary. This is a valid way to
	 * load tiles from the database.
	 * @param locations
	 * @return a Collection of tiles retrieved
	 */
	Collection<Tile> getTiles(Collection<Position> locationsArg) {
		Collection<Position> locations = new LinkedList<Position>();
		locations.addAll(locationsArg);
		Collection<Tile> returnTiles = new LinkedList<Tile>();
		
		//first get tiles that are already cached
		for (Position location : locations) {
			//add a map for this dungeon if there isn't one
			if (!dungeons.containsKey(new Long(location.getDungeon()))) {
				dungeons.put(new Long(location.getDungeon()), new HashMap<Position, Plot>());
			}
			
			//if the tile is already cached, be sure, add it to the list of tiles to return
			//and check off the location
			if (dungeons.get(new Long(location.getDungeon())).containsKey(location)) {
				returnTiles.add(dungeons.get(new Long(location.getDungeon())).get(location).getPlot());
				locations.remove(location);
			}
		}
		
		//check database for remaining locations
		Collection<Tile> dbTiles = db.getTiles(locations);
		for (Tile tile : dbTiles) {
			dungeons.get(new Long(tile.getLocation().getDungeon())).put(tile.getLocation(), new Plot(tile));
			locations.remove(tile.getLocation());
		}
		returnTiles.addAll(dbTiles);
		
		//the remaining tiles are in neither the database nor the cache. They are null tiles
		for (Position location : locations) {
			Tile tile = new Tile(location);
			dungeons.get(new Long(location.getDungeon()))
				.put(
					location,
					new Plot(tile)
				)
			;
			returnTiles.add(tile);
		}
		
		
		return returnTiles;
	}
	
	/**
	 * Registers an identity with the database with the given username and password
	 * @param username
	 * @param password
	 * @return
	 */
	public boolean register(String username, String password) {
		if (db.register(username, password)) {
			return true;
		} else {
			return false;
		}
	}
	
	/**
	 * Gets an identity for the given username and password
	 * @param username
	 * @param password
	 * @param client
	 * @return Returns null if authentication fails
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
	
	/**
	 * Flushes all tiles to the database
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
