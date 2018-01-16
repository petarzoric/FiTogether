package com.petarzoric.fitogether;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;


public class MainActivity extends AppCompatActivity {
    EditText email;
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
    private ProgressDialog progressDialog;
    private String emailString;
    private String passwordString;
    private DatabaseReference usersDatabase;








    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        email =  findViewById(R.id.email);
        password =  findViewById(R.id.password);
        auth = FirebaseAuth.getInstance();
        signup = findViewById(R.id.signup);
        login =  findViewById(R.id.login);
        database = FirebaseDatabase.getInstance();
        databaseReference = database.getReference();
        usersDatabase = FirebaseDatabase.getInstance().getReference().child("Users2").child(auth.getCurrentUser().getUid());
        emailString = email.getEditableText().toString();
        passwordString = password.getEditableText().toString();
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
                    progressDialog.setTitle("Auto Login");
                    progressDialog.setMessage("Please wait while we log you in");
                    progressDialog.show();
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

                String emailString = email.getEditableText().toString();
                String passwordString = password.getEditableText().toString();
                startLogIn(emailString, passwordString);
            }

        });
        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressDialog.setTitle("Registering User");
                progressDialog.setMessage("Please wait while we create your account");
                // progressDialog.setCanceledOnTouchOutside(false);
                progressDialog.show();
                String emailString = email.getEditableText().toString();
                String passwordString = password.getEditableText().toString();
                startSignIn(emailString, passwordString);
            }
        });


    }
    @Override
    public void onStart() {
        super.onStart();
        auth.addAuthStateListener(authListener);
        //





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
    public void startLogIn(String emailStr, String passwordStr) {


        final String mail = emailStr;
        String pw = passwordStr;

        if (!TextUtils.isEmpty(mail) && !TextUtils.isEmpty(pw)){
            auth.signInWithEmailAndPassword(mail, pw).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (!task.isSuccessful()){
                        progressDialog.dismiss();
                        Toast.makeText(MainActivity.this, "Try Again", Toast.LENGTH_LONG).show();
                    }else {

                        String deviceToken = FirebaseInstanceId.getInstance().getToken();
                        String currentUID = auth.getCurrentUser().getUid();

                        database.getReference().child("Users2").child(currentUID).child("device_token").setValue(deviceToken).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {

                            }
                        });

                        progressDialog.hide();
                        emailtext = email.getText().toString();
                        key = FirebaseAuth.getInstance().getCurrentUser().getUid();
                        databaseReference.child("Users2").addValueEventListener(new ValueEventListener() {
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
                                    startActivity(data);
                                }
                                else{

                                    currentUser = FirebaseAuth.getInstance().getCurrentUser();
                                    String uid = currentUser.getUid();
                                    //Hier beginnen wir, das Profil anzulegen und langsam zu befüllen
                                    UserProfile currentProfile = new UserProfile(uid);

                                    currentProfile.setEmail(mail);

                                    databaseReference = FirebaseDatabase.getInstance().getReference().child("Users2").child(uid);


                                    progressDialog.dismiss();
                                    emailtext = email.getText().toString();
                                    key = FirebaseAuth.getInstance().getCurrentUser().getUid();
                                    Toast.makeText(MainActivity.this, "Created Account", Toast.LENGTH_LONG).show();
                                    Intent dataFirstScreen = new Intent(MainActivity.this, SecondScreen.class);
                                    dataFirstScreen.putExtra("key", key);
                                    dataFirstScreen.putExtra("email", emailtext);
                                    //neuer ansatz
                                    dataFirstScreen.putExtra("userMail", mail);
                                    dataFirstScreen.putExtra("userID", uid);
                                    startActivity(dataFirstScreen);
                                    finish();

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

        databaseReference.child("Users2").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                Iterable<DataSnapshot> children = dataSnapshot.getChildren();

                String key = FirebaseAuth.getInstance().getCurrentUser().getUid();
                boolean exists = false;
                for (DataSnapshot child : children) {
                    if (child.getKey().equals(key)) {
                        exists = true;

                    }
                }
                if (exists) {
                    progressDialog.dismiss();
                    Intent data = new Intent(MainActivity.this, MainScreen.class);
                    startActivity(data);
                }else{
                    progressDialog.dismiss();
                    Toast.makeText(MainActivity.this, "Please Login and finish your account creation", Toast.LENGTH_LONG).show();
                }
            }


            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }


    public void checkButtons(){
        if (!(TextUtils.isEmpty(email.getText().toString()) || password.getText().toString().length() < 6 )){
            login.setEnabled(true);
            signup.setEnabled(true);
        }
        else{
            login.setEnabled(false);
            signup.setEnabled(false);

        }

    }


    public void startSignIn(String emailStr, String passwordStr){

        final String mail = emailStr;
        String pw = passwordStr;
        if (!TextUtils.isEmpty(mail) && !TextUtils.isEmpty(pw)){
            auth.createUserWithEmailAndPassword(mail, pw).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (!task.isSuccessful()){



                        progressDialog.hide();
                        Toast.makeText(MainActivity.this, "this email is already used", Toast.LENGTH_LONG).show();
                    }else {


                        currentUser = FirebaseAuth.getInstance().getCurrentUser();
                        String uid = currentUser.getUid();
                        //Hier beginnen wir, das Profil anzulegen und langsam zu befüllen
                        UserProfile currentProfile = new UserProfile(uid);

                        currentProfile.setEmail(mail);

                        databaseReference = FirebaseDatabase.getInstance().getReference().child("Users2").child(uid);

                        progressDialog.dismiss();
                        emailtext = email.getText().toString();
                        key = FirebaseAuth.getInstance().getCurrentUser().getUid();
                        Toast.makeText(MainActivity.this, "Created Account", Toast.LENGTH_LONG).show();
                        Intent dataFirstScreen = new Intent(MainActivity.this, SecondScreen.class);
                        dataFirstScreen.putExtra("key", key);
                        dataFirstScreen.putExtra("email", emailtext);
                        //neuer ansatz
                        dataFirstScreen.putExtra("userMail", mail);
                        dataFirstScreen.putExtra("userID", uid);

                        startActivity(dataFirstScreen);
                        finish();
                    }
                }
            });

        }
    }

}