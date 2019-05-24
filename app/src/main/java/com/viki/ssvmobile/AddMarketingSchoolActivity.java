package com.viki.ssvmobile;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class AddMarketingSchoolActivity extends AppCompatActivity {

    TextInputLayout msNameInp,msCPersonInp,msCMobileInp,msCEmailInp,msRemarksInp;
    String msName,msContactPerson,msContactNumber,msContactemail,msRemarks;
    String zone;
    FirebaseDatabase database;
    DatabaseReference dbRef;
    Button addLead;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_marketing_school);

        zone = FirebaseAuth.getInstance().getCurrentUser().getEmail().substring(4,10);

        database = FirebaseDatabase.getInstance();
        dbRef = database.getReference();

        msNameInp = findViewById(R.id.msNameInp);
        msCPersonInp = findViewById(R.id.msContactPersonInp);
        msCMobileInp = findViewById(R.id.msContactNumberInp);
        msCEmailInp = findViewById(R.id.msContactEmailInp);
        msRemarksInp = findViewById(R.id.msRemarksInp);
        addLead = findViewById(R.id.addLeadButton);

        addLead.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                msName = msNameInp.getEditText().getText().toString();
                msContactPerson = msCPersonInp.getEditText().getText().toString();
                msContactNumber = msCMobileInp.getEditText().getText().toString();
                msContactemail = msCEmailInp.getEditText().getText().toString();
                msRemarks = msRemarksInp.getEditText().getText().toString();

                Calendar c = Calendar.getInstance();
                @SuppressLint("SimpleDateFormat") final SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy");
                String tdydate = df.format(c.getTime());
                DateFormat datef = new SimpleDateFormat("HH:mm:ss");
                String time = datef.format(Calendar.getInstance().getTime());

                MarketingSchoolsModel mSchool = new MarketingSchoolsModel(msName,msContactPerson,msContactNumber,msContactemail,msRemarks);
                dbRef.child(zone).child("marketingSchools/" + tdydate).child(time).setValue(mSchool).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        startActivity(new Intent(AddMarketingSchoolActivity.this,MarketingSchoolsActivity.class));
                    }
                });
            }
        });
    }


}
