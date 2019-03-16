package com.myteam.intermon.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonValue;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.myteam.intermon.ClientSocket;
import com.myteam.intermon.Communication.MovementRequest;
import com.myteam.intermon.Communication.Request;
import com.myteam.intermon.Intermon;
import com.myteam.intermon.MOVEMENT;
import com.myteam.intermon.Map.Tile;
import com.myteam.intermon.Map.TileFactory;
import com.myteam.intermon.sprites.Player;

import java.io.IOException;
import java.util.Arrays;

public class PlayScreen implements Screen
{
    private ClientSocket clientSocket;
    private Intermon game;
    private Player player;
    private Stage gameStage;
    private String username;
    private Viewport gamePort;
    private boolean mapEmpty = true;
    private int xLeft, xRight, yLower, yUpper;
    private Tile[][] map;
    
    public PlayScreen(Intermon game, ClientSocket cs, String username)
    {
        this.game = game;
        clientSocket = cs;
        this.username = username;
        player = new Player(0, 0, username);
        gamePort = new FitViewport(1920, 1080);
        gameStage = new Stage(gamePort);
        map = new Tile[15][15];
        ClientThreadListener clt = new ClientThreadListener();
        clt.start();
        Request request = new Request("subscription");
        Json clientJson = new Json();
        String clientRequest = clientJson.toJson(request);
        requestSubscription(clientRequest);
    }
    @Override
    public void show()
    {
        ImageButton.ImageButtonStyle dpadBtnStyle = new ImageButton.ImageButtonStyle();
        dpadBtnStyle.up = new TextureRegionDrawable(new TextureRegion(new Texture(Gdx.files.internal("intermon_dpad.png"))));
        dpadBtnStyle.down = new TextureRegionDrawable(new TextureRegion(new Texture(Gdx.files.internal("intermon_dpad.png"))));
        ImageButton dpad = new ImageButton(dpadBtnStyle);
        dpad.addListener(new ClickListener()
        {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button)
            {
                if((x >= 10 && x <= 60) && (y >= 80 && y <= 120))
                {
                    handleMovement(MOVEMENT.LEFT);
                }
                else if((x >= 110 && x <= 170) && (y >= 80 && y <= 120))
                {
                    handleMovement(MOVEMENT.RIGHT);
                }
                else if((x >= 60 && x <= 120) && (y >= 130 && y <= 180))
                {
                    handleMovement(MOVEMENT.UP);
                }
                else if((x >= 60 && x <= 120) && (y >= 30 && y <= 80))
                {
                    handleMovement(MOVEMENT.DOWN);
                }
                
                return super.touchDown(event, x, y, pointer, button);
            }
        });
        
