package com.petarzoric.fitogether;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class ChatActivity extends AppCompatActivity {

    private String chatUser;
    private Toolbar chatToolbar;

    private DatabaseReference rootRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        chatToolbar = (Toolbar) findViewById(R.id.chat_app_bar);
        setSupportActionBar(chatToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        rootRef = FirebaseDatabase.getInstance().getReference();



        chatUser = getIntent().getStringExtra("user_id");

        rootRef.child("Users2").child(chatUser).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                String chatUserName = dataSnapshot.child("name").getValue().toString();
                getSupportActionBar().setTitle(chatUserName);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        getSupportActionBar().setTitle(chatUser);
    }
}
