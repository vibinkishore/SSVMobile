package com.viki.ssvmobile;

import android.app.ProgressDialog;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class OnlyDisplayStockActivity extends AppCompatActivity {

    String zone;
    TextView notebook,computerBasics,officeAutomation,programmingTechniques,cProgramming,cppProgramming,graphicsDesign,webDesign,twoDAnimation;
    FirebaseDatabase firebaseDb = FirebaseDatabase.getInstance();
    DatabaseReference invRef = firebaseDb.getReference();
    FirebaseAuth mAuth;
    ProgressDialog progress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_only_display_stock);

        mAuth = FirebaseAuth.getInstance();
        zone = mAuth.getCurrentUser().getEmail().substring(4,10);

        progress = new ProgressDialog(OnlyDisplayStockActivity.this);
        progress.setTitle("Loading");
        progress.setMessage("Please Wait");
        progress.show();


        notebook = findViewById(R.id.notesQty);
        computerBasics = findViewById(R.id.computerBasicsQty);
        officeAutomation = findViewById(R.id.officeautomationQty);
        programmingTechniques = findViewById(R.id.programmingTechniquesQty);
        cppProgramming = findViewById(R.id.cppProgrammingQty);
        cProgramming = findViewById(R.id.cProgrammingQty);
        graphicsDesign = findViewById(R.id.graphicsDesignQty);
        webDesign = findViewById(R.id.webDesignQty);
        twoDAnimation = findViewById(R.id.twoDanimationQty);

        invRef.child(zone + "/inventory").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                cProgramming.setText(dataSnapshot.child("CProgramming").getValue(String.class).toString());
                cppProgramming.setText(dataSnapshot.child("cppProgramming").getValue(String.class).toString());
                programmingTechniques.setText(dataSnapshot.child("programmingTechniques").getValue(String.class).toString());
                graphicsDesign.setText(dataSnapshot.child("graphicsDesign").getValue(String.class).toString());
                officeAutomation.setText(dataSnapshot.child("officeAutomation").getValue(String.class).toString());
                twoDAnimation.setText(dataSnapshot.child("twoDAnimation").getValue(String.class).toString());
                notebook.setText(dataSnapshot.child("notes").getValue(String.class).toString());
                computerBasics.setText(dataSnapshot.child("computerBasics").getValue(String.class).toString());
                webDesign.setText(dataSnapshot.child("webDesign").getValue(String.class).toString());
                progress.dismiss();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                //null
            }
        });

    }
}
