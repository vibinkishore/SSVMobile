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
import android.widget.TextView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class AdminStaffUpdatesActivity extends AppCompatActivity {

    ListView viewExpenses;
    Button fetchUpd;
    ArrayList<String> zonesinDb;
    Spinner selectZone;
    String zone;
    FirebaseDatabase firebaseDb;
    DatabaseReference dbRef;
    ArrayList<String>arrayList=new ArrayList<>();
    ArrayAdapter<String> arrayAdapter;
    ProgressDialog progress;
    TextView changeHeader;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_staff_updates);

        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Staff Updates");
        setSupportActionBar(toolbar);

        fetchUpd = findViewById(R.id.fetchExpenses);
        selectZone = findViewById(R.id.selectZone);
        viewExpenses = findViewById(R.id.expensesview);
        changeHeader = findViewById(R.id.Expheader);
        progress = new ProgressDialog(AdminStaffUpdatesActivity.this);
        progress.setMessage("Please Wait");
        progress.setTitle("Loading..");
        progress.setCancelable(false);
        progress.show();

        firebaseDb = FirebaseDatabase.getInstance();
        dbRef = firebaseDb.getReference();

        inflateZones();
        ArrayAdapter<String> zoneAdapter = new ArrayAdapter<>(this,android.R.layout.simple_spinner_item,zonesinDb);
        zoneAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        selectZone.setAdapter(zoneAdapter);

        arrayAdapter=new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,arrayList);
        viewExpenses.setAdapter(arrayAdapter);

        fetchUpd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                zone = selectZone.getSelectedItem().toString();
                fetchExpenses(zone);
            }
        });
    }

    private boolean fetchExpenses(String selectedZone) {
        String temp;
        temp = "Staff Updates For (" + selectedZone + ")";
        changeHeader.setText(temp);
        dbRef.child(selectedZone).child("dailyEvents").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                if (dataSnapshot.exists()) {
                    for(DataSnapshot ds:dataSnapshot.getChildren()) {
                        for(DataSnapshot postdata:ds.getChildren()) {
                            arrayList.add("Work Done: " + postdata.getValue(String.class) + "\n\nUpdated on " + dataSnapshot.getKey() + " at " + ds.getKey());
                            arrayAdapter.notifyDataSetChanged();
                        }
                    }
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
                progress.dismiss();
            }
        });
        return true;
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
