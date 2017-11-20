package com.petarzoric.fitogether;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class StudioScreen extends AppCompatActivity {

    int userlevel;
    int userage;
    String username;
    String usermail;
    UserProfile profile;
    DatabaseReference databaseReference;
    Button save;
    Spinner studio;
    Spinner location;
    int userstudio;
    int userlocation;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_studio_screen);
        databaseReference = FirebaseDatabase.getInstance().getReference("UserData");
        Intent intent = getIntent();
        userlevel = intent.getIntExtra("level", 0);
        userage = intent.getIntExtra("age", 0);
        usermail = intent.getStringExtra("mail");
        username = intent.getStringExtra("name");
        location = findViewById(R.id.Location);
        studio = findViewById(R.id.Studio);
        save =  findViewById(R.id.save);
        ArrayAdapter<CharSequence> studioadapter = ArrayAdapter.createFromResource(this, R.array.Studio, R.layout.support_simple_spinner_dropdown_item);
        studio.setAdapter(studioadapter);

        ArrayAdapter<CharSequence> fitstaradapter = ArrayAdapter.createFromResource(this, R.array.LocationFITSTAR, R.layout.support_simple_spinner_dropdown_item);
        ArrayAdapter<CharSequence> fitfirstadapter = ArrayAdapter.createFromResource(this, R.array.LocationFitnessFirst, R.layout.support_simple_spinner_dropdown_item);
        ArrayAdapter<CharSequence> basadapter = ArrayAdapter.createFromResource(this, R.array.LocationBodyandSoul, R.layout.support_simple_spinner_dropdown_item);
        ArrayAdapter<CharSequence> mcfitadapter = ArrayAdapter.createFromResource(this, R.array.LocationMcFit, R.layout.support_simple_spinner_dropdown_item);
        ArrayAdapter<CharSequence> cleveradapter = ArrayAdapter.createFromResource(this, R.array.LocationCleverFit, R.layout.support_simple_spinner_dropdown_item);
        ArrayAdapter<CharSequence> andereadapter = ArrayAdapter.createFromResource(this, R.array.LocationAndere, R.layout.support_simple_spinner_dropdown_item);

        if (studio.getSelectedItemPosition() == 0){
            location.setAdapter(fitstaradapter);
        }
        if (studio.getSelectedItemPosition() == 1){
            location.setAdapter(fitfirstadapter);
        }
        if (studio.getSelectedItemPosition() == 2){
            location.setAdapter(basadapter);
        }
        if (studio.getSelectedItemPosition() == 3){
            location.setAdapter(mcfitadapter);
        }
        if (studio.getSelectedItemPosition() == 4){
            location.setAdapter(cleveradapter);
        }
        if (studio.getSelectedItemPosition() == 5){
            location.setAdapter(andereadapter);
        }


        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveProfile();
            }
        });
    }
    void saveProfile() {
        userstudio = studio.getSelectedItemPosition();
        userlocation = location.getSelectedItemPosition();

        profile = new UserProfile(usermail, username, userage, userlevel, userstudio, userlocation);
        databaseReference.child(username).setValue(profile);

    }
}
