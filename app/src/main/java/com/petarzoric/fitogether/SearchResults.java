package com.petarzoric.fitogether;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;
import java.util.ArrayList;
import java.util.HashMap;


import de.hdodenhof.circleimageview.CircleImageView;


public class SearchResults extends AppCompatActivity {


    private ArrayList <UserTraining> matches = new ArrayList<>();
    private FirebaseDatabase database;
    private DatabaseReference databaseReference;
    private UserProfile[] userProfiles;
    private FirebaseAuth auth;
    private String currentUserId;
    private Dialog popupDialog;


    private TextView userName;
    private TextView userStudio;
    private EditText userMessage;
    private CircleImageView userImage;
    private DatabaseReference friendRequestDatabase;
    private DatabaseReference RequestDatabase;
    private DatabaseReference usersDatabase;
    private String clickedUserID = "";
    private Boolean friends = false;
    private DatabaseReference notificationDatabase;
    private Boolean sentRequest;
    private ProgressDialog dialog;
    private int current_state;
    private RecyclerView recycleList;
    private DatabaseReference rootRef;
    private int gender;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_results);

        recycleList = findViewById(R.id.recyclelist);
        recycleList.setHasFixedSize(true);
        recycleList.setLayoutManager(new LinearLayoutManager(this));
        auth = FirebaseAuth.getInstance();

        currentUserId = auth.getCurrentUser().getUid();
        popupDialog = new Dialog(this);
        friendRequestDatabase = FirebaseDatabase.getInstance().getReference().child("Friend_req");
        notificationDatabase = FirebaseDatabase.getInstance().getReference().child("notifications");

        current_state = 0;
        usersDatabase = FirebaseDatabase.getInstance().getReference().child("Users2").child(currentUserId);
        rootRef = FirebaseDatabase.getInstance().getReference();



        dialog = new ProgressDialog(this);
        dialog.setTitle("sending friend request");
        dialog.setMessage("wait a second...");
        dialog.setCanceledOnTouchOutside(false);

        Intent data = getIntent();
        gender = data.getIntExtra("gender", 0);




    }



    @Override
    protected void onStart() {
        String userImage;
        super.onStart();
        database = FirebaseDatabase.getInstance();
        databaseReference = database.getReference();
        final Intent data = getIntent();
        final int level = data.getIntExtra("level", 0);
        final int muscle = data.getIntExtra("muscle", 0);
        final String month = data.getStringExtra("month");
        final String day = data.getStringExtra("day");
        if (databaseReference.child("TrainingsDate").child(month).child(day)!= null){
            databaseReference.child("TrainingsDate").child(month).child(day).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    Iterable<DataSnapshot> children = dataSnapshot.getChildren();
                    for (DataSnapshot child : children) {
                        if (!child.getKey().equals(FirebaseAuth.getInstance().getCurrentUser().getUid())){
                            if(child.getValue(UserTraining.class).getLevel() == level) {

                            if(child.getValue(UserTraining.class).getTrainingstype() == muscle) {

                                matches.add(child.getValue(UserTraining.class));

                        }
                        }
                    }
                    }
                    matchesToProfile(matches);

                    FirebaseRecyclerAdapter<UserResults, SearchViewHolder> firebaseRecyclerAdapter= new FirebaseRecyclerAdapter<UserResults, SearchViewHolder>(
                            UserResults.class, R.layout.listviewitems, SearchViewHolder.class, databaseReference.child("Searchresults").child(FirebaseAuth.getInstance().getCurrentUser().getUid())) {
                        @Override
                        protected void populateViewHolder(SearchViewHolder viewHolder, UserResults model, int position) {
                            viewHolder.setName(model.getName());
                            viewHolder.setAge(String.valueOf(model.getAge()));
                            viewHolder.setLevel(Level.parseToString(model.getLevel()));
                            viewHolder.setGender(Gender.parseToString(model.getGender()));
                            viewHolder.setImage(model.getThumbnail(), getApplicationContext());
                            viewHolder.setTime(model.getTime());
                            viewHolder.setStudio(Converter.studioString(model.getStudio(), model.getLocation(),getResources()));

                            clickedUserID = getRef(position).getKey();

                           viewHolder.view.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {


                                    TextView userNamePopup;
                                    CircleImageView userImage;
                                    TextView closeIcon;
                                    Button requestButton;
                                    Button declineButton;
                                    EditText message;
                                    TextView userStudio;

                                    popupDialog.setContentView(R.layout.result_popup);
                                    closeIcon = (TextView) popupDialog.findViewById(R.id.close);
                                    requestButton = popupDialog.findViewById(R.id.popup_button);
                                    String clicked = getRef(position).getKey();
                                    message = popupDialog.findViewById(R.id.popup_message);
                                    userImage = popupDialog.findViewById(R.id.popup_image);
                                    userNamePopup = popupDialog.findViewById(R.id.popup_username);
                                    userStudio = popupDialog.findViewById(R.id.popup_fitnessstudio);
                                    declineButton = popupDialog.findViewById(R.id.popup_button_decline2);
                                    declineButton.setVisibility(View.INVISIBLE);

                                    rootRef.child("Friends").child(currentUserId).addValueEventListener(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(DataSnapshot dataSnapshot) {
                                            if(dataSnapshot.hasChild(clicked)){
                                                friends = true;
                                                requestButton.setVisibility(View.INVISIBLE);
                                                message.setVisibility(View.INVISIBLE);
                                            } else {
                                                friends = false;
                                                requestButton.setVisibility(View.VISIBLE);
                                                message.setVisibility(View.VISIBLE);
                                            }
                                        }

                                        @Override
                                        public void onCancelled(DatabaseError databaseError) {

                                        }
                                    });

                                    rootRef.child("Friend_req").child(currentUserId).addValueEventListener(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(DataSnapshot dataSnapshot) {
                                            if(dataSnapshot.hasChild(clicked)){
                                                sentRequest=true;
                                                requestButton.setText("Anfrage abbrechen");
                                                message.setVisibility(View.INVISIBLE);
                                                notifyDataSetChanged();
                                            } else {
                                                sentRequest=false;
                                                requestButton.setText("Ich will mittrainieren!");
                                                message.setVisibility(View.VISIBLE);
                                                if(friends == true){
                                                    message.setVisibility(View.INVISIBLE);
                                                } else {
                                                    message.setVisibility(View.VISIBLE);
                                                }

                                                notifyDataSetChanged();
                                            }
                                        }

                                        @Override
                                        public void onCancelled(DatabaseError databaseError) {

                                        }
                                    });

                                    if(sentRequest!= null){
                                        if(sentRequest==true){
                                            requestButton.setText("Anfrage abbrechen");
                                            message.setVisibility(View.INVISIBLE);
                                        }
                                        if(sentRequest==false) {
                                            message.setVisibility(View.VISIBLE);
                                            requestButton.setText("Ich will mittrainieren!");
                                        }
                                    }




                                    rootRef.child("Users2").child(clicked).addValueEventListener(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(DataSnapshot dataSnapshot) {
                                            String userImagee = dataSnapshot.child("image").getValue().toString();
                                            Picasso.with(SearchResults.this).load(userImagee).placeholder(R.drawable.image_preview).into(userImage);
                                            String name = dataSnapshot.child("name").getValue().toString();
                                            userNamePopup.setText(name);
                                            int studio = Integer.parseInt(dataSnapshot.child("studio").getValue().toString());
                                            int location = Integer.parseInt(dataSnapshot.child("location").getValue().toString());
                                            String gym = Converter.studioString(studio, location, getResources());
                                            userStudio.setText(gym);

                                        }

                                        @Override
                                        public void onCancelled(DatabaseError databaseError) {

                                        }
                                    });

                                    closeIcon.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            popupDialog.dismiss();
                                        }
                                    });

                                    requestButton.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            String clicked2 = getRef(position).getKey();
                                            declineButton.setVisibility(View.INVISIBLE);

                                            if(sentRequest == false ){
                                               if(!TextUtils.isEmpty(message.getText().toString())){
                                                   sentRequest = true;
                                                   dialog.setTitle("sending friend_request...");
                                                   dialog.show();

                                                   HashMap<String, String> requestData = new HashMap<>();
                                                   requestData.put("request_type", "sent");
                                                   requestData.put("message", message.getText().toString());

                                                   //notificationData.put("message", message.getText().toString());
                                                   friendRequestDatabase.child(currentUserId).child(clicked2).setValue(requestData)
                                                           .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                               @Override
                                                               public void onComplete(@NonNull Task<Void> task) {
                                                                   if(task.isSuccessful()){
                                                                       HashMap<String, String> requestData = new HashMap<>();
                                                                       requestData.put("request_type", "received");
                                                                       requestData.put("message", message.getText().toString());

                                                                       friendRequestDatabase.child(clicked2).child(currentUserId).setValue(requestData)
                                                                               .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                                   @Override
                                                                                   public void onSuccess(Void aVoid) {
                                                                                       HashMap<String, String> notificationData = new HashMap<>();
                                                                                       notificationData.put("from", currentUserId);
                                                                                       notificationData.put("type", "request");
                                                                                       notificationData.put("message", message.getText().toString());

                                                                                       notificationDatabase.child(clicked2).push().setValue(notificationData)
                                                                                               .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                                                   @Override
                                                                                                   public void onComplete(@NonNull Task<Void> task) {
                                                                                                       if(task.isSuccessful()){


                                                                                                           requestButton.setText("Anfrage abbrechen");
                                                                                                           Request request = new Request(message.getText().toString(), currentUserId);
                                                                                                           friendRequestDatabase.child(clicked2).child("requests").child(currentUserId).setValue(request);
                                                                                                           message.setText("");
                                                                                                           message.setVisibility(View.INVISIBLE);
                                                                                                           declineButton.setVisibility(View.INVISIBLE);
                                                                                                           dialog.dismiss();

                                                                                                       }
                                                                                                   }
                                                                                               });

                                                                                   }
                                                                               });
                                                                   }
                                                               }
                                                           });
                                               } else {
                                                   Toast.makeText(SearchResults.this, "Eine Anfrage ohne Nachricht ist bisschen strange, findest du nicht?", Toast.LENGTH_LONG).show();
                                               }
                                            } else {

                                                dialog.setTitle("deleting friend_request...");
                                                dialog.show();



                                                //notificationData.put("message", message.getText().toString());
                                                friendRequestDatabase.child(currentUserId).child(clicked2).removeValue()
                                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                            @Override
                                                            public void onComplete(@NonNull Task<Void> task) {
                                                                if(task.isSuccessful()){


                                                                    friendRequestDatabase.child(clicked2).child(currentUserId).removeValue()
                                                                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                                @Override
                                                                                public void onSuccess(Void aVoid) {
                                                                                   // sentRequest = false;
                                                                                    message.setText("");
                                                                                    message.setVisibility(View.VISIBLE);
                                                                                    requestButton.setText("Ich will mittrainieren!");
                                                                                    friendRequestDatabase.child(clicked2).child("requests").child(currentUserId).removeValue();
                                                                                    dialog.dismiss();



                                                                                }
                                                                            });
                                                                }
                                                            }
                                                        });
                                            }


                                        }
                                    });

                                    popupDialog.show();
                                }
                            });

                        }

                    };

                    recycleList.setAdapter(firebaseRecyclerAdapter);

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });}

    }

    public UserProfile[] matchesToProfile(ArrayList<UserTraining> match) {
        userProfiles = new UserProfile[match.size()];
        ArrayList<UserTraining> matches = match;
        database = FirebaseDatabase.getInstance();
        databaseReference = database.getReference();

        String key = FirebaseAuth.getInstance().getCurrentUser().getUid();
        databaseReference.child("Searchresults").child(key).removeValue();
            databaseReference.child("Users2").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    ArrayList<UserProfile> profiles = new ArrayList<>();
                    Iterable<DataSnapshot> children = dataSnapshot.getChildren();
                    int count = 0;
                    for (DataSnapshot child : children) {
                        profiles.add(child.getValue(UserProfile.class));
                        for (int i = 0; i < matches.size(); i++) {
                            if (child.getKey().equals(matches.get(i).getUser()) ) {
                                if (Gender.parseToInt(child.getValue(UserProfile.class).getGender()) == gender || gender == 2){
                                     userProfiles[count] = child.getValue(UserProfile.class);
                                        try{
                                        UserResults results = new UserResults(userProfiles[count].getUid(), userProfiles[count].getName(), userProfiles[count].getAge(), userProfiles[count].getLevel(), userProfiles[count].getStudio(), userProfiles[count].getLocation(), userProfiles[count].getGender(), userProfiles[count].getThumbnail(), match.get(count).getTime());
                                        databaseReference.child("Searchresults").child(key).child(child.getKey()).setValue(results);
                                        count++;
                                    }catch (NullPointerException e){
                                    SearchResults.super.onBackPressed();
                                }
                                }
                            }
                        }
                    }
                    if (count == 0){
                        Toast.makeText(SearchResults.this, "No Results found", Toast.LENGTH_SHORT).show();
                        SearchResults.super.onBackPressed();

                    }

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });

        return userProfiles;

    }

}
