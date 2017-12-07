package com.petarzoric.fitogether;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class SearchResults extends AppCompatActivity {


    ListView listView;
    ArrayList <UserProfile> matches;
    FirebaseDatabase database;
    DatabaseReference databaseReference;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_results);

        database = FirebaseDatabase.getInstance();
        databaseReference = database.getReference();
        final Intent data = getIntent();
        final String key = data.getStringExtra("key");
        final int level = data.getIntExtra("level", 0);
        databaseReference.child("UserData").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Iterable<DataSnapshot> children = dataSnapshot.getChildren();
                for (DataSnapshot child : children) {
                    if (!child.getKey().equals(key) && child.getValue(UserProfile.class).getUserlevel() == level) {
                        matches.add(child.getValue(UserProfile.class));
                    }
                }
            }
                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });


        listView = findViewById(R.id.userlist);
        ListAdapter adapter = new Listadapter(SearchResults.this,matches);
        listView.setAdapter(adapter);

    }
}
