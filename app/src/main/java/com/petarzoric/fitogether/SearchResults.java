package com.petarzoric.fitogether;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class SearchResults extends AppCompatActivity {


    ListView listView;
    ArrayList <UserTraining> matches = new ArrayList<UserTraining>();
    FirebaseDatabase database;
    DatabaseReference databaseReference;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_results);

        database = FirebaseDatabase.getInstance();
        databaseReference = database.getReference();
        final Intent data = getIntent();
        final int level = data.getIntExtra("level", 0);
        final int gender = data.getIntExtra("gender", 0);
        final int muscle = data.getIntExtra("muscle", 0);
        final String month = data.getStringExtra("month");
        final String day = data.getStringExtra("day");

        if ( databaseReference.child("TrainingsDate").child(month).child(day)!= null){
                    databaseReference.child("TrainingsDate").child(month).orderByChild(day).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Iterable<DataSnapshot> children = dataSnapshot.getChildren();
                for (DataSnapshot child : children) {
                    if ((!child.getKey().equals(FirebaseAuth.getInstance().getCurrentUser().getUid())) && (child.getValue(UserTraining.class).getLevel() == level) && (child.getValue(UserTraining.class).getTrainingstype() == muscle)) {
                        matches.add(child.getValue(UserTraining.class));
                   }
                }
            }
                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });}


        listView = findViewById(R.id.userlist);
        ListAdapter adapter = new Listadapter(SearchResults.this,matchesToProfile(matches));
        listView.setAdapter(adapter);

    }
    public UserProfile[] matchesToProfile(final ArrayList<UserTraining> match){
        final UserProfile[] userProfiles = new UserProfile[match.size()];
        database = FirebaseDatabase.getInstance();
        databaseReference = database.getReference();
        databaseReference.child("Users2").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Iterable<DataSnapshot> children = dataSnapshot.getChildren();
                int count = 0;
                for (DataSnapshot child : children) {
                    for (int i = 0; i <match.size() ; i++) {
                     if (child.getKey().equals(match.get(i).getUser())){
                         userProfiles[count] = child.getValue(UserProfile.class);
                         count++;
                     }
                    }
                    }
                }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        return userProfiles;

    }
}
