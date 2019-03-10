package com.myteam.intermon.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Json;
import com.myteam.intermon.Communication.Client;
import com.myteam.intermon.ClientSocket;

import java.io.IOException;

public class LoginState extends State
{
    private ClientSocket clientSocket;
    private Texture loginBtn;
    private Texture exitBtn;
    private boolean loginBtnPushed = false;
    private String username = "";
    private String password = "";
    private String serverMessage;
    private UsernameTextInputListener usernameListener = new UsernameTextInputListener();
    private PasswordTextInputListener passwordListener = new PasswordTextInputListener();
    
    public LoginState(GameStateManager gsm, ClientSocket cs)
    {
        super(gsm);
        clientSocket = cs;
        loginBtn = new Texture("login_button.jpg");
        exitBtn = new Texture("exit_button.jpg");
    }
    
    @Override
    public void handleInput()
    {
        //login button x min: 0, x max: 300
        //login button y min: 775, y max: 1075
        //exit button x min: 1475, x max: 1800
        //exit button y min: 775, y max: 1075
        int x = Gdx.input.getX();
        int y = Gdx.input.getY();
        
        
        if(Gdx.input.justTouched())
        {
            if((x >= 0 && x <= 300) && (y >= 775 && y <= 1075))
            {
                System.out.println("login button pushed");
                loginBtnPushed = true;
                Client client = new Client(username, password);
                Json json = new Json();
                String c = json.toJson(client);
                /*json.setOutputType(JsonWriter.OutputType.json);
                json.setElementType(Client.class, "password", Client.class);
                System.out.println(json.prettyPrint(client));*/
                try
                {
                    clientSocket.sendMessage(c);
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
                if(serverMessage.equals(serverMessage))
                {
                    gsm.set(new PlayState(gsm, clientSocket, username));
                    dispose();
                }
                
            }
            else if((x >= 1475 && x <= 1800) && (y >= 775 && y <= 1075))
            {
                System.out.println("exit button pushed");
                loginBtnPushed = false;
            }
            else
            {
                loginBtnPushed = false;
            }
            if(!loginBtnPushed)
            {
                Gdx.input.getTextInput(passwordListener, "Password", "", "password");
                Gdx.app.log("password", password);
                Gdx.input.getTextInput(usernameListener, "Username", "", "username");
                Gdx.app.log("username", username);
            }
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
        Gdx.gl.glClearColor(0, 0, 0, 0);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        sb.begin();
        sb.draw(loginBtn, 0, 0);
        sb.draw(exitBtn, Gdx.graphics.getWidth() - exitBtn.getWidth(), 0);
        sb.end();
    }
    
    @Override
    public void dispose()
    {
        loginBtn.dispose();
        exitBtn.dispose();
    }
    
    public class UsernameTextInputListener implements Input.TextInputListener
    {
        @Override
        public void input(String text)
        {
            username = text;
        }
        
        @Override
        public void canceled()
        {
            username = "";
        }
    }
    
    public class PasswordTextInputListener implements Input.TextInputListener
    {
        @Override
        public void input(String text)
        {
            password = text;
        }
        
        @Override
        public void canceled()
        {
            password = "";
        }
    }
}
