package com.petarzoric.fitogether;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.Calendar;
import java.util.TimeZone;

/**
 * Created by Alex on 24.11.2017.
 */

public class Tab3Search extends Fragment {
    Button search;
    int level;
    int gender;
    int muscle;;
    Spinner levelspinner;
    Spinner genderspinner;
    Spinner musclespinner;
    EditText day;
    EditText month;

    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.tab3search, container, false);
        search = rootView.findViewById(R.id.searchbutton);
        levelspinner = rootView.findViewById(R.id.levelspinner);
        genderspinner = rootView.findViewById(R.id.genderspinner);
        musclespinner = rootView.findViewById(R.id.musclespinner);
        ArrayAdapter<CharSequence> trainingadapter = ArrayAdapter.createFromResource(getActivity(), R.array.Training, R.layout.support_simple_spinner_dropdown_item);
        musclespinner.setAdapter(trainingadapter);
        ArrayAdapter<CharSequence> genderadapter = ArrayAdapter.createFromResource(getActivity(), R.array.Geschlecht, R.layout.support_simple_spinner_dropdown_item);
        genderspinner.setAdapter(genderadapter);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getActivity(), R.array.Level, R.layout.support_simple_spinner_dropdown_item);
        levelspinner.setAdapter(adapter);
        day = rootView.findViewById(R.id.day);
        month = rootView.findViewById(R.id.month);
        day.setOnClickListener(new View.OnClickListener() {
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
        month.setOnClickListener(new View.OnClickListener() {
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


        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!day.getText().toString().isEmpty()) {
                    level = levelspinner.getSelectedItemPosition();
                    gender = genderspinner.getSelectedItemPosition();
                    muscle = musclespinner.getSelectedItemPosition();
                    Intent intent = new Intent(getActivity(), SearchResults.class);
                    intent.putExtra("level", level);
                    intent.putExtra("muscle", muscle);
                    intent.putExtra("gender", gender);
                    intent.putExtra("day", (day.getText().toString()));
                    intent.putExtra("month", Converter.monthConverter(Integer.parseInt(month.getText().toString())));
                    startActivity(intent);
                } else {
                    Toast.makeText(getActivity(), "Select a Date", Toast.LENGTH_SHORT).show();

                }
            }
        });
        return rootView;
    }

    private DatePickerDialog.OnDateSetListener datePickerListener = new DatePickerDialog.OnDateSetListener() {

        // when dialog box is closed, below method will be called.
        public void onDateSet(DatePicker view, int selectedYear,
                              int selectedMonth, int selectedDay) {
            String month1 = String.valueOf(selectedMonth + 1);
            String day1 = String.valueOf(selectedDay);
            day.setText(day1);
            month.setText(month1);

        }
    };



}

