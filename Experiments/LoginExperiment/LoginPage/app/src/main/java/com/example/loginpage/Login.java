package com.example.loginpage;

import android.os.AsyncTask;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.PrintWriter;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class Login extends AsyncTask<String, Void, Void>
{
    Socket clientSocket;
    ObjectInputStream in;
    ObjectOutputStream out;
    PrintWriter pw;
    private static final int SERVER_PORT = 10044;
    private static final String SERVER_IP = "192.168.56.1";
    
    
    @Override
    protected Void doInBackground(String... voids)
    {
        String username = voids[0];
        String password = voids[1];
        try
        {
            System.out.println(username);
            clientSocket = new Socket("192.168.56.1", 10044);
            in = new ObjectInputStream(clientSocket.getInputStream());
            out = new ObjectOutputStream(clientSocket.getOutputStream());
            pw = new PrintWriter(clientSocket.getOutputStream());
            pw.println(username);
            pw.flush();
            pw.close();
            /*out.writeObject((Object)user);
            out.flush();
            out.close();*/
            clientSocket.close();
        }
        catch(IOException e)
        {
            e.printStackTrace();
        }
        return null;
    }
}
