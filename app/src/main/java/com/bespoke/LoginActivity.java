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
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.bespoke.Model.UserModel;
import com.bespoke.callback.APIRequestCallback;
import com.bespoke.commons.Commons;
import com.bespoke.network.CheckNetwork;
import com.bespoke.servercommunication.APIUtils;
import com.bespoke.servercommunication.Communicator;
import com.bespoke.servercommunication.CommunicatorNew;
import com.bespoke.servercommunication.ResponseParser;
import com.bespoke.sprefs.AppSPrefs;
import com.bespoke.utils.EmailSyntaxChecker;
import com.bespoke.utils.Logger;
import com.bespoke.utils.Utils;

import org.json.JSONObject;

import java.util.HashMap;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener, APIRequestCallback{
    private final String TAG = getClass().getSimpleName();
    private Context mContext;
    Button btnLogin, btnNewUser;
    EditText edtEmail, edtPassword;
    private long mLastClickTime = 0;
    String strEmail,strPassword;
    TextView txtForgetPassword;
    private ProgressDialog loader = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mContext=this;
        initializeComponents();
        //set loader
        loader = new ProgressDialog(this);
        loader.setMessage(getString(R.string.MessagePleaseWait));
        loader.setCancelable(false);
    }
    /**
     * Initialize the UI components.
     */
    public void initializeComponents() {
        btnLogin = (Button) findViewById(R.id.btnLogin);
        btnLogin.setOnClickListener(this);
        btnNewUser = (Button) findViewById(R.id.btnNewUser);
        btnNewUser.setOnClickListener(this);
        edtPassword = (EditText) findViewById(R.id.edtPassword);
        edtEmail = (EditText) findViewById(R.id.edtEmailID);
        txtForgetPassword=(TextView)findViewById(R.id.txtForgetPassword);
        txtForgetPassword.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnLogin:

                //This will check if your click on button successively.
                if (SystemClock.elapsedRealtime() - mLastClickTime < Commons.THRESHOLD_TIME_POST_SCREEN) {
                    return;
                }
                strEmail = edtEmail.getText().toString().trim();
                strPassword = edtPassword.getText().toString().trim();
                if (!validate()) {
                    return;
                }

                if (CheckNetwork.isInternetAvailable(mContext)) {
                    loader.show();
                    //Call API Request after check internet connection
                    new CommunicatorNew(mContext, Request.Method.POST, APIUtils.METHOD_LOGIN,
                            getLoginRequestMap(strEmail, strPassword));
                } else {
                    Logger.i(TAG, "Not connected to Internet.");
                    Toast.makeText(mContext, mContext.getString(R.string.MessageNoInternetConnection), Toast.LENGTH_LONG).show();
                }

                 /* startActivity(new Intent(LoginActivity.this, HomeActivity.class));
                 finish();*/
                break;
            case R.id.btnNewUser:
                startActivity(new Intent(LoginActivity.this, NewAccountActivity.class));
                finish();
                break;
            case R.id.txtForgetPassword:
                startActivity(new Intent(LoginActivity.this, ForgotPasswordActivity.class));
                break;
        }
    }


    /**
     * This method validate all the required fields.
     *
     * @return
     */
    public boolean validate() {
        boolean valid = true;
        String email = edtEmail.getText().toString();
        String password = edtPassword.getText().toString();

        if (email.isEmpty() || !EmailSyntaxChecker.check(email)) {
            edtEmail.setError(getString(R.string.SignUpEmailRequired));
            valid = false;
        } else {
            edtEmail.setError(null);
        }

        if (password.isEmpty()/* || password.length() < 4 || password.length() > 10*/) {
            edtPassword.setError(getString(R.string.SignUpPasswordRequired));
            valid = false;
        } else {
            edtPassword.setError(null);
        }
        return valid;
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

        if (APIUtils.METHOD_LOGIN.equalsIgnoreCase(name)) {
            try {
                final JSONObject responseObject = new JSONObject(object.toString());
                String error = responseObject.optString("error");
                if (TextUtils.isEmpty(error)) {

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            UserModel model= ResponseParser.parseLoginResponse(responseObject);
                            AppSPrefs.setAlreadyLoggedIn(true);
                            AppSPrefs.setString(Commons.USER_NAME, model.getUserName());
                            AppSPrefs.setString(Commons.EMAIL, model.getEmail());
                            AppSPrefs.setString(Commons.USER_ID, model.getUser_id());
                            AppSPrefs.setString(Commons.USER_TYPE, model.getUsertype());
                            String username=AppSPrefs.getString(Commons.USER_NAME);
                            String usertype=AppSPrefs.getString(Commons.USER_TYPE);
                            String email=AppSPrefs.getString(Commons.EMAIL);
                            String userid=AppSPrefs.getString(Commons.USER_ID);

                            startActivity(new Intent(LoginActivity.this, HomeActivity.class));
                            finish();
                        }
                    });


                } else {
                    // error msg here
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            String message = responseObject.optString("message");
                            //Toast.makeText(mContext, ""+message, Toast.LENGTH_SHORT).show();
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
                if(loader!=null) {
                    loader.dismiss();
                }
                Utils.alertDialog(mContext,getResources().getString(R.string.ErrorTitle),getResources().getString(R.string.SomethingWentWrong));
                //Toast.makeText(mContext, "On Failure of login", Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * @param email
     * @param password
     * @return
     */
    public HashMap<String, String> getLoginRequestMap(String email, String password) {
        HashMap<String, String> map = new HashMap<>();
        map.put(APIUtils.PARAM_EMAIL, email);
        map.put(APIUtils.PARAM_PASSWORD, password);
        return map;
    }
}
