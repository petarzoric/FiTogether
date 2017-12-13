package com.petarzoric.fitogether;

import android.app.ProgressDialog;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class StatusActivity extends AppCompatActivity {

   // private Toolbar toolbar;

    private TextInputLayout statusInput;
    private Button saveButton;
    //FIREBASE

    private DatabaseReference statusDatabase;
    private FirebaseUser currentUser;

    //PROGRESS
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_status);
        /*
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Account Status");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        */

        //FIREBASE
        currentUser = FirebaseAuth.getInstance().getCurrentUser();
        String current_uid = currentUser.getUid();
        statusDatabase = FirebaseDatabase.getInstance().getReference().child("Users2").child(current_uid);




        saveButton = (Button) findViewById(R.id.statusSaveButton);
        statusInput = (TextInputLayout) findViewById(R.id.statusInput);

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //PROGRESS
                progressDialog = new ProgressDialog(getApplicationContext());
                progressDialog.setTitle("Saving Changes");
                progressDialog.setMessage("Please wait while we save the changes");
                progressDialog.show();


                String status = statusInput.getEditText().getText().toString();
                statusDatabase.child("status").setValue(status).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            progressDialog.dismiss();
                        } else {

                            Toast.makeText(getApplicationContext(), "Error in savong changes", Toast.LENGTH_LONG);
                        }

                    }
                });

            }
        });




    }
}
