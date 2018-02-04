package com.petarzoric.fitogether;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.sql.Struct;

import de.hdodenhof.circleimageview.CircleImageView;

public class FriendsActivity extends AppCompatActivity {

    private RecyclerView friendsList;
    private DatabaseReference friendDatabase;
    private DatabaseReference usersDatabase;
    private FirebaseAuth auth;

    private String currentUserId;
    private Button back;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friends);

        friendsList = findViewById(R.id.friendsList);
        auth = FirebaseAuth.getInstance();

        currentUserId = auth.getCurrentUser().getUid();

        friendDatabase = FirebaseDatabase.getInstance().getReference().child("Friends").child(currentUserId);
        friendDatabase.keepSynced(true);
        usersDatabase = FirebaseDatabase.getInstance().getReference().child("Users2");

        friendsList.setHasFixedSize(true);
        friendsList.setLayoutManager(new LinearLayoutManager(FriendsActivity.this));
        back = findViewById(R.id.backb);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(FriendsActivity.this, MainScreen.class);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        FirebaseDatabase.getInstance().getReference().child("Users2").child(FirebaseAuth
                .getInstance().getCurrentUser().getUid()).child("online").setValue(ServerValue.TIMESTAMP);
    }


    @Override
    protected void onStart() {
        super.onStart();
        FirebaseDatabase.getInstance().getReference().child("Users2").child(FirebaseAuth
                .getInstance().getCurrentUser().getUid()).child("online").setValue(true);

        usersDatabase.child(currentUserId).child("online").setValue(true);

        FirebaseRecyclerAdapter<Friends, FriendsViewHolder> friendsRecycleViewAdapter = new FirebaseRecyclerAdapter<Friends, FriendsViewHolder>(
                Friends.class,
                R.layout.users_single_layout,
                FriendsViewHolder.class,
                friendDatabase
                ) {
            @Override
            protected void populateViewHolder(final FriendsViewHolder viewHolder, Friends model, int position) {

                viewHolder.setDate(model.getDate());


                final String list_user_id = getRef(position).getKey();

                usersDatabase.child(list_user_id).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        final String userName = dataSnapshot.child("name").getValue().toString();
                        String userThumb = dataSnapshot.child("thumbnail").getValue().toString();
                        if(dataSnapshot.hasChild("online")){

                            String userOnline =  dataSnapshot.child("online").getValue().toString();
                            viewHolder.setUserOnline(userOnline);
                        }



                        viewHolder.setName(userName);
                        viewHolder.setImage(userThumb, getApplicationContext());

                        viewHolder.view.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                CharSequence options[] = new CharSequence[]{"Open Profile", "Send message"};
                                AlertDialog.Builder builder = new AlertDialog.Builder(FriendsActivity.this);
                                builder.setTitle("Select Options");
                                builder.setItems(options, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {

                                        if(which == 0){

                                            Intent profileIntent = new Intent(FriendsActivity.this, ProfileActivity.class);
                                            profileIntent.putExtra("user_id", list_user_id);
                                            startActivity(profileIntent);
                                        }

                                        if(which == 1){

                                            Intent profileIntent = new Intent(FriendsActivity.this, ChatActivity.class);
                                            profileIntent.putExtra("user_id", list_user_id);
                                            profileIntent.putExtra("user_name", userName);
                                            startActivity(profileIntent);

                                        }



                                    }
                                });

                                builder.show();

                            }
                        });

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });


            }
        };
        friendsList.setAdapter(friendsRecycleViewAdapter);
    }



    public static class FriendsViewHolder extends RecyclerView.ViewHolder{

        View view;

        public FriendsViewHolder(View itemView) {
            super(itemView);

            view = itemView;
        }

        public void setDate(String date){
            TextView userNameView = (TextView) view.findViewById(R.id.user_single_status);
            userNameView.setText(date);
        }

        public void setName(String name){

            TextView userNameView = (TextView) view.findViewById(R.id.user_single_name);
            userNameView.setText(name);
        }

        public void setImage(String image, Context context){
            CircleImageView userImageView = view.findViewById(R.id.user_single_image);


            Picasso.with(context).load(image).placeholder(R.drawable.image_preview).into(userImageView);
        }

        public void setUserOnline(String online_status){

            ImageView userOnlineView = (ImageView) view.findViewById(R.id.user_single_online_icon);
            if(online_status.equals("true")){
                userOnlineView.setVisibility(View.VISIBLE);
            } else {

                userOnlineView.setVisibility(View.INVISIBLE);

            }
        }


    }
}
