package com.viki.ssvmobile;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class StockDetailsActivity extends AppCompatActivity {
    Button viewStock;
    ArrayList<String> zonesinDb;
    Spinner selectZone;
    Button addStock,deleteStock;
    String currentzone;
    TextView notebook,computerBasics,officeAutomation,programmingTechniques,cProgramming,cppProgramming,graphicsDesign,webDesign,twoDAnimation;
    FirebaseDatabase fdb = FirebaseDatabase.getInstance();
    DatabaseReference zoneRef = fdb.getReference();
    DatabaseReference dbRef = fdb.getReference();
    ProgressDialog progress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stock_details);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        zonesinDb = new ArrayList<>();

        progress = new ProgressDialog(StockDetailsActivity.this);
        progress.setTitle("Please Wait");
        progress.setMessage("Loading");

        notebook = findViewById(R.id.notesQty);
        computerBasics = findViewById(R.id.computerBasicsQty);
        selectZone = findViewById(R.id.selectZone);
        officeAutomation = findViewById(R.id.officeautomationQty);
        programmingTechniques = findViewById(R.id.programmingTechniquesQty);
        cppProgramming = findViewById(R.id.cppProgrammingQty);
        cProgramming = findViewById(R.id.cProgrammingQty);
        graphicsDesign = findViewById(R.id.graphicsDesignQty);
        webDesign = findViewById(R.id.webDesignQty);
        twoDAnimation = findViewById(R.id.twoDanimationQty);
        addStock = findViewById(R.id.addStockButton);
        deleteStock = findViewById(R.id.deleteStockButton);
        viewStock = findViewById(R.id.fetchExpenses);

        inflateZones();

        final ArrayAdapter<String> zoneAdapter = new ArrayAdapter<>(this,android.R.layout.simple_spinner_item,zonesinDb);
        zoneAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        selectZone.setAdapter(zoneAdapter);

        selectZone.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(StockDetailsActivity.this, "Select a Zone", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        viewStock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentzone = selectZone.getSelectedItem().toString();
                if (!currentzone.equals("Select Zone")) {
                    dbRef.child(currentzone + "/inventory").addValueEventListener(new ValueEventListener() {
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
                        }
                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                            //null
                        }
                    });
                } else {
                    Toast.makeText(StockDetailsActivity.this, "Select a Zone", Toast.LENGTH_SHORT).show();
                }
            }
        });

        addStock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentzone = selectZone.getSelectedItem().toString();
                if (!currentzone.equals("Select Zone")) {
                    Intent addStock = new Intent(StockDetailsActivity.this,AddStockActivity.class);
                    addStock.putExtra("zone",currentzone);
                    startActivity(addStock);
                } else {
                    Toast.makeText(StockDetailsActivity.this, "Select a Zone", Toast.LENGTH_SHORT).show();
                }
            }
        });

        deleteStock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentzone = selectZone.getSelectedItem().toString();
                if (!currentzone.equals("Select Zone")) {
                    Intent delStock = new Intent(StockDetailsActivity.this,DeleteStockActivity.class);
                    delStock.putExtra("zone",currentzone);
                    startActivity(delStock);
                } else {
                    Toast.makeText(StockDetailsActivity.this, "Select a Zone", Toast.LENGTH_SHORT).show();
                }
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
        zoneRef.child("zone").addListenerForSingleValueEvent(eventListener);
    }
}
