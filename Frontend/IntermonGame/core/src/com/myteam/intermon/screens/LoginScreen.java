package com.myteam.intermon.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonValue;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.myteam.intermon.ClientSocket;
import com.myteam.intermon.Communication.Client;
import com.myteam.intermon.Intermon;

import java.io.IOException;

public class LoginScreen implements Screen
{
    private ClientSocket clientSocket;
    private ImageButton loginBtn;
    private ImageButton.ImageButtonStyle loginBtnStyle;
    private Intermon game;
    private Stage loginStage;
    private Table table;
    private TextField username, password;
    private Viewport loginPort;
    private boolean loginBtnPressed, exitBtnPressed;
    
    public LoginScreen(Intermon game, ClientSocket cs)
    {
        this.game = game;
        clientSocket = cs;
        loginPort = new FitViewport(1920, 1080);
        loginStage = new Stage(loginPort);
    }
    
    @Override
    public void show()
    {
        TextField.TextFieldStyle style = new TextField.TextFieldStyle();
        style.fontColor = Color.WHITE;
        style.font = new BitmapFont();
        style.font.getData().setScale(5);
        Label usernameLabel = new Label("Username:", new Label.LabelStyle(new BitmapFont(), Color.WHITE));
        usernameLabel.setFontScale(5);
        username = new TextField("", style);
        username.setAlignment(Align.left);
        username.setBlinkTime(1);
        username.setTextFieldListener(new TextField.TextFieldListener()
        {
            @Override
            public void keyTyped(TextField textField, char c)
            {
                if(c == 10 || c == 13)
                {
                    Gdx.input.setOnscreenKeyboardVisible(false);
                    loginStage.unfocusAll();
                }
                else if(c == 9)
                {
                    username.next(true);
                }
            }
        });
    
        Label passwordLabel = new Label("password:", new Label.LabelStyle(new BitmapFont(), Color.WHITE));
        passwordLabel.setFontScale(5);
        password = new TextField("", style);
        password.setAlignment(Align.left);
        password.setTextFieldListener(new TextField.TextFieldListener()
        {
            @Override
            public void keyTyped(TextField textField, char c)
            {
                if(c == 10 || c == 13)
                {
                    Gdx.input.setOnscreenKeyboardVisible(false);
                    loginStage.unfocusAll();
                }
            }
        });
        
        loginBtnStyle = new ImageButton.ImageButtonStyle();
        loginBtnStyle.up = new TextureRegionDrawable(new TextureRegion(new Texture(Gdx.files.internal("login_button.jpg"))));
        loginBtnStyle.down = new TextureRegionDrawable(new TextureRegion(new Texture(Gdx.files.internal("exit_button.jpg"))));
        loginBtn = new ImageButton(loginBtnStyle);
        loginBtn.addListener(new ClickListener()
        {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button)
            {
                loginBtnPressed = true;
                handleInput();
                return loginBtnPressed;
            }
    
            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button)
            {
                super.touchUp(event, x, y, pointer, button);
                loginBtnPressed = false;
            }
        });
        
        table = new Table();
        table.debug();
        table.top();
        table.setFillParent(true);
        table.add(usernameLabel);
        table.add(username).width(1500).expandX();
        table.row();
        table.add(passwordLabel).expand().top();
        table.add(password).width(1500).expandX().top();
        table.row();
        table.add(loginBtn).left();
    
        loginStage.addActor(table);
        Gdx.input.setInputProcessor(loginStage);
    }
    
    @Override
    public void render(float delta)
    {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        loginStage.act(Gdx.graphics.getDeltaTime());
        loginStage.draw();
    }
    
    @Override
    public void resize(int width, int height)
    {
        loginPort.update(width, height);
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
        loginStage.dispose();
    }
    
    public void handleInput()
    {
        String serverMessage = "";
        if(loginBtnPressed)
        {
            System.out.println("username: " + username.getText());
            System.out.println("password: " + password.getText());
            Client client = new Client(username.getText(), password.getText(), "authentication");
            Json c = new Json();
            String clientJson = c.toJson(client);
            System.out.println(clientJson);
            try
            {
                clientSocket.sendMessage(clientJson);
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
            JsonReader reader = new JsonReader();
            JsonValue subscription = reader.parse(serverMessage);
            boolean validLogin = subscription.getBoolean("loginSuccess");
            if(validLogin)
            {
                game.setScreen(new PlayScreen(game, clientSocket, username.getText()));
            }
            
        }
        else if(exitBtnPressed)
        {
            dispose();
        }
        
    }
}
