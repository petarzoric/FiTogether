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
    private FirebaseDatabase database;
    private DatabaseReference databaseReference;
    private TextView upCommingT;
    private TextView trainingsday;
    private TextView trainingstime;
    private TextView trainingstype;
    private TextView training1day;
    private TextView training1time;
    private TextView training1type;
    private TextView training2day;
    private TextView training2time;
    private TextView training2type;
    private String uid;
    private GraphView graph;
    private int upcommingtraining1;
    private int upcommingtraining2;





    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.tab1dashboard, container, false);
        database = FirebaseDatabase.getInstance();
        databaseReference = database.getReference();
        uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        graph = rootView.findViewById(R.id.graph);
        upcommingtraining1 = 0;
        upcommingtraining2 = 0;

        upCommingT = rootView.findViewById(R.id.upCommingT);
        trainingsday = rootView.findViewById(R.id.trainingsday);
        training1day = rootView.findViewById(R.id.training1day);
        training2day = rootView.findViewById(R.id.training2day);
        trainingstime = rootView.findViewById(R.id.trainingstime);
        training1time = rootView.findViewById(R.id.training1time);
        training2time = rootView.findViewById(R.id.training2time);
        trainingstype = rootView.findViewById(R.id.trainingstype);
        training1type = rootView.findViewById(R.id.training1type);
        training2type = rootView.findViewById(R.id.training2type);




        return rootView;
    }

    @Override
    public void onStart() {
        super.onStart();
        Calendar c = Calendar.getInstance();
        int m = c.get(Calendar.MONTH) + 1;
        int d = c.get(Calendar.DAY_OF_MONTH);
        int monthdays = Converter.monthDays(Converter.monthConverter(m))+1;
        databaseReference.child("TotalTrainings").child(uid).child(Converter.monthConverter(m)).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Iterable<DataSnapshot> children = dataSnapshot.getChildren();
                int [] trainingsdays = new int[monthdays];

                for (DataSnapshot child : children) {
                trainingsdays[Integer.parseInt(child.getKey())] = 1;
                }
                DataPoint[] dataPoint = new DataPoint[trainingsdays.length];
                for (int i = 0; i <trainingsdays.length; i++) {
                    dataPoint[i] = new DataPoint(i, trainingsdays[i]);
                    if (i >= d && trainingsdays[i] == 1){
                        if (upcommingtraining1 == 0){
                            upcommingtraining1 = i;
                        }else{
                            if (upcommingtraining2 == 0 && i != upcommingtraining1){
                                upcommingtraining2 = i;
                            }
                        }
                    }

                }
                LineGraphSeries<DataPoint> series = new LineGraphSeries<DataPoint>(dataPoint);
                graph.getViewport().setMaxX(trainingsdays.length -1);
                graph.getViewport().setXAxisBoundsManual(true);
                graph.addSeries(series);


                if (upcommingtraining1 != 0){

                    databaseReference.child("TrainingsDate").child(Converter.monthConverter(m)).child(String.valueOf(upcommingtraining1)).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            UserTraining training = dataSnapshot.child(uid).getValue(UserTraining.class);
                            upCommingT.setText("Nächste Trainings");
                            trainingsday.setText("Tag");
                            trainingstime.setText("Zeit");
                            trainingstype.setText("Muskelgruppe");
                            training1day.setText(upcommingtraining1+"."+m);
                            if (training != null) {
                                training1time.setText(training.getTime());
                                training1type.setText(Converter.trainingstypeString(training.getTrainingstype()));
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                    if (upcommingtraining2 != 0){
                        databaseReference.child("TrainingsDate").child(Converter.monthConverter(m)).child(String.valueOf(upcommingtraining2)).addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                UserTraining training = dataSnapshot.child(uid).getValue(UserTraining.class);
                                training2day.setText(upcommingtraining2+"."+m);
                                if (training != null) {
                                    training2time.setText(training.getTime());
                                    training2type.setText(Converter.trainingstypeString(training.getTrainingstype()));
                                }
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });
                    }
                }else{
                    upCommingT.setText("Kein kommendes Training diesen Monat");
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        }
}
