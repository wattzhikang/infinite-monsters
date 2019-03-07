package com.myteam.intermon.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.myteam.intermon.ClientSocket;
import com.myteam.intermon.sprites.Player;

public class PlayState extends State
{
    private Player player;
    
    public PlayState(GameStateManager gsm, ClientSocket cs)
    {
        super(gsm);
        player = new Player(0, 0);
        cam.setToOrtho(false, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
    }
    
    @Override
    protected void handleIntput()
    {
    
    }
    
    @Override
    public void update(float dt)
    {
    
    }
    
    @Override
    public void render(SpriteBatch sb)
    {
        sb.setProjectionMatrix(cam.combined);
        sb.begin();
        sb.draw(player.getTexture(), player.getPosition().x, player.getPosition().y);
        sb.end();
    }
    
    @Override
    public void dispose()
    {
    
    }
}
