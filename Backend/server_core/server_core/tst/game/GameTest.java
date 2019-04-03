package game;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import server_core.Client;

class GameTest {
	
	Game game;

	@BeforeEach
	void setUp() throws Exception {
		game = new Game(new DBDummy());
	}

	@Test
	void getSubscriptionTest() {
		ClientKey key = game.authenticate("zjwatt", "password", new Client(null, null));
		
		Watcher watcher = game.getWatcher(key);
		
		RectangleBoundary movedBound = new RectangleBoundary(
				new Coordinates(
						watcher.getBounds().getLowerLeft().getX() + 1,
						watcher.getBounds().getLowerLeft().getY()),
				new Coordinates(
						watcher.getBounds().getUpperRight().getX() + 1,
						watcher.getBounds().getUpperRight().getY())
		);
		
		
		game.moveSubscription(key, watcher, movedBound, null, null);
	}

}
