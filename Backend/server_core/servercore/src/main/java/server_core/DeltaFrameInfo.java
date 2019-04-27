package server_core;

import java.util.Collection;

/**
 * JSON-convertible version of a Delta Frame object
 * @author zjwatt
 *
 */
public class DeltaFrameInfo {
	private int subscriptionID;
	private boolean newDungeon;
	private int xL;
	private int xR;
	private int yU;
	private int yL;
	
	Collection<TileInfo> tiles;

	public DeltaFrameInfo(int subscriptionID, boolean newDungeon, int xL, int xR, int yU, int yL,
			Collection<TileInfo> tiles) {
		super();
		this.subscriptionID = subscriptionID;
		this.newDungeon = newDungeon;
		this.xL = xL;
		this.xR = xR;
		this.yU = yU;
		this.yL = yL;
		this.tiles = tiles;
	}

	public int getSubscriptionID() {
		return subscriptionID;
	}

	public boolean isNewDungeon() {
		return newDungeon;
	}

	public int getxL() {
		return xL;
	}

	public int getxR() {
		return xR;
	}

	public int getyU() {
		return yU;
	}

	public int getyL() {
		return yL;
	}

	public Collection<TileInfo> getTiles() {
		return tiles;
	}
	
	
}
