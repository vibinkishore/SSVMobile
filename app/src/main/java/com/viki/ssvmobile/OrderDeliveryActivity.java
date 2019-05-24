package com.viki.ssvmobile;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.nfc.Tag;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;

public class OrderDeliveryActivity extends AppCompatActivity {
    Spinner ordersList;
    String orderId,zone;
    HashMap<String, String> oidSchool = new HashMap<>();
    int prevDelVal=0,newDelVal=0;
    HashMap<String,Object> delValue;
    ArrayList<String> Existingorders;
    EditText notebook,computerBasics,officeAutomation,programmingTechniques,cProgramming,cppProgramming,graphicsDesign,webDesign,twoDAnimation;
    FirebaseDatabase firebaseDb = FirebaseDatabase.getInstance();
    DatabaseReference dbRef = firebaseDb.getReference();
    Button proceed,fetchOrder;
    AlertDialog.Builder success;
    ProgressDialog progress;
    TextView ccb,coa,cpt,ccp,cpp,cgd,cwd,c2da,notes;
    int old_cProgramming= 0,old_cppProgramming= 0,old_programmingTechniques= 0,old_graphicsDesign= 0,old_officeAutomation= 0,old_twoDAnimation= 0,old_notes= 0,old_computerBasics= 0,old_webDesign= 0;
    int new_cProgramming = 0,new_cppProgramming= 0,new_programmingTechniques= 0,new_graphicsDesign= 0,new_officeAutomation= 0,new_twoDAnimation= 0,new_notes= 0,new_computerBasics= 0,new_webDesign= 0;
    int final_cProgramming= 0,final_cppProgramming= 0,final_programmingTechniques= 0,final_graphicsDesign= 0,final_officeAutomation= 0,final_twoDAnimation= 0,final_notes= 0,final_computerBasics= 0,final_webDesign= 0;
    int pd_cProgramming= 0,pd_cppProgramming= 0,pd_programmingTechniques= 0,pd_graphicsDesign= 0,pd_officeAutomation= 0,pd_twoDAnimation= 0,pd_notes= 0,pd_computerBasics= 0,pd_webDesign= 0;
    int ud_cProgramming= 0,ud_cppProgramming= 0,ud_programmingTechniques= 0,ud_graphicsDesign= 0,ud_officeAutomation= 0,ud_twoDAnimation= 0,ud_notes= 0,ud_computerBasics= 0,ud_webDesign= 0;
    HashMap<String,String> pushMapRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_delivery);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        zone = FirebaseAuth.getInstance().getCurrentUser().getEmail().substring(4,10);

        progress = new ProgressDialog(OrderDeliveryActivity.this);
        progress.setMessage("Please Wait");
        progress.setTitle("Updating..");
        progress.setCancelable(false);
        progress.show();

        success = new AlertDialog.Builder(this);
        success.setTitle("SSV Mobile");
        success.setMessage("Successfully Updated");
        success.setCancelable(true);
        success.setNeutralButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                onBackPressed();
            }
        });

        ordersList = findViewById(R.id.dispOrders);

        proceed = findViewById(R.id.proceedtoNext);
        computerBasics = findViewById(R.id.computerBasicsQty);
        officeAutomation = findViewById(R.id.officeautomationQty);
        programmingTechniques = findViewById(R.id.programmingTechniquesQty);
        cppProgramming = findViewById(R.id.cppProgrammingQty);
        cProgramming = findViewById(R.id.cProgrammingQty);
        graphicsDesign = findViewById(R.id.graphicsDesignQty);
        webDesign = findViewById(R.id.webDesignQty);
        twoDAnimation = findViewById(R.id.twoDanimationQty);
        fetchOrder = findViewById(R.id.fetchOrderButton);
        notebook = findViewById(R.id.notesQty);

        computerBasics.setEnabled(false);
        officeAutomation.setEnabled(false);
        programmingTechniques.setEnabled(false);
        cppProgramming.setEnabled(false);
        graphicsDesign.setEnabled(false);
        webDesign.setEnabled(false);
        twoDAnimation.setEnabled(false);
        notebook .setEnabled(false);
        cProgramming.setEnabled(false);


        ccb = findViewById(R.id.ccbText);
        coa = findViewById(R.id.coaText);
        cpt = findViewById(R.id.cptText);
        ccp = findViewById(R.id.ccpText);
        cpp = findViewById(R.id.cppText);
        cgd = findViewById(R.id.cgdText);
        cwd = findViewById(R.id.cwdText);
        c2da = findViewById(R.id.c2daText);
        notes = findViewById(R.id.notesText);





        inflateOrders();

        ArrayAdapter<String> orderAdapter = new ArrayAdapter<>(this,android.R.layout.simple_spinner_item,Existingorders);
        orderAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        ordersList.setAdapter(orderAdapter);

        pushMapRef = new HashMap<>();

        ordersList.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                fetchOrder.setEnabled(true);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        dbRef.child(zone + "/inventory").addValueEventListener(new ValueEventListener() {
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



        fetchOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                orderId = oidSchool.get(ordersList.getSelectedItem().toString());
                setPrevDelVal();
                computerBasics.setEnabled(true);
                officeAutomation.setEnabled(true);
                programmingTechniques.setEnabled(true);
                cppProgramming.setEnabled(true);
                graphicsDesign.setEnabled(true);
                webDesign.setEnabled(true);
                twoDAnimation.setEnabled(true);
                notebook .setEnabled(true);
                cProgramming.setEnabled(true);


                fetchOrderDetails(orderId);
            }
        });


        proceed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                getOldDelivery();

                if(getIInputDetails() && getOldDelivery() && calculateNewStock() && pushFinaltoInventory() && changeStatus()) {
                    toHome();
                    pushDeliveryTotal();
                    updateOrderDetails();
                }
            }
        });
    }

    private boolean getOldDelivery() {
        dbRef.child(zone + "/order/" + orderId).child("delivery").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                pd_cProgramming = Integer.parseInt(dataSnapshot.child("CProgramming").getValue(String.class).toString());
                pd_cppProgramming = Integer.parseInt(dataSnapshot.child("cppProgramming").getValue(String.class).toString());
                pd_programmingTechniques = Integer.parseInt(dataSnapshot.child("programmingTechniques").getValue(String.class).toString());
                pd_graphicsDesign = Integer.parseInt(dataSnapshot.child("graphicsDesign").getValue(String.class).toString());
                pd_officeAutomation = Integer.parseInt(dataSnapshot.child("officeAutomation").getValue(String.class).toString());
                pd_twoDAnimation = Integer.parseInt(dataSnapshot.child("twoDAnimation").getValue(String.class).toString());
                pd_notes = Integer.parseInt(dataSnapshot.child("notes").getValue(String.class).toString());
                pd_computerBasics = Integer.parseInt(dataSnapshot.child("computerBasics").getValue(String.class).toString());
                pd_webDesign = Integer.parseInt(dataSnapshot.child("webDesign").getValue(String.class).toString());
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                //null
            }
        });
        return true;
    }

    private void pushDeliveryTotal() {
        delValue = new HashMap<>();
        int orderTotal,cost_cProgramming = 350,cost_cppProgramming = 350,cost_programmingTechniques = 300,cost_graphicsDesign = 350,cost_officeAutomation = 300,cost_twoDAnimation = 350,cost_computerBasics = 250,cost_webDesign = 350;
        cost_computerBasics = cost_computerBasics * Integer.parseInt(String.valueOf(new_computerBasics));
        cost_cProgramming = cost_cProgramming * Integer.parseInt(String.valueOf(new_cProgramming));
        cost_cppProgramming = cost_cppProgramming * Integer.parseInt(String.valueOf(new_cppProgramming));
        cost_programmingTechniques = cost_programmingTechniques * Integer.parseInt(String.valueOf(new_programmingTechniques));
        cost_officeAutomation = cost_officeAutomation * Integer.parseInt(String.valueOf(new_officeAutomation));
        cost_graphicsDesign = cost_graphicsDesign * Integer.parseInt(String.valueOf(new_graphicsDesign));
        cost_webDesign = cost_webDesign * Integer.parseInt(String.valueOf(new_webDesign));
        cost_twoDAnimation = cost_twoDAnimation * Integer.parseInt(String.valueOf(new_twoDAnimation));

        orderTotal = cost_computerBasics + cost_cProgramming + cost_cppProgramming + cost_programmingTechniques + cost_officeAutomation + cost_graphicsDesign + cost_twoDAnimation + cost_webDesign;


        newDelVal = prevDelVal + orderTotal;
        delValue.put("deliveryamt",String.valueOf(newDelVal));
        dbRef.child(zone + "/order/" + orderId).child("deliveryamt").updateChildren(delValue);
    }


    private  void setPrevDelVal()
    {
        dbRef.child(zone + "/order/" + orderId+"/deliveryamt").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {


                prevDelVal = Integer.parseInt(dataSnapshot.child("deliveryamt").getValue(String.class).toString());
                Toast.makeText(OrderDeliveryActivity.this, String.valueOf(prevDelVal), Toast.LENGTH_SHORT).show();

            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void fetchOrderDetails(String oid) {

        dbRef.child(zone + "/order/" + oid).child("delivery").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                pd_computerBasics = Integer.parseInt(dataSnapshot.child("computerBasics").getValue(String.class).toString());
                pd_cProgramming = Integer.parseInt(dataSnapshot.child("CProgramming").getValue(String.class).toString());
                pd_cppProgramming = Integer.parseInt(dataSnapshot.child("cppProgramming").getValue(String.class).toString());
                pd_programmingTechniques = Integer.parseInt(dataSnapshot.child("programmingTechniques").getValue(String.class).toString());
                pd_twoDAnimation = Integer.parseInt(dataSnapshot.child("twoDAnimation").getValue(String.class).toString());
                pd_webDesign = Integer.parseInt(dataSnapshot.child("webDesign").getValue(String.class).toString());
                pd_graphicsDesign = Integer.parseInt(dataSnapshot.child("graphicsDesign").getValue(String.class).toString());
                pd_officeAutomation = Integer.parseInt(dataSnapshot.child("officeAutomation").getValue(String.class).toString());
                pd_notes = Integer.parseInt(dataSnapshot.child("notes").getValue(String.class).toString());

                ccp.append(" [" + String.valueOf(pd_cProgramming) + "]");
                cpp.append(" [" + String.valueOf(pd_cppProgramming) + "]");
                ccb.append(" [" + String.valueOf(pd_computerBasics) + "]");
                cpt.append(" [" + String.valueOf(pd_programmingTechniques) + "]");
                c2da.append(" [" + String.valueOf(pd_twoDAnimation) + "]");
                cgd.append(" [" + String.valueOf(pd_graphicsDesign) + "]");
                cwd.append(" [" + String.valueOf(pd_webDesign) + "]");
                coa.append(" [" + String.valueOf(pd_officeAutomation) + "]");
                notes.append(" [" + String.valueOf(pd_notes) + "]");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private boolean changeStatus() {
        HashMap<String,Object> stat= new HashMap<>();
        stat.put("orderStatus","delivered");
        dbRef.child(zone + "/order/" + orderId + "/status").updateChildren(stat);
        return true;
    }

    private void inflateOrders() {
        Existingorders = new ArrayList<>();
        Existingorders.add("Select an order");
        ValueEventListener eventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot ds : dataSnapshot.getChildren()) {
                    String oId = ds.getKey();
                    String school = ds.child("basicinfo/orderschool").getValue().toString();
                    String oStat = ds.child("status/orderStatus").getValue().toString();
                    if (!oStat.equals("completed")) {
                        oidSchool.put(school,oId);
                        Existingorders.add(school);
                        progress.dismiss();
                    }
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {}
        };
        dbRef.child(zone + "/order").addListenerForSingleValueEvent(eventListener);
    }

    private void toHome() {
        success.show();
    }

    private boolean updateOrderDetails() {
        getIInputDetails();
        HashMap<String,Object> orderMapRef = new HashMap<>();
        calculateDelivery();
        orderMapRef.put("notes", ud_notes+"");
        orderMapRef.put("computerBasics",ud_computerBasics+"");
        orderMapRef.put("CProgramming", ud_cProgramming+"");
        orderMapRef.put("cppProgramming", ud_cppProgramming+"");
        orderMapRef.put("officeAutomation", ud_officeAutomation+"");
        orderMapRef.put("graphicsDesign", ud_graphicsDesign+"");
        orderMapRef.put("programmingTechniques", ud_programmingTechniques+"");
        orderMapRef.put("twoDAnimation", ud_twoDAnimation+"");
        orderMapRef.put("webDesign", ud_webDesign+"");
        dbRef.child(zone + "/order/" + orderId).child("delivery").setValue(orderMapRef).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                progress.dismiss();
            }
        });
        return true;
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
        dbRef.child(zone + "/inventory").setValue(pushMapRef);
        return true;
    }

    private boolean getIInputDetails() {
        orderId = oidSchool.get(ordersList.getSelectedItem().toString());
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

    private boolean calculateNewStock() {

        final_notes = old_notes - new_notes;
        final_cProgramming = old_cProgramming - new_cProgramming;
        final_cppProgramming = old_cppProgramming - new_cppProgramming;
        final_computerBasics = old_computerBasics - new_computerBasics;
        final_graphicsDesign = old_graphicsDesign - new_graphicsDesign;
        final_officeAutomation = old_officeAutomation - new_officeAutomation;
        final_programmingTechniques = old_programmingTechniques - new_programmingTechniques;
        final_twoDAnimation = old_twoDAnimation - new_twoDAnimation;
        final_webDesign = old_webDesign - new_webDesign;
        return true;
    }

    private boolean calculateDelivery() {
        ud_notes = pd_notes + new_notes;
        ud_cProgramming = pd_cProgramming + new_cProgramming;
        ud_cppProgramming = pd_cppProgramming + new_cppProgramming;
        ud_computerBasics = pd_computerBasics + new_computerBasics;
        ud_graphicsDesign = pd_graphicsDesign + new_graphicsDesign;
        ud_officeAutomation = pd_officeAutomation + new_officeAutomation;
        ud_programmingTechniques = pd_programmingTechniques + new_programmingTechniques;
        ud_twoDAnimation = pd_twoDAnimation + new_twoDAnimation;
        ud_webDesign = pd_webDesign + new_webDesign;
        return true;
    }
}

