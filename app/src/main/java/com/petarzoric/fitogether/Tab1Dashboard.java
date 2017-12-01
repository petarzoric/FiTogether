package com.petarzoric.fitogether;


import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.support.v4.app.Fragment;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.FirebaseApp;
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
    Button logout;
    FirebaseDatabase database;
    DatabaseReference databaseReference;
    UserProfile profile;
    TextView emailtext;
    TextView nametext;
    TextView leveltext;
    TextView agetext;
    TextView studiotext;
    TextView gendertext;
    String level;
    String studio;
    String gender;
    Button editprofile;




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
        logout = rootView.findViewById(R.id.logout);
        editprofile = rootView.findViewById(R.id.editprofile);
        gendertext = rootView.findViewById(R.id.gendertext);
        final Intent data = getActivity().getIntent();
        final String key = data.getStringExtra("key");

        editprofile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent userdata = new Intent(getActivity(), EditProfile.class);
                userdata.putExtra("email", profile.getEmail());
                userdata.putExtra("name", profile.getName());
                userdata.putExtra("age", profile.getAge());
                userdata.putExtra("level", profile.getUserlevel());
                userdata.putExtra("location", profile.getLocation());
                userdata.putExtra("studio", profile.getStudio());
                userdata.putExtra("gender", gender);
                userdata.putExtra("studios", studio);
                userdata.putExtra("genderint", profile.getGender());
                userdata.putExtra("key", key );
                startActivity(userdata);

            }
        });
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), MainActivity.class);
                intent.putExtra("Signout", true);
                startActivity(intent);
            }
        });
        databaseReference.child("UserData").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
               Iterable<DataSnapshot> children = dataSnapshot.getChildren();
               for(DataSnapshot child : children){
                   if (child.getKey().equals(key)){
                       profile = child.getValue(UserProfile.class);
                   }
               }
                emailtext.setText(profile.getEmail());
                nametext.setText(profile.getName());
                agetext.setText(String.valueOf(profile.getAge()));
                if (profile.getUserlevel() == 0){
                    level = "Anfänger";
                }else if (profile.getUserlevel() == 1){
                    level = "Fortgeschritten";
                }else if (profile.getUserlevel() == 2){
                    level = "Profi";
                }else if (profile.getUserlevel() == 3){
                    level = "Arnold";
                }

                if (profile.getGender() == 0){
                    gender = "Männlich";
                }else if (profile.getGender() == 1){
                    gender = "Weiblich";
                }else if (profile.getGender() == 2){
                    gender = "Anderes";
                }

                leveltext.setText(level);
                gendertext.setText(gender);
                String[] studios = getResources().getStringArray(R.array.Studio);
                String[] location;
                if (profile.getStudio() == 0){
                        location = getResources().getStringArray(R.array.LocationFITSTAR);
                        studio = studios[profile.getStudio()] +" "+location[profile.getLocation()];

                }else if (profile.getStudio() == 1){
                        location = getResources().getStringArray(R.array.LocationFitnessFirst);
                        studio = studios[profile.getStudio()] +" "+location[profile.getLocation()];

                }else if (profile.getStudio() == 2){
                        location = getResources().getStringArray(R.array.LocationBodyandSoul);
                        studio = studios[profile.getStudio()] +" "+location[profile.getLocation()];

                }else if (profile.getStudio() == 3){
                        location = getResources().getStringArray(R.array.LocationMcFit);
                        studio = studios[profile.getStudio()] +" "+location[profile.getLocation()];

                }else if (profile.getStudio() == 4){
                        location = getResources().getStringArray(R.array.LocationCleverFit);
                        studio = studios[profile.getStudio()] +" "+location[profile.getLocation()];

                }else if (profile.getStudio() == 5){
                        location = getResources().getStringArray(R.array.LocationAndere);
                        studio = location[profile.getLocation()];

                }
                studiotext.setText(studio);



            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        return rootView;
    }
}
