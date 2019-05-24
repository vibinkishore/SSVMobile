package com.viki.ssvmobile;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;

import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


import java.util.ArrayList;
import java.util.List;

public class MarketingSchoolsActivity extends AppCompatActivity {

    FirebaseDatabase database;
    DatabaseReference dbRef ;
    Button addRecord;
    String zone;
    ListView dispMS;
    ArrayList<String>arrayList=new ArrayList<>();
    ArrayAdapter<String> arrayAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_marketing_schools);

        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Marketing Schools");
        setSupportActionBar(toolbar);
        zone = FirebaseAuth.getInstance().getCurrentUser().getEmail().substring(4,10);

        database = FirebaseDatabase.getInstance();
        dbRef = database.getReference();
        fetchMS();
        dispMS = findViewById(R.id.dispMarketingSchools);
        arrayAdapter=new ArrayAdapter<>(this, android.R.layout.simple_list_item_1,arrayList);
        dispMS.setAdapter(arrayAdapter);

        addRecord = findViewById(R.id.addMSRecord);
        addRecord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),AddMarketingSchoolActivity.class));
            }
        });
    }

    private void fetchMS() {
        dbRef.child(zone).child("marketingSchools").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {


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
