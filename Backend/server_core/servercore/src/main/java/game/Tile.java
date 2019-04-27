package game;

/**
 * Represents a tile on the grand game map
 * @author Zachariah Watt
 *
 */
public class Tile {
	private Position location;
	private boolean walkable;
	private String terrain;
	private String object;
	private String character;
	
	public String getCharacter() {
		return character;
	}

	/**
	 * Constructs a tile with the given information
	 * @param location
	 * @param walkable Whether the tile can be trod upon by ordinary players
	 * @param terrain The background terrain asset of this tile
	 * @param object The asset of this tile
	 * @param character The character occupying the tile
	 */
	public Tile(Position location, boolean walkable, String terrain, String object, String character) {
		this.location = location;
		this.walkable = walkable;
		this.terrain = terrain;
		this.object = object;
		this.character = character;
	}
	
	/**
	 * Constructs a blank tile at the given location
	 * @param location
	 */
	public Tile(Position location) {
		this.location = location;
		this.walkable = false;
		this.terrain = null;
		this.object = null;
		this.character = null;
	}
	
	public Position getLocation() {
		return location;
	}

	public boolean isWalkable() {
		return walkable;
	}

	public String getTerrain() {
		return terrain;
	}

	public String getObject() {
		return object;
	}
	
	public void setObject(String object) {
		this.object = object;
	}
	
	
	
	public void setCharacter(String character) {
		this.character = character;
	}

	public String toString() {
		return location.toString() + ", Walkable: " + walkable +
				", Terrain: " + terrain +
				", Object: " + object;
	}
}
