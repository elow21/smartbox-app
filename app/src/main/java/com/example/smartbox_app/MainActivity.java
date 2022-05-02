package com.example.smartbox_app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextClock;
import android.widget.TextView;
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
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.messaging.FirebaseMessaging;

import org.w3c.dom.Text;

import java.lang.reflect.Array;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.RandomAccess;

public class MainActivity extends AppCompatActivity {

    private static final String TAG ="MainActivity";
    private FirebaseAuth mAuth;
    private TextView timestamp;
    private TextView todayview;
    BarChart mybarchart;

    FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        BarChart mybarchart = findViewById(R.id.barchart);
        timestamp = findViewById(R.id.timestamp);
        todayview = findViewById(R.id.todayview);
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
            }
        });

        //Subscribe to specific topic from created the cloud function
        FirebaseMessaging.getInstance().subscribeToTopic("PushNotifications");

        //Show date and time
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        String currentDateandTime = sdf.format(new Date());
        timestamp.setText("Current time is " + currentDateandTime);

        //show deliveries today
        db.collection("TodayView")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                String amount = document.getData().get("value").toString();
                                todayview.setText("You have " + amount + " deliveries today!");
                            }
                        }
                    }
                });

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
        BarChart mybarchart = findViewById(R.id.barchart);
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser==null){
            startActivity(new Intent(MainActivity.this, LoginActivity.class));
        }

        //get chart data from firestore database
        db.collection("ChartData")
                .orderBy("id", Query.Direction.ASCENDING)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        //Creates an array which will store values from each document
                        List<String> valueList = new ArrayList<>();
                        //if values were return successfully from database
                        if(task.isSuccessful()) {
                            //Loops until every document snapshot has been read
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                //Gets the value from "value" field in our database
                                String value = document.getData().get("value").toString();
                                //Adds the data obtained into the valueList array
                                valueList.add(value);
                            }
                            //Creates an the "entries" array which will store chart data
                            ArrayList<BarEntry> entries = new ArrayList<>();
                            //Loops until the latest 5 entries are added to the barchart
                            for (int i = 0; i < 5; i++){
                                //Add bar entries from index 0-4, with their respective value from valueList
                                entries.add(new BarEntry(i, Float.parseFloat(valueList.get(i))));
                            }
                                //sets the "entries" array as the dataset to be used for our barchart
                                BarDataSet barDataSet = new BarDataSet(entries, "Daily deliveries");
                                BarData data = new BarData(barDataSet);
                                mybarchart.setData(data);

                                //Bar Chart configurations for legends,axis,and colours
                                barDataSet.setColors(ColorTemplate.COLORFUL_COLORS);
                                barDataSet.setValueTextSize(12f);
                                mybarchart.getDescription().setEnabled(false);
                                mybarchart.getXAxis().setEnabled(false);
                                mybarchart.getXAxis().setDrawGridLines(false);
                                mybarchart.getAxisLeft().setDrawGridLines(false);
                                mybarchart.getAxisRight().setEnabled(false);
                                mybarchart.getAxisRight().setDrawGridLines(false);
                                mybarchart.getLegend().setEnabled(false);
                        }
                        //enables the barchart to automatically generate on start
                        mybarchart.invalidate();
                        mybarchart.refreshDrawableState();
                    }
                });
    }


}