package game;

import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public class DBDummy implements DBInterface {
	private static final String grass = "greenGrass1";
	private static final String barrier = "genericBarrier1";
	
	private RectangleBoundary bounds = new RectangleBoundary(new Coordinates(0,0), new Coordinates(3,2));
	
	private Map<Coordinates, Tile> database;
	
	public DBDummy() {
		database = new HashMap<Coordinates, Tile>();
		
		Coordinates tmp;
		
		tmp = new Coordinates(0,0);
		database.put(tmp, new Tile(tmp, false, grass, barrier));
		
		tmp = new Coordinates(1,0);
		database.put(tmp, new Tile(tmp, false, grass, barrier));
		
		tmp = new Coordinates(2,0);
		database.put(tmp, new Tile(tmp, false, grass, barrier));
		
		tmp = new Coordinates(3,0);
		database.put(tmp, new Tile(tmp, false, grass, barrier));
		
		tmp = new Coordinates(0,1);
		database.put(tmp, new Tile(tmp, false, grass, barrier));
		
		tmp = new Coordinates(1,1);
		database.put(tmp, new Tile(tmp, false, grass, null));
		
		tmp = new Coordinates(2,1);
		database.put(tmp, new Tile(tmp, false, grass, null));
		
		tmp = new Coordinates(3,1);
		database.put(tmp, new Tile(tmp, false, grass, barrier));
		
		tmp = new Coordinates(0,2);
		database.put(tmp, new Tile(tmp, false, grass, barrier));
		
		tmp = new Coordinates(1,2);
		database.put(tmp, new Tile(tmp, false, grass, barrier));
		
		tmp = new Coordinates(2,2);
		database.put(tmp, new Tile(tmp, false, grass, barrier));
		
		tmp = new Coordinates(3,2);
		database.put(tmp, new Tile(tmp, false, grass, barrier));
	}

	@Override
	public boolean login(String username, String password) {
		return true;
	}

	@Override
	public boolean register(String username, String password) {
		return true;
	}

	@Override
	public void close() {
		return;
	}

	@Override
	public RectangleBoundary lastSubscriptionBounds(ClientKey key) {
		return bounds;
	}

	@Override
	public void updateSubscriptionBounds(ClientKey key, RectangleBoundary bound) {
		bounds = bound;
		
	}

	@Override
	public Collection<Tile> getTiles(Collection<Coordinates> locations) {
		List<Tile> tiles = new LinkedList<Tile>();
		for (Coordinates location : locations) {
			if (database.containsKey(location)) {
				tiles.add(database.get(location));
			}
		}
		
		return tiles;
	}

	@Override
	public void updateTiles(Collection<Tile> tiles) {
		for (Tile tile : tiles) {
			database.put(tile.getLocation(), tile);
		}
	}
}
