package game;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

import server_core.Client;
import server_core.DeltaFrame;

/**
 * Represents a client's subscription to a map area. Provides features
 * for manipulating that area.
 * @author Zachariah Watt
 *
 */
public class Subscription {
	long dungeon;
	Tile[][] map;
	Position player;
	
	SubscriptionLock lock;
	
	Map<Subscription, Integer> overlaps;
	ArrayList<Subscription> lockHierarchy;
	
	ArrayList<Tile> queuedUpdates;
	Position queuedPlayerPosition;
	
	Client client;
	
	Game game;
	
	int subscriptionID;
	
	RectangleBoundary bounds;
	
	/**
	 * Constructs a new Subscription with the given game to draw
	 * data from and the given client to push updates to
	 * @param game
	 * @param client
	 */
	public Subscription(Game game, Client client) {
		this.lock = new SubscriptionLock();
		this.overlaps = new HashMap<Subscription, Integer>();
		this.lockHierarchy = new ArrayList<Subscription>();
		addNeighbor(this);
		this.queuedUpdates = new ArrayList<Tile>();
		this.client = client;
		this.game = game;
	}
	
	/**
	 * Sets the Subscription ID (see communication protocol documentation)
	 * @param ID
	 */
	void setID(int ID) {
		this.subscriptionID = ID;
	}
	
	/**
	 * Returns the lock for this Subscription
	 * @return
	 */
	SubscriptionLock getLock() {
		return lock;
	}
	
	/**
	 * Changes the lock of this subscription
	 * @param lock
	 */
	void updateLock(SubscriptionLock lock) {
		this.lock = lock;
	}
	
	/**
	 * Returns the area of this subscription
	 * @return
	 */
	RectangleBoundary getBounds() {
		return bounds;
	}
	
	/**
	 * Returns the dungeon this subscription is located in
	 * @return
	 */
	public long dungeon() {
		return dungeon;
	}
	
	/**
	 * Flushes this subscription's entire map to the game
	 */
	void unsubscribe() {
		LinkedList<Tile> allTiles = new LinkedList<Tile>();
		for (Tile[] row : map) {
			for (Tile tile : row) {
				allTiles.add(tile);
			}
		}
		game.flushTiles(allTiles);
	}
	
	/**
	 * Called when the client wants to move this
	 * subscription from one location to another
	 * @param nBounds Desired boundary of subscription
	 * @param nPlayerLocation Desired new location of player
	 */
	public void move(RectangleBoundary nBounds, Position nPlayerLocation) {
		lockNeighbors();
		
		Collection<Tile> buffer = new LinkedList<Tile>();
		
		Tile nTile = map[player.getX()][player.getY()];
		Tile oTile = map[nPlayerLocation.getX()][nPlayerLocation.getY()];
		
		String tmp = oTile.getCharacter();
		oTile.setCharacter(nTile.getCharacter());
		nTile.setCharacter(tmp);
		
		buffer.add(nTile);
		enqueueUpdate(nTile);
		buffer.add(oTile);
		enqueueUpdate(oTile);
		
		game.flushTiles(buffer, this);
		
		game.adjustBounds(this, bounds, nBounds);
		
		unlockNeighbors();
	}
	
	/**
	 * Updates tiles in this Subscription
	 * @param tiles
	 */
	public void reverseDelta(Collection<Tile> tiles) {
		lockNeighbors();
		
		game.flushTiles(tiles);
		
		unlockNeighbors();
	}
	
	/**
	 * Notifies this Subscription that it has a new neighbor, and must
	 * lock it whenever it attempts to modify anything
	 * @param subscription
	 */
	void addNeighbor(Subscription subscription) {
		if (overlaps.containsKey(subscription)) {
			int i = overlaps.remove(subscription).intValue();
			overlaps.put(subscription, new Integer(i++));
		} else {
			overlaps.put(subscription, new Integer(1));
			lockHierarchy.add(subscription);
			int i = lockHierarchy.size() - 1, j = lockHierarchy.size() - 2;
			while (j >= 0) {
				if (lockHierarchy.get(i).hashCode() < lockHierarchy.get(j).hashCode()) {
					Subscription tmp = lockHierarchy.get(i);
					lockHierarchy.set(i, lockHierarchy.get(j));
					lockHierarchy.set(j, tmp);
				}
				j--;
			}
		}
	}
	
	/**
	 * Notifies this subscription that it no longer has to lock this
	 * neighbor if it modifies anything
	 * @param subscription
	 */
	void removeNeighbor(Subscription subscription) {
		if (overlaps.get(subscription).intValue() > 1) {
			int i = overlaps.remove(subscription).intValue();
			overlaps.put(subscription, new Integer(i--));
		} else {
			overlaps.remove(subscription);
			lockHierarchy.remove(subscription);
		}
	}
	
	/**
	 * Enqueues an update for the Client
	 * @param tile
	 */
	void enqueueUpdate(Tile tile) {
		queuedUpdates.add(tile);
		
		assert tile != null;
		
		if (tile.getCharacter() != null /* && tile.getCharacter().equals(client.getKey().getUser())*/) {
			queuedPlayerPosition = tile.getLocation();
			System.out.println("Player " + tile.getCharacter() + " at " + tile.getLocation().toString());
		}
	}
	
	/**
	 * Flushes all enqueued updates to the client
	 */
	void flushUpdates() {
		//TODO this method should be able to handle queuedUpdates being null
		for (Tile tile : queuedUpdates) {
			int nX = tile.getLocation().getX() - bounds.getLowerLeft().getX();
			int nY = tile.getLocation().getY() - bounds.getLowerLeft().getY();
			
			map[nY][nX] = tile;
		}
		
		client.enqueueDeltaFrame(new DeltaFrame(bounds, queuedUpdates, false, subscriptionID));
		queuedUpdates = new ArrayList<Tile>();
		
		if (queuedPlayerPosition != null) {
			player = queuedPlayerPosition;
			queuedPlayerPosition = null;
			
		}
	}
	
	/**
	 * Locks this subscription
	 */
	void lock() {
		lock.lock();
	}
	
	/**
	 * Unlocks this subscription
	 */
	void unlock() {
		lock.unlock();
	}
	
	/**
	 * Locks all neighbors in accordance with the standard
	 * lock hierarchy
	 */
	private void lockNeighbors() {
		for (Subscription subscription : lockHierarchy) {
			subscription.lock();
		}
	}
	
	/**
	 * Unlocks all neighbors in accordance with the standard
	 * lock hierarchy
	 */
	private void unlockNeighbors() {
		//I'd love to use a foreach loop, but I don't know
		//how to iterate backwards
		for (int i = lockHierarchy.size() - 1; i >= 0; i--) {
			lockHierarchy.get(i).unlock();
		}
	}
	
	/**
	 * Resets the boundaries of this subscription
	 * @param nBounds
	 */
	void setBounds(RectangleBoundary nBounds) {
		bounds = nBounds;
		dungeon = bounds.getDungeon();
		
		Tile[][] nMap = new Tile[bounds.getHeight()][bounds.getWidth()];
		if (map != null) {
			for (Tile[] row : map) {
				for (Tile tile : row) {
					int nX = tile.getLocation().getX() - bounds.getLowerLeft().getX();
					int nY = tile.getLocation().getY() - bounds.getLowerLeft().getY();
					
					if (nY > 0 && nY < nMap.length && nX > 0 && nX < nMap[0].length) {
						nMap[nY][nX] = tile;
					}
				}
			}
		}
		map = nMap;
	}
}
