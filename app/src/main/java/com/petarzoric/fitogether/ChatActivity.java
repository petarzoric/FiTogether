package com.petarzoric.fitogether;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

/*
Klasse für den Chat Screen.
Hier werden Nachrichten versendet.
 */

public class ChatActivity extends AppCompatActivity {



    private String chatUser;
    private Toolbar chatToolbar;

    private DatabaseReference rootRef;

    private TextView titleView;
    private TextView lastSeenView;
    private CircleImageView profileImage;
    private FirebaseAuth auth;
    private String currentUserId;

    private ImageButton chatAddBtn;
    private ImageButton chatSendBtn;
    private EditText chatMessageView;

    private RecyclerView mMessageList;
    private SwipeRefreshLayout refreshLayout;
    private DatabaseReference notificationDatabase;

    private final List<Messages> messagesList = new ArrayList<>();
    private LinearLayoutManager linearLayout;
    private MessageAdapter adapter;
    private CircleImageView customPic;

    private static final int ITEMS_TO_LOAD = 10;
    private int currentPage = 1;

    private int itemPosition = 0;
    private String lastKey = "";
    private String prefKey;
    private static final int GAL_PICK = 1;
    private StorageReference imageStorage;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        chatToolbar = (Toolbar) findViewById(R.id.chat_app_bar);



        setSupportActionBar(chatToolbar);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowCustomEnabled(true);

        rootRef = FirebaseDatabase.getInstance().getReference();
        auth = FirebaseAuth.getInstance();
        currentUserId = auth.getCurrentUser().getUid();



        chatUser = getIntent().getStringExtra("user_id");
        String userName = getIntent().getStringExtra("user_name");
        LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View actionBarView = inflater.inflate(R.layout.chat_custom_bar, null);

        actionBar.setCustomView(actionBarView);



        titleView = (TextView) findViewById(R.id.custom_bar_title);
        lastSeenView = (TextView) findViewById(R.id.custom_bar_seen);
        profileImage = (CircleImageView) findViewById(R.id.custom_bar_image);

        chatAddBtn = (ImageButton) findViewById(R.id.chat_add_button);
        chatSendBtn = (ImageButton) findViewById(R.id.chat_send_button);
        chatMessageView = (EditText) findViewById(R.id.chat_message_view);
        chatMessageView.setHint("Enter Message...");
        chatMessageView.setHintTextColor(Color.LTGRAY);

        adapter = new MessageAdapter(messagesList);

        mMessageList = (RecyclerView) findViewById(R.id.messages_list);

        linearLayout = new LinearLayoutManager(this);
        mMessageList.setHasFixedSize(true);
        refreshLayout = (SwipeRefreshLayout) findViewById(R.id.message_swipe_layout);
        notificationDatabase = FirebaseDatabase.getInstance().getReference().child("notifications");
        mMessageList.setLayoutManager(linearLayout);

        mMessageList.setAdapter(adapter);
        imageStorage = FirebaseStorage.getInstance().getReference();

        try {loadMessages(); } catch (Exception e){
            System.out.println(e.toString());
            Intent data = new Intent(ChatActivity.this, MainScreen.class);

            startActivity(data);
            return;
        }

        titleView.setText(userName);
        rootRef.child("Chat").child(currentUserId).child(chatUser).child("seen").setValue(true);

        rootRef.child("Users2").child(chatUser).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String online = dataSnapshot.child("online").getValue().toString();
                String image = dataSnapshot.child("image").getValue().toString();

                Picasso.with(getApplicationContext()).load(image).placeholder(R.drawable.image_preview).into(profileImage);

