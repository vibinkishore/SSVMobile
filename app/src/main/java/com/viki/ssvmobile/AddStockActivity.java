package com.viki.ssvmobile;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;

public class AddStockActivity extends AppCompatActivity {

    EditText notebook,computerBasics,officeAutomation,programmingTechniques,cProgramming,cppProgramming,graphicsDesign,webDesign,twoDAnimation;
    FirebaseDatabase firebaseDb = FirebaseDatabase.getInstance();
    DatabaseReference invRef = firebaseDb.getReference();
    String zone;
    Button proceed;
    ProgressDialog progress;
    int old_cProgramming= 0,old_cppProgramming= 0,old_programmingTechniques= 0,old_graphicsDesign= 0,old_officeAutomation= 0,old_twoDAnimation= 0,old_notes= 0,old_computerBasics= 0,old_webDesign= 0;
    int new_cProgramming = 0,new_cppProgramming= 0,new_programmingTechniques= 0,new_graphicsDesign= 0,new_officeAutomation= 0,new_twoDAnimation= 0,new_notes= 0,new_computerBasics= 0,new_webDesign= 0;
    int final_cProgramming= 0,final_cppProgramming= 0,final_programmingTechniques= 0,final_graphicsDesign= 0,final_officeAutomation= 0,final_twoDAnimation= 0,final_notes= 0,final_computerBasics= 0,final_webDesign= 0;
    HashMap<String,String> pushMapRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_stock);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        zone = getIntent().getStringExtra("zone");

        progress = new ProgressDialog(AddStockActivity.this);

        proceed = findViewById(R.id.proceedtoNext);
        computerBasics = findViewById(R.id.computerBasicsQty);
        officeAutomation = findViewById(R.id.officeautomationQty);
        programmingTechniques = findViewById(R.id.programmingTechniquesQty);
        cppProgramming = findViewById(R.id.cppProgrammingQty);
        cProgramming = findViewById(R.id.cProgrammingQty);
        graphicsDesign = findViewById(R.id.graphicsDesignQty);
        webDesign = findViewById(R.id.webDesignQty);
        twoDAnimation = findViewById(R.id.twoDanimationQty);
        notebook = findViewById(R.id.notesQty);

        pushMapRef = new HashMap<>();

        invRef.child(zone + "/inventory").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                old_cProgramming = Integer.parseInt(dataSnapshot.child("CProgramming").getValue(String.class).toString());
                old_cppProgramming = Integer.parseInt(dataSnapshot.child("cppProgramming").getValue(String.class).toString());
                old_programmingTechniques = Integer.parseInt(dataSnapshot.child("programmingTechniques").getValue(String.class).toString());
                old_graphicsDesign = Integer.parseInt(dataSnapshot.child("graphicsDesign").getValue(String.class).toString());
                old_officeAutomation = Integer.parseInt(dataSnapshot.child("officeAutomation").getValue(String.class).toString());
                old_twoDAnimation = Integer.parseInt(dataSnapshot.child("twoDAnimation").getValue(String.class).toString());
                old_notes = Integer.parseInt(dataSnapshot.child("notes").getValue(String.class).toString());
                old_computerBasics = Integer.parseInt(dataSnapshot.child("computerBasics").getValue(String.class).toString());
                old_webDesign = Integer.parseInt(dataSnapshot.child("webDesign").getValue(String.class).toString());
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                //null
            }
        });

        proceed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progress.setMessage("Please Wait");
                progress.setTitle("Updating..");
                progress.setCancelable(false);
                progress.show();

                if(getIInputDetails() && calculateDetails() && pushFinaltoInventory()) {
                    toHome();
                    progress.dismiss();
                }
            }
        });
    }

    private void toHome() {
        startActivity(new Intent(this,HomeActivity.class));
    }



    private boolean pushFinaltoInventory() {
        pushMapRef.put("notes", final_notes+"");
        pushMapRef.put("computerBasics",final_computerBasics+"");
        pushMapRef.put("CProgramming", final_cProgramming+"");
        pushMapRef.put("cppProgramming", final_cppProgramming+"");
        pushMapRef.put("officeAutomation", final_officeAutomation+"");
        pushMapRef.put("graphicsDesign", final_graphicsDesign+"");
        pushMapRef.put("programmingTechniques", final_programmingTechniques+"");
        pushMapRef.put("twoDAnimation", final_twoDAnimation+"");
        pushMapRef.put("webDesign", final_webDesign+"");
        invRef.child(zone + "/inventory").setValue(pushMapRef);
        return true;
    }

    private boolean getIInputDetails() {
        new_computerBasics = Integer.parseInt(computerBasics.getText().toString());
        new_cppProgramming = Integer.parseInt(cppProgramming.getText().toString());
        new_cProgramming = Integer.parseInt(cProgramming.getText().toString());
        new_graphicsDesign = Integer.parseInt(graphicsDesign.getText().toString());
        new_programmingTechniques = Integer.parseInt(programmingTechniques.getText().toString());
        new_officeAutomation = Integer.parseInt(officeAutomation.getText().toString());
        new_twoDAnimation = Integer.parseInt(twoDAnimation.getText().toString());
        new_notes = Integer.parseInt(notebook.getText().toString());
        new_webDesign = Integer.parseInt(webDesign.getText().toString());
        return true;
    }

    private boolean calculateDetails() {
        final_notes = old_notes + new_notes;
        final_cProgramming = old_cProgramming + new_cProgramming;
        final_cppProgramming = old_cppProgramming + new_cppProgramming;
        final_computerBasics = old_computerBasics + new_computerBasics;
        final_graphicsDesign = old_graphicsDesign + new_graphicsDesign;
        final_officeAutomation = old_officeAutomation + new_officeAutomation;
        final_programmingTechniques = old_programmingTechniques + new_programmingTechniques;
        final_twoDAnimation = old_twoDAnimation + new_twoDAnimation;
        final_webDesign = old_webDesign + new_webDesign;
        return true;
    }
}
