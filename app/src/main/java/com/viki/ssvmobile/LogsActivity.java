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

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class LogsActivity extends AppCompatActivity {
    ListView viewLog;

    String zone;
    FirebaseDatabase firebaseDb;
    DatabaseReference dbRef;
    ArrayList<String>arrayList=new ArrayList<>();
    ArrayAdapter<String> arrayAdapter;
    ProgressDialog progress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_logs);

        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Staff Logs");
        setSupportActionBar(toolbar);
        viewLog = findViewById(R.id.logView);

        progress = new ProgressDialog(LogsActivity.this);
        progress.setTitle("Please Wait");
        progress.setMessage("Loading");
        progress.show();

        firebaseDb = FirebaseDatabase.getInstance();
        dbRef = firebaseDb.getReference();


        arrayAdapter=new ArrayAdapter<>(this, android.R.layout.simple_list_item_1,arrayList);
        viewLog.setAdapter(arrayAdapter);

        zone = FirebaseAuth.getInstance().getCurrentUser().getEmail().substring(4,10);
        fetchExpenses(zone);
    }

    private void fetchExpenses(String selectedZone) {

        dbRef.child(selectedZone).child("log").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                if (dataSnapshot.exists()) {
                    for(DataSnapshot ds:dataSnapshot.getChildren())
                    {
                        for(DataSnapshot postdata:ds.getChildren())
                        {

                            arrayList.add("Module: "+postdata.getKey()+"\nChange: "+ postdata.getValue(String.class) + "\n\nPosted on " + dataSnapshot.getKey() + " at " + ds.getKey());
                            arrayAdapter.notifyDataSetChanged();
                            progress.dismiss();

                        }

                    }
                } else {
                    Toast.makeText(LogsActivity.this, "No Data", Toast.LENGTH_SHORT).show();
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
