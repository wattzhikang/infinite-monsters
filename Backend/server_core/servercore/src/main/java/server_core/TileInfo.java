package server_core;

/**
 * Dummy class encapsulating the information in
 * the client's JSON message
 * @author Zachariah Watt
 *
 */
public class TileInfo {
	private int x;
	private int y;
	private boolean walkable;
	private String terrainType;
	private String object;
	private String character;
	
	public TileInfo(int x, int y, boolean walkable, String terrainType, String object, String character) {
		super();
		this.x = x;
		this.y = y;
		this.walkable = walkable;
		this.terrainType = terrainType;
		this.object = object;
		this.character = character;
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

	public boolean isWalkable() {
		return walkable;
	}

	public String getTerrainType() {
		return terrainType;
	}

	public String getObject() {
		return object;
	}

	public String getCharacter() {
		return character;
	}
	
	
}
