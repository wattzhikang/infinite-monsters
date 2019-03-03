package server_core;

public class Tile {
	private Coordinates location;
	
	public Tile(Coordinates location) {
		this.location = location;
	}
	
	public Coordinates getLocation() {
		return location;
	}
}
