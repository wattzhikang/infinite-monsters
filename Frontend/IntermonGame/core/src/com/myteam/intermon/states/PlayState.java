package com.myteam.intermon.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonValue;
import com.myteam.intermon.ClientSocket;
import com.myteam.intermon.Communication.MessageConverter;
import com.myteam.intermon.Map.Tile;
import com.myteam.intermon.Map.TileFactory;
import com.myteam.intermon.sprites.Player;

import java.io.IOException;

public class PlayState extends State
{
    private Player player;
    private ClientSocket clientSocket;
    private String serverMessage = null;
    private Array<Tile> map;
    private Texture dpad;
    private MessageConverter mc;
    private int xLeft, xRight, yLower, yUpper;
    
    PlayState(GameStateManager gsm, ClientSocket cs, String username)
    {
        super(gsm);
        player = new Player(0, 0, username);
        dpad = new Texture("intermon_dpad.png");
        cam.setToOrtho(false, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        clientSocket = cs;
        mc = new MessageConverter();
        JsonValue request = new JsonValue("subscription");
        request.setName("requestType");
        String clientMessage = mc.setMessage(request);
        String sub = requestSubscription(clientMessage);
        setSubscription(sub);
        
    }
    
    @Override
    protected void handleInput()
    {
        
        //Up: x min: 65, y min: 895
        //Up: x max:115 , y max: 950
        //Down: x min: 65, y min: 1000
        //Down: x max: 115, y max: 1055
        //Left: x min: 10, y min: 950
        //Left: x max: 60, y max: 1000
        //Right: x min: 120, y min: 950
        //Right: x max: 165, y max: 1000
        int x = Gdx.input.getX();
        int y = Gdx.input.getY();
        if(Gdx.input.isTouched())
        {
            int playerX = (int) player.getPosition().x;
            int playerY = (int) player.getPosition().y;
            int newPlayerX = 0;
            int newPlayerY = 0;
            if ((x >= 65 && x <= 115) && (y >= 895 && y <= 950))//Move Up
            {
                System.out.println("up pushed");
                yUpper++;
                yLower++;
                newPlayerX = playerX;
                newPlayerY = playerY + 1;
            } else if ((x >= 65 && x <= 115) && (y >= 1000 && y <= 1055))//Move Down
            {
                System.out.println("down pushed");
                yUpper--;
                yLower--;
                newPlayerX = playerX;
                newPlayerY = playerY - 1;
            } else if ((x >= 10 && x <= 60) && (y >= 950 && y <= 1000))//Move Left
            {
                System.out.println("left pushed");
                xLeft--;
                xRight--;
                newPlayerX = playerX - 1;
                newPlayerY = playerY;
            } else if ((x >= 120 && x <= 170) && (y >= 950 && y <= 1000))//Move Right
            {
                System.out.println("right pushed");
                xLeft++;
                xRight++;
                newPlayerX = playerX + 1;
                newPlayerY = playerY;
            }
            JsonValue move = new JsonValue(xLeft);
            move.setName("xL");
            move.addChild("xR", new JsonValue(xRight));
            move.addChild("yL", new JsonValue(yLower));
            move.addChild("yU", new JsonValue(yUpper));
            move.addChild("oldPlayerX", new JsonValue(playerX));
            move.addChild("oldPlayerY", new JsonValue(playerY));
            move.addChild("newPlayerX", new JsonValue(newPlayerX));
            move.addChild("newPlayerY", new JsonValue(newPlayerY));
            String moveRequest = mc.setMessage(move);
            System.out.println(moveRequest);
            String newSub = requestSubscription(moveRequest);
            setSubscription(newSub);
        }
    }
    
    @Override
    public void update(float dt)
    {
        handleInput();
    }
    
    @Override
    public void render(SpriteBatch sb)
    {
        int size = map.size;
        sb.begin();
        for (int i = 0; i < size; i++)
        {
            if (map.get(i).getTerrainType() != null)
            {
                sb.draw(map.get(i).getTerrainType(), map.get(i).getX() * map.get(i).getTerrainType().getWidth(), map.get(i).getY() * map.get(i).getTerrainType().getHeight());
            }
            if (map.get(i).getObject() != null)
            {
                sb.draw(map.get(i).getObject(), map.get(i).getX() * map.get(i).getObject().getWidth(), map.get(i).getY() * map.get(i).getObject().getHeight());
            }
            if (map.get(i).getCharacter() != null)
            {
                sb.draw(map.get(i).getCharacter(), map.get(i).getX() * map.get(i).getTerrainType().getWidth(), map.get(i).getY() * map.get(i).getTerrainType().getHeight());
            }
        }
        sb.draw(dpad, 0, 0);
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
        } catch (IOException e)
        {
            e.printStackTrace();
        } catch (ClassNotFoundException e)
        {
            e.printStackTrace();
        }
        return serverMessage;
    }
    
    private void setSubscription(String sub)
    {
        boolean newDungeon;
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
