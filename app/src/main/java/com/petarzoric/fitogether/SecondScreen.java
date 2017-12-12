package com.petarzoric.fitogether;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class SecondScreen extends AppCompatActivity {
    Button signout;
    Button save;
    EditText name;
    EditText age;
    Spinner level;
    Spinner gender;
    int userage;
    String username;
    int userlevel;
    int usergender;
    String usermail;
    String key;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second_screen);


        signout =  findViewById(R.id.signout);
        signout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent data = new Intent(SecondScreen.this, MainActivity.class);
                data.putExtra("Signout", true);
                startActivity(data);

            }
        });
        save =  findViewById(R.id.saveprofile);
        name =  findViewById(R.id.name);
        age = findViewById(R.id.age);
        level = findViewById(R.id.level);
        gender = findViewById(R.id.gender);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.Level, R.layout.support_simple_spinner_dropdown_item);
        level.setAdapter(adapter);
        ArrayAdapter<CharSequence> genderadapter = ArrayAdapter.createFromResource(this, R.array.Geschlecht, R.layout.support_simple_spinner_dropdown_item);
        gender.setAdapter(genderadapter);
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                next();



            }
        });
    }
    void next(){
        if (!TextUtils.isEmpty(name.getText().toString()) && !TextUtils.isEmpty(age.getText().toString()) && Integer.parseInt(age.getText().toString()) > 0 && Integer.parseInt(age.getText().toString()) < 100) {
            username = name.getText().toString();
            userage = Integer.parseInt(age.getText().toString());
            userlevel = level.getSelectedItemPosition();
            usergender = gender.getSelectedItemPosition();
            Intent intent = getIntent();
            usermail = intent.getStringExtra("email");
            key = intent. getStringExtra("key");
            //DB, neuer Ansatz
            String userID = getIntent().getStringExtra("userID");
            String userMail = getIntent().getStringExtra("userMail");

            Intent dataSecondScreen = new Intent(SecondScreen.this, StudioScreen.class);
            dataSecondScreen.putExtra("mail", usermail);
            dataSecondScreen.putExtra("key", key);
            dataSecondScreen.putExtra("age", userage);
            dataSecondScreen.putExtra("level", userlevel);
            dataSecondScreen.putExtra("name", username);
            dataSecondScreen.putExtra("gender", usergender);

            //
            //DB-Umstellung, Tests
            dataSecondScreen.putExtra("userMail", userMail);
            dataSecondScreen.putExtra("userID", userID);
            dataSecondScreen.putExtra("userLevel", userlevel);
            dataSecondScreen.putExtra("userGender", usergender);
            dataSecondScreen.putExtra("userAge", userage);


            FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
            String uid = currentUser.getUid();
            DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("Users2").child(uid);

            HashMap<String, Object> userMap = new HashMap<String, Object>();
            userMap.put("age", userage);
            userMap.put("name", username);
            userMap.put("level", Level.valueOf(level.toString()));

            databaseReference.setValue(userMap);


            startActivity(dataSecondScreen);

        }
        else {
            Toast.makeText(SecondScreen.this, "Please Fill Out Every Field", Toast.LENGTH_SHORT).show();
        }

    }








}
