package game;

import static org.junit.jupiter.api.Assertions.*;

import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class CoordinatesTest {

	Coordinates c1, c2, c3;
	
	@BeforeEach
	void setUp() throws Exception {
		c1 = new Coordinates(0,0);
		c2 = new Coordinates(0,0);
		c3 = new Coordinates(1,0);
	}

	@Test
	void test() {
//		assert Coordinates.equals(c1, c2) == true;
//		assert Coordinates.equals(c1, c3) == false;
		
		System.out.println(((Object)c1).hashCode());
		System.out.println(c2.hashCode());
		
		assert c1.hashCode() == c2.hashCode();
		
		assert ((Object)c1).hashCode() == ((Object)c2).hashCode();
		
		assert c1.equals(c2) == true;
		assert c1.equals(c3) == false;
	}
	
	@Test
	void testMap() {
		Map<Coordinates, String> stringmap = new HashMap<Coordinates, String>();
		
		stringmap.put(c1, "string");
		stringmap.put(c2, "string2");
		
		System.out.println("String: " + stringmap.get(c1));
		System.out.println("String: " + stringmap.get(c2));
		System.out.println("String: " + stringmap.get(c3));
		
		assert stringmap.get(c1).equals(stringmap.get(c2));
		
		assert !stringmap.get(c1).equals(stringmap.get(c3));
	}

}
