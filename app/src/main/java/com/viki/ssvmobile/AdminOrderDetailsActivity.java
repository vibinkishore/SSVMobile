package com.viki.ssvmobile;

import android.app.ProgressDialog;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class AdminOrderDetailsActivity extends AppCompatActivity {

    ListView ordersData;
    Button fetchOrderData;
    ArrayList<String> zonesinDb;
    Spinner selectZone;
    String zone;
    String orderid,schoolName,orderStatus,paymentStatus,dateCreated;
    FirebaseDatabase firebaseDb;
    DatabaseReference dbRef;
    ProgressDialog progress;
    ArrayList<String>arrayList=new ArrayList<>();
    ArrayAdapter<String> arrayAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_order_details);

        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("SSV Orders");
        setSupportActionBar(toolbar);

        progress = new ProgressDialog(AdminOrderDetailsActivity.this);
        progress.setTitle("Please Wait");
        progress.setMessage("Loading");
        progress.show();

        fetchOrderData = findViewById(R.id.fetchOrders);
        selectZone = findViewById(R.id.selectZone);
        ordersData = findViewById(R.id.ordersView);

        firebaseDb = FirebaseDatabase.getInstance();
        dbRef = firebaseDb.getReference();

        inflateZones();
        ArrayAdapter<String> zoneAdapter = new ArrayAdapter<>(this,android.R.layout.simple_spinner_item,zonesinDb);
        zoneAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        selectZone.setAdapter(zoneAdapter);

        arrayAdapter=new ArrayAdapter<>(this, android.R.layout.simple_list_item_1,arrayList);
        ordersData.setAdapter(arrayAdapter);

        fetchOrderData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                zone = selectZone.getSelectedItem().toString();
                if (!zone.equals("Select Zone")) {
                    fetchExpenses(zone);
                } else {
                    Toast.makeText(AdminOrderDetailsActivity.this, "Select a Zone", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void fetchExpenses(String selectedZone) {

        dbRef.child(selectedZone).child("order").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot ds, @Nullable String s) {

                if (ds.exists()) {
                    schoolName = ds.child("basicinfo/orderschool").getValue(String.class);
                    orderid = ds.getKey();
                    orderStatus = ds.child("status/orderStatus").getValue(String.class);
                    paymentStatus = ds.child("paymentstatus/payStatus").getValue(String.class);
                    dateCreated = ds.child("basicinfo/orderDate").getValue(String.class);

                    arrayList.add("Order Id:\t" + orderid
                            + "\nSchool:\t" + schoolName
                            + "\nOrder Status:\t" + orderStatus
                            + "\nPayment Status:\t" + paymentStatus
                            + "\nOrder Date:\t" + dateCreated + "\n");
                    arrayAdapter.notifyDataSetChanged();
                } else {
                    Toast.makeText(AdminOrderDetailsActivity.this, "No Data", Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void inflateZones() {
        zonesinDb = new ArrayList<>();
        zonesinDb.add("Select Zone");
        ValueEventListener eventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot ds : dataSnapshot.getChildren()) {
                    String zone = ds.getKey();
                    zonesinDb.add(zone);
                    progress.dismiss();
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {}
        };
        dbRef.child("zone").addListenerForSingleValueEvent(eventListener);
    }
}
