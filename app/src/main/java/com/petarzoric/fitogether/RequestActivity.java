package com.petarzoric.fitogether;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

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

import java.text.DateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class RequestActivity extends AppCompatActivity {

    private RecyclerView requestsList;
    private DatabaseReference requestsDatabase;
    private FirebaseAuth auth;
    private String currentUserId;
    private DatabaseReference usersDatabase;
    private Dialog popupDialog;
    private DatabaseReference rootRef;
    private DatabaseReference requestsDatabaseList;
    private boolean accepted;
    private ProgressDialog dialog;
    private DatabaseReference friendRequestDatabase;
    private DatabaseReference friendDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request);
        requestsList = findViewById(R.id.requestsList);
        requestsDatabase = FirebaseDatabase.getInstance().getReference().child("Friend_req");

        auth = FirebaseAuth.getInstance();
        currentUserId = auth.getCurrentUser().getUid();
        friendDatabase = FirebaseDatabase.getInstance().getReference().child("Friends");
        usersDatabase = FirebaseDatabase.getInstance().getReference().child("Users2");
        requestsList.setHasFixedSize(true);
        requestsList.setLayoutManager(new LinearLayoutManager(RequestActivity.this));
        popupDialog = new Dialog(this);
        rootRef = FirebaseDatabase.getInstance().getReference();
        friendRequestDatabase = FirebaseDatabase.getInstance().getReference().child("Friend_req");
        requestsDatabaseList = FirebaseDatabase.getInstance().getReference().child("Friend_req").child(auth.getCurrentUser().getUid())
                .child("requests");
        dialog = new ProgressDialog(this);
        dialog.setTitle("sending friend request");
        dialog.setMessage("wait a second...");
        dialog.setCanceledOnTouchOutside(false);


    }

    @Override
    protected void onStart() {
        super.onStart();
        final String user_id = getIntent().getStringExtra("user_id");
        if(user_id != null){



        }



        FirebaseRecyclerAdapter<Request, RequestsViewHolder> requestsRecyclerViewAdapter = new FirebaseRecyclerAdapter<Request, RequestsViewHolder>(
                Request.class,
                R.layout.users_single_layout,
                RequestsViewHolder.class,
                requestsDatabaseList
        ) {
            @Override
            protected void populateViewHolder(final RequestsViewHolder viewHolder, Request model, int position) {


                final String clicked_id = getRef(position).getKey();



                viewHolder.setMessage(model.getRequestMessage());


                usersDatabase.child(clicked_id).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        final String userName = dataSnapshot.child("name").getValue().toString();
                        final String userThumb = dataSnapshot.child("thumbnail").getValue().toString();

                        viewHolder.setName(userName);
                        viewHolder.setImage(userThumb, getApplicationContext());



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
                                message = popupDialog.findViewById(R.id.popup_message);
                                userImage = popupDialog.findViewById(R.id.popup_image);
                                userNamePopup = popupDialog.findViewById(R.id.popup_username);
                                userStudio = popupDialog.findViewById(R.id.popup_fitnessstudio);
                                String clicked = getRef(position).getKey();
                                message.setFocusable(false);
                                declineButton = popupDialog.findViewById(R.id.popup_button_decline2);

                                message.setText(model.getRequestMessage());

                                rootRef.child("Friends").child(currentUserId).addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                        if(dataSnapshot.hasChild(clicked)){
                                            accepted=true;
                                            requestButton.setText("Entfernen");
                                            message.setVisibility(View.INVISIBLE);
                                            notifyDataSetChanged();
                                        } else {
                                            accepted = false;
                                            requestButton.setText("Annehmen");
                                            message.setVisibility(View.VISIBLE);
                                            notifyDataSetChanged();
                                        }
                                    }

                                    @Override
                                    public void onCancelled(DatabaseError databaseError) {

                                    }
                                });





                                rootRef.child("Users2").child(clicked).addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                        String userImagee = dataSnapshot.child("image").getValue().toString();
                                        Picasso.with(RequestActivity.this).load(userImagee).placeholder(R.drawable.image_preview).into(userImage);
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

                                declineButton.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        String clicked2 = clicked;
                                        friendRequestDatabase.child(currentUserId).child(clicked2).removeValue()
                                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<Void> task) {
                                                        if(task.isSuccessful()){


                                                            friendRequestDatabase.child(clicked2).child(currentUserId).removeValue()
                                                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                        @Override
                                                                        public void onSuccess(Void aVoid) {
                                                                            dialog.setTitle("Declining friend_request...");
                                                                            dialog.show();
                                                                            // sentRequest = false;
                                                                            message.setText("");
                                                                            message.setVisibility(View.VISIBLE);
                                                                            requestButton.setText("Ich will mittrainieren!");
                                                                            friendRequestDatabase.child(currentUserId).child("requests").child(clicked2).removeValue();
                                                                            declineButton.setVisibility(View.INVISIBLE);
                                                                            requestButton.setText("Entfernen");
                                                                            message.setVisibility(View.INVISIBLE);

                                                                            popupDialog.dismiss();
                                                                            dialog.dismiss();



                                                                        }
                                                                    });
                                                        }
                                                    }
                                                });
                                    }
                                });


                                requestButton.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        String clicked2 = clicked;

                                        if(accepted == false ){
                                            accepted = true;
                                            dialog.setTitle("accepting friend_request...");
                                            dialog.show();
                                            declineButton.setVisibility(View.VISIBLE);

                                            final String currentDate = DateFormat.getDateInstance().format(new Date());

                                            Map friendsMap = new HashMap();
                                            friendsMap.put("Friends/" + currentUserId + "/" + clicked2 + "/date", currentDate);
                                            friendsMap.put("Friends/" + clicked + "/" + currentUserId + "/date", currentDate);

                                            friendsMap.put("Friend_req" + currentUserId +  "/" + clicked2, null);
                                            friendsMap.put("Friend_req" + clicked2 +  "/" + currentUserId, null);

                                            rootRef.updateChildren(friendsMap, new DatabaseReference.CompletionListener() {
                                                @Override
                                                public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {

                                                    if(databaseError == null){

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
                                                                                            friendRequestDatabase.child(currentUserId).child("requests").child(clicked2).removeValue();
                                                                                            declineButton.setVisibility(View.INVISIBLE);
                                                                                            requestButton.setText("Entfernen");
                                                                                            message.setVisibility(View.INVISIBLE);


                                                                                            dialog.dismiss();



                                                                                        }
                                                                                    });
                                                                        }
                                                                    }
                                                                });


                                                    }

                                                }
                                            });


                                        } else {

                                            dialog.setTitle("removing from your friends list...");
                                            dialog.show();
                                            friendDatabase.child(currentUserId).child(clicked2).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void aVoid) {

                                                    friendDatabase.child(clicked2).child(currentUserId).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                                                        @Override
                                                        public void onSuccess(Void aVoid) {

                                                            //----
                                                            //declineButton.setVisibility(View.INVISIBLE);
                                                            //requestButton.setVisibility(View.INVISIBLE);
                                                            popupDialog.dismiss();

                                                            dialog.dismiss();
                                                        }
                                                    });
                                                }
                                            });


                                        }


                                    }
                                });

                                popupDialog.show();



                            }
                        });
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });


            }

        };
        requestsList.setAdapter(requestsRecyclerViewAdapter);


    }

    public static class RequestsViewHolder extends RecyclerView.ViewHolder {

        View view;

        public RequestsViewHolder(View itemView){
            super(itemView);

            view = itemView;
        }

        public void setName(String name){
            TextView userName = view.findViewById(R.id.user_single_name);
            userName.setText(name);
        }

        public void setMessage(String message){
            TextView lastMessage = view.findViewById(R.id.user_single_status);
            lastMessage.setText(message);
        }

        public void setImage(String image, Context context){
            CircleImageView userImageView = view.findViewById(R.id.user_single_image);
            Picasso.with(context).load(image).placeholder(R.drawable.image_preview).into(userImageView);
        }

    }

}





