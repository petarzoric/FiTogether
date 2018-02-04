package com.petarzoric.fitogether;

import android.content.Context;
import android.content.Intent;
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
import com.google.firebase.database.ServerValue;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

/*
    War urspünglich in der Action Bar unter All Users zu erreichen.
    War allerdings nur eine Übergangslösung, damit ich von dort aus den Chat und die ganze Requests Geschichte
    unabhängig von den ganzen Searchresults bauen konnte.
    Brauchen wir jetzt nicht mehr, aber hat auf jeden Fall ihren Beitrag dazu geleistet, dass der Chat gut
    funktioniert, wird teilweise noch zum testen verwendet, deswegen lass ich die immer noch drinnen.
 */


public class UsersActivity extends AppCompatActivity {

    private RecyclerView usersList;

    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_users);

        usersList = findViewById(R.id.usersList);
        usersList.setHasFixedSize(true);
        usersList.setLayoutManager(new LinearLayoutManager(this));
        databaseReference = FirebaseDatabase.getInstance().getReference().child("Users2");
        databaseReference.keepSynced(true);



    }

    @Override
    protected void onStart() {
        super.onStart();
        super.onStart();
        FirebaseDatabase.getInstance().getReference().child("Users2").child(FirebaseAuth
                .getInstance().getCurrentUser().getUid()).child("online").setValue(true);

        FirebaseRecyclerAdapter<Users, UsersViewHolder> firebaseRecyclerAdapter= new FirebaseRecyclerAdapter<Users, UsersViewHolder>(
                Users.class, R.layout.users_single_layout, UsersViewHolder.class,databaseReference ) {
            @Override
            protected void populateViewHolder(UsersViewHolder viewHolder, Users model, int position) {

                viewHolder.setName(model.getName());
                viewHolder.setStatus(model.getStatus());
                viewHolder.setImage(model.getThumbnail(), getApplicationContext());

                final String user_id = getRef(position).getKey();

                viewHolder.view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        Intent profileIntent = new Intent(UsersActivity.this, ProfileActivity.class);
                        profileIntent.putExtra("user_id", user_id);
                        startActivity(profileIntent);

                    }
                });

            }
        };

        usersList.setAdapter(firebaseRecyclerAdapter);
    }



    public static class UsersViewHolder extends RecyclerView.ViewHolder{

        View view;

        public UsersViewHolder(View itemView) {
            super(itemView);

            view = itemView;
        }

        public void setName(String name){
            TextView userNameView = (TextView) view.findViewById(R.id.user_single_name);
            userNameView.setText(name);
        }

        public void setStatus(String status){
            TextView userStatusView = (TextView) view.findViewById(R.id.user_single_status);
            userStatusView.setText(status);
        }

        public void setImage(String image, Context context){
            CircleImageView userImageView = view.findViewById(R.id.user_single_image);


            Picasso.with(context).load(image).placeholder(R.drawable.image_preview).into(userImageView);
        }



    }
    @Override
    protected void onPause() {
        super.onPause();
        FirebaseDatabase.getInstance().getReference().child("Users2").child(FirebaseAuth
                .getInstance().getCurrentUser().getUid()).child("online").setValue(ServerValue.TIMESTAMP);
    }

}
