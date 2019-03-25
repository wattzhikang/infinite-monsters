package game;

import java.util.Collection;
import java.util.LinkedList;

public class Coordinates /*implements Comparable<Coordinates> */ {
	private int x;
	private int y;
	private int dungeon;
	
	public Coordinates(int x, int y) {
		this.x = x;
		this.y = y;
	}
	
	public Coordinates(int x, int y, int dungeon) {
		this.x = x;
		this.y = y;
		this.dungeon = dungeon;
	}
	
	public int getX() { return x; }
	public int getY() { return y; }
	public int getDungeon() {return dungeon;}
	
	public String toString() {
		return "(" + x + ", " + y + ")";
	}

//	@Override
//	public int compareTo(Coordinates o) {
//		if (x == o.getX() && y == o.getY()) {
//			return 0;
//		} else if (x <= o.getX() || y <= o.getY()) {
//			return -1;
//		} else {
//			return 1;
//		}
//	}
	
//	public static boolean equals(Coordinates a, Coordinates b) {
//		return a.compareTo(b) == 0;
//	}
	
	@Override
	public boolean equals(Object b) {
		if (b instanceof Coordinates) {
			return x == ((Coordinates)b).getX() && y == ((Coordinates)b).getY();
		} else {
			return false;
		}
	}
	
	public int hashCode() {
		return x + y;
	}
}