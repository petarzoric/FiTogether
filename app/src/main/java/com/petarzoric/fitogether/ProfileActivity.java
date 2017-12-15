package com.petarzoric.fitogether;

import android.app.ProgressDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class ProfileActivity extends AppCompatActivity {

    private TextView displayName;
    private TextView displayStatus;
    private ImageView displayImage;
    private Button sendRequestButton;

    private DatabaseReference usersDatabase;

    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        String user_id = getIntent().getStringExtra("user_id");

        usersDatabase = FirebaseDatabase.getInstance().getReference().child("Users2").child(user_id);

        displayName = findViewById(R.id.profile_displayname);
        displayStatus = findViewById(R.id.profileStatus);
        displayImage = findViewById(R.id.profile_imageview);
        sendRequestButton = findViewById(R.id.sendRequestButton);

        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("loading user data");
        progressDialog.setMessage("please wait while we load the user data");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();


        usersDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String display_name = dataSnapshot.child("name").getValue().toString();
                String display_status = dataSnapshot.child("status").getValue().toString();
                String display_image = dataSnapshot.child("image").getValue().toString();

                displayName.setText(display_name);
                displayStatus.setText(display_status);

                Picasso.with(ProfileActivity.this).load(display_image).placeholder(R.drawable.image_preview).into(displayImage);

                progressDialog.dismiss();


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


    }
}
