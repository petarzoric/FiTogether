package com.petarzoric.fitogether;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Alex on 24.11.2017.
 */

public class Tab4Chat extends Fragment {

    private FloatingActionButton friendsButton;
    private TextView requestNotificationText;

    private DatabaseReference conversationDatabase;
    private DatabaseReference messageDatabase;
    private DatabaseReference userDatabase;
    private DatabaseReference requestsDatabase;
    private FirebaseAuth auth;
    private String current_user_id;
    private View mainView;
    private RecyclerView chatList;
    private long requestNumber;
    private boolean friends;
    private DatabaseReference friendsDatabase;

    //leerer Konstruktor wird benötigt, sonst gehts nicht
    public Tab4Chat(){

    }




    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        mainView = inflater.inflate(R.layout.tab4chat, container, false);
        friendsDatabase = FirebaseDatabase.getInstance().getReference().child("Friends");

        requestNumber = 0;
        auth = FirebaseAuth.getInstance();
        current_user_id = auth.getCurrentUser().getUid();
        requestsDatabase = FirebaseDatabase.getInstance().getReference().child("Friend_req");
        conversationDatabase = FirebaseDatabase.getInstance().getReference().child("Chat").child(current_user_id);
        conversationDatabase.keepSynced(true);
        userDatabase = FirebaseDatabase.getInstance().getReference().child("Users2");
        userDatabase.keepSynced(true);
        messageDatabase = FirebaseDatabase.getInstance().getReference().child("messages").child(current_user_id);
        chatList = (RecyclerView) mainView.findViewById(R.id.chatList2);
        System.out.println(chatList);





        requestNotificationText = mainView.findViewById(R.id.requestText2);
        requestNotificationText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //INTENT TO SCREEN WITH REQUESTS
                Intent requestIntent = new Intent(getActivity(), RequestActivity.class);
                startActivity(requestIntent);

            }
        });


        friendsButton = mainView.findViewById(R.id.friends_button);
        friendsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent settingsIntent = new Intent(getActivity(), FriendsActivity.class);
                startActivity(settingsIntent);
            }
        });

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);

        chatList.setHasFixedSize(true);
        chatList.setLayoutManager(linearLayoutManager);

        requestsDatabase.child(current_user_id).child("requests").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                requestNumber = dataSnapshot.getChildrenCount();
                requestNotificationText.setText(Long.toString(requestNumber));

                if(requestNumber == 0){
                    requestNotificationText.setVisibility(View.INVISIBLE);
                } else {
                    requestNotificationText.setVisibility(View.VISIBLE);
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });





        return mainView;
    }

    @Override
    public void onStart() {
        super.onStart();

        Query chatQuery = conversationDatabase.orderByChild("timestamp");

        FirebaseRecyclerAdapter<Conversation, ChatViewHolder> firebaseChatAdapter = new FirebaseRecyclerAdapter<Conversation, ChatViewHolder>(
                Conversation.class,
                R.layout.users_single_layout,
                ChatViewHolder.class,
                chatQuery
        ) {
            @Override
            protected void populateViewHolder(ChatViewHolder viewHolder, Conversation model, int position) {

                final String list_user_id = getRef(position).getKey();
                Query lastQuery = messageDatabase.child(list_user_id).limitToLast(1);

                lastQuery.addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(DataSnapshot dataSnapshot, String s) {

                        String data = dataSnapshot.child("message").getValue().toString();
                        viewHolder.setMessage(data, model.isSeen());
                    }

                    @Override
                    public void onChildChanged(DataSnapshot dataSnapshot, String s) {



                    }

                    @Override
                    public void onChildRemoved(DataSnapshot dataSnapshot) {

                    }

                    @Override
                    public void onChildMoved(DataSnapshot dataSnapshot, String s) {

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

                userDatabase.child(list_user_id).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        final String userName = dataSnapshot.child("name").getValue().toString();
                        String thumb = dataSnapshot.child("thumbnail").getValue().toString();


                        if(dataSnapshot.hasChild("online")){
                            String useronline = dataSnapshot.child("online").getValue().toString();
                            viewHolder.setUserOnline(useronline);
                        }

                        viewHolder.setName(userName);
                        viewHolder.setUserImage(thumb, getContext());

                        viewHolder.view.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                userDatabase.child(current_user_id).addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                        if(dataSnapshot.hasChild(list_user_id)){
                                            friends = true;
                                        } else {
                                            friends = false;
                                        }
                                    }

                                    @Override
                                    public void onCancelled(DatabaseError databaseError) {

                                    }
                                });

                                if(friends == true){
                                    Intent intent = new Intent(getContext(), ChatActivity.class);
                                    intent.putExtra("user_id", list_user_id);
                                    intent.putExtra("user_name", userName);
                                    startActivity(intent);
                                } else {
                                    Toast.makeText(getActivity(), "Ihr seid nicht mehr befreundet.", Toast.LENGTH_SHORT).show();
                                }


                            }
                        });
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

            }
        };



        userDatabase.child(current_user_id).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        chatList.setAdapter(firebaseChatAdapter);
    }

    public static class ChatViewHolder extends RecyclerView.ViewHolder {

        View view;


        public ChatViewHolder(View itemView){
            super(itemView);
            view = itemView;

        }
        public void setMessage(String message, boolean isSeen){
            TextView userStatusView = (TextView) view.findViewById(R.id.user_single_status);
            userStatusView.setText(message);

            if(!isSeen){
                userStatusView.setTypeface(userStatusView.getTypeface(), Typeface.BOLD);
                userStatusView.setTextColor(R.color.colorPrimary);
            } else {

                userStatusView.setTypeface(userStatusView.getTypeface(), Typeface.NORMAL);
            }
        }

        public void setName(String name){
            TextView userNameView = (TextView) view.findViewById(R.id.user_single_name);
            userNameView.setText(name);
        }

        public void setUserImage(String thumb, Context context){
            CircleImageView userImageView = (CircleImageView) view.findViewById(R.id.user_single_image);
            Picasso.with(context).load(thumb).placeholder(R.drawable.image_preview).into(userImageView);
        }

        public void setUserOnline(String status){
            ImageView userOnlineView = (ImageView) view.findViewById(R.id.user_single_online_icon);

            if(status.equals("true")){

                userOnlineView.setVisibility(View.VISIBLE);
            } else {

                userOnlineView.setVisibility(View.INVISIBLE);
            }

        }

    }
}













