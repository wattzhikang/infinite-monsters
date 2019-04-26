package game;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

import server_core.Client;
import server_core.DeltaFrame;

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
	
	public Subscription(Game game, Client client) {
		this.lock = new SubscriptionLock();
		this.overlaps = new HashMap<Subscription, Integer>();
		this.lockHierarchy = new ArrayList<Subscription>();
		addNeighbor(this);
		this.queuedUpdates = new ArrayList<Tile>();
		this.client = client;
		this.game = game;
	}
	
	void setID(int ID) {
		this.subscriptionID = ID;
	}
	
	SubscriptionLock getLock() {
		return lock;
	}
	
	void updateLock(SubscriptionLock lock) {
		this.lock = lock;
	}
	
	RectangleBoundary getBounds() {
		return bounds;
	}
	
	public long dungeon() {
		return dungeon;
	}
	void unsubscribe() {
		LinkedList<Tile> allTiles = new LinkedList<Tile>();
		for (Tile[] row : map) {
			for (Tile tile : row) {
				allTiles.add(tile);
			}
		}
		game.flushTiles(allTiles);
	}
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
	public void reverseDelta(Collection<Tile> tiles) {
		lockNeighbors();
		
		game.flushTiles(tiles);
		
		unlockNeighbors();
	}
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
	void removeNeighbor(Subscription subscription) {
		if (overlaps.get(subscription).intValue() > 1) {
			int i = overlaps.remove(subscription).intValue();
			overlaps.put(subscription, new Integer(i--));
		} else {
			overlaps.remove(subscription);
			lockHierarchy.remove(subscription);
		}
	}
	void enqueueUpdate(Tile tile) {
		queuedUpdates.add(tile);
		
		assert tile != null;
		
		if (tile.getCharacter() != null /* && tile.getCharacter().equals(client.getKey().getUser())*/) {
			queuedPlayerPosition = tile.getLocation();
			System.out.println("Player " + tile.getCharacter() + " at " + tile.getLocation().toString());
		}
	}
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
	void lock() {
		lock.lock();
	}
	void unlock() {
		lock.unlock();
	}
	private void lockNeighbors() {
		for (Subscription subscription : lockHierarchy) {
			subscription.lock();
		}
	}
	private void unlockNeighbors() {
		//I'd love to use a foreach loop, but I don't know
		//how to iterate backwards
		for (int i = lockHierarchy.size() - 1; i >= 0; i--) {
			lockHierarchy.get(i).unlock();
		}
	}
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
