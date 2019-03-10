package game;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.Collection;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class DBDummyTest {
	
	DBInterface db;
	ClientKey key0, key1;

	@BeforeEach
	void setUp() throws Exception {
		db = new DBDummy();
		key0 = new ClientKey("zjwatt", null, ClientKey.Privileges.PLAYER);
		key1 = new ClientKey("player", null, ClientKey.Privileges.PLAYER);
	}

	@Test
	void loginWorks() {
		assert db.login("johnSmith", "password") == true;
		
		assert db.login("randomUser", "stupidPassword") == true;
	}
	
	@Test
	void registrationWorks() {
		assert db.register("johnSmith", "password") == true;
		
		assert db.register("randomUser", "stupidPassword") == true;
	}
	
	@Test
	void lastSubscriptionWorks() {
		assert db.lastSubscriptionBounds(key0).getLowerLeft().getX() ==
				db.lastSubscriptionBounds(key1).getLowerLeft().getX();
	}
	
	@Test
	void upDateSubscriptionWorks() {
		RectangleBoundary nBound = new RectangleBoundary(new Coordinates(0,1), new Coordinates(11,12));
		
		db.updateSubscriptionBounds(key0, nBound);
		
		lastSubscriptionWorks();
	}
	
	@Test
	void getTilesWorks() {
		Collection<Tile> tiles =
				db.getTiles(
					(new RectangleBoundary(
						new Coordinates(0,0),
						new Coordinates(3,3)
					).getBetween()));
		
//		assert Coordinates.equals(new Coordinates(0,0), new Coordinates(0,0)) == true;
		
		System.out.println("Number of tiles: " + tiles.size());
		
		assert tiles.size() == 12;
	}
	
	@Test
	void updateTilesWorks() {
		Collection<Tile> tile = new ArrayList<Tile>();
		tile.add(new Tile(new Coordinates(0,0), true, null, null));
		
		db.updateTiles(tile);
		
		Collection<Tile> tiles =
				db.getTiles(
					(new RectangleBoundary(
						new Coordinates(0,0),
						new Coordinates(0,0)
					).getBetween()));
		
		System.out.println(tiles.iterator().next());
		
		assert tiles.iterator().next().isWalkable() == true;
	}

}
