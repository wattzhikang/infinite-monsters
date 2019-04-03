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
	Coordinates player;
	
	SubscriptionLock lock;
	
	Map<Subscription, Integer> overlaps;
	ArrayList<Subscription> lockHierarchy;
	
	ArrayList<Tile> queuedUpdates;
	
	Client client;
	
	Game game;
	
	int subscriptionID;
	
	RectangleBoundary bounds;
	
	public Subscription(Game game, Client client, int ID) {
		this.subscriptionID = ID;
		this.lock = new SubscriptionLock();
		this.overlaps = new HashMap<Subscription, Integer>();
		this.lockHierarchy = new ArrayList<Subscription>();
		addNeighbor(this);
		this.queuedUpdates = new ArrayList<Tile>();
		this.client = client;
		this.game = game;
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
		//TODO
	}
	public void move(RectangleBoundary nBounds, Coordinates nPlayerLocation) {
		game.adjustBounds(this, bounds, nBounds);
		
		lockNeighbors();
		
		Collection<Tile> buffer = new LinkedList<Tile>();
		
		String tmp = map[player.getX()][player.getY()].getObject();
		map[player.getX()][player.getY()].setObject(
				map[nPlayerLocation.getX()][nPlayerLocation.getY()].getObject()
		);
		map[nPlayerLocation.getX()][nPlayerLocation.getY()].setObject(
				map[player.getX()][player.getY()].getObject()
		);
		
		buffer.add(map[player.getX()][player.getY()]);
		buffer.add(map[nPlayerLocation.getX()][nPlayerLocation.getY()]);
		
		game.flushTiles(buffer);
		
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
	}
	void flushUpdates() {
		client.enqueueDeltaFrame(new DeltaFrame(bounds, queuedUpdates, false, subscriptionID));
		queuedUpdates = new ArrayList<Tile>();
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
	}
}
