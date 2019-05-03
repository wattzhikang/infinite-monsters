package game;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Collection;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class TestRectangleBoundary {

	RectangleBoundary bound1;
	RectangleBoundary bound2;
	
	@BeforeEach
	void setUp() throws Exception {
		bound1 = new RectangleBoundary(
				new Position(0,0, 0l),
				new Position(2,2, 0l));
		
		bound2 = new RectangleBoundary(
				new Position(0,0, 0l),
				new Position(0,0, 0l));
	}

	@Test
	void betweenTest() {
		Collection<Position> between = bound1.getBetween();
		
		System.out.println("Number of tiles: " + between.size());
		
		assert between.size() == 9;
		
		between = bound2.getBetween();
		
		assert between.size() == 1;
	}

}
