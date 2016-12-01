package com.bespoke;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.bespoke.callback.APIRequestCallback;
import com.bespoke.commons.Commons;
import com.bespoke.network.CheckNetwork;
import com.bespoke.servercommunication.APIUtils;
import com.bespoke.servercommunication.Communicator;
import com.bespoke.servercommunication.CommunicatorNew;
import com.bespoke.utils.EmailSyntaxChecker;
import com.bespoke.utils.Logger;
import com.bespoke.utils.UserTypeEnum;

import org.json.JSONObject;

import java.util.HashMap;


public class ForgotPasswordActivity extends AppCompatActivity implements View.OnClickListener, APIRequestCallback{
    RelativeLayout rlRoot;
    EditText edtEmailId,edtPassword,edtConfirmPassword;
    String strEmail,strPassword,strReEnteredPassword;
    Button btnResetPassword, btnCancel;
    private ProgressDialog loader = null;
    private Context mContext;
    private long mLastClickTime = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);
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
        btnResetPassword = (Button) findViewById(R.id.btnResetPassword);
        btnResetPassword.setOnClickListener(this);
        btnCancel = (Button) findViewById(R.id.btnCancel);
        btnCancel.setOnClickListener(this);
        edtPassword = (EditText) findViewById(R.id.edtPassword);
        edtEmailId = (EditText) findViewById(R.id.edtEmailID);
        edtConfirmPassword = (EditText) findViewById(R.id.edtConfirmPassword);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnResetPassword:
                //This will check if your click on button successively.
                if (SystemClock.elapsedRealtime() - mLastClickTime < Commons.THRESHOLD_TIME_POST_SCREEN) {
                    return;
                }
                strEmail = edtEmailId.getText().toString().trim();
                strPassword = edtPassword.getText().toString().trim();
                strReEnteredPassword = edtConfirmPassword.getText().toString().trim();
                if (!validate()) {
                    return;
                }
                if (CheckNetwork.isInternetAvailable(mContext)) {
                    loader.show();
                    //Call API Request after check internet connection
                    new CommunicatorNew(mContext, Request.Method.POST, APIUtils.METHOD_FORGOT_PASSWORD,
                            getForgotPasswordRequestMap(strEmail, strPassword));
                } else {
                    Toast.makeText(mContext, mContext.getString(R.string.MessageNoInternetConnection), Toast.LENGTH_LONG).show();
                }
                break;
            case R.id.txtAlreadyRegistered:
                startActivity(new Intent(ForgotPasswordActivity.this, LoginActivity.class));
                break;
            case R.id.btnCancel:
                this.finish();
                break;

        }
    }

    @Override
    public void onSuccess(String name, Object object) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {

                if(loader!=null) {
                    loader.dismiss();
                }
            }
        });

        if (APIUtils.METHOD_FORGOT_PASSWORD.equalsIgnoreCase(name)) {
            try {
               final  JSONObject responseObject = new JSONObject(object.toString());
                String error = responseObject.optString("error");
                if (TextUtils.isEmpty(error)) {
                    String success = responseObject.optString("success");
                    if(success.equalsIgnoreCase("true"))
                    {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(mContext, "Your Password reset Successfully", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(ForgotPasswordActivity.this, LoginActivity.class));
                                finish();
                            }
                        });
                    }
                    else{
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(mContext, "Something went wrong please try again later!", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                }else {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            String message = responseObject.optString("message");
                            Toast.makeText(mContext, ""+message, Toast.LENGTH_SHORT).show();
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
                if(loader!=null) {
                    loader.dismiss();
                }
                Toast.makeText(mContext, "OnFailure: Something went wrong please try again later!", Toast.LENGTH_SHORT).show();
            }
        });

    }

    /**
     * This method validate all the required fields.
     *
     * @return
     */
    public boolean validate() {
        boolean valid = true;
        String email = edtEmailId.getText().toString();
        String password = edtPassword.getText().toString();
        String rePassword = edtConfirmPassword.getText().toString();

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
        if (!password.equalsIgnoreCase(rePassword)/* || password.length() < 4 || password.length() > 10*/) {
            edtPassword.setError(getString(R.string.SignUpPasswordsNotMatch));
            valid = false;
        } else {
            edtPassword.setError(null);
        }

        return valid;
    }

    /**
     * @param email
     * @param password
     * @return
     */
    public HashMap<String, String> getForgotPasswordRequestMap(String email, String password) {
        HashMap<String, String> map = new HashMap<>();
        map.put(APIUtils.PARAM_EMAIL, email);
        map.put(APIUtils.PARAM_NEW_PASSWORD, password);
        return map;
    }
}
