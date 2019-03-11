package com.example.loginpage;

public class Tile
{
    private TERRAIN terrain;
    //private Player player;
    
    public Tile(TERRAIN terrain)
    {
        this.terrain = terrain;
    }
    
    public TERRAIN getTerrain()
    {
        return terrain;
    }
    
    /*public Player getPlayer()
    {
        return player;
    }
    
    public void setPlayer(Player player)
    {
        this.player = player;
    }*/
}
