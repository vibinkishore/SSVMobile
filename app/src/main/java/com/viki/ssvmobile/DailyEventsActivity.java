package com.viki.ssvmobile;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import android.widget.Toast;


import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseError;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DailyEventsActivity extends AppCompatActivity {

    EditText update;
    Button addUpdate;
    String formateddate;
    String time,zone;
    DatabaseReference dr;
    ListView todaysEvents;
    ArrayList<String>arrayList=new ArrayList<>();
    ArrayAdapter<String> arrayAdapter;
    ProgressDialog progress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_daily_events);

        zone = FirebaseAuth.getInstance().getCurrentUser().getEmail().substring(4,10);

        Calendar c=Calendar.getInstance();
        @SuppressLint("SimpleDateFormat") final SimpleDateFormat df=new SimpleDateFormat("dd-MM-yyyy");
        formateddate=df.format(c.getTime());

        progress = new ProgressDialog(DailyEventsActivity.this);


        progress.setMessage("while loading");
        progress.setTitle("Please wait ");
        progress.setCancelable(false);
        progress.show();

        update = findViewById(R.id.updateName);
        addUpdate = findViewById(R.id.addUpdate);
        todaysEvents=findViewById(R.id.addedevents);
        arrayAdapter=new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,arrayList);
        todaysEvents.setAdapter(arrayAdapter);
        arrayAdapter.notifyDataSetChanged();
        FirebaseDatabase firebaseDatabase=FirebaseDatabase.getInstance();
        dr=firebaseDatabase.getReference();

        dr.child(zone + "/dailyEvents").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                for(DataSnapshot ds:dataSnapshot.getChildren()) {
                    for(DataSnapshot postdata:ds.getChildren()) {
                        arrayList.add("Work Done: " + postdata.getValue(String.class) + "\n\nUpdated on " + dataSnapshot.getKey() + " at " + ds.getKey());
                        arrayAdapter.notifyDataSetChanged();
                    }
                }
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
        progress.dismiss();
    }



    public void OnDailyEventAddClick(View view) {

        HashMap<String,String> obj = new HashMap<>();
        HashMap<String,String> log = new HashMap<>();
        DateFormat datef = new SimpleDateFormat("HH:mm:ss");
        time = datef.format(Calendar.getInstance().getTime());
        String upVal = update.getText().toString();
        obj.put("update",upVal);

        if(update.getText().toString().isEmpty()) {
            update.setError("This field cannot be empty..");
        } else {
            progress.setMessage("Please Wait");
            progress.setTitle("Updating..");
            progress.setCancelable(false);
            progress.show();

            dr.child(zone + "/dailyEvents/" + formateddate + "/" + time).setValue(obj).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()) {
                        update.setText("");
                        Toast.makeText(DailyEventsActivity.this, "Success", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(DailyEventsActivity.this,HomeActivity.class));
                        progress.dismiss();
                    } else {
                        Toast.makeText(DailyEventsActivity.this, "Something went Wrong! Check your Network Connection", Toast.LENGTH_LONG).show();
                        progress.dismiss();
                    }
                }

            });
            log.put("dailyEvent","created | " + upVal);
            dr.child(zone + "/log/" + formateddate + "/" + time).setValue(log);
        }}
}
