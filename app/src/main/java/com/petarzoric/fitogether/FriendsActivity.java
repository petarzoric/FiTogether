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
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class FriendsActivity extends AppCompatActivity {

    private RecyclerView friendsList;
    private DatabaseReference friendDatabase;
    private DatabaseReference usersDatabase;
    private FirebaseAuth auth;

    private String currentUserId;


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
    }

    @Override
    protected void onStart() {
        super.onStart();
       // usersDatabase.child(currentUserId).child("online").setValue(true);

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

                        String userName = dataSnapshot.child("name").getValue().toString();
                        String userThumb = dataSnapshot.child("thumbnail").getValue().toString();
                        if(dataSnapshot.hasChild("online")){

                            Boolean userOnline = (boolean) dataSnapshot.child("online").getValue();
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

    @Override
    protected void onResume() {
        super.onResume();
        //usersDatabase.child(currentUserId).child("online").setValue(false);

    }

    @Override
    protected void onStop() {
        super.onStop();
       // usersDatabase.child(currentUserId).child("online").setValue(false);
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

        public void setUserOnline(boolean online_status){

            ImageView userOnlineView = (ImageView) view.findViewById(R.id.user_single_online_icon);
            if(online_status == true){
                userOnlineView.setVisibility(View.VISIBLE);
            } else {

                userOnlineView.setVisibility(View.INVISIBLE);

            }
        }


    }
}