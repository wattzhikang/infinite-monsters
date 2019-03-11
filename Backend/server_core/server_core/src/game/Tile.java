package game;

public class Tile {
	private Coordinates location;
	private boolean walkable;
	private String terrain;
	private String object;
	private String character;
	
	public String getCharacter() {
		return character;
	}

	public Tile(Coordinates location, boolean walkable, String terrain, String object, String character) {
		this.location = location;
		this.walkable = walkable;
		this.terrain = terrain;
		this.object = object;
		this.character = character;
	}
	
	public Tile(Coordinates location) {
		this.location = location;
	}
	
	public Coordinates getLocation() {
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
	
	public String toString() {
		return location.toString() + ", Walkable: " + walkable +
				", Terrain: " + terrain +
				", Object: " + object;
	}
}
