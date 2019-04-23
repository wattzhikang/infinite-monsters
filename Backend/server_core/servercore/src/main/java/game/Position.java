package game;

import java.util.Collection;
import java.util.LinkedList;

/**
 * Represents a location in the universe
 * @author Zachariah Watt
 */
public class Position {
	/** The x position in a dungeon */
	private int x;
	/** The y position in a dungeon */
	private int y;
	/** The dungeon ID */
	private long dungeon;
	
//	public Position(int x, int y) {
//		this.x = x;
//		this.y = y;
//	}
	
	public Position(int x, int y, long dungeon) {
		this.x = x;
		this.y = y;
		this.dungeon = dungeon;
	}
	
	public int getX() {
		return x;
	}
	public int getY() {
		return y;
	}
	public long getDungeon() {
		return dungeon;
	}
	
	public String toString() {
		return "(" + x + ", " + y + ")";
	}
	
	@Override
	public boolean equals(Object b) {
		if (b instanceof Position) {
			return x == ((Position)b).getX() && y == ((Position)b).getY();
		} else {
			return false;
		}
	}
	
	//TODO use dovetailing for this
	public int hashCode() {
		return x + y;
	}
}