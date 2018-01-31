package com.petarzoric.fitogether;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class RequestActivity extends AppCompatActivity {

    private RecyclerView requestsList;
    private DatabaseReference requestsDatabase;
    private FirebaseAuth auth;
    private String currentUserId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request);
        requestsList = findViewById(R.id.requestsList);
        requestsDatabase = FirebaseDatabase.getInstance().getReference().child("Friend_req");
        auth = FirebaseAuth.getInstance();
        currentUserId = auth.getCurrentUser().getUid();

        requestsList.setHasFixedSize(true);
        requestsList.setLayoutManager(new LinearLayoutManager(RequestActivity.this));

    }
    /*
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
            protected void populateViewHolder(RequestsViewHolder viewHolder, Request model, int position) {

            }
        };

    }
}

/*
public static  class RequestsViewHolder extends RecyclerView.ViewHolder {

    View view;

    public RequestsViewHolder(View itemView){
        super(itemView);

        view = itemView;
    }

}
*/
}

