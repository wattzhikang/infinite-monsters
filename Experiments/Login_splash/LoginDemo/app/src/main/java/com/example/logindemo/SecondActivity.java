package com.example.logindemo;

import android.os.Handler;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

public class SecondActivity extends AppCompatActivity {

    private static int splashInterval = 2000;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);

        new Handler().postDelayed(new Runnable(){
            @Override
            public void run(){
                //TODO Auto-generated method stub
                Intent i = new Intent(SecondActivity.this, MainActivity.class);
                startActivity(i);

                this.finish();
            }

         private void finish(){
            //TODO Auto-generated method stub
         }
        },splashInterval);
    };
}
