//===============================================================================
// (c) 2016 Basecamp Startups Pvt. Ltd.  All rights reserved.
// Original Author: Ankur Sharma
// Original Date: 12/12/2016
//===============================================================================
package com.bespoke;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.bespoke.Model.IssueModel;
import com.bespoke.Model.TicketModel;
import com.bespoke.Model.UserModel;
import com.bespoke.adapter.UserListDialogAdapter;
import com.bespoke.callback.APIRequestCallback;
import com.bespoke.commons.Commons;
import com.bespoke.network.CheckNetwork;
import com.bespoke.servercommunication.APIUtils;
import com.bespoke.servercommunication.CommunicatorNew;
import com.bespoke.servercommunication.ResponseParser;
import com.bespoke.utils.TicketStatus;
import com.bespoke.utils.Utils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;

public class UpdateRequestActivity extends AppCompatActivity implements View.OnClickListener, APIRequestCallback{
    private Toolbar mToolbar;
    /** context of current Activity */
    private Context mContext;
    Button btnUpdateTicket, btnCancel;
    private TextView tvIdValue,tvShortDescriptionValue,tvDescriptionValue,tvCategoryValue,tvAffectedAreaValue,tvUserValue,tvIssueOpenDateValue,tvAssignedToValue,tvStatusValue;
    private ProgressDialog loader = null;
    TextView tvId;

    TicketModel model;
    ArrayList<UserModel> userList;

