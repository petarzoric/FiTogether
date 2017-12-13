package com.petarzoric.fitogether;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;


public class Listadapter extends ArrayAdapter<String> {
    public ArrayList <UserProfile> matches;
    Activity context;
    String level;

    public Listadapter(Activity context, ArrayList <UserProfile> matches) {
        super(context, R.layout.listviewitems);
        this.context = context;
        this.matches = matches;

    }

    @Override
    public int getCount() {
        return matches.size();
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater in = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        convertView = in.inflate(R.layout.listviewitems, parent, false);
        TextView txt1 = convertView.findViewById(R.id.uname);
        TextView txt2 = convertView.findViewById(R.id.ulevel);
        ImageView img = convertView.findViewById(R.id.icon);
        if (matches.get(position).getLevel() == Level.ANFÄNGER){
            level = "Anfänger";
        }else if (matches.get(position).getLevel() == Level.FORTGESCHRITTEN){
            level = "Fortgeschritten";
        }else if (matches.get(position).getLevel() == Level.PROFI){
            level = "Profi";
        }else if (matches.get(position).getLevel() == Level.ARNOLD){
            level = "Arnold";
        }
        txt1.setText(matches.get(position).getName());
        txt2.setText(level);
        return convertView;
    }



}