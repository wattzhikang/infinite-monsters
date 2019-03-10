package game;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import server_core.Client;

class WatchedLocationTest {

	WatchedLocation watched;
	
	Watcher eye1;
	Watcher eye2;
	
	@BeforeEach
	void setUp() throws Exception {
		watched = new WatchedLocation(
				new Tile(new Coordinates(0,0), false, null, null));
		
		eye1 = new Watcher(new Client(null, null), 0);
		eye2 = new Watcher(new Client(null, null), 0);
	}

	@Test
	void sendsDeltasOnCreation() {
		watched = new WatchedLocation(
				new Tile(new Coordinates(0,0), false, null, null));
	}
	
	@Test
	void sendsDeltasOnChange() {
		watched.addWatcher(eye1);
		watched.addWatcher(eye2);
		
		watched.setTile(new Tile(new Coordinates(0,0), true, null, null));
	}

}
