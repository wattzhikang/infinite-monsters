package game;

class RulesPlayer implements Rules {
    public boolean isMoveLegal(
        RectangleBoundary oBounds,
        RectangleBoundary nBounds,
        Tile nPlayerLocation
    ) {
        if (oBounds.getUnitChange(nBounds) > 1) {
            return false;
        }
        if (!nPlayerLocation.isWalkable()) {
            return false;
        }
        if (nPlayerLocation.getCharacter() != null) {
            return false;
        }
        return true;
    }

    public boolean canReverseDelta() {
        return true;
    }
}