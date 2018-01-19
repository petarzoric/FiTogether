package com.petarzoric.fitogether;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Alex on 19.01.2018.
 */

public class SearchViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    View view;
    public SearchViewHolder(View itemView) {
        super(itemView);
        view = itemView;
        view.setOnClickListener(this);
    }

    public void setName(String name){
        TextView userNameView =  view.findViewById(R.id.uname);
        userNameView.setText(name);
    }

    public void setAge(String age){
        TextView userage =  view.findViewById(R.id.uage);
        userage.setText(age);
    }
    public void setLevel(String level){
        TextView userage =  view.findViewById(R.id.ulevel);
        userage.setText(level);
    }

    public void setGender(String gender){
        TextView usergender = view.findViewById(R.id.ugender);
        usergender.setText(gender);

    }

    public void setImage(String image, Context context){
        CircleImageView userImageView = view.findViewById(R.id.usericon);


        Picasso.with(context).load(image).placeholder(R.drawable.image_preview).into(userImageView);
    }


    @Override
    public void onClick(View view) {

    }
}

