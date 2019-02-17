package com.example.loginpage;

import android.os.AsyncTask;

import org.json.JSONException;
import org.json.JSONObject;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.PrintWriter;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.InetAddress;

public class Login extends AsyncTask<JSONObject, Void, Void>
{
    Socket clientSocket;
    ObjectInputStream in;
    ObjectOutputStream out;
    PrintWriter pw;
    InetAddress ina;
    private static final int SERVER_PORT = 10044;
    private static final String SERVER_IP = "192.168.1.2";
    
    
    @Override
    protected Void doInBackground(JSONObject... params)
    {
        JSONObject client = params[0];
        String username = "";
        String password = "";
        try
        {
            username = client.getString("username");
            password = client.getString("password");
        }
        catch(JSONException e)
        {
            e.printStackTrace();
        }
        System.out.println(username);
        System.out.println(password);
        try
        {
            ina = InetAddress.getLocalHost();
            clientSocket = new Socket(SERVER_IP, 10044);
            
            //in = new ObjectInputStream(clientSocket.getInputStream());
            out = new ObjectOutputStream(clientSocket.getOutputStream());
            out.writeObject(username);
            out.writeObject(password);
            out.flush();
            out.close();
        }
        catch(IOException e)
        {
            e.printStackTrace();
        }
        return null;
    }
}
