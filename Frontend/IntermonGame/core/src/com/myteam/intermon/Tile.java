package com.myteam.intermon;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.JsonValue;
import com.myteam.intermon.sprites.Player;

public class Tile
{
    private int x, y;
    private boolean walkable;
    private Texture terrainType, object, character;
    private Player player;
    
    public Tile(JsonValue tile, Player player)
    {
        this.player = player;
        x = tile.getInt("x");
        y = tile.getInt("y");
        walkable = tile.getBoolean("walkable");
        setTerrainType(tile.getString("terrainType"));
        setObject(tile.getString("object"));
        setCharacter(tile.getString("character"));
    }
    
    private void setTerrainType(String terrainType)
    {
        if(terrainType.equals("genericGrass1"))
        {
            this.terrainType = new Texture("pokemon_grass_01.png");
        }
    }
    
    private void setObject(String object)
    {
        if(object.equals("genericBarrier1"))
        {
            this.object = new Texture("pokemon_barrier_01.png");
        }
        else if(object.equals("genericTallGrass01"))
        {
            this.object = new Texture("pokemon_tallgrass_01.png");
        }
        else if(object.equals("genericPath1"))
        {
            this.object = new Texture("pokemon_path_01.png");
        }
    }
    
    private void setCharacter(String character)
    {
        if(character.equals(player.getName()))
        {
            this.character = player.getTexture();
        }
        /*else if(character == null)
        {
            this.character = "none";
        }*/
    }
    
    public int getX()
    {
        return x;
    }
    
    public int getY()
    {
        return y;
    }
    
    public boolean isWalkable()
    {
        return walkable;
    }
    
    public Texture getTerrainType()
    {
        return terrainType;
    }
    
    public Texture getObject()
    {
        return object;
    }
    
    public Texture getCharacter()
    {
        return character;
    }
}
