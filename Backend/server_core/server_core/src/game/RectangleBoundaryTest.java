package game;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Collection;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class RectangleBoundaryTest {

	RectangleBoundary bound1;
	RectangleBoundary bound2;
	
	@BeforeEach
	void setUp() throws Exception {
		bound1 = new RectangleBoundary(
				new Coordinates(0,0),
				new Coordinates(2,2));
		
		bound2 = new RectangleBoundary(
				new Coordinates(0,0),
				new Coordinates(0,0));
	}

	@Test
	void betweenTest() {
		Collection<Coordinates> between = bound1.getBetween();
		
		System.out.println("Number of tiles: " + between.size());
		
		assert between.size() == 9;
		
		between = bound2.getBetween();
		
		assert between.size() == 1;
	}

}
