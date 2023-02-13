package com.hw9.test;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;

public class Info extends AppCompatActivity {

    private ProgressBar progressBar;
    private int progressStatus = 0;

    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if(msg.obj.toString().equals("100")){
                ProgressBar viewById1 = findViewById(R.id.progressBar3);
                viewById1.setVisibility(View.GONE);
            }
        }
    };
    Handler handler2 = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            String s = msg.obj.toString();
            Gson gson=new Gson();
            InforAbas inforAbas = gson.fromJson(s, InforAbas.class);
            Toast.makeText(Info.this,inforAbas.toString(),Toast.LENGTH_LONG).show();

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);
        Intent intent = getIntent();
        String data = intent.getStringExtra("data");
        Toast.makeText(Info.this,data,Toast.LENGTH_LONG).show();

        ImageView imageView = findViewById(R.id.imageView3);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
}