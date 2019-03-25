package game;

import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import game.ClientKey.Privileges;
import server_core.Client;

public class Game {
	private DBInterface db;
	
	/*
	 * Since anyone can construct keys with whatever privileges he likes,
	 * this is the master record of all users who have actually logged in
	 */
	private Map<ClientKey, Object> keys;
	private Map<Coordinates, WatchedLocation> subscriptions;
	
	//problem: the map within the map will probably be locked
	private Map<Long, Map<Coordinates, Plot>> dungeons;
	

	public Game(DBInterface db) {
		this.db = db;
		keys = new ConcurrentHashMap<ClientKey, Object>();
		subscriptions = new ConcurrentHashMap<Coordinates, WatchedLocation>();
		
		dungeons = new ConcurrentHashMap<Long, Map<Coordinates, Plot>>();
	}
	
	void flushTiles(Collection<Tile> tiles) {
		//assume that tiles exist in the map
		for (Tile tile : tiles) {
			dungeons.get(new Long(tile.getLocation().getDungeon()))
				.get(tile.getLocation())
				.setPlot(tile)
			;
		}
	}
	
	void adjustBounds(Subscription subscription, RectangleBoundary oldBounds, RectangleBoundary newBounds) {
		/*
		 * Plant new stakes and dig up old ones. If any overlap is detected, create a new subscriptionLock
		 * and reset the locks for both subscriptions
		 */
		
		Collection<Coordinates> nTiles = oldBounds.getDifference(newBounds);
		
		getTiles(nTiles); //load tiles if they aren't already loaded
		
		//plant new stakes
		for (Coordinates position : nTiles) {
			Plot plot = dungeons.get(new Long(position.getDungeon())).get(position);
			if (plot.getStake() != null && plot.getStake() == subscription.getLock()) {
				overlapDetected(subscription, plot.getStake().getSubscribers(), plot.getStake());
			} else {
				plot.setStake(subscription.getLock());
			}
		}
		
		//remove old stakes, deal with the possibility that you aren't the only subscriber
		
	}
	
	private void overlapDetected(Subscription sub1, Collection<Subscription> sub2, SubscriptionLock otherLock) {
		synchronized (otherLock) {
			for (Subscription subscription : sub2) {
				subscription.updateLock(sub1.getLock());
			}
		}
	}
	
	/**
	 * Returns tiles at the desired locations, pulling from the database if necessary
	 */
	Collection<Tile> getTiles(Collection<Coordinates> locations) {
		return null;
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
	
	public Watcher getSubscription(ClientKey key) {
		Watcher subscription = null;
		if (keys.containsKey(key)) {
			subscription = new Watcher(key.getUserLink(), key.getNumSubscriptions());
			key.addSubscription(subscription);
			
			RectangleBoundary corners = db.lastSubscriptionBounds(key);
			subscription.setBounds(corners);
			
			Collection<Coordinates> mapping = corners.getBetween();
			Collection<Tile> tiles = db.getTiles(mapping);
			for (Tile tile : tiles) {
				WatchedLocation watched;
				if (subscriptions.containsKey(tile.getLocation())) {
					watched = subscriptions.get(tile.getLocation());
				} else {
					watched = new WatchedLocation(tile);
					subscriptions.put(tile.getLocation(), watched);
				}
				watched.addWatcher(subscription);
				watched.sendDeltas();
			}
		}
		return subscription;
	}
	
	public boolean moveSubscription(ClientKey key, Watcher sub, RectangleBoundary bounds, Coordinates oldLocation, Coordinates newLocation) {
		boolean success = false;
		if (keys.containsKey(key)) {
			switch (key.getPriveleges()) {
			case PLAYER:
				if (sub.getBounds().getUnitChange(bounds) <= 1) {
					sub.setBounds(bounds);
					
					/*
					Collection<Coordinates> oldLocations = sub.getBounds().getDifference(bounds);
					for (Coordinates location : oldLocations) {
						subscriptions.get(location).removeWatcher(sub);
					}
					Collection<Coordinates> newLocations = bounds.getDifference(sub.getBounds());
					Collection<Tile> tiles = db.getTiles(newLocations);
					for (Tile tile : tiles) {
						WatchedLocation watched;
						if (subscriptions.containsKey(tile.getLocation())) {
							watched = subscriptions.get(tile.getLocation());
						} else {
							watched = new WatchedLocation(tile);
							subscriptions.put(tile.getLocation(), watched);
						}
						watched.addWatcher(sub);
					}
					*/
					
					WatchedLocation oldCharacter = subscriptions.get(oldLocation);
					WatchedLocation newCharacter = subscriptions.get(newLocation);
					
					newCharacter.setTile(new Tile(
							newCharacter.getTile().getLocation(),
							newCharacter.getTile().isWalkable(),
							newCharacter.getTile().getTerrain(),
							newCharacter.getTile().getObject(),
							oldCharacter.getTile().getCharacter()
					));
					oldCharacter.setTile(new Tile(
							oldCharacter.getTile().getLocation(),
							oldCharacter.getTile().isWalkable(),
							oldCharacter.getTile().getTerrain(),
							oldCharacter.getTile().getObject(),
							null
					));
					
					Collection<WatchedLocation> places = subscriptions.values();
					for (WatchedLocation place : places) {
						place.sendDeltas();
					}
				}
				success = true;
				break;
			case SPECTATOR:
			case DESIGNER:
				//let's just have spectators do whatever they want
				break;
			}
		}
		return success;
	}
	//https://stackoverflow.com/questions/6992608/why-there-is-no-concurrenthashset-against-concurrenthashmap
	/*
	 * This method should join all of its threads to ensure complete shutdown
	 */
	public void shutDown() {
		List<Tile> tiles = new LinkedList<Tile>();
		Collection<WatchedLocation> locations = subscriptions.values();
		for (WatchedLocation location : locations) {
			tiles.add(location.getTile());
		}
		db.updateTiles(tiles);
		
		
		Collection<ClientKey> allKeys = keys.keySet();
		for (ClientKey key : allKeys) {
			Collection<Watcher> subscriptions = key.getSubscriptions();
			for (Watcher subscription : subscriptions) {
				db.updateSubscriptionBounds(key, subscription.getBounds());
			}
		}
		return;
	}
}
