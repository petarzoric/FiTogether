package com.petarzoric.fitogether;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

/**
 * Created by Alex on 24.11.2017.
 */

public class Tab3Search extends Fragment {
    Button search;
    int level;
    int gender;
    int muscle;
    Spinner levelspinner;
    Spinner genderspinner;
    Spinner musclespinner;

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
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                level = levelspinner.getSelectedItemPosition();
                gender = genderspinner.getSelectedItemPosition();
                muscle = musclespinner.getSelectedItemPosition();
                Intent intent = new Intent(getActivity(), SearchResults.class);
                intent.putExtra("level", level);
                intent.putExtra("muscle", muscle);
                intent.putExtra("gender", gender);
                startActivity(intent);
            }
        });
        return rootView;
}
}
