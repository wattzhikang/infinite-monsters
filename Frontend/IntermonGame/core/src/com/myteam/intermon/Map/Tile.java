package com.myteam.intermon.Map;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.JsonValue;
import com.myteam.intermon.sprites.Player;

public class Tile
{
    private int x, y;
    private boolean walkable;
    private String character, object, terrainType;
    private Player player;
    
    public Tile(JsonValue tile, int xLeft, int yLower, Player player)
    {
        this.player = player;
        x = tile.getInt("x") - xLeft;
        y = tile.getInt("y") - yLower;
        walkable = tile.getBoolean("walkable");
        setTerrainType(tile.getString("terrainType"));
        setObject(tile.getString("object"));
        setCharacter(tile.getString("character"));
    }
    
    private void setTerrainType(String terrainType)
    {
        if(terrainType != null)
        {
            if(terrainType.equals("greenGrass1"))
            {
                this.terrainType = "grass01";
            }
        }
    }
    
    private void setObject(String object)
    {
        if(object != null)
        {
            if(object.equals("genericBarrier1"))
            {
                this.object = "barrier01";
            }
            else if(object.equals("genericTallGrass01"))
            {
                this.object = "tallgrass01";
            }
            else if(object.equals("genericPath1"))
            {
                this.object = "path01";
            }
        }
    }
    
    private void setCharacter(String character)
    {
        if(character != null)
        {
            if(character.equals(player.getName()))
            {
                this.character = player.getName();
                player.updatePosition(x, y);
            }
        }
    }
    
    public int getX()
    {
        return x;
    }
    
    public int getY()
    {
        return y;
    }
    
    public String getTerrainType()
    {
        return terrainType;
    }
    
    public String getObject()
    {
        return object;
    }
    
    public String getCharacter()
    {
        return character;
    }
}
