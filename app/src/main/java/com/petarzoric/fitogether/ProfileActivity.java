package com.petarzoric.fitogether;

import android.app.ProgressDialog;
import android.content.Intent;
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
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.text.DateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/*
    Activity, die über die FriendsActitvity erreicht werden kann. Durch den klick auf einen user und dann auf "open profile"
    gelangt man zu diesem Screen.
    Die Klasse bietet auch die ganzen request sachen an, wird aber so nicht mehr verwendet. Wurde damals mit UsersActivity aber so verwendet.
    Hat jetzt "nur noch" die Funktion, eine Person unfrienden zu können, da der Screen jetzt nur noch erreichbar ist, wenn man
    bereits mit der Person befreundet ist.
    Löscht dann die Einträge aus der DB und auch den entsprechenden Chat.
 */

public class ProfileActivity extends AppCompatActivity {

    private TextView displayName;
    private TextView displayStatus;
    private ImageView displayImage;
    private Button sendRequestButton;
    private Button declineButton;
    private ProgressDialog dialog;

    private DatabaseReference usersDatabase;
    private DatabaseReference friendDatabase;
    private DatabaseReference notificationDatabase;
    private DatabaseReference rootref;

    private DatabaseReference friendRequestDatabase;
    private String user_id;

    private FirebaseUser currentUser;

    private int current_state;

    ProgressDialog progressDialog;

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseDatabase.getInstance().getReference().child("Users2").child(FirebaseAuth
                .getInstance().getCurrentUser().getUid()).child("online").setValue(true);
    }



    @Override
    protected void onPause() {
        super.onPause();
        FirebaseDatabase.getInstance().getReference().child("Users2").child(FirebaseAuth
                .getInstance().getCurrentUser().getUid()).child("online").setValue(ServerValue.TIMESTAMP);
    }

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
        rootref = FirebaseDatabase.getInstance().getReference();

        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("loading user data");
        progressDialog.setMessage("please wait while we load the user data");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();
        dialog = new ProgressDialog(this);
        dialog.setTitle("sending friend request");
        dialog.setMessage("wait a second...");
        dialog.setCanceledOnTouchOutside(false);




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
                    dialog.setTitle("sending friend request");
                    dialog.show();

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
                                                    dialog.dismiss();


                                                }     else {
                                                    //TODO error überlegen

                                                }
                                            }
                                        });




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
                    dialog.setTitle("cancelling your friend request");
                    dialog.show();
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
                                    dialog.dismiss();

                                }
                            });
                        }
                    });

                }

                // request received state -------------------
                if(current_state == 2){
                    dialog.setTitle("accepting...");
                    dialog.show();

                    final String currentDate = DateFormat.getDateInstance().format(new Date());

                    Map friendsMap = new HashMap();
                    friendsMap.put("Friends/" + currentUser.getUid() + "/" + user_id + "/date", currentDate);
                    friendsMap.put("Friends/" + user_id + "/" + currentUser.getUid() + "/date", currentDate);

                    friendsMap.put("Friend_req" + currentUser.getUid() +  "/" + user_id, null);
                    friendsMap.put("Friend_req" + user_id +  "/" + currentUser.getUid(), null);

                    rootref.updateChildren(friendsMap, new DatabaseReference.CompletionListener() {
                        @Override
                        public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {

                            if(databaseError == null){

                                current_state = 3;
                                sendRequestButton.setText("UNFRIEND THIS PERSON");
                                sendRequestButton.setEnabled(true);

                                declineButton.setVisibility(View.INVISIBLE);
                                declineButton.setEnabled(false);
                                dialog.dismiss();

                            }

                        }
                    });







                }
                // friends state
                if(current_state == 3){
                    dialog.setTitle("removing from your friends list...");
                    dialog.show();
                    friendDatabase.child(currentUser.getUid()).child(user_id).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            friendDatabase.child(user_id).child(currentUser.getUid()).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    DatabaseReference chatDataBase = rootref.child("Chat");
                                    DatabaseReference messagesDataBase =rootref.child("messages");
                                    messagesDataBase.child(currentUser.getUid()).child(user_id).removeValue();
                                    messagesDataBase.child(user_id).child(currentUser.getUid()).removeValue();
                                    chatDataBase.child(currentUser.getUid()).child(user_id).removeValue();
                                    chatDataBase.child(user_id).child(currentUser.getUid()).removeValue();
                                    dialog.dismiss();
                                    Intent backscreen = new Intent(ProfileActivity.this, FriendsActivity.class);
                                    startActivity(backscreen);
                                }
                            });
                        }
                    });

                }
            }
        });

    }
}




