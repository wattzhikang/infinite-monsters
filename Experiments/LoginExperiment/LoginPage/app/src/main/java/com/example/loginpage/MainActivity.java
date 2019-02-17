package com.example.loginpage;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class MainActivity extends AppCompatActivity
{
    Button login, cancel;
    EditText username, password;
    TextView tx1;
    String serverMessage = "false";
    
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        login =  findViewById(R.id.Login);
        cancel = findViewById(R.id.Cancel);
        username = findViewById(R.id.Username);
        password = findViewById(R.id.Password);
        tx1 = findViewById(R.id.Intermon);
        /*Thread clientThread = new Thread(new ServerThread());
        clientThread.start();*/
        
        login.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                JSONObject client = new JSONObject();
                try
                {
                    client.put("username", username.getText().toString());
                    client.put("password", password.getText().toString());
                }
                catch(JSONException e)
                {
                    e.printStackTrace();
                }
                Login login = new Login();
                login.execute(client);
                
                Toast.makeText(MainActivity.this, login.getServerMessage(), Toast.LENGTH_LONG).show();
                
                if(login.getValidLogin())
                {
                    Toast.makeText(MainActivity.this, "Redirecting...", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    Toast.makeText(MainActivity.this, "Wrong Credentials", Toast.LENGTH_LONG).show();
                    tx1.setVisibility(View.VISIBLE);
                    tx1.setBackgroundColor(Color.RED);
                }
            }
        });
        
        cancel.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                finish();
            }
        });
    }
    
    /*class ServerThread implements Runnable
    {
        Socket server;
        public void run()
        {
            try
            {
                ServerSocket serverSocket = new ServerSocket(10045);
                while(true)
                {
                    ObjectInputStream in = new ObjectInputStream(server.getInputStream());
                    server = serverSocket.accept();
                    serverMessage = (String) in.readObject();
                }
            }
            catch(IOException e1)
            {
                e1.printStackTrace();
            } catch (ClassNotFoundException e)
            {
                e.printStackTrace();
            }
        }
    }*/
}
