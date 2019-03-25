package game;

import java.util.Collection;
import java.util.LinkedList;

public class RectangleBoundary {
	private Coordinates upperRight;
	private Coordinates lowerLeft;
	
	public RectangleBoundary(Coordinates upperRight, Coordinates lowerLeft) {
		if (lowerLeft.getX() <= upperRight.getX() && lowerLeft.getY() <= upperRight.getY()) {
			this.upperRight = upperRight;
			this.lowerLeft = lowerLeft;
		} else {
			this.upperRight = lowerLeft;
			this.lowerLeft = upperRight;
		}
	}
	
	public RectangleBoundary(Coordinates location) {
		upperRight = location;
		lowerLeft = location;
	}

	public Collection<Coordinates> getBetween() {
		int x0 = lowerLeft.getX(), x1 = upperRight.getX();
		int y0 = lowerLeft.getY(), y1 = upperRight.getY();
		Collection<Coordinates> coordinates = new LinkedList<Coordinates>();
		for (; x0 <= x1; x0++ ) {
			for (y0 = lowerLeft.getY(); y0 <= y1; y0++) {
				coordinates.add(new Coordinates(x0,y0));
			}
		}
		return coordinates;
	}
	
	public int getUnitChange(RectangleBoundary bound) {
		int deltaX = bound.upperRight.getX() - upperRight.getX();
		int deltaY = bound.upperRight.getY() - upperRight.getY();
		return Math.abs(deltaX) + Math.abs(deltaY);
	}
	
	/*
	 * Get the coordinates that are not in this area, but are in other's area
	 */
	public Collection<Coordinates> getDifference(RectangleBoundary other) {
		Collection<Coordinates> difference = other.getBetween();
		difference.removeAll(this.getBetween());
		return difference;
	}
	
	public Coordinates getUpperRight() {return upperRight;}
	public Coordinates getLowerLeft() {return lowerLeft;}
	
	@Override
	public String toString() {
		return "lowerLeft: " + lowerLeft.toString() + " " +
				"upperRight: " + upperRight.toString() + " ";
	}
}
