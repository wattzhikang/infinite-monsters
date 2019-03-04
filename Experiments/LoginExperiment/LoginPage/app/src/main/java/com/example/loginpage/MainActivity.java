package com.example.loginpage;

import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.Color;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import org.json.JSONException;
import org.json.JSONObject;


public class MainActivity extends AppCompatActivity
{
    Button login, cancel;
    EditText username, password;
    TextView tx1;
    ClientSocket clientSocket;
    boolean isBound = false;
    String serverMessage = "";
    boolean messageReceived = false;
    
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
        Intent clientIntent = new Intent(MainActivity.this, ClientSocket.class);
        startService(clientIntent);
        bindService(clientIntent, clientConnection, Context.BIND_AUTO_CREATE);
        /*MapCreator map = new MapCreator();
        map.createMap();*/
        
        
        login.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                messageReceived = false;
                //clientSocket = new ClientSocket();
                JSONObject client = new JSONObject();
                try
                {
                    client.put("key", "login");
                    client.put("username", username.getText().toString());
                    client.put("password", password.getText().toString());
                }
                catch(JSONException e)
                {
                    e.printStackTrace();
                }
                
                clientSocket.setClientMessage(client.toString());
                //clientSocket.setMessage(client.toString());
                //String message = clientSocket.readMessage();
                /*try
                {
                    clientSocket.execute(client).get();
                }
                catch (InterruptedException | ExecutionException e)
                {
                    e.printStackTrace();
                }*/
                while(!messageReceived)
                {
                    if(clientSocket.getServerMessage() != null)
                    {
                        serverMessage = clientSocket.getServerMessage();
                        messageReceived = true;
                    }
                    
                }
                //serverMessage = clientSocket.getServerMessage();
                Log.i("message", serverMessage);
                
                //String message = clientSocket.getServerMessage();
                
                if(serverMessage.equals((serverMessage)))
                {
                    Toast.makeText(MainActivity.this, "login success: " + serverMessage, Toast.LENGTH_SHORT).show();
                    Intent i = new Intent(MainActivity.this, Game.class);
                    startActivity(i);
                    finish();
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
                if(!clientSocket.isClosed())
                {
                    clientSocket.close();
                }
                finish();
            }
        });
    }
    
    private ServiceConnection clientConnection = new ServiceConnection()
    {
        
        @Override
        public void onServiceConnected(ComponentName name, IBinder service)
        {
            ClientSocket.ServiceBinder clientService = (ClientSocket.ServiceBinder) service;
            clientSocket = clientService.getService();
            isBound = true;
        }
        
        @Override
        public void onServiceDisconnected(ComponentName name)
        {
            isBound = false;
        }
    };
    
    @Override
    protected void onStop()
    {
        super.onStop();
        if(isBound)
        {
            unbindService(clientConnection);
            //clientMessenger = null;
            isBound = false;
        }
    }
    
    private String isMyServiceRunning(Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return "true";
            }
        }
        return "false";
    }
}
