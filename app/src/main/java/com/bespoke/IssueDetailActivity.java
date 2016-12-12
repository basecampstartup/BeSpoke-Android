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
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.bespoke.Model.TicketModel;
import com.bespoke.callback.APIRequestCallback;
import com.bespoke.network.CheckNetwork;
import com.bespoke.servercommunication.APIUtils;
import com.bespoke.servercommunication.CommunicatorNew;
import com.bespoke.utils.TicketStatus;
import com.bespoke.utils.Utils;

import org.json.JSONObject;

import java.util.HashMap;

public class IssueDetailActivity extends AppCompatActivity implements View.OnClickListener, APIRequestCallback{
    private Toolbar mToolbar;
    private Context mContext;
    Button btnCloseTicket,btnEmailUser;
    private TextView tvIdValue,tvShortDescriptionValue,tvDescriptionValue,tvCategoryValue,tvAffectedAreaValue,tvUserValue,tvIssueOpenDateValue,tvAssignedToValue,tvStatusValue;
    private ProgressDialog loader = null;
    TicketModel model;
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

    }


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
        btnCloseTicket = (Button) findViewById(R.id.btnEmailUser);
        btnCloseTicket.setOnClickListener(this);
        btnEmailUser = (Button) findViewById(R.id.btnCloseTicket);
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
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.issue_detail_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnCloseTicket:
                confirmationAlertDialog(getResources().getString(R.string.Alert),getResources().getString(R.string.ConfirmCloseTicket));
                break;
            default:
                break;
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        this.finish();
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
                            new CommunicatorNew(mContext, Request.Method.POST, APIUtils.METHOD_UPDATE_TICKET, map);
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

    @Override
    public void onSuccess(String name, Object object) {

        if (APIUtils.METHOD_UPDATE_TICKET.equalsIgnoreCase(name)) {
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
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Utils.alertDialog(mContext,getResources().getString(R.string.ErrorTitle),getResources().getString(R.string.ErrorInUpdateTicket));
                                Toast.makeText(mContext, "Error in Updating Ticket status please try later!", Toast.LENGTH_SHORT).show();
                            }
                        });

                    }
                }else {
                    // In case of error occured.
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
}
