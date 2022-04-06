package com.example.smartbox_app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.messaging.FirebaseMessaging;

import java.util.ArrayList;
import java.util.Calendar;

public class MainActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private BarChart barChart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        barChart = findViewById(R.id.barchart);
        mAuth = FirebaseAuth.getInstance();

        //get device token for cloud messaging service
        FirebaseMessaging.getInstance().getToken().addOnCompleteListener(new OnCompleteListener<String>() {
            @Override
            public void onComplete(@NonNull Task<String> task) {
                if (!task.isSuccessful()) {
                    System.out.println("Fetching FCM registration token failed");
                    return;
                }
                // Get new FCM registration token
                String token = task.getResult();
                sendRegistrationToServer(token);
                // Log token to console
                System.out.println(token);
            }
        });

        //barchart call
        loadBarChartData();
        //bottom nav
        BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottom_navigation);
        Menu menu = bottomNavigationView.getMenu();
        MenuItem menuItem = menu.getItem(0);
        menuItem.setChecked(true);

        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.dashboard:
                        break;
                    case R.id.history:
                        Intent int1 = new Intent(MainActivity.this, HistoryActivity.class);
                        startActivity(int1);
                        break;
                    case R.id.profile:
                        Intent int2 = new Intent(MainActivity.this, ProfileActivity.class);
                        startActivity(int2);
                        break;
                }
                return false;
            }
        });
    }

    //On app launch check if user is logged in if not start LoginActivity
    @Override
    public void onStart(){
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser==null){
            startActivity(new Intent(MainActivity.this, LoginActivity.class));
        }
    }

    //barchart data load
    private void loadBarChartData(){
        ArrayList<BarEntry> entries = new ArrayList<>();
        entries.add(new BarEntry(0f, 2f));
        entries.add(new BarEntry(1f, 3f));
        entries.add(new BarEntry(2f, 4f));
        entries.add(new BarEntry(3f, 1f));

        ArrayList<Integer> colors = new ArrayList<>();
        for(int color: ColorTemplate.MATERIAL_COLORS){
            colors.add(color);
        }

        for (int color: ColorTemplate.VORDIPLOM_COLORS){
            colors.add(color);
        }

        BarDataSet set = new BarDataSet(entries, "Deliveries");
        set.setColors(colors);
        BarData data = new BarData(set);
        data.setBarWidth(0.9f); // set custom bar width
        barChart.setData(data);
        barChart.setFitBars(true); // make the x-axis fit exactly all bars
        barChart.invalidate(); // refresh
    }

    //send token to Firebase server
    private void sendRegistrationToServer(String token) {
        // TODO: Implement this method to send token to your app server.
    }


}