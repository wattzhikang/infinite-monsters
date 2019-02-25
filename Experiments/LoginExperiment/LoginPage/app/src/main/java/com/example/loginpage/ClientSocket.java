package com.example.loginpage;

import android.os.AsyncTask;
import android.os.Parcel;
import android.os.Parcelable;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.Socket;


public class ClientSocket extends AsyncTask<JSONObject, Void, Void> implements Serializable
{
    private static Socket clientSocket;
    private static ObjectInputStream in;
    private static ObjectOutputStream out;
    private  final String MY_IP = "192.168.1.2";
    String serverMessage = "";
    
    @Override
    protected Void doInBackground(JSONObject... params)
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
        return null;
    }
    
    public String readMessage()
    {
        String tmp = null;
        try
        {
            tmp = in.readObject().toString();
            try
            {
                JSONObject message = new JSONObject(tmp);
                String key = message.getString("key");
                switch(key)
                {
                    case "login":
                        serverMessage = message.getString("username");
                        break;
                    case "register":
                        serverMessage = message.getString("registrationSuccess");
                        break;
                    case "map":
                        serverMessage = tmp;
                        break;
                    default:
                        break;
                }
                
            }
            catch(JSONException e)
            {
                e.printStackTrace();
            }
            
        }
        catch(ClassNotFoundException | IOException e)
        {
            e.printStackTrace();
        }
        return serverMessage;
    }
    
    public void sendMessage(String message)
    {
        try
        {
            out.writeObject((Object)message);
            out.flush();
        }
        catch(IOException e)
        {
            e.printStackTrace();
        }
    }
    
    public String getServerMessage()
    {
        return serverMessage;
    }
    
    public static ObjectOutputStream getOutputStream()
    {
        return out;
    }
    
    public static ObjectInputStream getInputStream()
    {
        return in;
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
        catch(IOException e)
        {
            e.printStackTrace();
        }
    }
}


