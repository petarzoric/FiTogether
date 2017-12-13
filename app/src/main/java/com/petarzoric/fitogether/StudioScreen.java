package com.petarzoric.fitogether;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class StudioScreen extends AppCompatActivity {

    int userlevel;
    int userage;
    String username;
    String usermail;
    String key;
    UserProfile profile;
    DatabaseReference databaseReference;
    Button save;
    Spinner studio;
    Spinner location;
    int userstudio;
    int userlocation;
    int usergender;




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
        usergender = intent.getIntExtra("gender", 0);
        key = intent.getStringExtra("key");
        //neues db modell
        String userID = getIntent().getStringExtra("userID");
        String userMail = getIntent().getStringExtra("userMail");
        int userLevel = getIntent().getIntExtra("userLevel", -1);
        int userGender = getIntent().getIntExtra("userGender", -1);
        int userAge = getIntent().getIntExtra("userAge", -1);
        String userName = getIntent().getStringExtra("userName");
        int userStudio = studio.getSelectedItemPosition();
        int userStudioLocation = location.getSelectedItemPosition();
       // Toast.makeText(userStudioLocation, "LOL", Toast.LENGTH_LONG);


        Log.i("-------------------", "test");
        Log.i("-------------------", "test");
        Log.i("-------------------", "test");
        System.out.println(userID);
        System.out.println(userMail);
        System.out.println(userLevel);
        System.out.println(userGender);
        System.out.println(userAge);
        System.out.println(userName);
        System.out.println(userStudio);
        System.out.println(userStudioLocation);
        Log.i("-------------------", "test");
        Log.i("-------------------", "test");
        Log.i("-------------------", "test");


        UserProfile profileData = new UserProfile(userID, userMail, userName, userAge, Level.parseToEnum(userLevel), userStudio, userStudioLocation, Gender.parseToEnum(userGender), "default", "default");
        HashMap<String, Object> profileDBO = ProfileParser.parse(profileData);

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("Users2").child(FirebaseAuth.getInstance().getCurrentUser().getUid());
        databaseReference.setValue(profileDBO);



        Intent dataThirdScreen = new Intent(StudioScreen.this, MainScreen.class);
        dataThirdScreen.putExtra("key", key);
        startActivity(dataThirdScreen);

    }
}

class ProfileParser {

    public static HashMap<String, Object> parse(UserProfile profile){
        HashMap<String, Object> dataBaseObject= new HashMap<>();
        dataBaseObject.put("UID", profile.getUid());
        dataBaseObject.put("eMail", profile.getEmail());
        dataBaseObject.put("name", profile.getEmail());
        dataBaseObject.put("age", profile.getAge());
        dataBaseObject.put("level", profile.getLevel().toString());
        dataBaseObject.put("studio", profile.getStudio());
        dataBaseObject.put("studio location", profile.getLocation());
        dataBaseObject.put("gender", profile.getGender().toString());
        dataBaseObject.put("image", profile.getImageURL());
        dataBaseObject.put("thumbnail", profile.getThumbURL());

        return dataBaseObject;
    }
}
