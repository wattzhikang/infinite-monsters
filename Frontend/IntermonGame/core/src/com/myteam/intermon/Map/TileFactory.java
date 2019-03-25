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
    private Tile[][] map;
    
    public TileFactory(boolean newDungeon, int xL, int xR, int yL, int yU, JsonValue tiles, Player player, Tile[][] map)
    {
        this.newDungeon = newDungeon;
        xLeft = xL;
        xRight = xR;
        yLower = yL;
        yUpper = yU;
        this.player = player;
        this.tiles = tiles;
        this.map = map;
    }
    
    /*
    REMOVE ANYTHING ASSOCIATED WITH COUNT IN THIS CLASS AND THE PLAYSCREEN
    ONCE THE MAP IS FULLY IMPLEMENTED
     */
    public Tile[][] createTiles()
    {
        int numTiles = tiles.size;
        int count = 0;
        
        int xLeftOld = xLeft;
        int xRightOld = xRight;
        int yLowerOld = yLower;
        int yUpperOld = yUpper;
        boolean changedBoundaries;
        
        if(newDungeon)
        {
            map = new Tile[15][15];//reset the map to empty since the delta from is contains a new dungeon
            for(int y = 0; y < 15; y++)
            {
                for(int x = 0; x < 15; x++)
                {
                    if(count < numTiles)//will need to go away once we have enough tiles to fill the device
                    {
                        Tile t = new Tile(tiles.get(count), xLeft, yLower, player);
                        map[y][x] = t;
                    }
                    count++;
                }
            }
        }
        else
        {
            for(int n = 0; n < numTiles; n++)
            {
                Tile t = new Tile(tiles.get(n), xLeft, yLower, player);
                for(int y = 0; y < 15; y++)
                {
                    for(int x = 0; x < 15; x++)
                    {
                        if(count < 45)//will need to go away once we have enough tiles to fill the device
                        {
                            if(t.getX() == map[y][x].getX() && t.getY() == map[y][x].getY())
                            {
                                map[y][x] = t;
                            }
                            count++;
                        }
                    }
                }
                count = 0;
            }
            
        }
       
        
        return map;
    }

    public Tile[][] getMap()
    {
        return map;
    }
}
