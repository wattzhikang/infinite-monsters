package com.myteam.intermon;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

public class ClientSocket
{
    Socket clientSocket;
    ObjectInputStream in;
    ObjectOutputStream out;
    String serverMessage = "";
    
    public void connectSocket()
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
    
    public void connectStreams()
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
    
    public void sendMessage(String message) throws IOException
    {
        byte[] bytes = message.getBytes(StandardCharsets.US_ASCII);
        out.writeObject(Arrays.toString(bytes));
        out.flush();
    }
    
    public String readMessage() throws IOException, ClassNotFoundException
    {
        String message = (String) in.readObject();
        String temp = message.replaceAll("[\\[\\]\\s*]", "");
        System.out.println("temp: " + temp);
        String[] ascii = temp.split(",");
        int[] asciiValues = new int[ascii.length];
        for(int i = 0; i < asciiValues.length; i++)
        {
            asciiValues[i] = Integer.parseInt(ascii[i]);
            serverMessage += (char) asciiValues[i];
        }
        return serverMessage;
    }
}