                if(online.equals("true")){

                    lastSeenView.setText("Online");
                } else {
                    GetTimeAgo getTimeAgo = new GetTimeAgo();

                    long lastTime = Long.parseLong(online);
                    String lastSeenTime = getTimeAgo.getTimeAgo(lastTime, getApplicationContext());
                    lastSeenView.setText(lastSeenTime);
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        rootRef.child("Chat").child(currentUserId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                if(!dataSnapshot.hasChild(chatUser)){
                    Map chatAddMap = new HashMap();
                    chatAddMap.put("seen", false);
                    chatAddMap.put("timestamp", ServerValue.TIMESTAMP);

                    Map chatUserMap = new HashMap();
                    chatUserMap.put("Chat/"+ currentUserId + "/" + chatUser, chatAddMap);
                    chatUserMap.put("Chat/" + chatUser + "/" + currentUserId, chatAddMap);

                    rootRef.updateChildren(chatUserMap, new DatabaseReference.CompletionListener() {
                        @Override
                        public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {


                            if(databaseError != null){
                                Log.d("CHAT LOG", databaseError.getMessage().toString());
                            }
                        }
                    });
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });



        chatSendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                sendMessage();

            }
        });


        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                currentPage++;
                itemPosition = 0;


                loadMoreMessages();
            }
        });

        chatAddBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent galleryIntent = new Intent();
                galleryIntent.setType("image/*");
                galleryIntent.setAction(Intent.ACTION_GET_CONTENT);

                startActivityForResult(Intent.createChooser(galleryIntent, "Select image"), GAL_PICK);
            }
        });




    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == GAL_PICK && resultCode == RESULT_OK){

            Uri imageUri = data.getData();

            final String current_user_ref = "messages/" + currentUserId + "/" + chatUser;
            final String chat_user_ref = "messages/" + chatUser + "/" + currentUserId;

            DatabaseReference userMessagePush = rootRef.child("messages").child(currentUserId).child(chatUser).push();

            final String push_id = userMessagePush.getKey();

            StorageReference filepath = imageStorage.child("message_images").child(push_id + ".jpg");

            filepath.putFile(imageUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {

                    if(task.isSuccessful()){

                        String downloadurl = task.getResult().getDownloadUrl().toString();

                        Map messageMap = new HashMap();
                        messageMap.put("message", downloadurl);
                        messageMap.put("seen", false);
                        messageMap.put("type", "image");
                        messageMap.put("time", ServerValue.TIMESTAMP);
                        messageMap.put("from", currentUserId);

                        Map messageUserMap = new HashMap();
                        messageUserMap.put(current_user_ref + "/" + push_id, messageMap);
                        messageUserMap.put(chat_user_ref + "/" + push_id, messageMap);

                        chatMessageView.setText("");

                        rootRef.updateChildren(messageUserMap, (databaseError, databasReference) -> {

                            if(databaseError != null){

                                //iwas loggen
                            }

                        });
                    }
                }
            });
        }
    }
    //fürs Laden von neuen Nachrichten, wenn man das Refresh feature nutzt


    @Override
    protected void onStart() {
        super.onStart();
        FirebaseDatabase.getInstance().getReference().child("Users2").child(FirebaseAuth
                .getInstance().getCurrentUser().getUid()).child("online").setValue(true);
    }


    @Override
    protected void onPause() {
        super.onPause();
        super.onStop();
        FirebaseDatabase.getInstance().getReference().child("Users2").child(FirebaseAuth
                .getInstance().getCurrentUser().getUid()).child("online").setValue(ServerValue.TIMESTAMP);

    }


    /*
        Swipe to refresh funktion
 */
    private void loadMoreMessages(){

        DatabaseReference messageReference = rootRef.child("messages").child(currentUserId).child(chatUser);

        Query messageQuery = messageReference.orderByKey().endAt(lastKey).limitToLast(10);
        messageQuery.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {

                Messages message = dataSnapshot.getValue(Messages.class);


                if(!prefKey.equals(dataSnapshot.getKey())){

                    messagesList.add(itemPosition++, message);

                } else {

                     prefKey = lastKey;

                }


                if(itemPosition == 1){

                    lastKey = dataSnapshot.getKey();



                }




                adapter.notifyDataSetChanged();


                refreshLayout.setRefreshing(false);
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

    }


    private void loadMessages() {

        DatabaseReference messageReference = rootRef.child("messages").child(currentUserId).child(chatUser);

        Query messageQuery = messageReference.limitToLast(currentPage * ITEMS_TO_LOAD);

        messageQuery.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {

                Messages message = dataSnapshot.getValue(Messages.class);

                itemPosition++;

                if(itemPosition == 1){

                    lastKey = dataSnapshot.getKey();
                    prefKey = dataSnapshot.getKey();


                }

                messagesList.add(message);
                adapter.notifyDataSetChanged();

                mMessageList.scrollToPosition(messagesList.size() - 1);

                refreshLayout.setRefreshing(false);

                linearLayout.scrollToPositionWithOffset(10, 0);
                mMessageList.scrollToPosition(messagesList.size()-1);
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

    }




    /*
        Wird ausgelöst, sobald der Senden-Button gedrückt wird.
        Prüft ob, das Feld leer ist - falls nicht, wird die Nachricht in DB reingehauen und angezeigt.
    */
    private void sendMessage(){

        String message = chatMessageView.getText().toString();
        if(TextUtils.isEmpty(message)){
            Toast.makeText(ChatActivity.this, "Du kannst keine leere Nachricht senden.", Toast.LENGTH_LONG).show();
        }


        if(!TextUtils.isEmpty(message)){

            String current_user_ref = "messages/" + currentUserId + "/" + chatUser;
            String chat_user_ref = "messages/" + chatUser + "/" + currentUserId;

            DatabaseReference userMessagePush = rootRef.child("messages").child(currentUserId).child(chatUser).push();

            String push_id = userMessagePush.getKey();

            Map messageMap = new HashMap();
            messageMap.put("message", message);
            messageMap.put("seen", false);
            messageMap.put("type", "text");
            messageMap.put("time", ServerValue.TIMESTAMP);
            messageMap.put("from", currentUserId);

            Map messageUserMap = new HashMap();
            messageUserMap.put(current_user_ref + "/" + push_id, messageMap);
            messageUserMap.put(chat_user_ref + "/" + push_id, messageMap);
            HashMap<String, String> notificationData = new HashMap<>();
            notificationData.put("from", currentUserId);
            notificationData.put("type", "message");
            notificationData.put("message", chatMessageView.getText().toString());

            notificationDatabase.child(chatUser).push().setValue(notificationData);

            chatMessageView.setText("");
            chatMessageView.setHint("Enter Message...");
            chatMessageView.setHintTextColor(Color.LTGRAY);



            rootRef.updateChildren(messageUserMap, new DatabaseReference.CompletionListener() {
                @Override
                public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                    if(databaseError != null){
                        Log.d("CHAT LOG", databaseError.getMessage().toString());


                    }

                }
            });

        }


    }


}
















