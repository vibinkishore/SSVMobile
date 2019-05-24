package com.viki.ssvmobile;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
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
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class AdminMonthlyExpensesActivity extends AppCompatActivity {

    Spinner datePairs;
    ArrayList<String> zonesinDb;
    Spinner selectZone;
    String zoneVal;
    ArrayList<String> dPairs=new ArrayList<>();
    ArrayAdapter<String> arrayAdapter;
    ArrayList<String> arrayList=new ArrayList<>();
    ArrayAdapter<String> arrayAdapter2;
    TextView mTotal;
    int mSum = 0;
    ProgressDialog progress;
    Button getMRec,selZone;
    ListView exp;
    FirebaseDatabase firebaseDb;
    DatabaseReference dbRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_monthly_expenses);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        datePairs = findViewById(R.id.availablePairs);
        mTotal = findViewById(R.id.monthlyTotal);
        exp = findViewById(R.id.mExp);
        selZone = findViewById(R.id.selZone);
        selectZone = findViewById(R.id.selectZone);
        getMRec = findViewById(R.id.viewMonthlyRec);

        progress = new ProgressDialog(AdminMonthlyExpensesActivity.this);
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

        selZone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progress.show();
                zoneVal = selectZone.getSelectedItem().toString();
                getMRec.setEnabled(true);
                getDatePairs();
            }
        });

        arrayAdapter=new ArrayAdapter<>(this, android.R.layout.simple_list_item_1,dPairs);
        datePairs.setAdapter(arrayAdapter);

        arrayAdapter2=new ArrayAdapter<>(this, android.R.layout.simple_list_item_1,arrayList);
        exp.setAdapter(arrayAdapter2);

        getMRec.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mSum = 0;
                arrayAdapter2.clear();
                arrayAdapter2.notifyDataSetChanged();
                String item = (String) datePairs.getSelectedItem();
                fetchMonthExpenses(item);
            }
        });
    }

    private void fetchMonthExpenses(final String item) {
        dbRef.child("zone01").child("dailyExpenses").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {


                if (dataSnapshot.exists()) {
                    for(DataSnapshot ds:dataSnapshot.getChildren())
                    {
                        for(DataSnapshot postdata:ds.getChildren())
                        {
                            if (dataSnapshot.getKey().substring(3).equals(item)) {
                                int tempAmt = Integer.parseInt(postdata.getValue(String.class));
                                mSum = mSum + tempAmt;
                                arrayList.add("Expense: "+postdata.getKey()+"\nAmount: "+ postdata.getValue(String.class) + "\n\nPosted on " + dataSnapshot.getKey() + " at " + ds.getKey());
                                arrayAdapter2.notifyDataSetChanged();
                            }
                        }
                    }
                } else {
                    Toast.makeText(AdminMonthlyExpensesActivity.this, "No Data", Toast.LENGTH_SHORT).show();
                }
                mTotal.setText("Monthly Total: ₹ " + mSum);
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

    private void getDatePairs() {
        dPairs.add("Date Range");
        dbRef.child("zone01").child("dailyExpenses").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {


                if (dataSnapshot.exists()) {
                    for(DataSnapshot ds:dataSnapshot.getChildren())
                    {
                        String pair = dataSnapshot.getKey().substring(3);
                        dPairs.add(pair);
                        arrayAdapter.notifyDataSetChanged();
                    }
                    progress.dismiss();
                    removeDuplicates();
                } else {
                    Toast.makeText(AdminMonthlyExpensesActivity.this, "No Data", Toast.LENGTH_SHORT).show();
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
    }

    private void removeDuplicates() {
        Set<String> set = new HashSet<>(dPairs);
        dPairs.clear();
        dPairs.addAll(set);
    }
}
