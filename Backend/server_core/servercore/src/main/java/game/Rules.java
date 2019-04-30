package game;

interface Rules {
    boolean isMoveLegal(
        RectangleBoundary oBounds,
        RectangleBoundary nBounds,
        Tile nPlayerLocation
    );

    boolean canReverseDelta();
}