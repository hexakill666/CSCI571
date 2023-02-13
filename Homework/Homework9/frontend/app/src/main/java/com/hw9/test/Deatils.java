package com.hw9.test;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.hw9.test.untils.Http;
import com.google.gson.Gson;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class Deatils extends AppCompatActivity {
    private int progressStatus = 0;
    private ViewPager mViewPager;
    private List<View> mViews;
    private View view1,view2;
    private PagerAdapter mPagerAdapter;
    public String data;
    public String WorID;
    private ProgressBar progressBar;
    TextView name;

    Handler handler33 = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            Projects projects=(Projects) msg.obj;
            String data = projects.data;
            Gson gson=new Gson();
            ObjectA genes = gson.fromJson(data, ObjectA.class);
            List<Genes> genes1 = genes._embedded.genes;

            View view = projects.view;
            TextView viewById = view.findViewById(R.id.textView8);
            TextView viewById1 = view.findViewById(R.id.textView11);
            TextView textView1 = view.findViewById(R.id.Description);
            TextView textView13 = view.findViewById(R.id.Description1);

            if(!genes1.isEmpty()){
                Genes genes2 = genes1.get(Integer.parseInt(projects.id));
                viewById.setText("category");
                textView1.setText("Description:");
                viewById1.setText(genes2.name);
                textView13.setText(genes2.description);
                ImageView viewById3 = findViewById(R.id.imageView8);
                new Thread(()->{
                    try {
                        URL url = new URL(genes2._links.thumbnail.href);
                        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                        connection.setRequestMethod("GET");
                        connection.setConnectTimeout(10000);
                        int code = connection.getResponseCode();
                        if (code == 200) {
                            InputStream inputStream = connection.getInputStream();
                            Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                            Probject probject = new Probject();
                            probject.bitmap=bitmap;
                            probject.view=view;
                            Message msg1 = Message.obtain();
                            msg1.obj = probject;
                            handler22.sendMessage(msg1);
                        }
                    } catch (MalformedURLException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }).start();
            }
            else{
                textView1.setText("");
                textView13.setText("No Category Available");
            }

        }
    };

    Handler handler22 = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            Probject probject= (Probject) msg.obj;
            View view = probject.view;
            ImageView viewById = view.findViewById(R.id.imageView8);
            viewById.setImageBitmap(probject.bitmap);
        }
    };

    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if(msg.obj.toString().equals("100")){
                ProgressBar viewById1 = findViewById(R.id.progressBar22);
                viewById1.setVisibility(View.GONE);
            }
        }
    };
    Handler handler2 = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            String s = msg.obj.toString();
            Gson gson = new Gson();
            Information information = gson.fromJson(s, Information.class);

            TextView textView6 = findViewById(R.id.textView6);
            textView6.setText("Name:");

            TextView nationality = findViewById(R.id.Nationality);
            nationality.setText("Nationality:");

            TextView birthday = findViewById(R.id.Birthday);
            birthday.setText("Birthday:");

            TextView deathday = findViewById(R.id.Deathday);
            deathday.setText("Deathday:");

            TextView biography = findViewById(R.id.Biography);
            biography.setText("Biography:");

            name = findViewById(R.id.textView7);
            name.setText(information.name);

            TextView viewById = findViewById(R.id.Nationality1);
            viewById.setText(information.nationality);

            TextView Birthday1 =findViewById(R.id.Birthday1);
            Birthday1.setText(information.birthday);

            TextView Deathday1 =findViewById(R.id.Deathday1);
            Deathday1.setText(information.deathday);

            TextView Biography1 =findViewById(R.id.Biography1);
            Deathday1.setText(information.biography);

            ImageView viewById1 = findViewById(R.id.imageView4);
            viewById1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    SharedPreferences sp = getSharedPreferences("data", MODE_PRIVATE);
                    String obtain = sp.getString("msg", "");
                    if("".equals(obtain)){
                        sp.edit().putString("msg",s).apply();
                    }else{
                        Gson gson1=new Gson();
                        List<Information> list = gson1.fromJson(obtain, List.class);
                        list.add(information);
                    }
                }
            });
        }
    };
    Handler handler3 = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            String s = msg.obj.toString();
            Gson gson=new Gson();

            InforAbas information = gson.fromJson(s, InforAbas.class);
            List<Artworks> artworks = information._embedded.artworks;
            ArrayList<Picture> pictures = new ArrayList<>();

            if(!artworks.isEmpty()){
                for (int i = 0; i < artworks.size(); i++) {
                    Picture picture = new Picture();
                    Artworks artworks1 = artworks.get(i);
                    picture.id=artworks1.id;
                    picture.title=artworks1.title;
                    picture.url=artworks1._links.thumbnail.href;
                    pictures.add(picture);
                }
                MyAapter adapter = new MyAapter(Deatils.this, pictures);
                ListView viewById = findViewById(R.id.test);
                viewById.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        Picture browser = pictures.get(position);
                        final AlertDialog.Builder alertDialog7 = new AlertDialog.Builder(Deatils.this);
                        View view1 = View.inflate(Deatils.this, R.layout.activity_alter_dialog_setview, null);
                        new Thread(()->{
                            String s1 = Http.syncGet("https://hw9backend-dot-sound-splicer-351619.wl.r.appspot.com/getGenes/" + browser.id);
                            Projects projects = new Projects();
                            projects.data=s1;
                            projects.id=String.valueOf(position);
                            projects.view=view1;
                            Message message=new Message();
                            message.obj=projects;
                            handler33.sendMessage(message);
                        }).start();
                        alertDialog7
                                .setView(view1)
                                .create();
                        final AlertDialog show = alertDialog7.show();
                    }
                });
                viewById.setAdapter(adapter);
            }
            else {
                TextView textView = findViewById(R.id.tv_no_artwork);
                textView.setText("No Artwork Available");
                ListView viewById = findViewById(R.id.test);
                viewById.setVisibility(View.GONE);
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_deatils);
        Intent intent = getIntent();
        String data = intent.getStringExtra("data");
        String[] split = data.split("/");

        TextView viewById = findViewById(R.id.textView3);
        viewById.setText(split[1]);
        new Thread(new Runnable() {
            public void run() {
                String url1="https://hw9backend-dot-sound-splicer-351619.wl.r.appspot.com/getArtist/"+split[0];
                String s = Http.syncGet(url1);

                String url2="https://hw9backend-dot-sound-splicer-351619.wl.r.appspot.com/getArtworks/"+split[0];
                String s1 = Http.syncGet(url2);
                Message msg2 = new Message();
                msg2.obj = s1;
                handler3.sendMessage(msg2);
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

        mViewPager=findViewById(R.id.viewpager);
        LayoutInflater inflater = getLayoutInflater();
        view1=inflater.inflate(R.layout.layout1,null);

        view2=inflater.inflate(R.layout.layout2,null);
        mViews=new ArrayList<View>();
        mViews.add(view1);
        mViews.add(view2);
        mPagerAdapter=new PagerAdapter() {
            @Override   //返回要滑动的VIew的个数
            public int getCount() {
                return mViews.size();
            }

            @Override
            public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
                return view==object;
            }

            @Override
            public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
                container.removeView(mViews.get(position));
            }

            @NonNull
            @Override
            public Object instantiateItem(@NonNull ViewGroup container, int position) {
                container.addView(mViews.get(position));
                return mViews.get(position);
            }
        };
        mViewPager.setAdapter(mPagerAdapter);
        LinearLayout viewById2 = findViewById(R.id.linearLayout2);
        viewById2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mViewPager.setCurrentItem(0);
            }
        });
        LinearLayout viewById3 = findViewById(R.id.linearLayout3);
        viewById3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mViewPager.setCurrentItem(1);
            }
        });

        ImageView imageView = findViewById(R.id.imageView66);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
}