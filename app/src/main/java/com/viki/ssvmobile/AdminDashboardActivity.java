package com.viki.ssvmobile;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class AdminDashboardActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    LinearLayout existingZones,marketingSchools,viewSchoolLL,createZoneLL,milestonesLL,staffUpdatesLL,dailyExpensesLL,currentStockLL,orderDetailsLL,paymentsLL,logsLL,contactDevLL;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_dashboard);

        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("SSV Mobile");
        setSupportActionBar(toolbar);

        viewSchoolLL = findViewById(R.id.viewSchoolLayout);
        createZoneLL = findViewById(R.id.createZoneLayout); //done
        milestonesLL = findViewById(R.id.milestonesLayout); //done
        staffUpdatesLL = findViewById(R.id.staffUpdatesLayout); //done
        dailyExpensesLL = findViewById(R.id.dailyExpensesLayout); //done
        currentStockLL = findViewById(R.id.currentStockLayout); //done
        orderDetailsLL = findViewById(R.id.orderDeliveryLayout); //done
        paymentsLL = findViewById(R.id.paymentsLayout); //done
        logsLL = findViewById(R.id.logsLayout);
        contactDevLL = findViewById(R.id.contactDeveloperLayout); //done
        existingZones = findViewById(R.id.existingZones); //done
        marketingSchools = findViewById(R.id.marketingSchools); //done

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        viewSchoolLL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),AdminSchoolDetails.class));
            }
        });

        createZoneLL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),CreateZoneActivity.class));
            }
        });

        contactDevLL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),ContactActivity.class));
            }
        });

        milestonesLL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),AdminMilestonesActivity.class));
            }
        });

        currentStockLL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),StockDetailsActivity.class));
            }
        });

        dailyExpensesLL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),AdminMonthlyExpensesActivity.class));
            }
        });

        staffUpdatesLL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),AdminStaffUpdatesActivity.class));
            }
        });

        marketingSchools.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),AdminMarketingSchoolsActivity.class));
            }
        });

        existingZones.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),ExistingZonesActivity.class));
            }
        });

        orderDetailsLL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),AdminOrderDetailsActivity.class));
            }
        });

        paymentsLL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),AdminPaymentsActivity.class));
            }
        });

        logsLL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),AdminLogsActivity.class));
            }
        });
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
        finishAffinity();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.admin_dashboard, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_zones) {
            startActivity(new Intent(getApplicationContext(),ExistingZonesActivity.class));
        }

        if (id == R.id.action_signout) {
            FirebaseAuth.getInstance().signOut();
            startActivity(new Intent(getApplicationContext(),LoginActivity.class));
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_schoolDetails) {
            startActivity(new Intent(getApplicationContext(),AdminSchoolDetails.class));
        }
        else if (id == R.id.nav_orderDetails) {
            startActivity(new Intent(getApplicationContext(),AdminOrderDetailsActivity.class));
        }
        else if (id == R.id.nav_payments) {
            startActivity(new Intent(getApplicationContext(),AdminPaymentsActivity.class));
        }
        else if (id == R.id.nav_staffUpdates) {
            startActivity(new Intent(getApplicationContext(),AdminStaffUpdatesActivity.class));
        }
        else if (id == R.id.nav_dailyExpenses) {
            startActivity(new Intent(getApplicationContext(),AdminExpensesActivity.class));
        }
        else if (id == R.id.nav_milestones) {
            startActivity(new Intent(getApplicationContext(),AdminMilestonesActivity.class));
        }
        else if (id == R.id.nav_about) {
            startActivity(new Intent(getApplicationContext(),AboutActivity.class));
        }
        else if (id == R.id.nav_contact) {
            startActivity(new Intent(getApplicationContext(),ContactActivity.class));
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if(currentUser !=null) {
            if (currentUser.getEmail().substring(4,9).equals("admin")) {
                //continue
            } else {
                startActivity(new Intent(getApplicationContext(),HomeActivity.class));
            }
        } else {
            Toast.makeText(this, "Login to Continue", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(getApplicationContext(),LoginActivity.class));
        }
    }
}
