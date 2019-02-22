package com.example.loginpage;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class serverCommunication
{
    ObjectInputStream in;
    ObjectOutputStream out;
    static String serverMessage = "";
    
    serverCommunication(Socket clientSocket)
    {
        try
        {
            out = new ObjectOutputStream(clientSocket.getOutputStream());
            out.flush();
            in = new ObjectInputStream(clientSocket.getInputStream());
        }
        catch(IOException e)
        {
            e.printStackTrace();
        }
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
                        serverMessage = message.getString("loginSuccess");
                        break;
                    case "register":
                        serverMessage = message.getString("loginSuccess");
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
    
    public static String getServerMessage()
    {
        return serverMessage;
    }
    
    public Boolean getValidLogin()
    {
        if(!serverMessage.isEmpty())
        {
            return true;
        }
        return false;
    }
}
