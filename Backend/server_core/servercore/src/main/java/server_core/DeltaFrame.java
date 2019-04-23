package server_core;

import java.util.Collection;

import game.RectangleBoundary;
import game.Tile;

public class DeltaFrame {

	RectangleBoundary bounds;
	Collection<Tile> tiles;
	boolean dungeonChange;
	private int subscriptionID;
	
	public RectangleBoundary getBounds() {
		return bounds;
	}

	public Collection<Tile> getTiles() {
		return tiles;
	}

	public boolean isDungeonChange() {
		return dungeonChange;
	}
	
	public int getSubscriptionID() {
		return subscriptionID;
	}

	public DeltaFrame(RectangleBoundary bounds, Collection<Tile> tiles, boolean dungeonChange, int subscriptionID) {
		this.bounds = bounds;
		this.tiles = tiles;
		this.dungeonChange = dungeonChange;
		this.subscriptionID = subscriptionID;
	}
	
	@Override
	public String toString() {
		String toReturn = new String();
		toReturn += bounds.toString();
		for (Tile tile : tiles) {
			toReturn += tile.toString();
		}
		toReturn += dungeonChange;
		return toReturn;
	}
}
