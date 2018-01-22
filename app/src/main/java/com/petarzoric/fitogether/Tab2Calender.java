package com.petarzoric.fitogether;

import android.app.TimePickerDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Calendar;
import java.util.TimeZone;

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
    int trainingType;
    UserTraining trainingProfile;
    UserProfile profile;
    int level;
    int studio;
    int location;
    int m;
    int d;
    EditText time;
    String times;



    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.tab2calender, container, false);
        training = rootView.findViewById(R.id.training);
        ArrayAdapter<CharSequence> trainingadapter = ArrayAdapter.createFromResource(getActivity(), R.array.Training, R.layout.support_simple_spinner_dropdown_item);
        training.setAdapter(trainingadapter);
        time = rootView.findViewById(R.id.timestart);
        saveTraining = rootView.findViewById(R.id.savetraining);
        saveTraining.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        key = FirebaseAuth.getInstance().getCurrentUser().getUid();
        calendar = rootView.findViewById(R.id.calendarView);
        calendar.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {

            @Override
            public void onSelectedDayChange(CalendarView view, int year, int month,
                                            int dayOfMonth) {
                d = dayOfMonth;
                m = month + 1;
                selectedDate = String.valueOf(d)+ "_" + String.valueOf(m) + "_" + String.valueOf(year);
            }
        });


        saveTraining.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveTraining();
            }
        });
        time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar cal = Calendar.getInstance(TimeZone.getDefault()); // Get current date


// Create the DatePickerDialog instance
                TimePickerDialog timePicker = new TimePickerDialog(getContext(),
                        timePickerListener,
                        R.style.Theme_AppCompat_Light,
                        cal.get(Calendar.HOUR_OF_DAY),
                        true);
                        timePicker.setCancelable(false);
                timePicker.setTitle("Select the time");
                timePicker.show();
            }
        });


        database = FirebaseDatabase.getInstance();
        databaseReferenceprofile = database.getReference();
        databaseReferenceprofile.child("Users2").child(key).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                        profile = dataSnapshot.getValue(UserProfile.class);


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        return rootView;
    }



    public void saveTraining(){
        if (selectedDate != null) {
            if (!time.getText().toString().equals("")) {
                databaseReferencecalender = FirebaseDatabase.getInstance().getReference("TrainingsDate");
                trainingType = training.getSelectedItemPosition();
                studio = profile.getStudio();
                location = profile.getLocation();
                level = Level.parseToInt(profile.getLevel());
                times = time.getText().toString();
                trainingProfile = new UserTraining(selectedDate, trainingType, key, level, studio, location, times);
                databaseReferencecalender.child(Converter.monthConverter(m)).child(String.valueOf(d)).child(key).setValue(trainingProfile);
                Toast.makeText(getActivity(), "Training saved", Toast.LENGTH_SHORT).show();


            }else{
                Toast.makeText(getActivity(), "Please Select a Time", Toast.LENGTH_SHORT).show();
            }
        }else{
            Toast.makeText(getActivity(), "Please Select a Date", Toast.LENGTH_SHORT).show();
        }

    }
    private TimePickerDialog.OnTimeSetListener timePickerListener = new TimePickerDialog.OnTimeSetListener(){
        @Override
        public void onTimeSet(TimePicker timePicker, int hourOfDay, int minute) {
                String times = String.valueOf(hourOfDay)+":"+String.valueOf(minute);
                time.setText(times);


        }

        // when dialog box is closed, below method will be called.

    };

}