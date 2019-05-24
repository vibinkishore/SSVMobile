package com.viki.ssvmobile;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

public class AllModulesActivity extends AppCompatActivity {

    Button specimen,createZone,orderPayments,stockDetails,addmSchool,viewmSchool,createOrder,orderDelivery,dailyEvents,expTracker,viewSchool,viewRemainders,login,about,returnBooks,signout,viewUser,contact,toHome;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_modules);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        viewSchool = findViewById(R.id.viewSchoolButton);
        stockDetails = findViewById(R.id.stockDetailsButton);
        addmSchool = findViewById(R.id.addmSchool);
        viewmSchool = findViewById(R.id.viewmSchools);
        createOrder = findViewById(R.id.createOrderButton);
        orderDelivery = findViewById(R.id.orderDeliveryButton);
        returnBooks = findViewById(R.id.returnBooksButton);
        orderPayments = findViewById(R.id.orderPaymentButton);

        dailyEvents = findViewById(R.id.dailyEventsButton);
        expTracker = findViewById(R.id.expenseTrackerButton);
        viewRemainders = findViewById(R.id.viewRemaindersButton);

        login = findViewById(R.id.loginButton);
        signout = findViewById(R.id.signoutButton);
        viewUser = findViewById(R.id.currentUserButton);

        about = findViewById(R.id.aboutButton);
        contact = findViewById(R.id.contactbutton);
        createZone = findViewById(R.id.createZone);
        specimen = findViewById(R.id.specimen);
/*
        toHome = findViewById(R.id.toHomeButton);
*/
        createOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),CreateOrderActivity.class));
            }
        });

        addmSchool.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),AddMarketingSchoolActivity.class));
            }
        });

        viewmSchool.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),MarketingSchoolsActivity.class));
            }
        });

        viewSchool.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(AllModulesActivity.this,ViewSchoolDetailsActivity.class));
            }
        });

        orderDelivery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(AllModulesActivity.this,OrderDeliveryActivity.class));
            }
        });

        returnBooks.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),ReturnBooksActivity.class));
            }
        });

        dailyEvents.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),DailyEventsActivity.class));
            }
        });

        expTracker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(),ExpenseActivity.class));
            }
        });

        viewRemainders.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),ActiveRemaindersActivity.class));
            }
        });

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(AllModulesActivity.this,LoginActivity.class));
            }
        });

        signout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                Toast.makeText(getApplicationContext(),"User Signed Out",Toast.LENGTH_SHORT).show();
            }
        });

        viewUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String user = FirebaseAuth.getInstance().getCurrentUser().getEmail();
                Toast.makeText(AllModulesActivity.this, user, Toast.LENGTH_SHORT).show();
            }
        });

        about.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(AllModulesActivity.this,AboutActivity.class));
            }
        });

        contact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),ContactActivity.class));
            }
        });

        stockDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),StockDetailsActivity.class));
            }
        });

        orderPayments.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),OrderPaymentActivity.class));
            }
        });

        createZone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),CreateZoneActivity.class));
            }
        });

        specimen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),SpecimenCopyActivity.class));
            }
        });
    }
}
