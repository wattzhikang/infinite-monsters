package com.myteam.intermon.sprites;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector3;

public class Player
{
    private Vector3 position;
    private Texture player;
    private String name;
    
    public Player(int x, int y, String name)
    {
        position = new Vector3(x, y, 0);
        player = new Texture("intermon_player.jpg");
        this.name = name;
    }
    
    public void updatePosition(int x, int y)
    {
        position.x = x;
        position.y = y;
    }
    
    public Vector3 getPosition()
    {
        return position;
    }
    
    public Texture getTexture()
    {
        return player;
    }
    
    public String getName()
    {
        return name;
    }
}
