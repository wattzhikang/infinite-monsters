package com.example.loginpage;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity
{
    Button b1, b2;
    EditText username, password;
    TextView tx1;
    
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        b1 =  findViewById(R.id.Login);
        b2 = findViewById(R.id.Cancel);
        username = findViewById(R.id.Username);
        password = findViewById(R.id.Password);
        tx1 = findViewById(R.id.Intermon);
        
        b1.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if(username.getText().toString().equals("admin") && password.getText().toString().equals("admin"))
                {
                    Toast.makeText(MainActivity.this, "Redirectin...", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    Toast.makeText(MainActivity.this, "Wrong Credentials", Toast.LENGTH_SHORT).show();
                    tx1.setVisibility(View.VISIBLE);
                    tx1.setBackgroundColor(Color.RED);
                }
            }
        });
        
        b2.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                finish();
            }
        });
    }
}
