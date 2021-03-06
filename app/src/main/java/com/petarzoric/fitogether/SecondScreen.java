package com.petarzoric.fitogether;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;


public class SecondScreen extends AppCompatActivity {
    private Button signout;
    private Button save;
    private EditText name;
    private EditText age;
    private Spinner level;
    private Spinner gender;
    private int userage;
    private String username;
    private int userlevel;
    private int usergender;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second_screen);


        signout =  findViewById(R.id.signout);
        signout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent data = new Intent(SecondScreen.this, MainActivity.class);
                startActivity(data);
                FirebaseAuth.getInstance().signOut();


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

    @Override
    public void onBackPressed() {
        Intent data = new Intent(SecondScreen.this, MainActivity.class);
        startActivity(data);
        FirebaseAuth.getInstance().signOut();
    }

    void next(){
        if (!TextUtils.isEmpty(name.getText().toString())) {
            if (!TextUtils.isEmpty(age.getText().toString())){
               if (Integer.parseInt(age.getText().toString()) < 100) {
                   username = name.getText().toString();
                   if (15 < Integer.parseInt(age.getText().toString())){


                       userage = Integer.parseInt(age.getText().toString());

                       userlevel = level.getSelectedItemPosition();
                       usergender = gender.getSelectedItemPosition();
                       Intent intent = getIntent();
                       //DB, neuer Ansatz
                       String userID = getIntent().getStringExtra("userID");
                       String userMail = getIntent().getStringExtra("userMail");


                       Intent dataSecondScreen = new Intent(SecondScreen.this, StudioScreen.class);

                       //
                       //DB-Umstellung, Tests
                       dataSecondScreen.putExtra("userMail", userMail);
                       dataSecondScreen.putExtra("userID", userID);
                       dataSecondScreen.putExtra("userLevel", userlevel);
                       dataSecondScreen.putExtra("userGender", usergender);
                       dataSecondScreen.putExtra("userAge", userage);
                       dataSecondScreen.putExtra("userName", username);


                       startActivity(dataSecondScreen);
                   }else {
                       Toast.makeText(SecondScreen.this, "You must be over 15 years old", Toast.LENGTH_SHORT).show();
                   }
                }
                else {
                    Toast.makeText(SecondScreen.this, "You must be under 100 years old", Toast.LENGTH_SHORT).show();
                }
            }else {
                    Toast.makeText(SecondScreen.this, "Please Select an Age", Toast.LENGTH_SHORT).show();
                }

        }
        else {
            Toast.makeText(SecondScreen.this, "Please Enter a Name", Toast.LENGTH_SHORT).show();
        }

    }








}
