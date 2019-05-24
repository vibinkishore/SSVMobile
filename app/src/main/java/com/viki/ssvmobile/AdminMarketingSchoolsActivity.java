package com.viki.ssvmobile;

import android.app.ProgressDialog;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
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

public class AdminMarketingSchoolsActivity extends AppCompatActivity {

    FirebaseDatabase database;
    DatabaseReference dbRef ;
    Button viewMS;
    String zone;
    ListView dispMS;
    ArrayList<String> arrayList=new ArrayList<>();
    ArrayAdapter<String> arrayAdapter;
    ArrayList<String> zonesinDb;
    ProgressDialog progress;
    Spinner selectZone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_marketing_schools);

        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Marketing Schools");
        setSupportActionBar(toolbar);

        progress = new ProgressDialog(AdminMarketingSchoolsActivity.this);
        progress.setMessage("Please Wait");
        progress.setTitle("Loading..");
        progress.setCancelable(false);
        progress.show();

        database = FirebaseDatabase.getInstance();
        dbRef = database.getReference();

        viewMS = findViewById(R.id.fetchMS);
        dispMS = findViewById(R.id.dispMarketingSchools);
        selectZone = findViewById(R.id.selectZone);

        inflateZones();
        ArrayAdapter<String> zoneAdapter = new ArrayAdapter<>(this,android.R.layout.simple_spinner_item,zonesinDb);
        zoneAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        selectZone.setAdapter(zoneAdapter);

        arrayAdapter=new ArrayAdapter<>(this, android.R.layout.simple_list_item_1,arrayList);
        dispMS.setAdapter(arrayAdapter);


        viewMS.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!selectZone.getSelectedItem().toString().equals("Select Zone")) {
                    fetchMS(selectZone.getSelectedItem().toString());
                } else {
                    Toast.makeText(AdminMarketingSchoolsActivity.this, "Select a Zone", Toast.LENGTH_SHORT).show();
                }

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

    private void fetchMS(String zone) {
        dbRef.child(zone).child("marketingSchools").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                if (dataSnapshot.exists()) {
                    for(DataSnapshot ds:dataSnapshot.getChildren())
                    {
                        arrayList.add("School Name: "+ ds.child("msName").getValue(String.class)
                                + "\nContact Person: " + ds.child("msContactPerson").getValue(String.class)
                                + "\nMobile: " + ds.child("msContactNumber").getValue(String.class)
                                + "\nEmail: " + ds.child("msContactEmail").getValue(String.class)
                                + "\nRemarks: "+ ds.child("msRemarks").getValue(String.class)
                                + "\n\nPosted on " + dataSnapshot.getKey() + " at " + ds.getKey()+ "\n");
                        arrayAdapter.notifyDataSetChanged();

                    }
                } else {
                    Toast.makeText(AdminMarketingSchoolsActivity.this, "No Data", Toast.LENGTH_SHORT).show();
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
            public void onCancelled(@NonNull DatabaseError databaseError) {}
        });
    }
}
