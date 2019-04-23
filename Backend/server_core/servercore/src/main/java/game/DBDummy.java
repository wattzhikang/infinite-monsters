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
	
	private RectangleBoundary bounds = new RectangleBoundary(new Position(0,0,0), new Position(3,2,0));
	
	private Map<Position, Tile> database;
	
	public DBDummy() {
		database = new HashMap<Position, Tile>();
		
		Position tmp;
		
		tmp = new Position(0,0,0);
		database.put(tmp, new Tile(tmp, false, grass, barrier, null));
		
		tmp = new Position(1,0,0);
		database.put(tmp, new Tile(tmp, false, grass, barrier, null));
		
		tmp = new Position(2,0,0);
		database.put(tmp, new Tile(tmp, false, grass, barrier, null));
		
		tmp = new Position(3,0,0);
		database.put(tmp, new Tile(tmp, false, grass, barrier, null));
		
		tmp = new Position(0,1,0);
		database.put(tmp, new Tile(tmp, false, grass, barrier, null));
		
		tmp = new Position(1,1,0);
		database.put(tmp, new Tile(tmp, false, grass, null, "lance"));
		
		tmp = new Position(2,1,0);
		database.put(tmp, new Tile(tmp, false, grass, null, null));
		
		tmp = new Position(3,1,0);
		database.put(tmp, new Tile(tmp, false, grass, barrier, null));
		
		tmp = new Position(0,2,0);
		database.put(tmp, new Tile(tmp, false, grass, barrier, null));
		
		tmp = new Position(1,2,0);
		database.put(tmp, new Tile(tmp, false, grass, barrier, null));
		
		tmp = new Position(2,2,0);
		database.put(tmp, new Tile(tmp, false, grass, barrier, null));
		
		tmp = new Position(3,2,0);
		database.put(tmp, new Tile(tmp, false, grass, barrier, null));
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
	public Collection<Tile> getTiles(Collection<Position> locations) {
		List<Tile> tiles = new LinkedList<Tile>();
		for (Position location : locations) {
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
