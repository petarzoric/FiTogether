package com.petarzoric.fitogether;

import android.app.TimePickerDialog;
import android.content.Intent;
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
    private Spinner training;
    private Button saveTraining;
    private Button search;
    private CalendarView calendar;
    private String selectedDate;
    private String key;
    private DatabaseReference databaseReferencecalender;
    private FirebaseDatabase database;
    private DatabaseReference databaseReferenceprofile;
    private int trainingType;
    private UserTraining trainingProfile;
    private UserProfile profile;
    private int level;
    private int studio;
    private int location;
    private int m;
    private int d;
    private int day;
    private int month;
    private EditText time;
    private String times;




    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.tab2calender, container, false);
        training = rootView.findViewById(R.id.training);
        ArrayAdapter<CharSequence> trainingadapter = ArrayAdapter.createFromResource(getActivity(), R.array.Training, R.layout.support_simple_spinner_dropdown_item);
        training.setAdapter(trainingadapter);
        time = rootView.findViewById(R.id.timestart);

        saveTraining = rootView.findViewById(R.id.savetraining);
        search = rootView.findViewById(R.id.search);
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                searchForDate();

            }
        });


        key = FirebaseAuth.getInstance().getCurrentUser().getUid();
        calendar = rootView.findViewById(R.id.calendarView);
        Calendar c = Calendar.getInstance();
        d = c.get(Calendar.DAY_OF_MONTH);
        m = c.get(Calendar.MONTH) + 1;
        day = c.get(Calendar.DAY_OF_MONTH);
        month = c.get(Calendar.MONTH) + 1;
        calendar.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
                                             @Override
                                             public void onSelectedDayChange(CalendarView view, int year, int month,int dayOfMonth) {
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
        if (m != month || d>=day) {
            if (!time.getText().toString().equals("")) {
                databaseReferencecalender = FirebaseDatabase.getInstance().getReference("TrainingsDate");
                trainingType = training.getSelectedItemPosition();
                studio = profile.getStudio();
                location = profile.getLocation();
                level = Level.parseToInt(profile.getLevel());
                times = time.getText().toString();
                trainingProfile = new UserTraining(selectedDate, trainingType, key, level, studio, location, times);
                databaseReferencecalender.child(Converter.monthConverter(m)).child(String.valueOf(d)).child(key).setValue(trainingProfile);
                databaseReferenceprofile.child("TotalTrainings").child(key).child(Converter.monthConverter(m)).child(String.valueOf(d)).setValue(training.getSelectedItem().toString());

                Toast.makeText(getActivity(), "Training saved", Toast.LENGTH_SHORT).show();


            }else{
                Toast.makeText(getActivity(), "Please Select a Time", Toast.LENGTH_SHORT).show();
            }
        }else{
            Toast.makeText(getActivity(), "Please Select the Current or Future Date", Toast.LENGTH_SHORT).show();
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

    private void searchForDate() {
        if (m != month || d>=day) {
                Intent intent = new Intent(getActivity(), DateResults.class);
                intent.putExtra("day", String.valueOf(d));
                intent.putExtra("month", Converter.monthConverter(m));
                startActivity(intent);


        }else{
            Toast.makeText(getActivity(), "Please Select the Current or Future Date", Toast.LENGTH_SHORT).show();
        }
    }



}