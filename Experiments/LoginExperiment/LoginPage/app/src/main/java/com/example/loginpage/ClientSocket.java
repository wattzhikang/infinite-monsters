package com.example.loginpage;

import android.os.AsyncTask;

import org.json.JSONObject;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;


public class ClientSocket extends AsyncTask<JSONObject, Void, Void>
{
    private Socket clientSocket;
    private ObjectInputStream in;
    private ObjectOutputStream out;
    private static final int SERVER_PORT = 10042;
    private static final String SERVER_URL = "cs309-yt-1.misc.iastate.edu";
    String serverMessage = "";
    private final String SERVER_IP = "10.24.226.77";
    private  final String MY_IP = "192.168.56.1";

    @Override
    protected Void doInBackground(JSONObject... params)
    {
        String clientMessage = params[0].toString();
        try
        {
            clientSocket = new Socket(SERVER_URL, SERVER_PORT);
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
        serverMessage = null;
        try
        {
            serverMessage = in.readObject().toString();
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
    
    public Boolean getValidLogin()
    {
        if(!serverMessage.isEmpty())
        {
            return true;
        }
        return false;
    }
    
    public String getServerMessage()
    {
        return serverMessage;
    }
    
}


