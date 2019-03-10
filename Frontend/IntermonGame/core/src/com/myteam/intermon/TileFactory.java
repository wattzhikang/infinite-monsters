package com.myteam.intermon;

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
    }
    
    public Array<Tile> createTiles()
    {
        int size = tiles.size;
        for(int i = 0; i < size; i++)
        {
    
            Tile t = new Tile(tiles.get(i), player);
            map.add(t);
        }
        return map;
    }
    
    public int getxLeft()
    {
        return xLeft;
    }
    
    public int getxRight()
    {
        return xRight;
    }
    
    public int getyLower()
    {
        return yLower;
    }
    
    public int getyUpper()
    {
        return yUpper;
    }
}
