//===============================================================================
// (c) 2016 Basecamp Startups Pvt. Ltd.  All rights reserved.
// Original Author: Ankur Sharma
// Original Date: 09/12/2016
//===============================================================================
package com.bespoke;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.bespoke.Model.TicketModel;
import com.bespoke.callback.APIRequestCallback;
import com.bespoke.commons.Commons;
import com.bespoke.network.CheckNetwork;
import com.bespoke.servercommunication.APIUtils;
import com.bespoke.servercommunication.CommunicatorNew;
import com.bespoke.sprefs.AppSPrefs;
import com.bespoke.utils.TicketStatus;
import com.bespoke.utils.UserTypeEnum;
import com.bespoke.utils.Utils;

import org.json.JSONObject;

import java.util.HashMap;

public class IssueDetailActivity extends AppCompatActivity implements View.OnClickListener, APIRequestCallback{
    private Toolbar mToolbar;
    /** context of current Activity */
    private Context mContext;
    Button btnCloseTicket,btnEmailUser;
    private TextView tvIdValue,tvShortDescriptionValue,tvDescriptionValue,tvCategoryValue,tvAffectedAreaValue,tvUserValue,tvIssueOpenDateValue,tvAssignedToValue,tvStatusValue;
    private ProgressDialog loader = null;
    TicketModel model;
    boolean isRecordUpdated=false;
    LinearLayout layoutBtns;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_issue_detail);
        mContext=this;
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(getString(R.string.IssuesDetail));
        loader = new ProgressDialog(this);
        loader.setMessage(getString(R.string.MessagePleaseWait));
        loader.setCancelable(false);
        initializeComponents();
        Intent intent = getIntent();
         model= (TicketModel) intent.getExtras().getSerializable("SelectedModel");
        setDataOnComponents(model);
        String usertype=AppSPrefs.getString(Commons.USER_TYPE);
        if(UserTypeEnum.NORMALUSER.getId()==Integer.parseInt(usertype))
        {
            layoutBtns.setVisibility(View.GONE);
            invalidateOptionsMenu();
        }

    }

    /**
     * This method will set all model data of issue to UI components.
     * @param model
     */
    public void setDataOnComponents(TicketModel model)
    {
      tvIdValue.setText(model.getTicket_id());
        tvShortDescriptionValue.setText(model.getShortdesc());
        tvDescriptionValue.setText(model.getDescription());
        tvCategoryValue.setText(model.getCategoryName());
        tvAffectedAreaValue.setText(model.getSubCategoryName());
        tvUserValue.setText(model.getUserName());
        tvIssueOpenDateValue.setText(model.getTicketopendate());
        tvAssignedToValue.setText(model.getAssignedToName());
        tvStatusValue.setText(TicketStatus.keyToEnum(model.getTicketstatus()).toString());
    }

    /**
     * Initialize the UI components.
     */
    public void initializeComponents() {
        btnCloseTicket = (Button) findViewById(R.id.btnCloseTicket);
        btnCloseTicket.setOnClickListener(this);
        btnEmailUser = (Button) findViewById(R.id.btnEmailUser);
        btnEmailUser.setOnClickListener(this);
        tvIdValue=(TextView)findViewById(R.id.tvIDValue);
        tvShortDescriptionValue=(TextView)findViewById(R.id.tvShortDescriptionValue);
        tvDescriptionValue=(TextView)findViewById(R.id.tvDescriptionValue);
        tvCategoryValue=(TextView)findViewById(R.id.tvCategoryValue);
        tvAffectedAreaValue=(TextView)findViewById(R.id.tvAffectedAreaValue);
        tvUserValue=(TextView)findViewById(R.id.tvUserValue);
        tvIssueOpenDateValue=(TextView)findViewById(R.id.tvIssueOpenDateValue);
        tvAssignedToValue=(TextView)findViewById(R.id.tvAssignedToValue);
        tvStatusValue=(TextView)findViewById(R.id.tvTicketStatusValue);
        layoutBtns=(LinearLayout)findViewById(R.id.layoutBtns);
    }

    /**
     * This is the overrided method to show option menu  items on header or Appbar.
     * @param menu
     * @return
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.issue_detail_menu, menu);
    //    MenuItem item = menu.findItem(R.id.addAction);
        (menu.findItem(R.id.menuUpdate)).setVisible(true);
        (menu.findItem(R.id.menuDoc)).setVisible(true);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        String usertype=AppSPrefs.getString(Commons.USER_TYPE);
        MenuItem item = (MenuItem) menu.findItem(R.id.menuUpdate);
        MenuItem itemDoc = (MenuItem) menu.findItem(R.id.menuDoc);
        if(UserTypeEnum.NORMALUSER.getId()==Integer.parseInt(usertype))
        {
            item.setVisible(false);

        }
        return super.onPrepareOptionsMenu(menu);
    }

    /**
     * Overrided method for handling menu option item clicks.
     * @param item
     * @return
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            case R.id.menuUpdate:
                Intent i=new Intent(IssueDetailActivity.this,UpdateIssueActivity.class);
                i.putExtra("SelectedModel", model);
                startActivityForResult(i,1);
                return  true;
            case R.id.menuDoc:
                Intent intent=new Intent(IssueDetailActivity.this,DocumentsListActivity.class);
                intent.putExtra("CategoryID", model.getCat_id());
                startActivity(intent);
                return  true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    /**
     *  Overridden method to handle clicks of UI components
     *  @param v
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnCloseTicket:
                if(model.getTicketstatus()==TicketStatus.CLOSED.getId())
                {
                    Toast.makeText(mContext,getResources().getString(R.string.AlreadyClosed),Toast.LENGTH_LONG).show();
                }
                else{
                    confirmationAlertDialog(getResources().getString(R.string.Alert),getResources().getString(R.string.ConfirmCloseTicket));
                }

                break;
            case R.id.btnEmailUser:
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("text/html");
                intent.putExtra(Intent.EXTRA_EMAIL, AppSPrefs.getString(Commons.EMAIL));
                intent.putExtra(Intent.EXTRA_SUBJECT, "Subject");
                intent.putExtra(Intent.EXTRA_TEXT, "Email body.");
                startActivity(intent);
              //  startActivity(Intent.createChooser(intent, "Send Email"));
            default:
                break;
        }
    }

    /**
     * Overridden method will execute when user click on back button of device.
     */
    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        if(isRecordUpdated)
        setResult(1);
        finish();
    }




    /**
     * dialog to confirming user to logout.
     * @param title
     * @param message
     */
    public void confirmationAlertDialog(String title, String message) {
       AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                IssueDetailActivity.this);
        // set title
        alertDialogBuilder.setTitle(title);
        // set dialog message
        alertDialogBuilder
                .setMessage(message)
                .setCancelable(false)
                .setPositiveButton(getResources().getString(R.string.CommonYes), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                        if (CheckNetwork.isInternetAvailable(mContext)) {
                            loader.show();
                            HashMap<String,String> map = new HashMap<String, String>();
                            map.put(APIUtils.TICKET_STATUS,TicketStatus.CLOSED.getId()+"");
                            map.put(APIUtils.TICKET_ID,String.valueOf(model.getTicket_id()));
                            map.put(APIUtils.TICKET_TYPE,String.valueOf(model.getTickettype()));
                            new CommunicatorNew(mContext, Request.Method.POST, APIUtils.METHOD_UPDATE_TICKET_STATUS, map);
                        } else {
                            Utils.alertDialog(mContext,getResources().getString(R.string.Alert),mContext.getString(R.string.MessageNoInternetConnection));
                        }

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
        alertDialog.getButton(DialogInterface.BUTTON_POSITIVE).setTextColor(IssueDetailActivity.this.getResources().getColor(R.color.colorButtonBG));
        alertDialog.getButton(DialogInterface.BUTTON_NEGATIVE).setTextColor(IssueDetailActivity.this.getResources().getColor(R.color.colorBlack));
    }

    /**
     * This is a overridden method to Handle API call response.
     * @param name   string call name returned from ajax response on success
     * @param object object returned from ajax response on success
     */
    @Override
    public void onSuccess(String name, Object object) {

        if (APIUtils.METHOD_UPDATE_TICKET_STATUS.equalsIgnoreCase(name)) {
            try {
                final JSONObject responseObject = new JSONObject(object.toString());
                String error = responseObject.optString("error");
                if (TextUtils.isEmpty(error)) {
                    String success = responseObject.optString("success");
                    if (success.equalsIgnoreCase("true")) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                issueUpdateSucessDialog(getResources().getString(R.string.TicketUpdatedSuccessfully));
                            }
                        });
                    }
                    else
                    {
                        // Show Error message in case of any failure in Server API call.
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Utils.alertDialog(mContext,getResources().getString(R.string.ErrorTitle),getResources().getString(R.string.ErrorInUpdateTicket));
                                Toast.makeText(mContext, "Error in Updating Ticket status please try later!", Toast.LENGTH_SHORT).show();
                            }
                        });

                    }
                }else {
                    // Show Error message in case of any failure in Server API call.
                    final String message = responseObject.optString("message");
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(mContext, "" + message, Toast.LENGTH_SHORT).show();
                            Utils.alertDialog(mContext,getResources().getString(R.string.ErrorTitle),message);
                        }
                    });

                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * This is a overridden method to Handle API call in case of Failure response.
     * @param name   string call name returned from ajax response on failure
     * @param object returned from ajax response on failure
     */
    @Override
    public void onFailure(String name, Object object) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (loader != null) {
                    loader.dismiss();
                }
                Utils.alertDialog(mContext,getResources().getString(R.string.ErrorTitle),getResources().getString(R.string.SomethingWentWrong));
            }
        });
    }
    /**
     * To display alert dialog for invalid fields
     *
     * @param message (msg to display)
     */

    public void issueUpdateSucessDialog(String message) {
        ;
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        // set dialog message
        alertDialogBuilder
                .setMessage(message)
                .setCancelable(false)
                .setPositiveButton(getResources().getString(R.string.CommonOK), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        setResult(1);
                        finish();
                        dialog.cancel();
                    }
                });
        final AlertDialog alertDialog = alertDialogBuilder.create();
        // show it
        alertDialog.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode==1)
        {
            String updatedAssignedToName=data.getStringExtra("AssignedToName");
            String updatedStatus=data.getStringExtra("TicketStatus");
            tvAssignedToValue.setText(updatedAssignedToName);
            tvStatusValue.setText(updatedStatus);
            isRecordUpdated=true;

        }
    }

}
