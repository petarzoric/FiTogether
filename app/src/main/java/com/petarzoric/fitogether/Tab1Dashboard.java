package com.petarzoric.fitogether;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.support.v4.app.Fragment;
import android.widget.TextView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.util.Calendar;


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
    String uid;
    GraphView graph;





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
        uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        graph = rootView.findViewById(R.id.graph);



        return rootView;
    }

    @Override
    public void onStart() {
        super.onStart();

        databaseReference.child("Users2").child(uid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                profile = dataSnapshot.getValue(UserProfile.class);
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
        Calendar c = Calendar.getInstance();
        int m = c.get(Calendar.MONTH) + 1;
        int d = c.get(Calendar.DAY_OF_MONTH);
        databaseReference.child("TotalTrainings").child(uid).child(Converter.monthConverter(m)).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Iterable<DataSnapshot> children = dataSnapshot.getChildren();
                int [] trainingsdays = new int[Converter.monthDays(Converter.monthConverter(m))+1];

                for (DataSnapshot child : children) {
                trainingsdays[Integer.parseInt(child.getKey())] = 1;
                }
                DataPoint[] dataPoint = new DataPoint[trainingsdays.length];
                for (int i = 0; i <trainingsdays.length; i++) {
                    dataPoint[i] = new DataPoint(i, trainingsdays[i]);

                }
                LineGraphSeries<DataPoint> series = new LineGraphSeries<DataPoint>(dataPoint);
                graph.getViewport().setMaxX(trainingsdays.length -1);
                graph.getViewport().setXAxisBoundsManual(true);
                graph.addSeries(series);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }
}
