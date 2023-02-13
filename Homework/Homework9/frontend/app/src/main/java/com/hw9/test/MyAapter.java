package com.hw9.test;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.hw9.test.demo.Browser;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

public class MyAapter extends ArrayAdapter<Picture> {
    private final Context context;
    private final List<Picture> values;

    private int resourceID;
    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            Probject s = (Probject)msg.obj;
            ImageView viewById = s.view.findViewById(R.id.imageView2);
            viewById.setImageBitmap(s.bitmap);
        }
    };
    public MyAapter(Context context, List<Picture> values) {
        super(context, R.layout.item, values);
        this.context = context;
        this.values = values;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Picture browser = getItem(position);
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.item, parent, false);
        TextView browserName = rowView.findViewById(R.id.textView10);
        browserName.setText(browser.title);
        new Thread(()->{
            try {
                URL url = new URL(browser.url);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");
                connection.setConnectTimeout(10000);
                int code = connection.getResponseCode();
                if (code == 200) {
                    InputStream inputStream = connection.getInputStream();
                    Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                    Probject probject = new Probject();
                    probject.bitmap=bitmap;
                    probject.picture=browser;
                    probject.view=rowView;
                    Message msg = Message.obtain();
                    msg.obj = probject;
                    handler.sendMessage(msg);
                }
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();

        return rowView;
    }


}
