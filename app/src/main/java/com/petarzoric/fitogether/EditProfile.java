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

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class EditProfile extends AppCompatActivity {
    TextView emailtext;
    EditText name;
    EditText age;
    Spinner level;
    TextView studio;
    TextView gendertext;
    Button savechanges;
    DatabaseReference databaseReference;
    UserProfile profile;
    String key;
    int intentstudio;
    int intentlocation;
    int genderint;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);
        emailtext = findViewById(R.id.emailtext2);
        name = findViewById(R.id.nametext);
        age = findViewById(R.id.agetext);
        level = findViewById(R.id.levelspinner);
        studio = findViewById(R.id.studiotext);
        savechanges = findViewById(R.id.savechanges);
        gendertext = findViewById(R.id.gendertext);

        Intent intent = getIntent();

        emailtext.setText(intent.getStringExtra("email"));
        name.setText(intent.getStringExtra("name"));
        gendertext.setText(intent.getStringExtra("gender"));
        age.setText(String.valueOf(intent.getIntExtra("age", 0)));
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.Level, R.layout.support_simple_spinner_dropdown_item);
        level.setAdapter(adapter);
        level.setSelection(intent.getIntExtra("level", 0));
        studio.setText(intent.getStringExtra("studios"));
        databaseReference = FirebaseDatabase.getInstance().getReference("UserData");
        intentlocation= intent.getIntExtra("location",0);
        intentstudio= intent.getIntExtra("studio",0);
        genderint = intent.getIntExtra("genderint", 0);
        key = intent.getStringExtra("key");
        savechanges.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveChanges();
            }
        });



    }
    public void saveChanges(){
        profile = new UserProfile(emailtext.getText().toString(), name.getText().toString(), Integer.parseInt(age.getText().toString()), level.getSelectedItemPosition() , intentstudio, intentlocation, genderint);
        databaseReference.child(key).setValue(profile);
        Intent data = new Intent(EditProfile.this, MainScreen.class);
        data.putExtra("key", key);
        startActivity(data);

    }


}
