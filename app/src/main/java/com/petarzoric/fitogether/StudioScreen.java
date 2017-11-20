package com.petarzoric.fitogether;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
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
        location = findViewById(R.id.Location);
        studio = findViewById(R.id.Studio);
        save =  findViewById(R.id.save);
        ArrayAdapter<CharSequence> studioadapter = ArrayAdapter.createFromResource(this, R.array.Studio, R.layout.support_simple_spinner_dropdown_item);
        studio.setAdapter(studioadapter);


        studio.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (studio.getSelectedItemPosition() == 0 ){
                    ArrayAdapter<CharSequence> fitstaradapter = ArrayAdapter.createFromResource(StudioScreen.this, R.array.LocationFITSTAR, R.layout.support_simple_spinner_dropdown_item);
                    location.setAdapter(fitstaradapter);
                }

                if (studio.getSelectedItemPosition() == 1){
                    ArrayAdapter<CharSequence> fitfirstadapter = ArrayAdapter.createFromResource(StudioScreen.this, R.array.LocationFitnessFirst, R.layout.support_simple_spinner_dropdown_item);

                    location.setAdapter(fitfirstadapter);
                }
                if (studio.getSelectedItemPosition() == 2){
                    ArrayAdapter<CharSequence> basadapter = ArrayAdapter.createFromResource(StudioScreen.this, R.array.LocationBodyandSoul, R.layout.support_simple_spinner_dropdown_item);

                    location.setAdapter(basadapter);
                }
                if (studio.getSelectedItemPosition() == 3){
                    ArrayAdapter<CharSequence> mcfitadapter = ArrayAdapter.createFromResource(StudioScreen.this, R.array.LocationMcFit, R.layout.support_simple_spinner_dropdown_item);

                    location.setAdapter(mcfitadapter);
                }
                if (studio.getSelectedItemPosition() == 4){
                    ArrayAdapter<CharSequence> cleveradapter = ArrayAdapter.createFromResource(StudioScreen.this, R.array.LocationCleverFit, R.layout.support_simple_spinner_dropdown_item);

                    location.setAdapter(cleveradapter);
                }
                if (studio.getSelectedItemPosition() == 5){
                    ArrayAdapter<CharSequence> andereadapter = ArrayAdapter.createFromResource(StudioScreen.this, R.array.LocationAndere, R.layout.support_simple_spinner_dropdown_item);

                    location.setAdapter(andereadapter);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });



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
        Intent intent = getIntent();
        userlevel = intent.getIntExtra("level", 0);
        userage = intent.getIntExtra("age", 0);
        usermail = intent.getStringExtra("mail");
        username = intent.getStringExtra("name");

        profile = new UserProfile(usermail, username, userage, userlevel, userstudio, userlocation);
        databaseReference.child(username).setValue(profile);
        Intent data = new Intent(StudioScreen.this, MainScreen.class);
        startActivity(data);

    }
}
