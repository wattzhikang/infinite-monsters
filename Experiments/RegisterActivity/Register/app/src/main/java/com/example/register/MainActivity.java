package com.example.register;

import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;

public class MainActivity extends AppCompatActivity {

    EditText e1;
    EditText e2;
    EditText e3;
    Button b1;
    Socket s;
    ServerSocket ss;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        e1 = (EditText)findViewById(R.id.etUsername);
        e2 = (EditText)findViewById(R.id.etPassword);
        e3 = (EditText)findViewById(R.id.etConfirmPass);
        b1 = (Button)findViewById(R.id.btRegister);
        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String s1 = e1.getText().toString();
                String s2 = e2.getText().toString();
                String s3 = e3.getText().toString();
                if(s1.equals("")||s2.equals("")||s3.equals(""))
                    Toast.makeText(getApplicationContext(),"Fields are empty",Toast.LENGTH_SHORT).show();
                else if(!s2.equals(s3))
                    Toast.makeText(getApplicationContext(),"Passwords do not match",Toast.LENGTH_SHORT).show();
                else{

                }
            }
        });
        Thread myThread = new Thread (new MyServerThread());
        myThread.start();
    }

    public void send(View v){
        MessageSender messageSender = new MessageSender();
        messageSender.execute(e1.getText().toString());
        messageSender.execute(e2.getText().toString());
    }

    class MyServerThread implements Runnable{

        Socket s;
        ServerSocket ss;
        InputStreamReader isr;
        BufferedReader bufferedReader;
        Handler h = new Handler();
        String message;
        @Override
        public void run() {
            try{
                ss = new ServerSocket(10040);
                while(true)
                {
                    s = ss.accept();
                    isr = new InputStreamReader(s.getInputStream());
                    bufferedReader = new BufferedReader(isr);
                    message = bufferedReader.readLine();

                    h.post(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getApplicationContext(),message, Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
            catch(IOException e){
                e.printStackTrace();
            }
        }
    }
}
