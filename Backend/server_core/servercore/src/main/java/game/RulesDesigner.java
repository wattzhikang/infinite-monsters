package game;

class RulesDesigner implements Rules {
    public boolean isMoveLegal(
        RectangleBoundary oBounds,
        RectangleBoundary nBounds,
        Tile nPlayerLocation
    ) {
        return true;
    }

    public boolean canReverseDelta() {
        return true;
    }
}