package com.bespoke;

import android.content.Context;
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
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bespoke.sprefs.AppSPrefs;

public class HomeActivity extends AppCompatActivity
        implements View.OnClickListener {

    private TextView tvHome, tvViewIssues, tvViewRequests, tvDocuments, tvReports, tvCategory, tvSettings, tvLogout;
    private LinearLayout llHomePanel, llViewIssues, llViewRequests, llDocuments,
            llReports, llCategory, llSettings, llLogout;
    Button btnViewIssues,btnViewRequests,btnDocuments,btnReports;
    private DrawerLayout drawer;
    private Context mContext;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();
        mContext=this;
        initializeComponents();

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home, menu);
        return true;
    }


    /**
     * Method for initialize component of screen.
     */
    private void initializeComponents() {

        tvHome = (TextView) findViewById(R.id.tvHome);
        tvViewIssues = (TextView) findViewById(R.id.tvViewIssues);
        tvViewRequests = (TextView) findViewById(R.id.tvViewRequests);
        tvDocuments = (TextView) findViewById(R.id.tvDocuments);
        tvReports = (TextView) findViewById(R.id.tvReports);
        tvCategory = (TextView) findViewById(R.id.tvCategory);
        tvSettings = (TextView) findViewById(R.id.tvSettings);
        tvLogout = (TextView) findViewById(R.id.tvLogout);

        llHomePanel = (LinearLayout) findViewById(R.id.llHomePanel);
        llViewIssues = (LinearLayout) findViewById(R.id.llViewIssues);
        llViewRequests = (LinearLayout) findViewById(R.id.llViewRequests);
        llDocuments = (LinearLayout) findViewById(R.id.llDocuments);
        llReports = (LinearLayout) findViewById(R.id.llReports);
        llCategory = (LinearLayout) findViewById(R.id.llCategory);
        llSettings = (LinearLayout) findViewById(R.id.llSettings);
        llLogout = (LinearLayout) findViewById(R.id.llLogoutPanel);

        btnViewIssues = (Button) findViewById(R.id.btnViewIssues);
        btnViewRequests = (Button) findViewById(R.id.btnViewRequests);
        btnDocuments = (Button) findViewById(R.id.btnDocuments);
        btnReports = (Button) findViewById(R.id.btnReports);

        llHomePanel.setOnClickListener(this);
        llViewIssues.setOnClickListener(this);
        llViewRequests.setOnClickListener(this);
        llDocuments.setOnClickListener(this);
        llReports.setOnClickListener(this);
        llCategory.setOnClickListener(this);
        llSettings.setOnClickListener(this);
        llLogout.setOnClickListener(this);

        btnViewIssues.setOnClickListener(this);
        btnViewRequests.setOnClickListener(this);
        btnDocuments.setOnClickListener(this);
        btnReports.setOnClickListener(this);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
    /**
     * Method to close Close Drawer
     */
    private void closeDrawer() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        }
    }

    @Override
    public void onClick(View v) {
        closeDrawer();
        //switch case
        switch (v.getId()) {
            case R.id.llHomePanel:
                break;
            case R.id.llViewIssues:
                startActivity(new Intent(mContext, ViewIssuesActivity.class));
                break;
            case R.id.llViewRequests:
                startActivity(new Intent(mContext, ViewRequestsActivity.class));
                break;
            case R.id.llDocuments:
                startActivity(new Intent(mContext, DocumentsActivity.class));
                break;
            case R.id.llReports:
                startActivity(new Intent(mContext, ReportsActivity.class));
                break;
            case R.id.llCategory:
                startActivity(new Intent(mContext, CategoryActivity.class));
                break;
            case R.id.llSettings:
                startActivity(new Intent(mContext, SettingsActivity.class));
                break;

            case R.id.llLogoutPanel:
              // AppSPrefs.clearAppSPrefs();
               /* Intent intent = new Intent(mContext, HomeActivity.class);
                startActivity(intent);*/
                finish();
                break;

            case R.id.btnViewIssues:
                startActivity(new Intent(mContext, ViewIssuesActivity.class));
                break;
            case R.id.btnViewRequests:
                startActivity(new Intent(mContext, ViewRequestsActivity.class));
                break;
            case R.id.btnDocuments:
                startActivity(new Intent(mContext, DocumentsActivity.class));
                break;
            case R.id.btnReports:
                startActivity(new Intent(mContext, ReportsActivity.class));
                break;
        }
    }
}
