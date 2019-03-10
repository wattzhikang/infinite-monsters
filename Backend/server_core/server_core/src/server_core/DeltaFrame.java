package server_core;

import java.util.Collection;

import game.RectangleBoundary;
import game.Tile;

public class DeltaFrame {

	RectangleBoundary bounds;
	Collection<Tile> tiles;
	boolean dungeonChange;
	
	public DeltaFrame(RectangleBoundary bounds, Collection<Tile> tiles, boolean dungeonChange) {
		this.bounds = bounds;
		this.tiles = tiles;
		this.dungeonChange = dungeonChange;
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
