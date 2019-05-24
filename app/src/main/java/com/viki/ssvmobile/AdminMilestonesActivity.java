package com.viki.ssvmobile;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
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

public class AdminMilestonesActivity extends AppCompatActivity {

    TextInputLayout milestoneContent;
    Spinner selectZone;
    Button addMilestone,viewExisting;
    ArrayList<String> arrayList = new ArrayList<>();
    ArrayAdapter<String> arrayAdapter;
    ListView listView;
    String formateddate;
    String taskday,fday;
    String taskmonth,fmonth;
    String taskyear,fyear;
    int d,m,y,td,tm,ty;
    String milestoneValue,selectedZone;
    ArrayList<String> zonesinDb;
    FirebaseDatabase firebaseRef;
    DatabaseReference dbRef,listRef;
    HashMap<String,String> newMilestone;
    ProgressDialog progress;
    TextView milestoneHeader;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_milestones);

        android.support.v7.widget.Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        progress = new ProgressDialog(AdminMilestonesActivity.this);
        progress.setMessage("Please Wait");
        progress.setTitle("Loading..");
        progress.setCancelable(false);
        progress.show();

        dbRef = firebaseRef.getInstance().getReference();

        milestoneContent = findViewById(R.id.milestoneDesc);
        selectZone = findViewById(R.id.selectZone);
        addMilestone = findViewById(R.id.addMilestone);
        viewExisting = findViewById(R.id.viewMilestones);
        listView = findViewById(R.id.userMilestones);
        milestoneHeader = findViewById(R.id.milestoneHeader);

        inflateZones();

        @SuppressLint("SimpleDateFormat") final SimpleDateFormat df=new SimpleDateFormat("dd/MM/yyyy hh:mm:ss");
        Calendar c=Calendar.getInstance();
        formateddate=df.format(c.getTime());
        fday = formateddate.substring(0,2);
        fmonth = formateddate.substring(3,5);
        fyear = formateddate.substring(6,10);

        d = Integer.parseInt(fday);
        m = Integer.parseInt(fmonth);
        y = Integer.parseInt(fyear);

        ArrayAdapter<String> zoneAdapter = new ArrayAdapter<>(this,android.R.layout.simple_spinner_item,zonesinDb);
        zoneAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        selectZone.setAdapter(zoneAdapter);

        arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,arrayList);
        listView.setAdapter(arrayAdapter);
        listRef = FirebaseDatabase.getInstance().getReference();

        addMilestone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                milestoneValue = milestoneContent.getEditText().getText().toString();
                selectedZone = selectZone.getSelectedItem().toString();
                Calendar c=Calendar.getInstance();
                @SuppressLint("SimpleDateFormat") final SimpleDateFormat df=new SimpleDateFormat("dd-MM-yyyy");
                String date = df.format(c.getTime());
                DateFormat datef = new SimpleDateFormat("HH:mm:ss");
                String time = datef.format(Calendar.getInstance().getTime());

                if (milestoneValue.isEmpty()) {
                    milestoneContent.setError("Enter Value");

                } else {
                    newMilestone = new HashMap<>();
                    newMilestone.put("work",milestoneValue);
                    dbRef.child(selectedZone).child("milestones").child(date + "/" + time).setValue(newMilestone).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {

                        }
                    });
                }
            }
        });

        viewExisting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String temp;
                selectedZone = selectZone.getSelectedItem().toString();
                arrayList.clear();
                listRef.child(selectedZone).child("milestones").addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                        for(DataSnapshot ds:dataSnapshot.getChildren()) {
                            for(DataSnapshot postdata:ds.getChildren()) {

                                taskday= dataSnapshot.getKey().substring(0,2);
                                taskmonth=dataSnapshot.getKey().substring(3,5);
                                taskyear=dataSnapshot.getKey().substring(6,10);

                                td=Integer.parseInt(taskday);
                                tm=Integer.parseInt(taskmonth);
                                ty=Integer.parseInt(taskyear);

                                if(m==tm ||td<=d ) {
                                    arrayList.add(postdata.getValue(String.class) + "\n\nPosted on: " + dataSnapshot.getKey() + " at " + ds.getKey());
                                    arrayAdapter.notifyDataSetChanged();
                                }
                                else if(td>d && tm>m && ty<y) {
                                    arrayList.add(postdata.getValue(String.class) + "\n\nPosted on: " + dataSnapshot.getKey() + " at " + ds.getKey());
                                    arrayAdapter.notifyDataSetChanged();
                                }
                                else {
                                    Toast.makeText(getApplicationContext(),"No task to show",Toast.LENGTH_LONG).show();
                                }
                            }

                        }
                    }
                    @Override
                    public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {}
                    @Override
                    public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {}
                    @Override
                    public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {}
                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {}
                });
                temp = "MILESTONES (" + selectedZone.toUpperCase() + ")";
                milestoneHeader.setText(temp);
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