        Table table = new Table();
        table.debug();
        table.setFillParent(true);
        table.bottom().left();
        table.add(dpad);
        gameStage.addActor(table);
        Gdx.input.setInputProcessor(gameStage);
    }
    
    @Override
    public void render(float delta)
    {
        Gdx.gl.glClearColor(1, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        Intermon.batch.begin();
        int count = 0;
        if(!mapEmpty)
        {
            for(int y = 0; y < 15; y++)
            {
                for(int x = 0; x < 15; x++)
                {
                    if(count < 45) //will need to go away once we have enough tiles to fully display on the device
                    {
                        int tileWidth = 128;
                        int tileHeight = 72;
                        if (map[y][x].getTerrainType() != null)
                        {
                            String terrain = map[y][x].getTerrainType();
                            if(terrain.equals("grass01"))
                            {
                                Texture grass = new Texture("intermon_grass_01.png");
                                Intermon.batch.draw(grass, map[y][x].getX() * tileWidth, map[y][x].getY() * tileHeight);
                            }
                        }
                        if (map[y][x].getObject() != null)
                        {
                            String object = map[y][x].getObject();
                            if(object.equals("barrier01"))
                            {
                                Texture barrier = new Texture("intermon_barrier_01.png");
                                Intermon.batch.draw(barrier, map[y][x].getX() * tileWidth, map[y][x].getY() * tileHeight);
                            }
                        }
                        if (map[y][x].getCharacter() != null)
                        {
                            String character = map[y][x].getCharacter();
                            if(character.equals(username))
                            {
                                Intermon.batch.draw(player.getTexture(), player.getPosition().x * tileWidth, player.getPosition().y * tileHeight);
                            }
                        }
                        count++;
                    }
                }
            }
        }
        Intermon.batch.end();
        gameStage.act(Gdx.graphics.getDeltaTime());
        gameStage.draw();
    }
    
    @Override
    public void resize(int width, int height)
    {
        gamePort.update(width, height);
    }
    
    @Override
    public void pause()
    {
    
    }
    
    @Override
    public void resume()
    {
    
    }
    
    @Override
    public void hide()
    {
    
    }
    
    @Override
    public void dispose()
    {
        gameStage.dispose();
    }
    
    public void handleMovement(MOVEMENT move)
    {
        int playerX = (int) player.getPosition().x;
        int playerY = (int) player.getPosition().y;
        int newPlayerX = 0;
        int newPlayerY = 0;
        String requestType = "mod_move_subscription";
        MovementRequest clientMove = null;
        
        if(move == MOVEMENT.LEFT)
        {
            System.out.println("left pushed");
            xLeft--;
            xRight--;
            newPlayerX = playerX - 1;
            newPlayerY = playerY;
            clientMove = new MovementRequest(xLeft, xRight, yUpper, yLower, playerX, playerY, newPlayerX, newPlayerY, requestType);
        }
        else if(move == MOVEMENT.RIGHT)
        {
            System.out.println("right pushed");
            xLeft++;
            xRight++;
            newPlayerX = playerX + 1;
            newPlayerY = playerY;
            clientMove = new MovementRequest(xLeft, xRight, yUpper, yLower, playerX, playerY, newPlayerX, newPlayerY, requestType);
        }
        else if(move == MOVEMENT.UP)
        {
            System.out.println("up pushed");
            yUpper++;
            yLower++;
            newPlayerX = playerX;
            newPlayerY = playerY + 1;
            clientMove = new MovementRequest(xLeft, xRight, yUpper, yLower, playerX, playerY, newPlayerX, newPlayerY, requestType);
        }
        else if(move == MOVEMENT.DOWN)
        {
            System.out.println("down pushed");
            yUpper--;
            yLower--;
            newPlayerX = playerX;
            newPlayerY = playerY - 1;
            clientMove = new MovementRequest(xLeft, xRight, yUpper, yLower, playerX, playerY, newPlayerX, newPlayerY, requestType);
        }
        
        Json clientJson = new Json();
        String clientRequest = clientJson.toJson(clientMove);
        requestSubscription(clientRequest);
    }
    
    private void requestSubscription(String message)
    {
        System.out.println("client: " + message);
        try
        {
            clientSocket.sendMessage(message);
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }
    
    private void setSubscription(String sub)
    {
        JsonReader reader = new JsonReader();
        JsonValue subscription = reader.parse(sub);
        JsonValue tileArray = subscription.get("tiles");
        boolean newDungeon = subscription.getBoolean("newDungeon");
        xLeft = subscription.getInt("xL");
        xRight = subscription.getInt("xR");
        yLower = subscription.getInt("yL");
        yUpper = subscription.getInt("yU");
        generateMap(newDungeon, xLeft, xRight, yLower, yUpper, tileArray);
    }
    
    private void generateMap(boolean newDungeon, int xL, int xR, int yL, int yU, JsonValue tiles)
    {
        TileFactory tiledMap = new TileFactory(newDungeon, xL, xR, yL, yU, tiles, player, map);
        map = tiledMap.createTiles();
        mapEmpty = false;
    }
    
    private class ClientThreadListener extends Thread
    {
        JsonReader read = new JsonReader();
        JsonValue response;
        @Override
        public void run()
        {
            while(true)
            {
                String message = "";
                try
                {
                    message = clientSocket.readMessage();
                    System.out.println("server: " + message);
                    response = read.parse(message);
                }
                catch (IOException e)
                {
                    e.printStackTrace();
                }
                catch (ClassNotFoundException e)
                {
                    e.printStackTrace();
                }
                
                if(response.has("modificationSuccess"))
                {
                    if(response.getBoolean("modificationSuccess"))
                    {
                        System.out.println("modification successful");
                    }
                    else
                    {
                        System.out.println("modification failed");
                    }
                }
                else
                {
                    setSubscription(message);
                }
            }
        }
    }
}
