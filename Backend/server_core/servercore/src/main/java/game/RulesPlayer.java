package game;

class RulesPlayer implements Rules {
    public boolean isMoveLegal(
        RectangleBoundary oBounds,
        RectangleBoundary nBounds,
        Tile nPlayerLocation
    ) {
        if (oBounds.getUnitChange(nBounds) > 1) {
            System.out.println("UnitChange greater than 1");
            return false;
        }
        if (!nPlayerLocation.isWalkable()) {
            System.out.println("Destination Tile is not walkable");
            return false;
        }
        if (nPlayerLocation.getCharacter() != null) {
            System.out.println("Destination Tile has a player on it already");
            return false;
        }
        return true;
    }

    public boolean canReverseDelta() {
        return true;
    }
}