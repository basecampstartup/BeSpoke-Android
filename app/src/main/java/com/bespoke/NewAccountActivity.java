//===============================================================================
// (c) 2016 Basecamp Startups Pvt. Ltd.  All rights reserved.
// Original Author: Ankur Sharma
// Original Date: 29/11/2016
//===============================================================================
package com.bespoke;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.bespoke.callback.APIRequestCallback;
import com.bespoke.commons.Commons;
import com.bespoke.network.CheckNetwork;
import com.bespoke.servercommunication.APIUtils;
import com.bespoke.servercommunication.CommunicatorNew;
import com.bespoke.utils.EmailSyntaxChecker;
import com.bespoke.utils.UserTypeEnum;
import com.bespoke.utils.Utils;

import org.json.JSONObject;

import java.util.HashMap;

public class NewAccountActivity extends AppCompatActivity implements View.OnClickListener, APIRequestCallback {

    EditText edtEmailId,edtPassword,edtConfirmPassword,edtUserName;
    RadioGroup radioRoleGroup;
    Button btnCreateAccount, btnCancel;
    TextView txtAlreadyRegistered;
    private long mLastClickTime = 0;
    String strEmail,strPassword,strReEnteredPassword,strUserName,strUserType;
    RadioGroup radGroupRole;
    RadioButton radAdmin,radSuperAdmin,radNormalUser;
    /** context of current Activity */
    private Context mContext;
    private ProgressDialog loader = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_account);
        mContext=this;
        initializeComponents();
        //set loader
        loader = new ProgressDialog(this);
        loader.setMessage(getString(R.string.MessagePleaseWait));
    }

    /**
     * Initialize the UI components.
     */
    public void initializeComponents() {
        btnCreateAccount = (Button) findViewById(R.id.btnCreateAccount);
        btnCreateAccount.setOnClickListener(this);
        btnCancel = (Button) findViewById(R.id.btnCancel);
        btnCancel.setOnClickListener(this);
        edtPassword = (EditText) findViewById(R.id.edtPassword);
        edtEmailId = (EditText) findViewById(R.id.edtEmailID);
        edtUserName = (EditText) findViewById(R.id.edtUserName);
        edtConfirmPassword = (EditText) findViewById(R.id.edtConfirmPassword);
        txtAlreadyRegistered=(TextView) findViewById(R.id.txtAlreadyRegistered);
        radGroupRole = (RadioGroup) findViewById(R.id.radGroupRole);
        radAdmin = (RadioButton) findViewById(R.id.radAdmin);
        radSuperAdmin = (RadioButton) findViewById(R.id.radSuperAdmin);
        radNormalUser = (RadioButton) findViewById(R.id.radNormalUser);
        txtAlreadyRegistered.setOnClickListener(this);
    }

    /**
     *  Overridden method to handle clicks of UI components
     *  @param v
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnCreateAccount:

                //This will handle  multiple click on button at same time.
                if (SystemClock.elapsedRealtime() - mLastClickTime < Commons.THRESHOLD_TIME_POST_SCREEN) {
                    return;
                }
                strEmail = edtEmailId.getText().toString().trim();
                strPassword = edtPassword.getText().toString().trim();
                strReEnteredPassword = edtConfirmPassword.getText().toString().trim();
                strUserName = edtUserName.getText().toString().trim();

                if (!validate()) {
                    return;
                }
                if (radAdmin.isChecked() || radSuperAdmin.isChecked() || radNormalUser.isChecked()) {

                } else {
                    Utils.alertDialog(mContext, getResources().getString(R.string.Alert), getResources().getString(R.string.RoleAlert));
                    return;
                }
                if (!strPassword.equalsIgnoreCase(strReEnteredPassword)/* || password.length() < 4 || password.length() > 10*/) {
                    //Utils.showErrorDialog(mContext,getString(R.string.SignUpPasswordsNotMatch));
                    Utils.alertDialog(mContext,getResources().getString(R.string.Alert),getResources().getString(R.string.SignUpPasswordsNotMatch));
                    return;
                }

                String radiovalue = ((RadioButton) findViewById(radGroupRole.getCheckedRadioButtonId())).getText().toString();
                if(radiovalue.equalsIgnoreCase(getResources().getString(R.string.AdminText)))
                {
                    strUserType= UserTypeEnum.ADMIN.getId()+"";
                }
                if(radiovalue.equalsIgnoreCase(getResources().getString(R.string.SuperAdmin)))
                {
                    strUserType= UserTypeEnum.SUPERADMIN.getId()+"";
                }
                if(radiovalue.equalsIgnoreCase(getResources().getString(R.string.NormalUser)))
                {
                    strUserType= UserTypeEnum.NORMALUSER.getId()+"";
                }
                if (CheckNetwork.isInternetAvailable(mContext)) {
                    loader.show();
                    //Call API Request after check internet connection
                    new CommunicatorNew(mContext, Request.Method.POST, APIUtils.METHOD_REGISTER_USER,
                            getRegistrationRequestMap(strEmail, strPassword,strUserName,strUserType));
                } else {
                    Toast.makeText(mContext, mContext.getString(R.string.MessageNoInternetConnection), Toast.LENGTH_LONG).show();
                }
                break;
            case R.id.txtAlreadyRegistered:
                startActivity(new Intent(NewAccountActivity.this, LoginActivity.class));
                break;
            case R.id.btnCancel:
                this.finish();
                break;

        }
    }


    /**
     * This method validate all the required fields.
     * @return
     */
    public boolean validate() {
        boolean valid = true;
        String email = edtEmailId.getText().toString();
        String password = edtPassword.getText().toString();
        String rePassword = edtConfirmPassword.getText().toString();
        String username = edtUserName.getText().toString();

        if (email.isEmpty() || !EmailSyntaxChecker.check(email)) {
            edtEmailId.setError(getString(R.string.SignUpEmailRequired));
            valid = false;
        } else {
            edtEmailId.setError(null);
        }

        if (password.isEmpty()/* || password.length() < 4 || password.length() > 10*/) {
            edtPassword.setError(getString(R.string.SignUpPasswordRequired));
            valid = false;
        } else {
            edtPassword.setError(null);
        }
        if (rePassword.isEmpty()/* || password.length() < 4 || password.length() > 10*/) {
            edtConfirmPassword.setError(getString(R.string.SignUpRePasswordRequired));
            valid = false;
        } else {
            edtConfirmPassword.setError(null);
        }
        if (username.isEmpty()/* || password.length() < 4 || password.length() > 10*/) {
            edtUserName.setError(getString(R.string.SignUpUserNameRequired));
            valid = false;
        } else {
            edtUserName.setError(null);
        }
        return valid;
    }

    /**
     * @param email
     * @param password
     * @return
     */
    public HashMap<String, String> getRegistrationRequestMap(String email, String password,String username,String userType) {
        HashMap<String, String> map = new HashMap<>();
        map.put(APIUtils.USER_NAME_SMALL, username);
        map.put(APIUtils.PARAM_EMAIL, email);
        map.put(APIUtils.PARAM_PASSWORD, password);
        map.put(APIUtils.PARAM_USER_TYPE, userType);
        return map;
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

        if (APIUtils.METHOD_REGISTER_USER.equalsIgnoreCase(name)) {
            try {
                JSONObject responseObject = new JSONObject(object.toString());
                String error = responseObject.optString("error");
                if (TextUtils.isEmpty(error)) {
                    String success = responseObject.optString("success");
                    if(success.equalsIgnoreCase("true"))
                    {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                RegisterCompleateDialog(getResources().getString(R.string.RegisterdSuccessfully));
                             //   Toast.makeText(mContext, "User Registerd Successfully!", Toast.LENGTH_SHORT).show();

                            }
                        });

                    }
                    else
                    {
                        // Show Error message in case of any failure in Server API call.
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Utils.alertDialog(mContext,getResources().getString(R.string.ErrorTitle),getResources().getString(R.string.SomethingWentWrong));
                            }
                        });

                    }
                }
                else {
                    // Show Error message in case of any failure in Server API call.
                    final String messageFromServer = responseObject.optString("message");
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Utils.alertDialog(mContext,getResources().getString(R.string.ErrorTitle),messageFromServer);
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
                if(loader!=null) {
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

    public void RegisterCompleateDialog(String message) {
       ;
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        // set dialog message
        alertDialogBuilder
                .setMessage(message)
                .setCancelable(false)
                .setPositiveButton(getResources().getString(R.string.CommonOK), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        startActivity(new Intent(NewAccountActivity.this, LoginActivity.class));
                        finish();
                        dialog.cancel();
                    }
                });
        final AlertDialog alertDialog = alertDialogBuilder.create();
        // show it
        alertDialog.show();
        //alertDialog.getButton(DialogInterface.BUTTON_POSITIVE);
    }

}
