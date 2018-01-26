package com.petarzoric.fitogether;

import android.app.DatePickerDialog;
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
import android.widget.DatePicker;
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
    Button search;
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
    EditText dayView;
    EditText monthView;



    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.tab2calender, container, false);
        training = rootView.findViewById(R.id.training);
        ArrayAdapter<CharSequence> trainingadapter = ArrayAdapter.createFromResource(getActivity(), R.array.Training, R.layout.support_simple_spinner_dropdown_item);
        training.setAdapter(trainingadapter);
        time = rootView.findViewById(R.id.timestart);
        dayView = rootView.findViewById(R.id.dayView);
        monthView = rootView.findViewById(R.id.monthView);
        saveTraining = rootView.findViewById(R.id.savetraining);
        search = rootView.findViewById(R.id.search);
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                searchForDate();

            }
        });
        dayView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar cal = Calendar.getInstance(TimeZone.getDefault()); // Get current date

// Create the DatePickerDialog instance
                DatePickerDialog datePicker = new DatePickerDialog(getContext(),
                        R.style.Theme_AppCompat_Light, datePickerListener,
                        cal.get(Calendar.YEAR),
                        cal.get(Calendar.MONTH),
                        cal.get(Calendar.DAY_OF_MONTH));
                datePicker.setCancelable(false);
                datePicker.setTitle("Select the date");
                datePicker.show();

// Listener

            }
        });
        monthView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar cal = Calendar.getInstance(TimeZone.getDefault()); // Get current date

// Create the DatePickerDialog instance
                DatePickerDialog datePicker = new DatePickerDialog(getContext(),
                        R.style.Theme_AppCompat_Light, datePickerListener,
                        cal.get(Calendar.YEAR),
                        cal.get(Calendar.MONTH),
                        cal.get(Calendar.DAY_OF_MONTH));
                datePicker.setCancelable(false);
                datePicker.setTitle("Select the date");
                datePicker.show();

// Listener

            }
        });

        key = FirebaseAuth.getInstance().getCurrentUser().getUid();


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
        if (!dayView.getText().toString().equals("")) {
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

    private void searchForDate() {
        if (!dayView.getText().toString().equals("")) {
                Intent intent = new Intent(getActivity(), DateResults.class);
                intent.putExtra("day", String.valueOf(d));
                intent.putExtra("month", Converter.monthConverter(m));
                startActivity(intent);


        }else{
            Toast.makeText(getActivity(), "Please Select a Date", Toast.LENGTH_SHORT).show();
        }
    }

    private DatePickerDialog.OnDateSetListener datePickerListener = new DatePickerDialog.OnDateSetListener() {

        // when dialog box is closed, below method will be called.
        public void onDateSet(DatePicker view, int selectedYear,
                              int selectedMonth, int selectedDay) {
            m =selectedMonth + 1;
            d = selectedDay ;
            dayView.setText(String.valueOf(d));
            monthView.setText(String.valueOf(m));

        }
    };

}