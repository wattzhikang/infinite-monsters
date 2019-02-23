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
    private  final String MY_IP = "192.168.1.2";

    @Override
    protected Void doInBackground(JSONObject... params)
    {
        String clientMessage = params[0].toString();
        try
        {
            clientSocket = new Socket(MY_IP, Constants.SERVER_PORT);
            serverCommunication sc = new serverCommunication(clientSocket);
            sc.sendMessage(clientMessage);
            sc.readMessage();
        }
        catch(IOException e)
        {
            e.printStackTrace();
        }
        return null;
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


