package com.example.loginpage;

import android.os.AsyncTask;

import org.json.JSONObject;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.InetAddress;

public class Login extends AsyncTask<JSONObject, Void, Void>
{
    private static final int SERVER_PORT = 10044;
    private static final String SERVER_IP = "192.168.56.1";
    String serverMessage = "false";
    
    /*Login()
    {
        Thread clientThread = new Thread(new ServerThread());
        clientThread.start();
    }*/
    
    @Override
    protected Void doInBackground(JSONObject... params)
    {
        String clientMessage = params[0].toString();
        System.out.println(clientMessage);
        try
        {
            Socket clientSocket = new Socket(SERVER_IP, SERVER_PORT);
            ObjectOutputStream out = new ObjectOutputStream(clientSocket.getOutputStream());
            out.writeObject(clientMessage);
            out.flush();
            out.close();
        }
        catch(IOException e)
        {
            e.printStackTrace();
        }
        return null;
    }
    
    public Boolean getValidLogin()
    {
        if(serverMessage.equals("true"))
        {
            return true;
        }
        return false;
    }
    
    public String getServerMessage()
    {
        return serverMessage;
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
                    server = serverSocket.accept();
                    ObjectInputStream in = new ObjectInputStream(server.getInputStream());
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


