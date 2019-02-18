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
    private static final int SERVER_PORT = 10044;
    private static final String SERVER_URL = "cs309-yt-1.misc.iastate.edu";
    String serverMessage = "";
    private final String SERVER_IP = "10.24.226.77";
    private  final String MY_IP = "192.168.1.2";
    private boolean received = true;
    
    @Override
    protected Void doInBackground(JSONObject... params)
    {
        String clientMessage = params[0].toString();
        try
        {
            clientSocket = new Socket(MY_IP, SERVER_PORT);
            //System.out.println("connected");
            in = new ObjectInputStream(clientSocket.getInputStream());
            out = new ObjectOutputStream(clientSocket.getOutputStream());
            System.out.println("here");
            sendMessage(clientMessage);
            while(received != true)
            {
                serverMessage = readMessage();
            }
        }
        catch(IOException e)
        {
            e.printStackTrace();
        }
        return null;
    }
    public String readMessage()
    {
        try
        {
            serverMessage = (String) in.readObject();
        }
        catch(ClassNotFoundException | IOException e)
        {
            e.printStackTrace();
        }
        if(!serverMessage.isEmpty())
        {
            received = true;
        }
        return serverMessage;
    }
    
    public void sendMessage(String message)
    {
        try
        {
            System.out.println("connected");
            System.out.println(clientSocket.isConnected());
            out.writeObject((Object)message);
            out.flush();
            out.close();
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


