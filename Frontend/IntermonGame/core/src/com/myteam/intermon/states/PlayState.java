package com.myteam.intermon.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonValue;
import com.myteam.intermon.ClientSocket;
import com.myteam.intermon.Communication.Request;
import com.myteam.intermon.Tile;
import com.myteam.intermon.TileFactory;
import com.myteam.intermon.sprites.Player;

import java.io.IOException;

public class PlayState extends State
{
    private Player player;
    private ClientSocket clientSocket;
    private String serverMessage = null;
    private Array<Tile> map;
    
    PlayState(GameStateManager gsm, ClientSocket cs, String username)
    {
        super(gsm);
        player = new Player(0, 0, username);
        cam.setToOrtho(false, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        clientSocket = cs;
        Json server = new Json();
        Request clientRequest = new Request("subscription");
        String clientMessage = server.toJson(clientRequest);
        String sub = requestSubscription(clientMessage);
        setSubscription(sub);
        
    }
    
    @Override
    protected void handleInput()
    {
    
    }
    
    @Override
    public void update(float dt)
    {
    
    }
    
    @Override
    public void render(SpriteBatch sb)
    {
        int size = map.size;
        sb.begin();
        for(int i = 0; i < size; i++)
        {
            
            if(map.get(i).getTerrainType() != null)
            {
                sb.draw(map.get(i).getTerrainType(), map.get(i).getX()*map.get(i).getTerrainType().getWidth(), map.get(i).getY()*map.get(i).getTerrainType().getHeight());
            }
            if(map.get(i).getObject() != null)
            {
                sb.draw(map.get(i).getObject(), map.get(i).getX()*map.get(i).getObject().getWidth(), map.get(i).getY()*map.get(i).getObject().getHeight());
            }
            if(map.get(i).getCharacter() != null)
            {
                sb.draw(map.get(i).getCharacter(), map.get(i).getX()*map.get(i).getTerrainType().getWidth(), map.get(i).getY()*map.get(i).getTerrainType().getHeight());
            }
        }
        sb.end();
    }
    
    @Override
    public void dispose()
    {
    
    }
    
    private String requestSubscription(String message)
    {
        try
        {
            clientSocket.sendMessage(message);
            serverMessage = clientSocket.readMessage();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        catch (ClassNotFoundException e)
        {
            e.printStackTrace();
        }
        System.out.println("server message: " + serverMessage);
        return serverMessage;
    }
    
    private void setSubscription(String sub)
    {
        boolean newDungeon;
        int xLeft, xRight, yLower, yUpper;
        JsonReader reader = new JsonReader();
        JsonValue subscription = reader.parse(sub);
        JsonValue tileArray = subscription.get("tiles");
        newDungeon = subscription.getBoolean("newDungeon");
        xLeft = subscription.getInt("xL");
        xRight = subscription.getInt("xR");
        yLower = subscription.getInt("yL");
        yUpper = subscription.getInt("yU");
        generateMap(newDungeon, xLeft, xRight, yLower, yUpper, tileArray);
        
    }
    
    private void generateMap(boolean newDungeon, int xL, int xR, int yL, int yU, JsonValue tiles)
    {
       TileFactory tiledMap = new TileFactory(newDungeon, xL, xR, yL, yU, tiles, player);
       map = tiledMap.createTiles();
    }
}