    String selectedUserId;
    int selectedTicketStatusId;
    String selectedUserName;
    String selectTicketStatusText;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_request);
        mContext=this;
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(getString(R.string.UpdateRequestTitle));
        initializeComponents();
        loader = new ProgressDialog(this);
        loader.setMessage(getString(R.string.MessagePleaseWait));
        loader.setCancelable(false);
        Intent intent = getIntent();
        model= (TicketModel) intent.getExtras().getSerializable("SelectedModel");
        selectTicketStatusText= TicketStatus.keyToEnum(model.getTicketstatus()).toString();
        selectedUserName=model.getAssignedToName();
        setDataOnComponents(model);
        if (CheckNetwork.isInternetAvailable(mContext)) {
            loader.show();
            //Call API Request after check internet connection
            HashMap<String, String> map = new HashMap<String, String>();
            map.put(APIUtils.PARAM_USER_TYPE, String.valueOf(Commons.TWO));
            new CommunicatorNew(mContext, Request.Method.GET, APIUtils.METHOD_GET_USER_BY_TYPE, map);
        } else {
            Toast.makeText(mContext, mContext.getString(R.string.MessageNoInternetConnection), Toast.LENGTH_LONG).show();
        }
    }
    /**
     * Initialize the UI components.
     */
    public void initializeComponents() {
        btnUpdateTicket = (Button) findViewById(R.id.btnUpdateTicket);
        btnUpdateTicket.setOnClickListener(this);
        btnCancel = (Button) findViewById(R.id.btnCancel);
        btnCancel.setOnClickListener(this);
        tvIdValue=(TextView)findViewById(R.id.tvIDValue);
        tvShortDescriptionValue=(TextView)findViewById(R.id.tvShortDescriptionValue);
        tvDescriptionValue=(TextView)findViewById(R.id.tvDescriptionValue);
        tvCategoryValue=(TextView)findViewById(R.id.tvCategoryValue);
        tvAffectedAreaValue=(TextView)findViewById(R.id.tvAffectedAreaValue);
        tvUserValue=(TextView)findViewById(R.id.tvUserValue);
        tvIssueOpenDateValue=(TextView)findViewById(R.id.tvIssueOpenDateValue);
        tvAssignedToValue=(TextView)findViewById(R.id.tvAssignedToValue);
        tvStatusValue=(TextView)findViewById(R.id.tvTicketStatusValue);
        tvAssignedToValue.setOnClickListener(this);
        tvStatusValue.setOnClickListener(this);
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

    public void showUserListDialog() {

        final Dialog dialog = new Dialog(this);
        View view = getLayoutInflater().inflate(R.layout.list_dialog_layout, null);
        ListView lv = (ListView) view.findViewById(R.id.lstCategory);
        UserListDialogAdapter userListDialogAdapter = new UserListDialogAdapter(UpdateRequestActivity.this, userList);
        lv.setAdapter(userListDialogAdapter);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                UserModel user = userList.get(position);
                tvAssignedToValue.setText(user.getUserName());
                selectedUserId = userList.get(position).getUser_id();
                selectedUserName=user.getUserName();
                dialog.dismiss();
            }
        });
        dialog.setContentView(view);
        dialog.show();
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

    /**
     * Overridden method will execute when user click on back button of device.
     */
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        this.finish();
    }


    public void showTicketStatusDialog() {

        final Dialog dialog = new Dialog(this);
        View view = getLayoutInflater().inflate(R.layout.list_dialog_layout, null);
        ListView lv = (ListView) view.findViewById(R.id.lstCategory);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, android.R.id.text1, getTicketStatusLabelList());
        lv.setAdapter(adapter);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String status = getTicketStatusLabelList().get(position);
                tvStatusValue.setText(status);
                //  selectedTicketStatusId = TicketStatus.getType(status).getId();
                selectedTicketStatusId=TicketStatus.getType(status.toUpperCase()).getId();
                selectTicketStatusText=status;
                // selectedTicketStatusId = getTicketStatusIdList().get(position);
                dialog.dismiss();
            }
        });
        dialog.setContentView(view);
        dialog.show();
    }

    public ArrayList<String> getTicketStatusLabelList() {
        ArrayList<String> list = new ArrayList<>();
        list.add("Open");
        list.add("Inprogress");
        list.add("Closed");
        return list;
    }

    /**
     *  Overridden method to handle clicks of UI components
     *  @param v
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnUpdateTicket:
                confirmationAlertDialog(getResources().getString(R.string.Alert),getResources().getString(R.string.ConfirmCloseTicket));
                break;
            case R.id.tvAssignedToValue:
                showUserListDialog();
                break;
            case R.id.tvTicketStatusValue:
                showTicketStatusDialog();
                break;
            default:
                break;
        }
    }

    /**
     * This is a overridden method to Handle API call response.
     * @param name   string call name returned from ajax response on success
     * @param object object returned from ajax response on success
     */
    @Override
    public void onSuccess(String name, Object object) {

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (loader != null) {
                    loader.dismiss();
                }
            }
        });
        if (APIUtils.METHOD_GET_USER_BY_TYPE.equalsIgnoreCase(name)) {
            try {
                final JSONObject responseObject = new JSONObject(object.toString());
                String error = responseObject.optString("error");
                if (TextUtils.isEmpty(error)) {

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            userList = ResponseParser.parseUserResponse(responseObject);
                        }
                    });


                } else {
                    // Show Error message in case of any failure in Server API call.
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            String message = responseObject.optString("message");
                            Utils.alertDialog(mContext,getResources().getString(R.string.ErrorTitle),message);
                        }
                    });

                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }


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
                                issueUpdateSucessDialog(getResources().getString(R.string.TicketUpdatedSuccessfullyMsg));
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
                            }
                        });

                    }
                }else {
                    // Show Error message in case of any failure in Server API call.
                    final String message = responseObject.optString("message");
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
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

                        Intent intent=new Intent();
                        intent.putExtra("AssignedToName",selectedUserName);
                        intent.putExtra("TicketStatus",selectTicketStatusText);
                        setResult(1,intent);
                        finish();
                        dialog.cancel();
                    }
                });
        final AlertDialog alertDialog = alertDialogBuilder.create();
        // show it
        alertDialog.show();
    }

    /**
     * dialog to confirming user to logout.
     * @param title
     * @param message
     */
    public void confirmationAlertDialog(String title, String message) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                UpdateRequestActivity.this);
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
                            model.setAssignedto(selectedUserId);
                            model.setTicketstatus(selectedTicketStatusId);
                            //model.setTickettype(Commons.TICKET_TYPE_ISSUE);

                            Gson gson = new Gson();
                            Type type = new TypeToken<IssueModel>() {}.getType();
                            String json = gson.toJson(model, type);
                            HashMap<String, String> retMap = new Gson().fromJson(json, new TypeToken<HashMap<String, String>>() {}.getType());
                            Log.e("RequestJSon",json);
                            loader.show();
                            new CommunicatorNew(mContext, Request.Method.POST, APIUtils.METHOD_UPDATE_TICKET, retMap);
                            //Call API Request after check internet connection
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
        alertDialog.getButton(DialogInterface.BUTTON_POSITIVE).setTextColor(UpdateRequestActivity.this.getResources().getColor(R.color.colorButtonBG));
        alertDialog.getButton(DialogInterface.BUTTON_NEGATIVE).setTextColor(UpdateRequestActivity.this.getResources().getColor(R.color.colorBlack));
    }

}
