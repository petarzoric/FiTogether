package com.petarzoric.fitogether;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity {
    EditText email;
    EditText email2;
    EditText password;
    FirebaseAuth auth;
    FirebaseAuth.AuthStateListener authListener;
    Button signup;
    Button login;
    String emailtext;
    String key;
    DatabaseReference databaseReference;
    FirebaseDatabase database;
    FirebaseUser currentUser;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Intent intent = getIntent();
        if (intent.getBooleanExtra("Signout", true)){
            FirebaseAuth.getInstance().signOut();
            Toast.makeText(MainActivity.this, "Signed Out", Toast.LENGTH_LONG).show();

        }
        email =  findViewById(R.id.email);
        email2 =  findViewById(R.id.email2);
        password =  findViewById(R.id.password);
        auth = FirebaseAuth.getInstance();
        signup = findViewById(R.id.signup);
        login =  findViewById(R.id.login);
        database = FirebaseDatabase.getInstance();
        databaseReference = database.getReference();





        authListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                currentUser = firebaseAuth.getCurrentUser();
                if (currentUser != null) {

                    Toast.makeText(MainActivity.this, "Welcome", Toast.LENGTH_LONG).show();


                    // User is signed in
                } else {


                    // User is signed out
                    Toast.makeText(MainActivity.this, "user is signed in", Toast.LENGTH_LONG).show();
                    Intent startIntent = new Intent(MainActivity.this, MainScreen.class);
                    startActivity(startIntent);
                    finish();
                }
                // ...
            }
        };
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startLogIn();
            }
        });
        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startSignIn();
            }
        });


    }
    @Override
    public void onStart() {
        super.onStart();
        auth.addAuthStateListener(authListener);



    }
    @Override
    public void onStop() {
        super.onStop();
        if (authListener != null) {
            auth.removeAuthStateListener(authListener);
        }else {
            Toast.makeText(MainActivity.this, "Please enter correct values", Toast.LENGTH_LONG).show();
        }
    }
    public void startLogIn() {
        String mail = email.getText().toString()+"." + email2.getText().toString();
        String pw = password.getText().toString();
        if (!TextUtils.isEmpty(mail) && !TextUtils.isEmpty(pw)){
            auth.signInWithEmailAndPassword(mail, pw).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (!task.isSuccessful()){
                        Toast.makeText(MainActivity.this, "Try Again", Toast.LENGTH_LONG).show();
                    }else {
                        emailtext = email.getText().toString() + "." + email2.getText().toString();
                        key = email.getText().toString() + "_DOT_" + email2.getText().toString();
                        databaseReference.child("UserData").addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                Iterable<DataSnapshot> children = dataSnapshot.getChildren();
                                boolean exists = false;
                                for (DataSnapshot child : children) {
                                    if (child.getKey().equals(key)) {
                                        exists = true;
                                    }
                                }
                                    if (exists){
                                        Intent data = new Intent(MainActivity.this, MainScreen.class);
                                        data.putExtra("key", key);
                                        startActivity(data);
                                    }
                                    else{
                                        Intent data = new Intent(MainActivity.this, SecondScreen.class);
                                        data.putExtra("key", key);
                                        data.putExtra("email", emailtext);
                                        startActivity(data);
                                    }
                                }


                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });


                    }
                }
            });
        }
    }
    public void startSignIn(){
        String mail = email.getText().toString() + "." + email2.getText().toString();
        String pw = password.getText().toString();
        if (!TextUtils.isEmpty(mail) && !TextUtils.isEmpty(pw)){
            auth.createUserWithEmailAndPassword(mail, pw).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (!task.isSuccessful()){
                        Toast.makeText(MainActivity.this, "this email is already used", Toast.LENGTH_LONG).show();
                    }else {
                        emailtext = email.getText().toString() + "." + email2.getText().toString();
                        key = email.getText().toString() + "_DOT_" + email2.getText().toString();
                        Toast.makeText(MainActivity.this, "Created Account", Toast.LENGTH_LONG).show();
                        Intent data = new Intent(MainActivity.this, SecondScreen.class);
                        data.putExtra("key", key);
                        data.putExtra("email", emailtext);
                        startActivity(data);
                    }
                }
            });

        }else {
            Toast.makeText(MainActivity.this, "Please enter correct values", Toast.LENGTH_LONG).show();
        }
    }

}
