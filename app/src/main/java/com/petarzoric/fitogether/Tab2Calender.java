package com.petarzoric.fitogether;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

/**
 * Created by Alex on 26.11.2017.
 */

public class Tab2Calender extends Fragment {
    Spinner training;
    Button saveTraining;
    CalendarView calendar;
    String selectedDate;
    String key;
    DatabaseReference databaseReferencecalender;
    FirebaseDatabase database;
    DatabaseReference databaseReferenceprofile;
    String trainingType;
    Training trainingProfile;
    UserProfile profile;


    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.tab2calender, container, false);
        training = rootView.findViewById(R.id.training);
        ArrayAdapter<CharSequence> trainingadapter = ArrayAdapter.createFromResource(getActivity(), R.array.Training, R.layout.support_simple_spinner_dropdown_item);
        training.setAdapter(trainingadapter);
        saveTraining = rootView.findViewById(R.id.savetraining);

        saveTraining.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        Intent intent = getActivity().getIntent();
        key = intent.getStringExtra("key");
        calendar = rootView.findViewById(R.id.calendarView);
        calendar.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {

            @Override
            public void onSelectedDayChange(CalendarView view, int year, int month,
                                            int dayOfMonth) {
                int d = dayOfMonth;
                int m = month;
                selectedDate = String.valueOf(d)+ "_" + String.valueOf(m) + "_" + String.valueOf(year);
            }
        });


        saveTraining.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveTraining();
            }
        });


        database = FirebaseDatabase.getInstance();
        databaseReferenceprofile = database.getReference();
        databaseReferenceprofile.child("UserData").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Iterable<DataSnapshot> children = dataSnapshot.getChildren();
                for(DataSnapshot child : children){
                    if (child.getKey().equals(key)){
                        profile = child.getValue(UserProfile.class);
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        return rootView;
    }



            public void saveTraining(){
        if (selectedDate != null) {
            databaseReferencecalender = FirebaseDatabase.getInstance().getReference(selectedDate);
            trainingType = training.getSelectedItem().toString();
            trainingProfile = new Training(trainingType, Level.parseToInt(profile.getLevel()), profile.getStudio(), profile.getLocation());
            databaseReferencecalender.child(key).setValue(trainingProfile);
        }else{
            Toast.makeText(getActivity(), "Please Select a Date", Toast.LENGTH_SHORT).show();
        }

    }
}