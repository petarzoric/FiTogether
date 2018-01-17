package com.petarzoric.fitogether;


import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.support.v4.app.Fragment;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


/**
 * Created by Alex on 24.11.2017.
 */

public class Tab1Dashboard extends Fragment {
    FirebaseDatabase database;
    DatabaseReference databaseReference;
    UserProfile profile;
    TextView emailtext;
    TextView nametext;
    TextView leveltext;
    TextView agetext;
    TextView studiotext;
    TextView gendertext;





    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.tab1dashboard, container, false);
        database = FirebaseDatabase.getInstance();
        databaseReference = database.getReference();
        emailtext = rootView.findViewById(R.id.emailtext);
        nametext = rootView.findViewById(R.id.nametext);
        leveltext = rootView.findViewById(R.id.leveltext);
        agetext = rootView.findViewById(R.id.agetext);
        studiotext = rootView.findViewById(R.id.studiotext);
        gendertext = rootView.findViewById(R.id.gendertext);
        final String key = FirebaseAuth.getInstance().getCurrentUser().getUid();



        databaseReference.child("Users2").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
               Iterable<DataSnapshot> children = dataSnapshot.getChildren();
               for(DataSnapshot child : children){
                   if (child.getKey().equals(FirebaseAuth.getInstance().getCurrentUser().getUid())){
                       profile = child.getValue(UserProfile.class);
                   }
               }
                emailtext.setText(profile.getEmail());
                nametext.setText(profile.getName());
                agetext.setText(String.valueOf(profile.getAge()));
                leveltext.setText(Level.parseToString(profile.getLevel()));
                gendertext.setText(Gender.parseToString(profile.getGender()));
                studiotext.setText(Converter.studioString(profile.getStudio(), profile.getLocation(), getResources()));

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        return rootView;
    }
}
