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
import android.widget.TextView;
import android.widget.Toast;

public class SecondScreen extends AppCompatActivity {
    Button signout;
    Button save;
    EditText name;
    EditText age;
    Spinner level;
    int userage;
    String username;
    int userlevel;
    String usermail;


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
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.Level, R.layout.support_simple_spinner_dropdown_item);
        level.setAdapter(adapter);
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
            Intent intent = getIntent();
            usermail = intent.getStringExtra("email");
            Intent userdata = new Intent(SecondScreen.this, StudioScreen.class);
            Intent user = new Intent(SecondScreen.this, StudioScreen.class);
           // user.putExtra("mail", usermail);
           // user.putExtra("age", userage);
           // user.putExtra("level", userlevel);
           // user.putExtra("name", username);
            startActivity(userdata);

        }
        else {
            Toast.makeText(SecondScreen.this, "Please Fill Out Every Field", Toast.LENGTH_SHORT).show();
        }

    }








}
