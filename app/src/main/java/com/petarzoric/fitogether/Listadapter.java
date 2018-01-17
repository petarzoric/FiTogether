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
import com.squareup.picasso.Picasso;



public class Listadapter extends ArrayAdapter<String> {
    UserProfile[] matches;
    Activity context;

    public Listadapter(Activity context, UserProfile[] matches) {
        super(context, R.layout.listviewitems);
        this.context = context;
        this.matches = matches;

    }

    @Override
    public int getCount() {
        return matches.length;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater in = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        convertView = in.inflate(R.layout.listviewitems, parent, false);
        TextView txt1 = convertView.findViewById(R.id.uname);
        TextView txt2 = convertView.findViewById(R.id.ulevel);
        TextView txt3 = convertView.findViewById(R.id.age);
        TextView txt4 = convertView.findViewById(R.id.ugender);
        ImageView img = convertView.findViewById(R.id.icon);
        Picasso.with(context).load(matches[position].getThumbURL()).placeholder(R.drawable.image_preview).into(img);


        txt1.setText(matches[position].getName());
        txt2.setText(Level.parseToString(matches[position].getLevel()));
        txt3.setText(String.valueOf(matches[position].getAge()));
        txt4.setText(Gender.parseToString(matches[position].getGender()));
        return convertView;
    }






}