package com.myteam.intermon.Map;

import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.JsonValue;
import com.myteam.intermon.sprites.Player;

public class TileFactory
{
    private boolean newDungeon;
    private int xLeft = 0, xRight = 0, yLower = 0, yUpper = 0;
    private JsonValue tiles;
    private Player player;
    private Array<Tile> map = new Array<Tile>();
    
    public TileFactory(boolean newDungeon, int xL, int xR, int yL, int yU, JsonValue tiles, Player player)
    {
        this.newDungeon = newDungeon;
        xLeft = xL;
        xRight = xR;
        yLower = yL;
        yUpper = yU;
        this.player = player;
        this.tiles = tiles;
        createTile();
    }
    
    public void createTile()
    {
        int size = tiles.size;
        int mapSize = map.size;
        for(JsonValue tile : tiles)
        {
            Tile t = new Tile(tile, xLeft, yLower, player);
            for(int j = 0; j < mapSize; j++)
            {
                if(t.getX() == map.get(j).getX() && t.getY() == map.get(j).getY())
                {
                    map.removeIndex(j);
                }
            }
            map.add(t);
        }
    }

    public Array<Tile> getMap()
    {
        return map;
    }
}
