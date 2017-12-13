package com.petarzoric.fitogether;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class SettingsActivity extends AppCompatActivity {

    private DatabaseReference mUserDatabase;
    private FirebaseUser currentUser;
    private Button changeStatusButton;
    private Button changeImageButton;
    private TextView displayName;
    private TextView displayStatus;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        changeStatusButton = (Button) findViewById(R.id.changeStatus);
        changeImageButton = (Button) findViewById(R.id.changeImage);
        displayName = (TextView) findViewById(R.id.displayName);
        displayStatus = (TextView) findViewById(R.id.hiThere);
        currentUser = FirebaseAuth.getInstance().getCurrentUser();

        currentUser = FirebaseAuth.getInstance().getCurrentUser();
        String current_uid = currentUser.getUid();
        mUserDatabase = FirebaseDatabase.getInstance().getReference().child("Users2").child(current_uid);

        mUserDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                displayName.setText((String) dataSnapshot.child("name").getValue());
                displayStatus.setText((String) dataSnapshot.child("status").getValue());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        changeStatusButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Intent statusIntent = new Intent(SettingsActivity.this, StatusActivity.class);
                startActivity(statusIntent);
            }
        });

    }
}
