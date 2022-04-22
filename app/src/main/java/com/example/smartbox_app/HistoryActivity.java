package com.example.smartbox_app;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldPath;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.protobuf.InvalidProtocolBufferException;

import java.lang.reflect.Array;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class HistoryActivity extends AppCompatActivity {

    private ListView deliveryList;
    private DeliveryAdapter mDeliveryAdapter;
    private ArrayList<Delivery> mDeliveryList;
    private static final String TAG ="HistoryActivity";
    private SwipeRefreshLayout refreshLayout;
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);
        refreshLayout = findViewById(R.id.swiperefresh);

        //refresh
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                onStart();
                refreshLayout.setRefreshing(false);
                Toast.makeText(HistoryActivity.this, "Delivery Updated", Toast.LENGTH_SHORT).show();
            }
        });

        //bottom nav
        BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottom_navigation);
        Menu menu = bottomNavigationView.getMenu();
        MenuItem menuItem = menu.getItem(1);
        menuItem.setChecked(true);

        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.dashboard:
                        Intent int1 = new Intent(HistoryActivity.this, MainActivity.class);
                        startActivity(int1);
                        break;
                    case R.id.history:
                        break;
                    case R.id.profile:
                        Intent int2 = new Intent(HistoryActivity.this, ProfileActivity.class);
                        startActivity(int2);
                        break;
                }
                return false;
            }
        });
    }

    public void onStart() {
        super.onStart();
        //initialise variables
        deliveryList = (ListView) findViewById(R.id.deliveryList);
        //set up adapter
        mDeliveryAdapter = new DeliveryAdapter(this, mDeliveryList);
        //setup array list
        mDeliveryList = new ArrayList<Delivery>();
        //get database
        db = FirebaseFirestore.getInstance();

        //Select "Deliveries" database from our firestore database
        db.collection("Deliveries")
                //order the entries with the field "id" by descending
                //latest deliveries will be shown at the top of the list
                .orderBy("id", Query.Direction.DESCENDING)
                //returns the value that adhere to criteria above
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        //creates an array for the list "mDeliveryList"
                        List<Delivery> mDeliveryList = new ArrayList<>();
                        //if values were returned from the database successfully
                        if (task.isSuccessful()){
                            for (QueryDocumentSnapshot document: task.getResult()){
                                //initialise entries to the "Delivery" class
                                Delivery del = document.toObject(Delivery.class);
                                //add entries to the previously created array
                                mDeliveryList.add(del);
                            }
                            //The adapter converts array list "mDeliveryAdapter" into list view items assigned for the History page
                            DeliveryAdapter mDeliveryAdapter = new DeliveryAdapter(HistoryActivity.this, mDeliveryList);
                            //When any of the data changes, this will notify the "mDeliveryAdapter" to reflect the changes
                            mDeliveryAdapter.notifyDataSetChanged();
                            //Associates our list view "deliveryList" with the array list "mDeliveryAdapter"
                            //The list view "deliveryList" will show the values from our database on the History page
                            deliveryList.setAdapter(mDeliveryAdapter);
                        }
                    }
                });
    }
}

