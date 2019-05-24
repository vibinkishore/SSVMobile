package com.viki.ssvmobile;

import android.app.ProgressDialog;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ExistingZonesActivity extends AppCompatActivity {

    FirebaseDatabase database;
    DatabaseReference myRef ;
    ArrayList<String> arrayList = new ArrayList<>();
    ArrayAdapter<String> arrayAdapter;
    ListView zonesList;
    ProgressDialog progress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_existing_zones);

        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Existing Zones");
        setSupportActionBar(toolbar);

        zonesList = findViewById(R.id.zonesList);
        arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,arrayList);
        zonesList.setAdapter(arrayAdapter);
        database = FirebaseDatabase.getInstance();
        myRef = database.getReference("zoneDetails");
        progress = new ProgressDialog(ExistingZonesActivity.this);
        progress.setTitle("Please Wait");
        progress.setMessage("Loading");
        progress.show();

        displayContent();
    }

    private boolean displayContent() {
        myRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                String sName = dataSnapshot.child("sName").getValue().toString();
                String sContact = dataSnapshot.child("sContact").getValue().toString();
                String sEmail = dataSnapshot.child("sEmail").getValue().toString();
                String zoneCode = dataSnapshot.child("sZone").getValue().toString();

                arrayList.add("ZONE: \t\t" + zoneCode + "\n\nSTAFF NAME: \t\t" + sName
                        + "\nEMAIL ID: \t\t" + sEmail + "\nCONTACT: \t\t" + sContact + "\n");
                arrayAdapter.notifyDataSetChanged();
                progress.dismiss();
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
        return true;
    }
}
