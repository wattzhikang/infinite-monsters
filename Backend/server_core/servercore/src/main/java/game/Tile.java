package game;

public class Tile {
	private Position location;
	private boolean walkable;
	private String terrain;
	private String object;
	private String character;
	
	public String getCharacter() {
		return character;
	}

	public Tile(Position location, boolean walkable, String terrain, String object, String character) {
		this.location = location;
		this.walkable = walkable;
		this.terrain = terrain;
		this.object = object;
		this.character = character;
	}
	
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
