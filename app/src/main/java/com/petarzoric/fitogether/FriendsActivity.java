package com.petarzoric.fitogether;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class FriendsActivity extends AppCompatActivity {

    private RecyclerView friendsList;
    private DatabaseReference friendDatabase;
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

        friendsList.setHasFixedSize(true);
        friendsList.setLayoutManager(new LinearLayoutManager(FriendsActivity.this));
    }

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseRecyclerAdapter<Friends, FriendsViewHolder> friendsRecycleViewAdapter = new FirebaseRecyclerAdapter<Friends, FriendsViewHolder>(
                Friends.class,
                R.layout.users_single_layout,
                FriendsViewHolder.class,
                friendDatabase
                ) {
            @Override
            protected void populateViewHolder(FriendsViewHolder viewHolder, Friends model, int position) {

                viewHolder.setDate(model.getDate());
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





    }
}
