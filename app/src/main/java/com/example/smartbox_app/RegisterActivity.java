package com.example.smartbox_app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseError;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class RegisterActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private EditText email, password;
    private Button btnsignup;
    private Button btnlogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mAuth = FirebaseAuth.getInstance();
        email = findViewById(R.id.signup_email);
        password = findViewById(R.id.signup_pass);
        btnsignup = findViewById(R.id.btnsignup);
        btnlogin = findViewById(R.id.btnlogin);

        btnsignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Register();
            }
        });

        btnlogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
            }
        });
    }

    private void Register() {
        //initialises input fields as email and password
        String user = email.getText().toString().trim();
        String pass = password.getText().toString().trim();
        //if either input fields are empty, an error message will be shown
        if(user.isEmpty()){
            email.setError("Email field can not be empty! ");
        }
        if(pass.isEmpty()){
            password.setError("Password field can not be empty! ");
        }
        //if both input fields are filled in, create a new user
        else{
            //mAuth utilises firebase authentication to create a new user entry
            mAuth.createUserWithEmailAndPassword(user,pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                //once a new user entry has been created on firebase, display the results
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    //if a new user entry is created successfully
                    if(task.isSuccessful()){
                        //A pop up message will be shown
                        Toast.makeText(RegisterActivity.this, "Signed Up successfully. ", Toast.LENGTH_SHORT).show();
                        //Direct users to the dashboard page
                        startActivity(new Intent(RegisterActivity.this, MainActivity.class));
                    }
                    //if creating a new user entry is unsuccessful
                    else{
                        //A pop up message will be shown with the relevant error code or message
                        Toast.makeText(RegisterActivity.this, "Sign up failed. Please try again. "+task.getException().getMessage(),Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }
}