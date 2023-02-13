package com.hw9.test;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.hw9.test.demo.Browser;
import com.hw9.test.demo.BrowserAdapter;
import com.google.gson.Gson;

import java.time.LocalDateTime;
import java.time.format.TextStyle;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class Index extends AppCompatActivity {

    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            String s = msg.obj.toString();
            Intent intent = new Intent(Index.this,Vague.class);
            intent.putExtra("data", s);
            startActivity(intent);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_index);
        TextView viewById = findViewById(R.id.textView);
        TextView viewById1 = findViewById(R.id.textView2);

        LocalDateTime localDateTime = LocalDateTime.now();
        viewById.setText(localDateTime.getDayOfMonth() + " " + localDateTime.getMonth().getDisplayName(TextStyle.SHORT, Locale.ENGLISH) + " " + localDateTime.getYear());

        SearchView search = findViewById(R.id.searchView);
        search.setIconifiedByDefault(false);
        search.setSubmitButtonEnabled(true);
        search.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                Message msg = new Message();
                msg.obj = query;
                handler.sendMessage(msg);
                return true;
            }
            @Override
            public boolean onQueryTextChange(String newText) {
                return true;
            }
        });

        SharedPreferences sp1 = getSharedPreferences("data", MODE_PRIVATE);
        String obtain = sp1.getString("msg", "");
        Toast.makeText(Index.this, obtain, Toast.LENGTH_SHORT).show();
        if("".equals(obtain)){

        }else {
            List<Browser> browsers = new ArrayList<>();
            Gson gson=new Gson();
            List<Information> list = gson.fromJson(obtain, List.class);
            for (int i = 0; i < list.size(); i++) {
                Browser browser = new Browser();
                Information information = list.get(i);
                browser.name=information.name;
                browser.city=information.nationality;
                browser.year=information.birthday;
                browsers.add(browser);
            }
            BrowserAdapter adapter = new BrowserAdapter(Index.this, R.layout.browser_item, browsers);
            ListView listView = (ListView) findViewById(R.id.ListView1);
            listView.setAdapter(adapter);
        }

        TextView artsy = findViewById(R.id.tv_artsy);
        artsy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse("https://www.artsy.net/"));
                startActivity(intent);
            }
        });
    }

}