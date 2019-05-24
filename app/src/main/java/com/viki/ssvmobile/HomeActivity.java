package com.viki.ssvmobile;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.NavUtils;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class HomeActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    LinearLayout logsLayout,paymentsLayout,specimenLayout,createOrderLayout,inventoryView,marketingSchools,viewSchoolLayout,dailyEventsLayout,orderDeliveryLayout,booksReturnLayout,remaindersLayout,expenseTrackerLayout;

    FirebaseAuth mAuth = FirebaseAuth.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        viewSchoolLayout = findViewById(R.id.viewSchoolLayout);
        dailyEventsLayout = findViewById(R.id.dailyEventsLayout);
        orderDeliveryLayout = findViewById(R.id.orderDeliveryLayout);
        booksReturnLayout = findViewById(R.id.booksReturnLayout);
        remaindersLayout = findViewById(R.id.remainderLayout);
        expenseTrackerLayout = findViewById(R.id.dailyExpensesLayout);
        inventoryView = findViewById(R.id.inventoryView);
        marketingSchools = findViewById(R.id.marketingSchools);
        specimenLayout = findViewById(R.id.specimenLayout);
        createOrderLayout = findViewById(R.id.createOrderLayout);
        logsLayout = findViewById(R.id.logsLayout);
        paymentsLayout = findViewById(R.id.paymentsLayout);

        paymentsLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),OrderPaymentActivity.class));
            }
        });

        logsLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),LogsActivity.class));
            }
        });

        specimenLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),SpecimenCopyActivity.class));
            }
        });

        createOrderLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),CreateOrderActivity.class));
            }
        });

        viewSchoolLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(HomeActivity.this,ViewSchoolDetailsActivity.class));
            }
        });

        dailyEventsLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(HomeActivity.this,DailyEventsActivity.class));
            }
        });

        orderDeliveryLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(HomeActivity.this,OrderDeliveryActivity.class));
            }
        });

        booksReturnLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(HomeActivity.this,ReturnBooksActivity.class));
            }
        });

        remaindersLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(HomeActivity.this,Remainder.class));
            }
        });

        expenseTrackerLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(HomeActivity.this,ExpenseActivity.class));
            }
        });

        inventoryView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),OnlyDisplayStockActivity.class));
            }
        });

        marketingSchools.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),MarketingSchoolsActivity.class));
            }
        });

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        /*Toast.makeText(this, mAuth.getCurrentUser().getEmail().substring(4,10), Toast.LENGTH_SHORT).show();*/

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
            finishAffinity();
        }
        finishAffinity();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == R.id.action_signout) {
            mAuth.signOut();
            startActivity(new Intent(HomeActivity.this,LoginActivity.class));
            return true;
        } if (item.getItemId() == R.id.profile) {
            startActivity(new Intent(this,ZoneProfileActivity.class));
        } /*if (item.getItemId() == R.id.temp) {
            startActivity(new Intent(this,AdminMonthlyExpensesActivity.class));
        }*/
        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.nav_schoolDetails) {
            startActivity(new Intent(HomeActivity.this,ViewSchoolDetailsActivity.class));
        } else if (id == R.id.nav_orderDelivery) {
            startActivity(new Intent(HomeActivity.this,OrderDeliveryActivity.class));
        } else if (id == R.id.nav_adddReturns) {
            startActivity(new Intent(HomeActivity.this,ReturnBooksActivity.class));
        } else if (id == R.id.nav_dailyEvents) {
            startActivity(new Intent(HomeActivity.this,DailyEventsActivity.class));
        } else if (id == R.id.nav_expenseTracker) {
            startActivity(new Intent(HomeActivity.this,ExpenseActivity.class));
        } else if (id == R.id.nav_about) {
            startActivity(new Intent(HomeActivity.this,AboutActivity.class));
        } else if (id == R.id.nav_todo) {
            startActivity(new Intent(HomeActivity.this,ActiveRemaindersActivity.class));
        } else if (id == R.id.nav_contact) {
            startActivity(new Intent(HomeActivity.this,ContactActivity.class));
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
    public void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser !=null) {
            if (currentUser.getEmail().substring(4,9).equals("admin")) {
                startActivity(new Intent(getApplicationContext(),AdminDashboardActivity.class));
            }
        } else {
            Toast.makeText(this, "Login to Continue", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(HomeActivity.this,LoginActivity.class));
        }
    }
}
