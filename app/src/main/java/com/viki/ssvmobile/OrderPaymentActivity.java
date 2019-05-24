package com.viki.ssvmobile;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
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
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

public class OrderPaymentActivity extends AppCompatActivity {

    Spinner ordersList;
    String orderId;
    String paidamt,deliveredamt,orderamt;
    int oldPay,newPay;
    Button fetchOrder,collectPayment,markasCompleted;
    ArrayList<String> Existingorders;
    EditText inwardAmount;
    FirebaseDatabase firebaseDb = FirebaseDatabase.getInstance();
    HashMap<String, String> oidSchool = new HashMap<>();
    FirebaseAuth mUser;
    String zone;
    AlertDialog.Builder success;
    ProgressDialog progress;
    DatabaseReference orderRef = firebaseDb.getReference();
    TextView ccbOrder,coaOrder,cptOrder,ccpOrder,cppOrder,cgdOrder,cwdOrder,c2daOrder;
    TextView ccbDeliver,coaDeliver,cptDeliver,ccpDeliver,cppDeliver,cgdDeliver,cwdDeliver,c2daDeliver;
    TextView ccbReturn,coaReturn,cptReturn,ccpReturn,cppReturn,cgdReturn,cwdReturn,c2daReturn;
    TextView orderAmount,deliveredAmount,balance,paidAmount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_payment);

        mUser = FirebaseAuth.getInstance();
        zone = mUser.getCurrentUser().getEmail().substring(4,10);
        inflateOrders();

        ordersList = findViewById(R.id.dispOrders);
        fetchOrder = findViewById(R.id.fetchOrderButton);
        ccbOrder = findViewById(R.id.ccbOrder);
        ccbDeliver = findViewById(R.id.ccbDeliver);
        ccbReturn = findViewById(R.id.ccbReturn);
        markasCompleted = findViewById(R.id.markasCompleted);
        collectPayment = findViewById(R.id.collectPayment);
        inwardAmount = findViewById(R.id.inwardPayment);
        paidAmount = findViewById(R.id.paidAmountVal);

        coaOrder = findViewById(R.id.coaOrder);
        coaDeliver = findViewById(R.id.coaDeliver);
        coaReturn = findViewById(R.id.coaReturn);

        cptOrder = findViewById(R.id.cptOrder);
        cptDeliver = findViewById(R.id.cptDeliver);
        cptReturn = findViewById(R.id.cptReturn);

        ccpOrder = findViewById(R.id.ccpOrder);
        ccpDeliver = findViewById(R.id.ccpDeliver);
        ccpReturn = findViewById(R.id.ccpReturn);

        cppOrder = findViewById(R.id.cppOrder);
        cppDeliver = findViewById(R.id.cppDeliver);
        cppReturn = findViewById(R.id.cppReturn);

        cgdOrder = findViewById(R.id.cgdOrder);
        cgdDeliver = findViewById(R.id.cgdDeliver);
        cgdReturn = findViewById(R.id.cgdReturn);

        cwdOrder = findViewById(R.id.cwdOrder);
        cwdDeliver = findViewById(R.id.cwdDeliver);
        cwdReturn = findViewById(R.id.cwdReturn);

        c2daOrder = findViewById(R.id.c2daOrder);
        c2daDeliver = findViewById(R.id.c2daDeliver);
        c2daReturn = findViewById(R.id.c2daReturn);

        progress = new ProgressDialog(OrderPaymentActivity.this);
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

        ArrayAdapter<String> orderAdapter = new ArrayAdapter<>(this,android.R.layout.simple_spinner_item,Existingorders);
        orderAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        ordersList.setAdapter(orderAdapter);

        //Toast.makeText(this, mUser.getCurrentUser().getEmail().substring(4,9)+"", Toast.LENGTH_SHORT).show();

        fetchPayDetails();

        balance = findViewById(R.id.balanceAmount);
        orderAmount = findViewById(R.id.orderAmount);
        deliveredAmount = findViewById(R.id.deliverAmount);

        fetchOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                orderId = oidSchool.get(ordersList.getSelectedItem().toString());
                retreivePrimary(orderId);
                retreiveDelivery(orderId);
                retreiveReturns(orderId);
                retreivePayments();
            }
        });

        collectPayment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addPayment();
            }
        });

        markasCompleted.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HashMap<String,Object> orderStat = new HashMap<>();
                HashMap<String,Object> payStat = new HashMap<>();
                orderStat.put("orderStatus","completed");
                payStat.put("paymentStatus","completed");

                orderRef.child(zone).child("order").child(oidSchool.get(ordersList.getSelectedItem().toString())).child("status/orderStatus").updateChildren(orderStat);
                orderRef.child(zone).child("order").child(oidSchool.get(ordersList.getSelectedItem().toString())).child("paymentStatus").updateChildren(payStat);

                success.show();
            }
        });
    }

    private void fetchPayDetails() {
        ordersList.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                orderRef.child(zone).child("order/" + oidSchool.get(ordersList.getSelectedItem().toString())).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        deliveredamt = dataSnapshot.child("deliveryamt/deliveryamt").getValue(String.class);
                        orderamt = dataSnapshot.child("orderamt/orderamt").getValue(String.class);
                        paidamt = dataSnapshot.child("paidamt/paidamt").getValue(String.class);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        //f
                    }
                });
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here
            }

        });
    }

    private void addPayment() {
        HashMap<String,Object> payMap = new HashMap<>();
        newPay = Integer.parseInt(inwardAmount.getText().toString());
        oldPay = Integer.parseInt(paidamt);
        newPay = newPay + oldPay;
        String newPaidamt = String.valueOf(newPay);
        payMap.put("paidamt",newPaidamt);
        orderRef.child(zone).child("order/" + oidSchool.get(ordersList.getSelectedItem().toString())).child("paidamt").updateChildren(payMap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                inwardAmount.clearFocus();
                inwardAmount.setText("");
                Toast.makeText(OrderPaymentActivity.this, "Payment Received!", Toast.LENGTH_SHORT).show();
            }
        });
        addPaymentRecord(newPaidamt);
        fetchPayDetails();
        retreivePayments();
    }

    private void addPaymentRecord(String paid) {
        HashMap<String,String> paymentMap = new HashMap<>();
        Calendar c=Calendar.getInstance();
        @SuppressLint("SimpleDateFormat") final SimpleDateFormat df=new SimpleDateFormat("dd-MM-yyyy");
        String date =df.format(c.getTime());
        DateFormat datef = new SimpleDateFormat("HH:mm:ss");
        String time = datef.format(Calendar.getInstance().getTime());
        paymentMap.put("amount",paid);
        orderRef.child(zone).child("order/"+oidSchool.get(ordersList.getSelectedItem().toString())).child("paymentrecord").child(date).child(time).setValue(paymentMap);
    }

    private void retreivePayments() {
        String oa = "₹ " + orderamt;
        String da = "₹ " + deliveredamt;
        String pa = "₹ " + paidamt;
        orderAmount.setText(oa);
        deliveredAmount.setText(da);
        paidAmount.setText(pa);
        int tempCal = calculateBalance(deliveredamt,paidamt);
        if (tempCal >= 0) {
            String val = "₹ " + String.valueOf(tempCal);
            balance.setText(val);
            balance.setTextColor(Color.RED);
        } else {
            int posVal = -1 * tempCal;
            String val = "₹ " + String.valueOf(posVal);
            balance.setText(val);
            balance.setTextColor(Color.GREEN);
        }
    }

    private int calculateBalance(String deli, String paid) {
        int dVal = Integer.parseInt(deli);
        int pVal = Integer.parseInt(paid);

        int balance = dVal - pVal;
        return balance;
    }


    private void retreiveReturns(String oid) {
        orderRef.child(zone).child("order/" + oid).child("return").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                ccbReturn.setText(dataSnapshot.child("computerBasics").getValue(String.class));
                coaReturn.setText(dataSnapshot.child("officeAutomation").getValue(String.class));
                cptReturn.setText(dataSnapshot.child("programmingTechniques").getValue(String.class));
                ccpReturn.setText(dataSnapshot.child("CProgramming").getValue(String.class));
                cppReturn.setText(dataSnapshot.child("cppProgramming").getValue(String.class));
                cgdReturn.setText(dataSnapshot.child("graphicsDesign").getValue(String.class));
                cwdReturn.setText(dataSnapshot.child("webDesign").getValue(String.class));
                c2daReturn.setText(dataSnapshot.child("twoDAnimation").getValue(String.class));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void retreiveDelivery(String oid) {
        orderRef.child(zone).child("order/" + oid).child("delivery").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                ccbDeliver.setText(dataSnapshot.child("computerBasics").getValue(String.class));
                coaDeliver.setText(dataSnapshot.child("officeAutomation").getValue(String.class));
                cptDeliver.setText(dataSnapshot.child("programmingTechniques").getValue(String.class));
                ccpDeliver.setText(dataSnapshot.child("CProgramming").getValue(String.class));
                cppDeliver.setText(dataSnapshot.child("cppProgramming").getValue(String.class));
                cgdDeliver.setText(dataSnapshot.child("graphicsDesign").getValue(String.class));
                cwdDeliver.setText(dataSnapshot.child("webDesign").getValue(String.class));
                c2daDeliver.setText(dataSnapshot.child("twoDAnimation").getValue(String.class));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void retreivePrimary(String oid) {
        orderRef.child(zone).child("order/" + oid).child("createorder").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                ccbOrder.setText(dataSnapshot.child("computerBasics").getValue(String.class));
                coaOrder.setText(dataSnapshot.child("officeAutomation").getValue(String.class));
                cptOrder.setText(dataSnapshot.child("programmingTechniques").getValue(String.class));
                ccpOrder.setText(dataSnapshot.child("CProgramming").getValue(String.class));
                cppOrder.setText(dataSnapshot.child("cppProgramming").getValue(String.class));
                cgdOrder.setText(dataSnapshot.child("graphicsDesign").getValue(String.class));
                cwdOrder.setText(dataSnapshot.child("webDesign").getValue(String.class));
                c2daOrder.setText(dataSnapshot.child("twoDAnimation").getValue(String.class));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    private void inflateOrders() {
        Existingorders = new ArrayList<>();
        Existingorders.add("Select a Value");

        orderRef.child(zone).child("order").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot ds : dataSnapshot.getChildren()) {
                    String oId = ds.getKey();
                    String school = ds.child("basicinfo/orderschool").getValue().toString();
                    String oStat = ds.child("paymentstatus/payStatus").getValue().toString();
                    if (!oStat.equals("completed")) {
                        oidSchool.put(school,oId);
                        Existingorders.add(school);
                        progress.dismiss();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
