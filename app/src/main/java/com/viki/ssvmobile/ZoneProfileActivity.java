package com.viki.ssvmobile;

import android.app.ProgressDialog;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;
import android.support.v7.widget.Toolbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ZoneProfileActivity extends AppCompatActivity {

    FirebaseAuth mAuth;
    DatabaseReference dbRef;
    String zone;
    TextView zoneEmail;
    TextView sName,sContact;
    ProgressDialog progress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_zone_profile);

        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Zone Details");
        setSupportActionBar(toolbar);

        progress = new ProgressDialog(ZoneProfileActivity.this);
        progress.setMessage("Please Wait");
        progress.setTitle("Updating..");
        progress.setCancelable(false);
        progress.show();

        mAuth = FirebaseAuth.getInstance();
        zone = mAuth.getCurrentUser().getEmail().substring(4,10);
        dbRef = FirebaseDatabase.getInstance().getReference("zoneDetails");

        zoneEmail = findViewById(R.id.zoneEmail);
        sName = findViewById(R.id.sName);
        sContact = findViewById(R.id.sContact);

        dbRef.child(zone).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                zoneEmail.append(dataSnapshot.child("sEmail").getValue().toString());
                sName.append(dataSnapshot.child("sName").getValue().toString());
                sContact.append(dataSnapshot.child("sContact").getValue().toString());
                progress.dismiss();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
