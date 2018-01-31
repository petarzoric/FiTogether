package com.petarzoric.fitogether;

import android.app.Dialog;
import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class RequestActivity extends AppCompatActivity {

    private RecyclerView requestsList;
    private DatabaseReference requestsDatabase;
    private FirebaseAuth auth;
    private String currentUserId;
    private DatabaseReference usersDatabase;
    private Dialog popupDialog;
    private DatabaseReference rootRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request);
        requestsList = findViewById(R.id.requestsList);
        requestsDatabase = FirebaseDatabase.getInstance().getReference().child("Friend_req");
        auth = FirebaseAuth.getInstance();
        currentUserId = auth.getCurrentUser().getUid();
        usersDatabase = FirebaseDatabase.getInstance().getReference().child("Users2");
        requestsList.setHasFixedSize(true);
        requestsList.setLayoutManager(new LinearLayoutManager(RequestActivity.this));
        popupDialog = new Dialog(this);
        rootRef = FirebaseDatabase.getInstance().getReference();


    }

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseRecyclerAdapter<Request, RequestsViewHolder> requestsRecyclerViewAdapter = new FirebaseRecyclerAdapter<Request, RequestsViewHolder>(
                Request.class,
                R.layout.users_single_layout,
                RequestsViewHolder.class,
                requestsDatabase
        ) {
            @Override
            protected void populateViewHolder(final RequestsViewHolder viewHolder, Request model, int position) {


                final String clicked_id = getRef(position).getKey();

                usersDatabase.child(clicked_id).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        final String userName = dataSnapshot.child("name").getValue().toString();
                        String userThumb = dataSnapshot.child("thumbnail").getValue().toString();
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


                                rootRef.child("Users2").child(clicked_id).addValueEventListener(new ValueEventListener() {
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

                                //----------
                            }
                        });
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

            }
        };

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





