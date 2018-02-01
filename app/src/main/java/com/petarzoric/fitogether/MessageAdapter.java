package com.petarzoric.fitogether;

import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by petarzoric on 17.01.18.
 */

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.MessageViewHolder> {


    private List<Messages> messagesList;
    private FirebaseAuth auth;
    private DatabaseReference userDatabase;

    public MessageAdapter(List<Messages> messagesList) {
        this.messagesList = messagesList;
    }

    @Override
    public MessageViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.message_single_layout, parent, false);
        return new MessageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final MessageViewHolder holder, int position) {
        auth = FirebaseAuth.getInstance();

        String current_user_id = auth.getCurrentUser().getUid();
        Messages c = messagesList.get(position);
        long time = c.getTime();
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
        String timeToDisplay = sdf.format(time);
        holder.time.setText(timeToDisplay);



        String from_user = c.getFrom();
        String message_type = c.getType();

        userDatabase = FirebaseDatabase.getInstance().getReference().child("Users2").child(from_user);

        userDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String image = dataSnapshot.child("thumbnail").getValue().toString();
                Picasso.with(holder.profileImage.getContext()).load(image).placeholder(R.drawable.image_preview).into(holder.profileImage);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        if(message_type.equals("text")){

            holder.messageText.setText(c.getMessage());
            holder.messageImage.setVisibility(View.INVISIBLE);
        } else {

            holder.messageText.setVisibility(View.INVISIBLE);

            Picasso.with(holder.profileImage.getContext()).load(c.getMessage()).placeholder(R.drawable.image_preview)
                    .into(holder.messageImage);
        }



        if(from_user.equals(current_user_id)){
            holder.messageText.setBackgroundResource(R.drawable.message_text_background2);
          //  holder.messageText.setBackgroundColor(Color.WHITE);
            holder.messageText.setTextColor(Color.BLACK);
            holder.layout.setHorizontalGravity(Gravity.LEFT);
            holder.messageText.setHint("Enter Message...");


        } else {
            //layout ändern, damit man siegt, dass die Nachricht von der anderen Person ist

            holder.messageText.setBackgroundResource(R.drawable.message_text_background);
            holder.messageText.setTextColor(Color.BLACK) ;
            holder.layout.setHorizontalGravity(Gravity.RIGHT);
            holder.messageText.setHint("Enter Message...");


        }

        holder.messageText.setText(c.getMessage());


    }

    @Override
    public int getItemCount() {
        return messagesList.size();
    }


    public class MessageViewHolder extends RecyclerView.ViewHolder {

        public TextView time;
        public CircleImageView profileImage;
        public ImageView messageImage;
        public TextView messageText;
        public RelativeLayout messageLayout;
        public RelativeLayout layout;

        public MessageViewHolder(View view) {
            super(view);
            time = view.findViewById(R.id.message_time);
            time.setTextColor(Color.BLACK);
            messageText = (TextView) view.findViewById(R.id.message_text_layout);
            profileImage = (CircleImageView) view.findViewById(R.id.message_profile_layout);
            messageImage = (ImageView) view.findViewById(R.id.message_image_layout);
            messageLayout = view.findViewById(R.id.messageRelativeLayout);
            layout = view.findViewById(R.id.message_single_layout);
            messageText.setHint("Enter Message...");
            messageText.setHintTextColor(Color.BLACK);
        }



    }
}