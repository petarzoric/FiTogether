package com.petarzoric.fitogether;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

/**
 * Created by Alex on 26.11.2017.
 */

public class Tab2Calender extends Fragment {
    Spinner training;
    Button savetraining;
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.tab2calender, container, false);
        training = rootView.findViewById(R.id.training);
        ArrayAdapter<CharSequence> trainingadapter = ArrayAdapter.createFromResource(getActivity(), R.array.Training, R.layout.support_simple_spinner_dropdown_item);
        training.setAdapter(trainingadapter);
        savetraining = rootView.findViewById(R.id.savetraining);
        savetraining.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        return rootView;
    }
}