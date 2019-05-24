package com.viki.ssvmobile;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.database.annotations.NotNull;
import com.google.firebase.database.annotations.Nullable;

import java.util.ArrayList;

public class AdminSchoolDetails extends AppCompatActivity {

    AutoCompleteTextView sName;
    ArrayList<String> existingSchools;
    TextView ssvCode,ssv_schoolName, ssv_schoolAddress, ssv_schoolLocation,
            ssv_officeNumber, ssv_schoolEmail, ssv_principalName, ssv_principalNumber;
    String schoolName, schoolAddress, schoolLocation,
            officeNumber, schoolEmail, principalName, principalNumber, ssv;
    Button viewdetails,selZone;
    ListView staffList;
    ArrayList<String> arrayList = new ArrayList<>();
    Spinner selectZone;
    ArrayList<String> zonesinDb;
    ArrayAdapter<String> arrayAdapter;
    String zone;
    ProgressDialog progress;
    Spinner spinner;
    TextView contact;
    DatabaseReference dbRef;
    FirebaseDatabase firebaseDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_school_details);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        existingSchools = new ArrayList<>();
        selectZone = findViewById(R.id.selectZone);
        staffList = findViewById(R.id.expensesview);
        sName = findViewById(R.id.schoolNameAutoType);
        staffList = findViewById(R.id.staffdetails);
        ssvCode = findViewById(R.id.ssvCodeDisp);
        ssv_schoolName = findViewById(R.id.disp_school_name);
        ssv_schoolAddress = findViewById(R.id.disp_school_address);
        ssv_officeNumber = findViewById(R.id.disp_school_phone);
        ssv_schoolEmail = findViewById(R.id.disp_school_email);
        ssv_principalName = findViewById(R.id.disp_principal_name);
        ssv_principalNumber = findViewById(R.id.disp_principal_number);
        ssv_schoolLocation = findViewById(R.id.disp_school_location);
        viewdetails = findViewById(R.id.updateDetailsButton);
        contact = findViewById(R.id.disp_school_phone);
        selZone = findViewById(R.id.selZone);

        progress = new ProgressDialog(AdminSchoolDetails.this);
        progress.setMessage("Please Wait");
        progress.setTitle("Updating..");
        progress.setCancelable(false);
        progress.show();

        firebaseDatabase = FirebaseDatabase.getInstance();
        dbRef = firebaseDatabase.getReference();

        arrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, arrayList);

        inflateZones();
        ArrayAdapter<String> zoneAdapter = new ArrayAdapter<>(this,android.R.layout.simple_spinner_item,zonesinDb);
        zoneAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        selectZone.setAdapter(zoneAdapter);

        selZone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                zone = selectZone.getSelectedItem().toString();
                getExistingSchools();
            }
        });

        ArrayAdapter<String> schoolAdapter = new ArrayAdapter<>(this,android.R.layout.simple_spinner_item,existingSchools);
        schoolAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sName.setThreshold(1);
        sName.setAdapter(schoolAdapter);



        staffList.setAdapter(arrayAdapter);


        viewdetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progress.show();
                getStaffList(ssv);
                if (!TextUtils.isEmpty(ssv)) {
                    dbRef.child(zone).child("furnish/furnishdetail").child(ssv).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NotNull DataSnapshot dataSnapshot) {
                            if (dataSnapshot.exists()) {
                                schoolName = dataSnapshot.child("ssv_schoolName").getValue().toString();
                                schoolAddress = dataSnapshot.child("ssv_schoolAddress").getValue().toString();
                                schoolLocation = dataSnapshot.child("ssv_locationType").getValue().toString();
                                officeNumber = dataSnapshot.child("ssv_officeNumber").getValue().toString();
                                schoolEmail = dataSnapshot.child("ssv_schoolEmail").getValue().toString();
                                principalName = dataSnapshot.child("ssv_principalName").getValue().toString();
                                principalNumber = dataSnapshot.child("ssv_principalNumber").getValue().toString();
                                updateRecords();
                                progress.dismiss();
                            } else {
                                Toast.makeText(AdminSchoolDetails.this, "No Data", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onCancelled(@NotNull DatabaseError databaseError) {

                        }
                    });
                }
            }
        });

        sName.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                viewdetails.setEnabled(true);
                final String school = parent.getItemAtPosition(position).toString();
                dbRef.child(zone).child("autofill").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for (DataSnapshot ds : dataSnapshot.getChildren()) {
                            String val = ds.getValue().toString();
                            if (val == school) {
                                ssv = ds.getKey();
                                String value = "SSV code: " + ssv;
                                ssvCode.setText(value);
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }
        });

        contact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:" + contact.getText()));
                startActivity(intent);
            }
        });

        staffList.setOnItemClickListener(new android.widget.AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String item = (String) staffList.getItemAtPosition(position);
                int temppos = item.indexOf("-");
                temppos = temppos + 2;
                String num = item.substring(temppos);
                Intent sCall = new Intent(Intent.ACTION_DIAL);
                sCall.setData(Uri.parse("tel:" + num));
                if (!num.equals("na")) {
                    startActivity(sCall);
                }
            }
        });
    }

    private void updateRecords() {
        ssv_schoolName.setText(schoolName);
        ssv_schoolAddress.setText(schoolAddress);
        ssv_schoolLocation.setText(schoolLocation);
        ssv_officeNumber.setText(officeNumber);
        ssv_schoolEmail.setText(schoolEmail);
        ssv_principalName.setText(principalName);
        ssv_principalNumber.setText(principalNumber);
    }

    private void getExistingSchools() {
        dbRef.child(zone).child("autofill").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot ds : dataSnapshot.getChildren()) {
                    String schoolName = ds.getValue().toString();
                    existingSchools.add(schoolName);
                    progress.dismiss();
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void getStaffList(String ssvcode) {
        dbRef.child(zone).child("furnish/furnishdetail").child(ssvcode).child("ssv_table").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NotNull DataSnapshot dataSnapshot, @Nullable String s) {


                if (dataSnapshot.exists()) {
                    for (DataSnapshot ds : dataSnapshot.getChildren()) {
                        arrayList.add(dataSnapshot.child("name").getValue() + " - " + dataSnapshot.child("phone").getValue());
                        arrayAdapter.notifyDataSetChanged();

                    }
                } else {
                    Toast.makeText(AdminSchoolDetails.this, "No Data", Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @android.support.annotation.Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @android.support.annotation.Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

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
