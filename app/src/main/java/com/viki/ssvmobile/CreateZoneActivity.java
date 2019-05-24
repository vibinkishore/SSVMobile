package com.viki.ssvmobile;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CreateZoneActivity extends AppCompatActivity {

    TextInputLayout staffEmail,staffName,staffPass,staffConPass,staffContact;
    String sEmail,sPass,sConPass,sName,sContact;
    Button createZone;
    String zone;
    FirebaseDatabase fbDatabase = FirebaseDatabase.getInstance();
    DatabaseReference dbRef = fbDatabase.getReference();
    FirebaseAuth mauth = FirebaseAuth.getInstance();
    ProgressDialog progress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_zone);

        staffEmail = findViewById(R.id.staffemail);
        staffName = findViewById(R.id.staffname);
        staffPass = findViewById(R.id.staffpass);
        staffConPass = findViewById(R.id.staffconpass);
        staffContact = findViewById(R.id.staffcontact);

        createZone = findViewById(R.id.createZone);
        progress = new ProgressDialog(CreateZoneActivity.this);

        createZone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progress.show();
                sEmail = staffEmail.getEditText().getText().toString();
                sPass = staffPass.getEditText().getText().toString();
                sConPass = staffConPass.getEditText().getText().toString();
                sName = staffName.getEditText().getText().toString();
                sContact = staffContact.getEditText().getText().toString();

                zone = sEmail.substring(4,10);
                Toast.makeText(CreateZoneActivity.this, zone+"", Toast.LENGTH_SHORT).show();

                if (comparePass() && validateEmail() && detailsNotNull()) {
                    if (sEmail.substring(0,3).equals("ssv") && sEmail.substring(10).equals("@dhanamtechsolutions.com")) {
                        mauth.createUserWithEmailAndPassword(sEmail, sPass)
                                .addOnCompleteListener(CreateZoneActivity.this, new OnCompleteListener<AuthResult>() {
                                    @Override
                                    public void onComplete(@NonNull Task<AuthResult> task) {
                                        if (!task.isSuccessful()) {
                                            Toast.makeText(CreateZoneActivity.this, "Authentication failed." + task.getException(),
                                                    Toast.LENGTH_SHORT).show();
                                            progress.dismiss();
                                        } else {
                                            startActivity(new Intent(CreateZoneActivity.this, AdminDashboardActivity.class));
                                            createEmptyDb();
                                            signOutUser();
                                            progress.dismiss();
                                            finish();
                                        }
                                    }
                                });
                    } else {
                        Toast.makeText(CreateZoneActivity.this, "Enter Email in the proposed format", Toast.LENGTH_SHORT).show();
                    }
                }

            }
        });
    }

    private void signOutUser() {
        mauth.signOut();
        startActivity(new Intent(getApplicationContext(),LoginActivity.class));
    }

    private void createEmptyDb() {
        HashMap<String,String> invMap;
        HashMap<String,Object> zoneMap;
        HashMap<String,String> userMap;

        invMap = new HashMap<>();
        invMap.put("notes", "0");
        invMap.put("computerBasics", "0");
        invMap.put("CProgramming",  "0");
        invMap.put("cppProgramming", "0");
        invMap.put("officeAutomation",  "0");
        invMap.put("graphicsDesign",  "0");
        invMap.put("programmingTechniques",  "0");
        invMap.put("twoDAnimation",  "0");
        invMap.put("webDesign",  "0");

        zoneMap = new HashMap<>();
        zoneMap.put(zone,"0");

        userMap = new HashMap<>();
        userMap.put("sName",sName);
        userMap.put("sEmail",sEmail);
        userMap.put("sContact",sContact);
        userMap.put("sZone",zone);

        dbRef.child("zone").updateChildren(zoneMap);
        dbRef.child(zone).child("inventory").setValue(invMap);
        dbRef.child("zoneDetails").child(zone).setValue(userMap);
    }

    private boolean detailsNotNull() {
        if (!TextUtils.isEmpty(sContact) && !TextUtils.isEmpty(sName) && !TextUtils.isEmpty(sEmail)) {
            return true;
        } else
        return false;
    }

    private boolean validateEmail() {
        Pattern pattern;
        Matcher matcher;
        final String EMAIL_PATTERN = "^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
        pattern = Pattern.compile(EMAIL_PATTERN);
        matcher = pattern.matcher(sEmail);
        return matcher.matches();
    }

    private boolean comparePass() {
        if (!TextUtils.isEmpty(sPass) && sPass.equals(sConPass)) {
            return true;
        } else {
            staffPass.setError("Enter a Valid Password");
            staffConPass.setError("Passwords Dont Match");
            return false;
        }
    }
}
