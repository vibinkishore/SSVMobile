package com.viki.ssvmobile;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;

import static  com.viki.ssvmobile.Appinfo.CHANNEL_1_ID;
import static  com.viki.ssvmobile.Appinfo.CHANNEL_2_ID;

public class Remainder extends AppCompatActivity {
    ArrayList<String> arrayList=new ArrayList<>();
    ArrayAdapter<String> arrayAdapter;
    ListView listView;
    DatabaseReference databaseReference;
    FirebaseDatabase firebaseDatabase;
    private NotificationManagerCompat notificationManager;
    String zone;
    String formateddate;
    String taskday,fday;
    String taskmonth,fmonth;
    ProgressDialog progress;
    String taskyear,fyear;
    int d,m,y,td,tm,ty;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_remainder);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        progress = new ProgressDialog(Remainder.this);
        progress.setMessage("Please Wait");
        progress.setTitle("Updating..");
        progress.setCancelable(false);
        progress.show();

        notificationManager = NotificationManagerCompat.from(this);
        @SuppressLint("SimpleDateFormat") final SimpleDateFormat df=new SimpleDateFormat("dd/MM/yyyy hh:mm:ss");
        Calendar c=Calendar.getInstance();
        formateddate=df.format(c.getTime());
        fday=formateddate.substring(0,2);
        fmonth=formateddate.substring(3,5);
        fyear=formateddate.substring(6,10);

        zone = FirebaseAuth.getInstance().getCurrentUser().getEmail().substring(4,10);

        d=Integer.parseInt(fday);
        m=Integer.parseInt(fmonth);
        y=Integer.parseInt(fyear);


        listView=findViewById(R.id.activities);
        arrayAdapter=new ArrayAdapter<>(this, android.R.layout.simple_list_item_1,arrayList);
        listView.setAdapter(arrayAdapter);
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getInstance().getReference(zone + "/milestones");


        databaseReference.addChildEventListener(new ChildEventListener() {


            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                if (dataSnapshot.exists()) {
                    for(DataSnapshot ds:dataSnapshot.getChildren()) {

                        for(DataSnapshot postdata:ds.getChildren()) {

                            taskday= dataSnapshot.getKey().substring(0,2);
                            taskmonth=dataSnapshot.getKey().substring(3,5);
                            taskyear=dataSnapshot.getKey().substring(6,10);

                            td=Integer.parseInt(taskday);
                            tm=Integer.parseInt(taskmonth);
                            ty=Integer.parseInt(taskyear);


                            if(m==tm ||td<=d ){

                                arrayList.add(postdata.getValue(String.class) + "\n\nPosted on: " + dataSnapshot.getKey() + " at " + ds.getKey());
                                arrayAdapter.notifyDataSetChanged();
                                notification(dataSnapshot.getKey());
                            }

                            else if(td>d && tm>m && ty<y)
                            {

                                arrayList.add(postdata.getValue(String.class) + "\n\nPosted on: " + dataSnapshot.getKey() + " at " + ds.getKey());
                                arrayAdapter.notifyDataSetChanged();
                                notification(dataSnapshot.getKey());
                            }
                            else
                            {


                                Toast.makeText(getApplicationContext(),"No task to show",Toast.LENGTH_LONG).show();
                            }

                            progress.dismiss();
                        }

                    }
                } else {
                    Toast.makeText(Remainder.this, "No Data", Toast.LENGTH_SHORT).show();
                    progress.dismiss();
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
    public void notification(String name) {

        Random random = new Random();
        int m = random.nextInt(9999 - 1000) + 1000;
        Notification notification = new NotificationCompat.Builder(this, CHANNEL_1_ID)
                .setSmallIcon(R.drawable.logo)
                .setContentTitle("Pending Update")
                .setContentText(name + " task is pending")
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setCategory(NotificationCompat.CATEGORY_MESSAGE)
                .build();

        notificationManager.notify(m, notification);
    }
}
