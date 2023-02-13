package com.hw9.test;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.widget.ProgressBar;

public class Splash extends AppCompatActivity {

    private ProgressBar progressBar;
    private int progressStatus = 0;

    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            progressBar.setProgress((Integer) msg.obj);
            if ((Integer)msg.obj==100){
                Intent in=new Intent(Splash.this,Index.class);
                startActivity(in);
                finish();
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        progressBar = findViewById(R.id.progressBar);
        new Thread(new Runnable() {
            public void run() {
                int pm = progressBar.getMax();

                while (progressStatus<pm){

                    progressStatus+=10;
                    Message msg = new Message();
                    msg.obj = progressStatus;

                    handler.sendMessage(msg);
                    try {
                        Thread.sleep(300);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

            }
        }).start();
    }

}