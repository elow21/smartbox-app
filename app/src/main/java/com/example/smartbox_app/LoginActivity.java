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
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private EditText email,password;
    private Button btnlogin;
    private Button btnsignup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mAuth = FirebaseAuth.getInstance();
        email = findViewById(R.id.login_email);
        password = findViewById(R.id.login_pass);
        btnlogin = findViewById(R.id.btnlogin);
        btnsignup = findViewById(R.id.btnsignup);

        btnlogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                login();
            }
        });

        btnsignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
            }
        });
    }
    private void login() {
        //initialise the input fields for email and password
        String user = email.getText().toString().trim();
        String pass = password.getText().toString().trim();
        //if either fields are empty, an error message will be shown
        if(user.isEmpty()){
            email.setError("Email field can not be empty!");
        }
        if(pass.isEmpty()){
            password.setError("Password field can not be empty!");
        }
        //if both fields are valid start authentication with firebase
        else{
            //use the email and password from the input field to sign in with firebase authentication
            mAuth.signInWithEmailAndPassword(user,pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    //if user sign in is successful
                    if(task.isSuccessful()){
                        //A pop up message will be shown
                        Toast.makeText(LoginActivity.this, "Login Successful! ", Toast.LENGTH_SHORT).show();
                        //Direct users to their dashboard
                        startActivity(new Intent(LoginActivity.this, MainActivity.class));
                    }
                    //if user sign in failed
                    else{
                        //A pop up message will be shown with the relevant error code or message
                        Toast.makeText(LoginActivity.this, "Login Failed. " + task.getException().getMessage(),Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }
}