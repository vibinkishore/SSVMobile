package com.viki.ssvmobile;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
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

import static com.viki.ssvmobile.R.color.colorPrimaryDark;

public class CreateOrderActivity extends AppCompatActivity {

    EditText notebook,computerBasics,officeAutomation,programmingTechniques,cProgramming,cppProgramming,graphicsDesign,webDesign,twoDAnimation;
    AutoCompleteTextView schoolName;
    ArrayList<String> existingSchools;
    FirebaseDatabase firebaseDb = FirebaseDatabase.getInstance();
    ProgressDialog progress;
    String schoolNameInp,ssv;
    String time,date,zone;
    TextView ssvCode;
    FirebaseAuth mAuth;
    AlertDialog.Builder success;
    DatabaseReference orderRef = firebaseDb.getReference();
    Button proceed;
    int final_cProgramming = 0,final_cppProgramming = 0,final_programmingTechniques = 0,final_graphicsDesign = 0,final_officeAutomation = 0,final_twoDAnimation = 0,final_notes = 0,final_computerBasics = 0,final_webDesign = 0;
    HashMap<String,String> pushMapRef,basicMapRef,emptyOrderMap, rootStatus, deliamt, paidamt,orderTotal,orderStatus,paymentStatus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_order);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

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

        mAuth = FirebaseAuth.getInstance();
        zone = mAuth.getCurrentUser().getEmail().substring(4,10);
        existingSchools = new ArrayList<>();

        @SuppressLint("SimpleDateFormat") DateFormat datef = new SimpleDateFormat("HH:mm:ss");
        time = datef.format(Calendar.getInstance().getTime());
        Calendar c = Calendar.getInstance();
        @SuppressLint("SimpleDateFormat") final SimpleDateFormat df=new SimpleDateFormat("dd-MM-yyyy");
        date = df.format(c.getTime());

        progress = new ProgressDialog(CreateOrderActivity.this);
        progress.setMessage("Please Wait");
        progress.setTitle("Updating..");
        progress.setCancelable(false);
        progress.show();

        ArrayAdapter<String> schoolAdapter = new ArrayAdapter<>(this,android.R.layout.simple_spinner_item,existingSchools);
        schoolAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        getExistingSchools();
        schoolName = findViewById(R.id.schoolNameAutoType);
        schoolName.setThreshold(1);
        schoolName.setAdapter(schoolAdapter);

        ssvCode = findViewById(R.id.ssvCodeDisp);
        proceed = findViewById(R.id.proceedtoNext);
        schoolName = findViewById(R.id.schoolNameAutoType);
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


        proceed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                getIInputDetails();
                createOrder();
                success.show();
                }
            });

        schoolName.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                final String school = parent.getItemAtPosition(position).toString();
                orderRef.child(zone).child("autofill").addValueEventListener(new ValueEventListener() {
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
    }

    private void getExistingSchools() {
        orderRef.child(zone).child("autofill").addValueEventListener(new ValueEventListener() {
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

    private void createOrder() {

        basicMapRef = new HashMap<>();
        emptyOrderMap = new HashMap<>();
        rootStatus = new HashMap<>();
        deliamt = new HashMap<>();
        orderTotal = new HashMap<>();
        paidamt = new HashMap<>();
        orderStatus = new HashMap<>();
        paymentStatus = new HashMap<>();

        orderStatus.put("orderStatus","created");
        paymentStatus.put("payStatus","Non Paid");
        orderRef.child(zone + "/order/" + ssv + date.substring(5)).child("status").setValue(orderStatus);
        orderRef.child(zone + "/order/" + ssv + date.substring(5)).child("paymentstatus").setValue(paymentStatus);

        deliamt.put("deliveryamt","0");
        paidamt.put("paidamt","0");
        orderTotal.put("orderamt",calculateOrderTotal());
        orderRef.child(zone + "/order/" + ssv + date.substring(5)).child("deliveryamt").setValue(deliamt);
        orderRef.child(zone + "/order/" + ssv + date.substring(5)).child("paidamt").setValue(paidamt);
        orderRef.child(zone + "/order/" + ssv + date.substring(5)).child("orderamt").setValue(orderTotal);

        basicMapRef.put("orderDate",date);
        basicMapRef.put("orderTime",time);
        basicMapRef.put("orderschool",schoolNameInp);
        basicMapRef.put("orderamt",calculateOrderTotal());
        orderRef.child(zone + "/order/" + ssv + date.substring(5)).child("basicinfo").setValue(basicMapRef);

        pushMapRef.put("notes", final_notes+"");
        pushMapRef.put("computerBasics",final_computerBasics+"");
        pushMapRef.put("CProgramming", final_cProgramming+"");
        pushMapRef.put("cppProgramming", final_cppProgramming+"");
        pushMapRef.put("officeAutomation", final_officeAutomation+"");
        pushMapRef.put("graphicsDesign", final_graphicsDesign+"");
        pushMapRef.put("programmingTechniques", final_programmingTechniques+"");
        pushMapRef.put("twoDAnimation", final_twoDAnimation+"");
        pushMapRef.put("webDesign", final_webDesign+"");
        orderRef.child(zone + "/order/" + ssv + date.substring(5)).child("createorder").setValue(pushMapRef);

        emptyOrderMap.put("notes", "0");
        emptyOrderMap.put("computerBasics", "0");
        emptyOrderMap.put("CProgramming",  "0");
        emptyOrderMap.put("cppProgramming", "0");
        emptyOrderMap.put("officeAutomation",  "0");
        emptyOrderMap.put("graphicsDesign",  "0");
        emptyOrderMap.put("programmingTechniques",  "0");
        emptyOrderMap.put("twoDAnimation",  "0");
        emptyOrderMap.put("webDesign",  "0");
        orderRef.child(zone + "/order/" + ssv + date.substring(5)).child("delivery").setValue(emptyOrderMap);
        orderRef.child(zone + "/order/" + ssv + date.substring(5)).child("return").setValue(emptyOrderMap);
        orderRef.child(zone + "/order/" + ssv + date.substring(5)).child("speciments").setValue(emptyOrderMap);
    }

    private String calculateOrderTotal() {
        int orderTotal,cost_cProgramming = 350,cost_cppProgramming = 350,cost_programmingTechniques = 300,cost_graphicsDesign = 350,cost_officeAutomation = 300,cost_twoDAnimation = 350,cost_computerBasics = 250,cost_webDesign = 350;
        cost_computerBasics = cost_computerBasics * Integer.parseInt(String.valueOf(final_computerBasics));
        cost_cProgramming = cost_cProgramming * Integer.parseInt(String.valueOf(final_cProgramming));
        cost_cppProgramming = cost_cppProgramming * Integer.parseInt(String.valueOf(final_cppProgramming));
        cost_programmingTechniques = cost_programmingTechniques * Integer.parseInt(String.valueOf(final_programmingTechniques));
        cost_officeAutomation = cost_officeAutomation * Integer.parseInt(String.valueOf(final_officeAutomation));
        cost_graphicsDesign = cost_graphicsDesign * Integer.parseInt(String.valueOf(final_graphicsDesign));
        cost_webDesign = cost_webDesign * Integer.parseInt(String.valueOf(final_webDesign));
        cost_twoDAnimation = cost_twoDAnimation * Integer.parseInt(String.valueOf(final_twoDAnimation));

        orderTotal = cost_computerBasics + cost_cProgramming + cost_cppProgramming + cost_programmingTechniques + cost_officeAutomation + cost_graphicsDesign + cost_twoDAnimation + cost_webDesign;
        return String.valueOf(orderTotal);
    }

    private boolean getIInputDetails() {
        schoolNameInp = schoolName.getText().toString();
        final_computerBasics = Integer.parseInt(computerBasics.getText().toString());
        final_cppProgramming = Integer.parseInt(cppProgramming.getText().toString());
        final_cProgramming = Integer.parseInt(cProgramming.getText().toString());
        final_graphicsDesign = Integer.parseInt(graphicsDesign.getText().toString());
        final_programmingTechniques = Integer.parseInt(programmingTechniques.getText().toString());
        final_officeAutomation = Integer.parseInt(officeAutomation.getText().toString());
        final_twoDAnimation = Integer.parseInt(twoDAnimation.getText().toString());
        final_notes = Integer.parseInt(notebook.getText().toString());
        final_webDesign = Integer.parseInt(webDesign.getText().toString());
        return true;
    }
}
