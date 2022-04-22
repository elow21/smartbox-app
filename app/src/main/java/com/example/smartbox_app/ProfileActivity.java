package com.example.smartbox_app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class ProfileActivity extends AppCompatActivity {

    private Button btnlogout;
    private TextView email_text;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        btnlogout = findViewById(R.id.btnlogout);
        email_text = findViewById(R.id.email_text);
        //when a user clicks logout button call logout() function
        btnlogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                logout();
            }
        });

        //reset password function

        //bottom nav
        BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottom_navigation);
        Menu menu = bottomNavigationView.getMenu();
        MenuItem menuItem = menu.getItem(2);
        menuItem.setChecked(true);

        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.dashboard:
                        Intent int2 = new Intent(ProfileActivity.this, MainActivity.class);
                        startActivity(int2);
                        break;
                    case R.id.history:
                        Intent int1 = new Intent(ProfileActivity.this, HistoryActivity.class);
                        startActivity(int1);
                        break;
                    case R.id.profile:
                        break;
                }
                return false;
            }
        });

        //Get user email from Firebase authentication and show
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String email = user.getEmail();
        if (user!=null){
            //shows the email of the current user logged in
            email_text.setText(email);
        } else{
            Toast.makeText(ProfileActivity.this,"Fail", Toast.LENGTH_LONG).show();
        }


    }
    //logout() function
    public void logout(){
        FirebaseAuth.getInstance().signOut();
        startActivity(new Intent(ProfileActivity.this, LoginActivity.class));
    }



}