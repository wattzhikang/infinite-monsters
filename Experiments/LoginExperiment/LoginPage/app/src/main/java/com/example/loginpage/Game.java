package com.example.loginpage;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;

public class Game extends Activity
{
    ClientSocket clientSocket;
    boolean isBound = false;
    
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(new GameView(this));
        Intent gameIntent = new Intent(Game.this, ClientSocket.class);
        bindService(gameIntent, clientConnection, Context.BIND_AUTO_CREATE);
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
