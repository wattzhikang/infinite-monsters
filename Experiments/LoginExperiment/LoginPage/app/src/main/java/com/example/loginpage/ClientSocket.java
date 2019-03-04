package com.example.loginpage;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;


public class ClientSocket extends Service /*AsyncTask<JSONObject, Void, Void>*/
{
    private Socket clientSocket;
    private ObjectInputStream in;
    private ObjectOutputStream out;
    ClientSocketThread cst;
    private final String MY_IP = "192.168.1.2";
    private String serverMessage;
    private String clientMessage;
    
    private final IBinder clientBinder = new ServiceBinder();
    static final int LOGIN = 1;
    static final int REGISTER = 2;
    static final int SERVER_RESPONSE = 3;
    
    public int onStartCommand(Intent intent, int flags, int startId)
    {
        Log.i("service", "service started");
        return START_STICKY;
    }
    
    @Override
    public void onCreate()
    {
        super.onCreate();
        cst = new ClientSocketThread();
        cst.start();
    }
    
    @Override
    public IBinder onBind(Intent intent)
    {
        Log.i("service", "service binding");
        return clientBinder;
    }
    
    public void onDestroy()
    {
        Log.i("service", "service destroyed");
    }
    
    public class ServiceBinder extends Binder
    {
        ClientSocket getService()
        {
            return ClientSocket.this;
        }
    }
    
    /*@Override
    protected Socket doInBackground(JSONObject... params)
    {
        String clientMessage = params[0].toString();
        try
        {
            clientSocket = new Socket(MY_IP, Constants.SERVER_PORT);
            out = new ObjectOutputStream(clientSocket.getOutputStream());
            out.flush();
            in = new ObjectInputStream(clientSocket.getInputStream());
            sendMessage(clientMessage);
            serverMessage = readMessage();
        }
        catch(IOException e)
        {
            e.printStackTrace();
        }
        return clientSocket;
    }*/
    
    public void setClientMessage(String message)
    {
        clientMessage = message;
    }
    public String getServerMessage()
    {
        return serverMessage;
    }
    
    public boolean isClosed()
    {
        return clientSocket.isClosed();
    }
    
    public void close()
    {
        try
        {
            clientSocket.close();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }
    
    public class ClientSocketThread extends Thread
    {
        
        public void run()
        {
            try
            {
                if (clientSocket == null)
                {
                    clientSocket = new Socket(MY_IP, Constants.SERVER_PORT);
                    System.out.println("connected: " + clientSocket.isConnected());
                    Log.i("server", "connected");
                }
                out = new ObjectOutputStream(clientSocket.getOutputStream());
                out.flush();
                Log.i("server", "out stream connected");
                in = new ObjectInputStream(clientSocket.getInputStream());
                Log.i("server", "in stream connected");
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
            while(true)
            {
                if(clientMessage != null)
                {
                    Log.i("message", "sending message");
                    sendMessage(clientMessage);
                    Log.i("message", "reading message");
                    serverMessage = readMessage();
                }
            }
        }
        public String readMessage()
        {
            String tmp = null;
            String message = null;
            try
            {
                tmp = in.readObject().toString();
                try
                {
                    JSONObject tempMessage = new JSONObject(tmp);
                    String key = tempMessage.getString("key");
                    switch (key)
                    {
                        case "login":
                            message = tempMessage.getString("username");
                            break;
                        case "register":
                            message = tempMessage.getString("registrationSuccess");
                            break;
                        case "map":
                            message = tmp;
                            break;
                        default:
                            break;
                    }
                
                }
                catch (JSONException e)
                {
                    e.printStackTrace();
                }
            
            }
            catch (ClassNotFoundException | IOException e)
            {
                e.printStackTrace();
            }
            Log.i("server", "message received");
            return message;
        }
    
        public void sendMessage(String message)
        {
        
            try
            {
                out.writeObject((Object) message);
                out.flush();
            } catch (IOException e)
            {
                e.printStackTrace();
            }
            Log.i("server", "message sent");
        }
    }
}

