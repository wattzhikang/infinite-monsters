package game;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.Collection;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import server_core.Client;

class WatcherTest {

	Client client;
	Watcher watcher;
	
	@BeforeEach
	void setUp() throws Exception {
		client = new Client(null, null);
		watcher = new Watcher(client);
	}

	@Test
	void sendDeltaWorks() {
		Collection<Tile> tiles = new ArrayList<Tile>();
		tiles.add(new Tile(new Coordinates(0,0), false, "greenGrass1", "genericBarrier1"));
		
		watcher.sendDelta(tiles);
	}
	
	@Test
	void setAndGetBoundsWorks() {
		watcher.setBounds(
				new RectangleBoundary(
						new Coordinates(0,0),
						new Coordinates(2,2)
						));
		
		assert watcher.getBounds().getBetween().size() == 9;
	}

}
