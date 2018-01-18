package com.petarzoric.fitogether;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

/**
 * Created by Alex on 24.11.2017.
 */

public class Tab4Chat extends Fragment {

    private Button friendsButton;

    private DatabaseReference conversationDatabase;
    private DatabaseReference messageDatabase;
    private DatabaseReference userDatabase;
    private FirebaseAuth auth;
    private String current_user_id;
    private View mainView;

    //leerer Konstruktor wird benötigt, sonst gehts nicht
    public Tab4Chat(){

    }




    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.tab4chat, container, false);

        friendsButton = rootView.findViewById(R.id.friends_button);
        friendsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent settingsIntent = new Intent(getActivity(), FriendsActivity.class);
                startActivity(settingsIntent);
            }
        });


        auth = FirebaseAuth.getInstance();
        current_user_id = auth.getCurrentUser().getUid();


        return rootView;
    }
}
