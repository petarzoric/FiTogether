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

import java.text.DateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class ProfileActivity extends AppCompatActivity {

    private TextView displayName;
    private TextView displayStatus;
    private ImageView displayImage;
    private Button sendRequestButton;
    private Button declineButton;

    private DatabaseReference usersDatabase;
    private DatabaseReference friendDatabase;
    private DatabaseReference notificationDatabase;
    private  DatabaseReference rootRef;

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
        notificationDatabase = FirebaseDatabase.getInstance().getReference().child("notifications");
        //not friends state
        current_state = 0;
        currentUser = FirebaseAuth.getInstance().getCurrentUser();
        friendDatabase = FirebaseDatabase.getInstance().getReference().child("Friends");
        rootRef = FirebaseDatabase.getInstance().getReference();

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

                // friends list / request feature --------------------------

                friendRequestDatabase.child(currentUser.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        if(dataSnapshot.hasChild(user_id)){
                            String request_type = dataSnapshot.child(user_id).child("request_type").getValue().toString();
                            if(request_type.equals("received")){
                                current_state = 2;
                                sendRequestButton.setText("ACCEPT FRIEND REQUEST");

                                declineButton.setVisibility(View.VISIBLE);
                                declineButton.setEnabled(true);

                            } else if(request_type.equals("sent")) {
                                current_state = 1;
                                sendRequestButton.setText("CANCEL FRIEND REQUEST");

                                declineButton.setVisibility(View.INVISIBLE);
                                declineButton.setEnabled(false);

                            }

                            progressDialog.dismiss();

                        } else {
                            declineButton.setVisibility(View.INVISIBLE);
                            declineButton.setEnabled(false);

                            friendDatabase.child(currentUser.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    if(dataSnapshot.hasChild(user_id)){

                                        current_state = 3;
                                        sendRequestButton.setText("UNFRIEND THIS PERSON");

                                        declineButton.setVisibility(View.INVISIBLE);
                                        declineButton.setEnabled(false);
                                    }

                                    progressDialog.dismiss();
                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {

                                    progressDialog.dismiss();

                                }
                            });
                        }



                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });




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

                                        HashMap<String, String>  notificationData = new HashMap<>();
                                        notificationData.put("from", currentUser.getUid());
                                        notificationData.put("type", "request");

                                        notificationDatabase.child(user_id).push().setValue(notificationData).addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if(task.isSuccessful()){

                                                    // state = 1 : sent req
                                                    current_state = 1;
                                                    sendRequestButton.setText("Cancel Friend request");

                                                    declineButton.setVisibility(View.INVISIBLE);
                                                    declineButton.setEnabled(false);


                                                }     else {
                                                    //TODO error überlegen

                                                }
                                            }
                                        });



                                        //Toast.makeText(ProfileActivity.this, "request sent succesfully", Toast.LENGTH_LONG);
                                    }
                                });

                            } else {
                                Toast.makeText(ProfileActivity.this, "failed sending request", Toast.LENGTH_LONG);
                            }
                            sendRequestButton.setEnabled(true);
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

                                    declineButton.setVisibility(View.INVISIBLE);
                                    declineButton.setEnabled(false);

                                }
                            });
                        }
                    });

                }

                // request received state -------------------
                if(current_state == 2){

                    final String currentDate = DateFormat.getDateInstance().format(new Date());

                    friendDatabase.child(currentUser.getUid()).child(user_id).setValue(currentDate).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {

                            friendDatabase.child(user_id).child(currentUser.getUid()).setValue(currentDate).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {

                                    friendRequestDatabase.child(currentUser.getUid()).child(user_id).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {

                                            friendRequestDatabase.child(user_id).child(currentUser.getUid()).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void aVoid) {

                                                    //current state = 3: friends
                                                    current_state = 3;
                                                    sendRequestButton.setText("UNFRIEND THIS PERSON");
                                                    sendRequestButton.setEnabled(true);

                                                    declineButton.setVisibility(View.INVISIBLE);
                                                    declineButton.setEnabled(false);

                                                }
                                            });
                                        }
                                    });

                                }
                            });

                        }
                    });

                }
                // friends state
                if(current_state == 3){
                    friendDatabase.child(currentUser.getUid()).child(user_id).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            friendDatabase.child(user_id).child(currentUser.getUid()).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    current_state = 0;
                                    sendRequestButton.setText("SEND FRIEND REQUEST");
                                }
                            });
                        }
                    });

                }


            }
        });

    }
}
