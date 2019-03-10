package com.myteam.intermon;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

public class ClientSocket
{
    private Socket clientSocket;
    private ObjectInputStream in;
    private ObjectOutputStream out;
    private String clientMessage = null;
    private String serverMessage = null;
    
    /*public ClientSocket()
    {
        ClientThread ct = new ClientThread();
        ct.start();
    }*/
    void connectSocket()
    {
        try
        {
            clientSocket = new Socket("192.168.1.2", 10044);
            System.out.println("socket connected: " + clientSocket.isConnected());
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
    
    void connectStreams()
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
    
    public void setMessage(String message)
    {
        clientMessage = message;
    }
    
    public String getMessage()
    {
        return serverMessage;
    }
    
    public void sendMessage(String message) throws IOException
    {
        System.out.println("client: " + message);
        byte[] bytes = message.getBytes(StandardCharsets.US_ASCII);
        out.writeObject(Arrays.toString(bytes));
        out.flush();
    }
    
    public String readMessage() throws IOException, ClassNotFoundException
    {
        String message = (String) in.readObject();
        String temp = message.replaceAll("[\\[\\]\\s*]", "");
        String[] ascii = temp.split(",");
        int[] asciiValues = new int[ascii.length];
        String tempMessage = "";
        for(int i = 0; i < asciiValues.length; i++)
        {
            asciiValues[i] = Integer.parseInt(ascii[i]);
            tempMessage += (char) asciiValues[i];
        }
        return tempMessage;
    }
}
