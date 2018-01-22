package com.petarzoric.fitogether;

import android.app.Dialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;


public class SearchResults extends AppCompatActivity {


    ArrayList <UserTraining> matches = new ArrayList<>();
    FirebaseDatabase database;
    DatabaseReference databaseReference;
    UserProfile[] userProfiles;
    FirebaseAuth auth;
    String currentUserId;
    Dialog popupDialog;
    Button requestButton;
    TextView userName;
    TextView userStudio;
    EditText userMessage;
    CircleImageView userImage;

    RecyclerView recycleList;

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









    }



    @Override
    protected void onStart() {
        super.onStart();
        database = FirebaseDatabase.getInstance();
        databaseReference = database.getReference();
        final Intent data = getIntent();
        final int level = data.getIntExtra("level", 0);
        final int gender = data.getIntExtra("gender", 0);
        final int muscle = data.getIntExtra("muscle", 0);
        final String month = data.getStringExtra("month");
        final String day = data.getStringExtra("day");

        if ( databaseReference.child("TrainingsDate").child(month).child(day)!= null){
            databaseReference.child("TrainingsDate").child(month).child(day).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    Iterable<DataSnapshot> children = dataSnapshot.getChildren();
                    for (DataSnapshot child : children) {
                        if ((!child.getKey().equals(FirebaseAuth.getInstance().getCurrentUser().getUid())) && (child.getValue(UserTraining.class).getLevel() == level) && (child.getValue(UserTraining.class).getTrainingstype() == muscle) ) {
                            matches.add(child.getValue(UserTraining.class));

                        }
                    }

                    matchesToProfile(matches);

                    FirebaseRecyclerAdapter<UserProfile, SearchViewHolder> firebaseRecyclerAdapter= new FirebaseRecyclerAdapter<UserProfile, SearchViewHolder>(
                            UserProfile.class, R.layout.listviewitems, SearchViewHolder.class, databaseReference.child("Searchresults").child(FirebaseAuth.getInstance().getCurrentUser().getUid())) {
                        @Override
                        protected void populateViewHolder(SearchViewHolder viewHolder, UserProfile model, int position) {
                            viewHolder.setName(model.getName());
                            viewHolder.setAge(String.valueOf(model.getAge()));
                            viewHolder.setLevel(Level.parseToString(model.getLevel()));
                            viewHolder.setGender(Gender.parseToString(model.getGender()));
                            viewHolder.setImage(model.getThumbnail(), getApplicationContext());

                           viewHolder.view.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {

                                    TextView closeIcon;
                                    Button requestButton;

                                    popupDialog.setContentView(R.layout.result_popup);
                                    closeIcon = (TextView) popupDialog.findViewById(R.id.close);
                                    requestButton = popupDialog.findViewById(R.id.popup_button);
                                    closeIcon.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            popupDialog.dismiss();
                                        }
                                    });

                                    requestButton.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            //TODO ANFRAGE SCHICKEN
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

    public UserProfile[] matchesToProfile(final ArrayList<UserTraining> match) {
        userProfiles = new UserProfile[match.size()];
        database = FirebaseDatabase.getInstance();
        databaseReference = database.getReference();
        databaseReference.child("Searchresults").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).removeValue();
            databaseReference.child("Users2").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    ArrayList<UserProfile> profiles = new ArrayList<>();

                    Iterable<DataSnapshot> children = dataSnapshot.getChildren();
                    int count = 0;
                    for (DataSnapshot child : children) {
                        profiles.add(child.getValue(UserProfile.class));
                        for (int i = 0; i < match.size(); i++) {
                            if (child.getKey().equals(match.get(i).getUser())) {
                                userProfiles[count] = child.getValue(UserProfile.class);
                                databaseReference.child("Searchresults").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child(child.getKey()).setValue(child.getValue(UserProfile.class));

                                count++;
                            }
                        }
                    }
                    if (count == 0){
                        Toast.makeText(SearchResults.this, "No Results found", Toast.LENGTH_SHORT).show();
                        SearchResults.super.onBackPressed();

                    }

                    //SearchAdapter searchAdapter = new SearchAdapter(SearchResults.this, userProfiles);
                    //recycleList.setAdapter(searchAdapter);
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });



        return userProfiles;

    }

}
