package com.hw9.test;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.hw9.test.demo.Results;
import com.hw9.test.demo.Search;
import com.hw9.test.untils.Http;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

public class Vague extends AppCompatActivity {

    private ProgressBar progressBar;
    private int progressStatus = 0;

    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if(msg.obj.toString().equals("100")){
                View viewById1 = findViewById(R.id.progressBar3);
                viewById1.setVisibility(View.GONE);
            }
        }
    };

    Handler handler1 = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            Picture s = (Picture)msg.obj;
            Intent intent = new Intent(Vague.this,Deatils.class);
            intent.putExtra("data",s.id+"/"+s.title);
            startActivity(intent);
        }
    };


    Handler handler2 = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            String s = msg.obj.toString();
            Gson gson=new Gson();
            Search search = gson.fromJson(s, Search.class);

            TextView noResult = findViewById(R.id.tv_no_result);

            List<Results> results = search._embedded.results;
            ArrayList<Picture> pictures = new ArrayList<>();
            if(!results.isEmpty()){
                noResult.setVisibility(View.GONE);
                for (int i = 0; i < results.size(); i++) {
                    Picture picture = new Picture();
                    Results results1 = results.get(i);
                    String title = results1.title;
                    String[] split = results1._links.self.href.split("/");
                    picture.id=split[split.length-1];
                    picture.title=title;

                    String[] urlArray = results1._links.thumbnail.href.split("/");
                    picture.url = urlArray[urlArray.length - 1].startsWith("missing_image") ?
                            "https://play-lh.googleusercontent.com/wvJ2bstHlk_uoSZ5FhYu8pRb2ejSvqVAa6QqzREnfI7cbAnov2yUBvPDsSaxg8I6neb6" :
                            results1._links.thumbnail.href;

                    pictures.add(picture);
                }
                MySimpleArrayAdapter adapter = new MySimpleArrayAdapter(Vague.this, pictures);
                ListView listView  = (ListView) findViewById(R.id.ListView12);
                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        Picture browser = pictures.get(position);
                        new Thread(()->{
                            Message message = new Message();
                            message.obj=browser;
                            handler1.sendMessage(message);
                        }).start();
                    }
                });
                listView.setAdapter(adapter);
            }
            else{
                noResult.setVisibility(View.VISIBLE);
            }

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vague);
        Intent intent = getIntent();
        String data = intent.getStringExtra("data");
//        Toast.makeText(Vague.this,data,Toast.LENGTH_LONG).show();
        TextView viewById = findViewById(R.id.textView9);
        viewById.setText(data);
        new Thread(new Runnable() {
            public void run() {
                String s = Http.syncGet("https://hw9backend-dot-sound-splicer-351619.wl.r.appspot.com/search/" + data);
                Message msg1 = new Message();
                msg1.obj = s;
                handler2.sendMessage(msg1);
                progressStatus=100;
                Message msg = new Message();
                msg.obj = progressStatus;
                handler.sendMessage(msg);
                try {
                    Thread.sleep(300);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();

        ImageView imageView = findViewById(R.id.imageView9);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
}