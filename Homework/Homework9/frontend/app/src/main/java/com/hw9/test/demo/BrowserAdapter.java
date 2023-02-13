package com.hw9.test.demo;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.hw9.test.R;

import java.util.List;

public class BrowserAdapter extends ArrayAdapter<Browser> {

    private int resourceID;

    public BrowserAdapter(Context context, int textViewResourceID , List<Browser> objects){
        super(context,textViewResourceID,objects);
        resourceID = textViewResourceID;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Browser browser = getItem(position);
        View view = LayoutInflater.from(getContext()).inflate(resourceID,null);
        TextView browserName = (TextView)view.findViewById(R.id.name);
        TextView browserCity = (TextView)view.findViewById(R.id.city);
        TextView browserYear = (TextView)view.findViewById(R.id.year);
        browserCity.setText(browser.getCity());
        browserYear.setText(browser.getYear());
        browserName.setText(browser.getName());
        return view;
    }

}
