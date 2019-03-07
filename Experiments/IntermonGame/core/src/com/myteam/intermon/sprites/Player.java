package com.myteam.intermon.sprites;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector3;

public class Player
{
    private Vector3 position;
    private Texture player;
    
    public Player(int x, int y)
    {
        position = new Vector3(x, y, 0);
        player = new Texture("pokemon_player.jpg");
    }
    
    public void update(float dt)
    {
        position.add(0, 0, 0);
    }
    
    public Vector3 getPosition()
    {
        return position;
    }
    
    public Texture getTexture()
    {
        return player;
    }
}
