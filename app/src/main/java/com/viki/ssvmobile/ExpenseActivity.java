package com.viki.ssvmobile;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.annotations.NotNull;
import com.google.firebase.database.annotations.Nullable;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;


public class ExpenseActivity extends AppCompatActivity {


    EditText expense,cost;
    Button sub;
    String formateddate;
    String time;
    DatabaseReference databaseReference,dr;
    expensedetails expensedetails;
    String zone;
    FirebaseAuth mAuth;
    ListView listView;
    ArrayList<String>arrayList=new ArrayList<>();
    ArrayAdapter<String> arrayAdapter;

    ProgressDialog progress;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_expense);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        listView=findViewById(R.id.expensesview);
        arrayAdapter=new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,arrayList);
        listView.setAdapter(arrayAdapter);
        progress = new ProgressDialog(ExpenseActivity.this);
        progress.setMessage("Please Wait");
        progress.setTitle("Loading..");
        progress.setCancelable(false);
        progress.show();
        expensedetails=new expensedetails();
        Calendar c=Calendar.getInstance();
        @SuppressLint("SimpleDateFormat") final SimpleDateFormat df=new SimpleDateFormat("dd-MM-yyyy");
        formateddate=df.format(c.getTime());

        mAuth = FirebaseAuth.getInstance();
        zone = mAuth.getCurrentUser().getEmail().substring(4,10);
        expense=findViewById(R.id.inpExpenseName);
        cost=findViewById(R.id.expamt);
        sub=(Button) findViewById(R.id.add);



        FirebaseDatabase firebaseDatabase=FirebaseDatabase.getInstance();
        dr=firebaseDatabase.getReference(zone + "/dailyExpenses");

        dr.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NotNull DataSnapshot dataSnapshot, @Nullable String s) {


                if (dataSnapshot.exists()) {
                    for(DataSnapshot ds:dataSnapshot.getChildren())
                    {
                        for(DataSnapshot postdata:ds.getChildren())
                        {

                            arrayList.add("Expense: "+postdata.getKey()+"\nAmount: "+ postdata.getValue(String.class) + "\n\nPosted on " + dataSnapshot.getKey() + " at " + ds.getKey());
                            arrayAdapter.notifyDataSetChanged();

                        }

                    }
                } else {
                    Toast.makeText(ExpenseActivity.this, "No Data", Toast.LENGTH_SHORT).show();
                }
                progress.dismiss();

            }

            @Override
            public void onChildChanged(@NotNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NotNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NotNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NotNull DatabaseError databaseError) {
                progress.dismiss();
            }
        });
        progress.dismiss();

    }

    public void OnAddClick(View view) {
        HashMap<String,String> obj = new HashMap<>();
        HashMap<String,String> log = new HashMap<>();
        DateFormat datef = new SimpleDateFormat("HH:mm:ss");
        time = datef.format(Calendar.getInstance().getTime());
        String exp = expense.getText().toString();
        String price = cost.getText().toString();
        obj.put(exp,price);
        String refValue = zone + "/dailyExpenses/" + formateddate + "/" + time;
        databaseReference =FirebaseDatabase.getInstance().getReference();

        if (TextUtils.isEmpty(exp)) {
            expense.setError("Enter a valid input");
        }

        if (TextUtils.isEmpty(price)) {
            cost.setError("Enter Amount");
        }

        if (!TextUtils.isEmpty(exp) && !TextUtils.isEmpty(price)) {
            databaseReference.child(refValue).setValue(obj).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NotNull Task<Void> task) {
                    expense.setText("");
                    cost.setText("");
                    Toast.makeText(ExpenseActivity.this, "Success", Toast.LENGTH_SHORT).show();
                }
            });

            log.put("expense","created | " + exp + " | " + price);
            databaseReference.child(zone + "/log/" + formateddate + "/" + time).setValue(log);
        }
    }
}
