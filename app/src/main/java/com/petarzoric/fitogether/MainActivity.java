package com.petarzoric.fitogether;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.OAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;


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
    SharedPreferences settings;
    private ProgressDialog progressDialog;







    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        email =  findViewById(R.id.email);
        email2 =  findViewById(R.id.email2);
        password =  findViewById(R.id.password);
        auth = FirebaseAuth.getInstance();
        signup = findViewById(R.id.signup);
        login =  findViewById(R.id.login);
        database = FirebaseDatabase.getInstance();
        databaseReference = database.getReference();
        final String emailString = email.getEditableText().toString()+"."+email2.getEditableText().toString();
        final String passwordString = password.getEditableText().toString();
        login.setEnabled(false);
        signup.setEnabled(false);
        progressDialog = new ProgressDialog(this);
        email.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                checkButtons();
            }

            @Override
            public void afterTextChanged(Editable s) {
                checkButtons();

            }
        });
        email2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                checkButtons();
            }

            @Override
            public void afterTextChanged(Editable s) {
                checkButtons();

            }
        });

        password.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                checkButtons();
            }

            @Override
            public void afterTextChanged(Editable s) {
                checkButtons();

            }
        });

        email.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                checkButtons();

            }
        });




        authListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                currentUser = firebaseAuth.getCurrentUser();
                if (currentUser != null) {
                    //TODO
                    //eigentlich sollte man logged bleiben, klappt aber nicht
                    //intent sorgt für nullpointer
                    //aufrufen von startLogin() bringt auch nichts
                    autoLogin();




                    Toast.makeText(MainActivity.this, currentUser.getEmail(), Toast.LENGTH_LONG).show();
                    Toast.makeText(MainActivity.this, "Welcome", Toast.LENGTH_LONG).show();

                } else {


                    // User is signed out

                }
                // ...
            }
        };


        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                progressDialog.setTitle("Logging in");
                progressDialog.setMessage("Please wait while we check your credentials. ");
                progressDialog.setCanceledOnTouchOutside(false);
                progressDialog.show();

                    startLogIn();
                }

        });
        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressDialog.setTitle("Registering User");
                progressDialog.setMessage("Please wait while we create your account");
                progressDialog.setCanceledOnTouchOutside(false);
                progressDialog.show();
                startSignIn(emailString, passwordString);
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
                        progressDialog.dismiss();
                        Toast.makeText(MainActivity.this, "Try Again", Toast.LENGTH_LONG).show();
                    }else {
                        progressDialog.hide();
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
                                        getIntent().putExtra("key", key);
                                        SharedPreferences sharedPreferences = getSharedPreferences("User", 0);
                                        SharedPreferences.Editor editor = sharedPreferences.edit();
                                        editor.putString("key", key);
                                        editor.commit();
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

    public void autoLogin(){

        databaseReference.child("UserData").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Iterable<DataSnapshot> children = dataSnapshot.getChildren();
                SharedPreferences settings = getSharedPreferences("User", 0);
                String key = settings.getString("key", "");
                boolean exists = false;
                for (DataSnapshot child : children) {
                    if (child.getKey().equals(key)) {
                        exists = true;

                    }
                }
                if (exists) {
                    Intent data = new Intent(MainActivity.this, MainScreen.class);
                    data.putExtra("key", key);
                    getIntent().putExtra("key", key);
                    startActivity(data);
                }
            }


            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }


    public void checkButtons(){
        if (!(TextUtils.isEmpty(email.getText().toString()) || password.getText().toString().length() < 6 || TextUtils.isEmpty(email2.getText().toString()))){
            login.setEnabled(true);
            signup.setEnabled(true);
        }
        else{
            login.setEnabled(false);
            signup.setEnabled(false);

        }

    }
    
    
    public void startSignIn(String emailStr, String passwordStr){

        String mail = emailStr;
        String pw = passwordStr;
        if (!TextUtils.isEmpty(mail) && !TextUtils.isEmpty(pw)){
            auth.createUserWithEmailAndPassword(mail, pw).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (!task.isSuccessful()){
                        progressDialog.dismiss();
                        Toast.makeText(MainActivity.this, "this email is already used", Toast.LENGTH_LONG).show();
                    }else {
                        progressDialog.hide();
                        emailtext = email.getText().toString() + "." + email2.getText().toString();
                        key = email.getText().toString() + "_DOT_" + email2.getText().toString();
                        Toast.makeText(MainActivity.this, "Created Account", Toast.LENGTH_LONG).show();
                        Intent data = new Intent(MainActivity.this, SecondScreen.class);
                        data.putExtra("key", key);
                        data.putExtra("email", emailtext);
                        startActivity(data);
                        finish();
                    }
                }
            });

        }else {
            Toast.makeText(MainActivity.this, "Please enter correct values", Toast.LENGTH_LONG).show();
        }
    }

}
