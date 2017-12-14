package com.petarzoric.fitogether;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

/**
 * Created by Alex on 24.11.2017.
 */

public class Tab3Search extends Fragment {
    Button search;

    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.tab3search, container, false);
        search = rootView.findViewById(R.id.searchbutton);
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), SearchResults.class);
                intent.putExtra("level", 1); // dummylevel
                startActivity(intent);
            }
        });
        return rootView;
}
}
