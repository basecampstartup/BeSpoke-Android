package com.bespoke;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
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

import com.android.volley.Request;
import com.bespoke.commons.Commons;
import com.bespoke.fragments.HomeFragment;
import com.bespoke.network.CheckNetwork;
import com.bespoke.servercommunication.APIUtils;
import com.bespoke.servercommunication.CommunicatorNew;
import com.bespoke.sprefs.AppSPrefs;
import com.bespoke.utils.TicketStatus;
import com.bespoke.utils.Utils;

import java.util.HashMap;

public class HomeActivity extends AppCompatActivity
        implements View.OnClickListener {

    private TextView tvHome, tvViewIssues, tvViewRequests, tvDocuments, tvReports, tvCategory, tvSettings, tvLogout;
    private LinearLayout llViewIssues, llViewRequests, llDocuments,llCataegoryHome;
    ImageView btnViewIssues,btnViewRequests,btnDocuments,btnReports;
    private DrawerLayout drawer;
    private Context mContext;
    TextView tvUsername;
    private LinearLayout LeftPanelLogoutPanel,LeftPanelHome,LeftPanelViewIssues,
            LeftPanelViewRequests,LeftPanelDocuments,LeftPanelCategory,LeftPanelSettings,LeftPanelReports;
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
      //  setFragment(new HomeFragment());
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
       // getMenuInflater().inflate(R.menu.home, menu);
        return true;
    }


    /**
     * Method for initialize component of screen.
     */
    private void initializeComponents() {
        tvUsername=(TextView)findViewById(R.id.userName);
        tvHome = (TextView) findViewById(R.id.tvHome);
        tvViewIssues = (TextView) findViewById(R.id.tvViewIssues);
        tvViewRequests = (TextView) findViewById(R.id.tvViewRequests);
        tvDocuments = (TextView) findViewById(R.id.tvDocuments);
        tvReports = (TextView) findViewById(R.id.tvReports);
        tvCategory = (TextView) findViewById(R.id.tvCategory);
        tvSettings = (TextView) findViewById(R.id.tvSettings);
        tvLogout = (TextView) findViewById(R.id.tvLogout);


        llViewIssues = (LinearLayout) findViewById(R.id.llViewIssues);
        llViewRequests = (LinearLayout) findViewById(R.id.llViewRequests);
        llDocuments = (LinearLayout) findViewById(R.id.llDocuments);
        llCataegoryHome = (LinearLayout) findViewById(R.id.llCataegoryHome);



        LeftPanelViewIssues= (LinearLayout) findViewById(R.id.LeftPanelViewIssues);
        LeftPanelViewRequests= (LinearLayout) findViewById(R.id.LeftPanelViewRequests);
        LeftPanelHome= (LinearLayout) findViewById(R.id.LeftPanelHome);
        LeftPanelDocuments= (LinearLayout) findViewById(R.id.LeftPanelDocuments);
        LeftPanelReports= (LinearLayout) findViewById(R.id.LeftPanelReports);
        LeftPanelCategory= (LinearLayout) findViewById(R.id.LeftPanelCategory);
        LeftPanelSettings= (LinearLayout) findViewById(R.id.LeftPanelSettings);
        LeftPanelLogoutPanel= (LinearLayout) findViewById(R.id.LeftPanelLogoutPanel);

        btnViewIssues = (ImageView) findViewById(R.id.btnViewIssues);
        btnViewRequests = (ImageView) findViewById(R.id.btnViewRequests);
        btnDocuments = (ImageView) findViewById(R.id.btnDocuments);
        btnReports = (ImageView) findViewById(R.id.btnCategory);

        llViewIssues.setOnClickListener(this);
        llViewRequests.setOnClickListener(this);
        llDocuments.setOnClickListener(this);
        llCataegoryHome.setOnClickListener(this);

        btnViewIssues.setOnClickListener(this);
        btnViewRequests.setOnClickListener(this);
        btnDocuments.setOnClickListener(this);
        btnReports.setOnClickListener(this);
        tvUsername.setText(AppSPrefs.getString(Commons.USER_NAME));


        LeftPanelViewIssues.setOnClickListener(this);
        LeftPanelViewRequests.setOnClickListener(this);
        LeftPanelHome.setOnClickListener(this);
        LeftPanelDocuments.setOnClickListener(this);
        LeftPanelReports.setOnClickListener(this);
        LeftPanelCategory.setOnClickListener(this);
        LeftPanelSettings.setOnClickListener(this);
        LeftPanelLogoutPanel.setOnClickListener(this);
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

            case R.id.llViewIssues:
                //setFragment(new HomeFragment());
                startActivity(new Intent(mContext, ViewIssuesActivity.class));
                break;
            case R.id.llViewRequests:
                startActivity(new Intent(mContext, ViewRequestsActivity.class));
                break;
            case R.id.llDocuments:
                startActivity(new Intent(mContext, DocumentsActivity.class));
                break;
            case R.id.llCataegoryHome:
                startActivity(new Intent(mContext, CategoryActivity.class));
                break;

            case R.id.LeftPanelHome:
                //  setFragment(new HomeFragment());
                break;
            case R.id.LeftPanelViewIssues:
                //  setFragment(new HomeFragment());
                startActivity(new Intent(mContext, ViewIssuesActivity.class));
                break;
            case R.id.LeftPanelViewRequests:
                //  setFragment(new HomeFragment());
                startActivity(new Intent(mContext, ViewRequestsActivity.class));
                break;
            case R.id.LeftPanelDocuments:
                startActivity(new Intent(mContext, DocumentsActivity.class));
                break;
            case R.id.LeftPanelReports:
                //  setFragment(new HomeFragment());
                break;
            case R.id.LeftPanelCategory:
                startActivity(new Intent(mContext, CategoryActivity.class));
                break;
            case R.id.LeftPanelSettings:
                startActivity(new Intent(mContext, SettingsActivity.class));
                break;
            case R.id.LeftPanelLogoutPanel:
                confirmationLogoutAlertDialog(getResources().getString(R.string.Alert),getResources().getString(R.string.LogoutConfirmationMessage));

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
            case R.id.btnCategory:
                startActivity(new Intent(mContext, CategoryActivity.class));
                break;
        }
    }

    private void setFragment(Fragment fragment){
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        //transaction.replace(R.id.frameContainer, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }




    /**
     * dialog to confirming user to logout.
     * @param title
     * @param message
     */
    public void confirmationLogoutAlertDialog(String title, String message) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(HomeActivity.this);
        // set title
        alertDialogBuilder.setTitle(title);
        // set dialog message
        alertDialogBuilder
                .setMessage(message)
                .setCancelable(false)
                .setPositiveButton(getResources().getString(R.string.CommonYes), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        AppSPrefs.clearAppSPrefs();
                        finish();
                        startActivity(new Intent(mContext, LoginActivity.class));
                        dialog.cancel();
                        //call close ticket API
                    }
                });
        // set dialog message
        alertDialogBuilder
                .setCancelable(false)
                .setNegativeButton(getResources().getString(R.string.CommonNo), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        final AlertDialog alertDialog = alertDialogBuilder.create();
        // show it
        alertDialog.show();
        // change color of delete text
        alertDialog.getButton(DialogInterface.BUTTON_POSITIVE).setTextColor(HomeActivity.this.getResources().getColor(R.color.colorButtonBG));
       // alertDialog.getButton(DialogInterface.BUTTON_NEGATIVE).setTextColor(HomeActivity.this.getResources().getColor(R.color.colorBlack));
    }
}
