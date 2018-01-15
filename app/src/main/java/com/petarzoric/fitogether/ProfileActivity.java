package com.petarzoric.fitogether;

import android.app.ProgressDialog;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
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
    private Button declineButton;

    private DatabaseReference usersDatabase;

    private DatabaseReference friendRequestDatabase;

    private FirebaseUser currentUser;

    private int current_state;

    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        final String user_id = getIntent().getStringExtra("user_id");

        usersDatabase = FirebaseDatabase.getInstance().getReference().child("Users2").child(user_id);

        friendRequestDatabase = FirebaseDatabase.getInstance().getReference().child("Friend_req");

        displayName = findViewById(R.id.profile_displayname);
        displayStatus = findViewById(R.id.profileStatus);
        displayImage = findViewById(R.id.profile_imageview);
        sendRequestButton = findViewById(R.id.sendRequestButton);
        declineButton = findViewById(R.id.declineButton);
        //not friends state
        current_state = 0;
        currentUser = FirebaseAuth.getInstance().getCurrentUser();

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

        sendRequestButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                sendRequestButton.setEnabled(false);

                //if not friends ------------------------------
                if(current_state == 0){

                    friendRequestDatabase.child(currentUser.getUid()).child(user_id).child("request_type").setValue("sent").addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()){

                                friendRequestDatabase.child(user_id).child(currentUser.getUid()).child("request_type").setValue("received").addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {

                                        sendRequestButton.setEnabled(true);
                                        // state = 1 : sent req
                                        current_state = 1;
                                        sendRequestButton.setText("Cancel Friend request");

                                        Toast.makeText(ProfileActivity.this, "request sent succesfully", Toast.LENGTH_LONG);
                                    }
                                });

                            } else {
                                Toast.makeText(ProfileActivity.this, "failed sending request", Toast.LENGTH_LONG);
                            }
                        }
                    });

                }
                //cancel req state -----------------------------
                if (current_state == 1){
                    friendRequestDatabase.child(currentUser.getUid()).child(user_id).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {

                            friendRequestDatabase.child(user_id).child(currentUser.getUid()).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {

                                    current_state = 0;
                                    sendRequestButton.setText("SEND FRIEND REQUEST");
                                    sendRequestButton.setEnabled(true);

                                }
                            });
                        }
                    });

                }
            }
        });

    }
}
