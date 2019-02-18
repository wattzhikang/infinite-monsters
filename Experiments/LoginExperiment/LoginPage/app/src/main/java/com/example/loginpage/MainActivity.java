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

import java.util.concurrent.ExecutionException;

public class MainActivity extends AppCompatActivity
{
    Button login, cancel;
    EditText username, password;
    TextView tx1;
    
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
                ClientSocket clientSocket = new ClientSocket();
                try
                {
                    clientSocket.execute(client).get();
                }
                catch (ExecutionException e)
                {
                    e.printStackTrace();
                }
                catch (InterruptedException e)
                {
                    e.printStackTrace();
                }

                JSONObject validLogin = new JSONObject();
                try
                {
                    validLogin.put("loginSuccess", clientSocket.getServerMessage());
                }
                catch(JSONException e)
                {
                    e.printStackTrace();
                }

                String message = clientSocket.getServerMessage();

                String login = null;
                login = message.split("\"")[3];

                //tx2.setText("Server: " + clientSocket.getServerMessage());
                
                if(clientSocket.getValidLogin())
                {
                    Toast.makeText(MainActivity.this, "login success: " + login, Toast.LENGTH_SHORT).show();
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
}
