package com.petarzoric.fitogether;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class EditProfile extends AppCompatActivity {
    TextView emailtext;
    EditText name;
    EditText age;
    Spinner level;
    TextView studios;
    TextView gendertext;
    Button savechanges;
    DatabaseReference childReference;
    FirebaseDatabase database;
    DatabaseReference databaseReference;
    UserProfile profile;
    String UID;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);
        database = FirebaseDatabase.getInstance();
        databaseReference = database.getReference();
        emailtext = findViewById(R.id.emailtext2);
        name = findViewById(R.id.nametext);
        if(FirebaseAuth.getInstance().getCurrentUser() != null){
            UID = FirebaseAuth.getInstance().getCurrentUser().getUid();

        }
        age = findViewById(R.id.agetext);
        level = findViewById(R.id.levelspinner);
        studios = findViewById(R.id.studiotext);
        savechanges = findViewById(R.id.savechanges);
        gendertext = findViewById(R.id.gendertext);
        childReference = FirebaseDatabase.getInstance().getReference("Users2");
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.Level, R.layout.support_simple_spinner_dropdown_item);
        level.setAdapter(adapter);

        databaseReference.child("Users2").child(UID).addValueEventListener(new ValueEventListener() {
                                                                   @Override
                                                                   //TODO updaten
                                                                   public void onDataChange(DataSnapshot dataSnapshot) {
                                                                       profile = (UserProfile) dataSnapshot.getValue();
                                                                       emailtext.setText(profile.getEmail());
                                                                       name.setText(profile.getName());
                                                                       gendertext.setText(Gender.parseToString(profile.getGender()));
                                                                       age.setText(String.valueOf(profile.getAge()));
                                                                       level.setSelection(Level.parseToInt(profile.getLevel()));
                                                                       studios.setText(Converter.studioString(profile.getStudio(), profile.getLocation(), getResources()));


                                                                   }
                                                                       @Override
                                                                       public void onCancelled
                                                                       (DatabaseError databaseError)
                                                                       {

                                                                       }

                                                                   });

        savechanges.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveChanges();
            }
        });
        UID = FirebaseAuth.getInstance().getCurrentUser().getUid();


    }

    public void saveChanges(){
        DatabaseReference ref = childReference.child(UID);
        ref.child("uid").setValue(UID);
        ref.child("email").setValue(emailtext.getText().toString());
        ref.child("name").setValue(name.getText().toString());
        ref.child("age").setValue(Integer.parseInt(age.getText().toString()));
        ref.child("level").setValue(Level.parseToEnum(level.getSelectedItemPosition()));

        Intent data = new Intent(EditProfile.this, ChatActivity.class);
        startActivity(data);

    }



}


