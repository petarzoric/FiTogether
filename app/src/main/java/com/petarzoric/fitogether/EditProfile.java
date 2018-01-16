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
    int intentstudio;
    int intentlocation;
    int genderint;
    String gender;
    String studio;
    String UID;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);
        database = FirebaseDatabase.getInstance();
        databaseReference = database.getReference();
        emailtext = findViewById(R.id.emailtext2);
        name = findViewById(R.id.nametext);
        age = findViewById(R.id.agetext);
        level = findViewById(R.id.levelspinner);
        studios = findViewById(R.id.studiotext);
        savechanges = findViewById(R.id.savechanges);
        gendertext = findViewById(R.id.gendertext);
        childReference = FirebaseDatabase.getInstance().getReference("Users2");
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.Level, R.layout.support_simple_spinner_dropdown_item);
        level.setAdapter(adapter);

        Intent intent = getIntent();


        databaseReference.child("Users2").addValueEventListener(new ValueEventListener() {
                                                                   @Override
                                                                   public void onDataChange(DataSnapshot dataSnapshot) {
                                                                       Iterable<DataSnapshot> children = dataSnapshot.getChildren();
                                                                       for (DataSnapshot child : children) {
                                                                           if (child.getKey().equals(FirebaseAuth.getInstance().getCurrentUser().getUid())) {
                                                                               profile = child.getValue(UserProfile.class);
                                                                           }
                                                                       }
                                                                       emailtext.setText(profile.getEmail());
                                                                       name.setText(profile.getName());
                                                                       if (profile.getGender() == Gender.MÄNNLICH){
                                                                           gender = "Männlich";
                                                                       }else if (profile.getGender() == Gender.WEIBLICH){
                                                                           gender = "Weiblich";
                                                                       }else if (profile.getGender() == Gender.NOTDEFINED){
                                                                           gender = "Anderes";
                                                                       }
                                                                       gendertext.setText(gender);
                                                                       age.setText(String.valueOf(profile.getAge()));
                                                                       level.setSelection(Level.parseToInt(profile.getLevel()));
                                                                       String[] studiosString = getResources().getStringArray(R.array.Studio);
                                                                       String[] location;
                                                                       if (profile.getStudio() == 0){
                                                                           location = getResources().getStringArray(R.array.LocationFITSTAR);
                                                                           studio = studiosString[profile.getStudio()] +" "+location[profile.getLocation()];

                                                                       }else if (profile.getStudio() == 1){
                                                                           location = getResources().getStringArray(R.array.LocationFitnessFirst);
                                                                           studio = studiosString[profile.getStudio()] +" "+location[profile.getLocation()];

                                                                       }else if (profile.getStudio() == 2){
                                                                           location = getResources().getStringArray(R.array.LocationBodyandSoul);
                                                                           studio = studiosString[profile.getStudio()] +" "+location[profile.getLocation()];

                                                                       }else if (profile.getStudio() == 3){
                                                                           location = getResources().getStringArray(R.array.LocationMcFit);
                                                                           studio = studiosString[profile.getStudio()] +" "+location[profile.getLocation()];

                                                                       }else if (profile.getStudio() == 4){
                                                                           location = getResources().getStringArray(R.array.LocationCleverFit);
                                                                           studio = studiosString[profile.getStudio()] +" "+location[profile.getLocation()];

                                                                       }else if (profile.getStudio() == 5){
                                                                           location = getResources().getStringArray(R.array.LocationAndere);
                                                                           studio = location[profile.getLocation()];

                                                                       }
                                                                       studios.setText(studio);



                                                                   }
                                                                       @Override
                                                                       public void onCancelled
                                                                       (DatabaseError databaseError)
                                                                       {

                                                                       }

                                                                   });
      /*  emailtext.setText(intent.getStringExtra("email"));
        name.setText(intent.getStringExtra("name"));
        gendertext.setText(intent.getStringExtra("gender"));
        age.setText(String.valueOf(intent.getIntExtra("age", 0)));
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.Level, R.layout.support_simple_spinner_dropdown_item);
        level.setAdapter(adapter);
        level.setSelection(intent.getIntExtra("level", 0));
        studios.setText(intent.getStringExtra("studios"));
        intentlocation= intent.getIntExtra("location",0);
        intentstudio= intent.getIntExtra("studio",0);
        genderint = intent.getIntExtra("genderint", 0);*/
        savechanges.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveChanges();
            }
        });
        UID = FirebaseAuth.getInstance().getCurrentUser().getUid();




    }

    public void saveChanges(){
        profile = new UserProfile(UID, emailtext.getText().toString(), name.getText().toString(), Integer.parseInt(age.getText().toString()), Level.parseToEnum(level.getSelectedItemPosition()) , intentstudio, intentlocation, Gender.parseToEnum(genderint), "default", "default", "Hi I am using FiTogether");
        DatabaseReference ref = childReference.child(FirebaseAuth.getInstance().getCurrentUser().getUid());
        ref.child("uid").setValue(UID);
        ref.child("email").setValue(emailtext.getText().toString());
        ref.child("name").setValue(name.getText().toString());
        ref.child("age").setValue( Integer.parseInt(age.getText().toString()));
        ref.child("level").setValue(Level.parseToEnum(level.getSelectedItemPosition()));


        Intent data = new Intent(EditProfile.this, MainScreen.class);
        startActivity(data);

    }



}


