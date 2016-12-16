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
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.Request;
import com.bespoke.Model.Category;
import com.bespoke.adapter.CategoryListDialogAdapter;
import com.bespoke.callback.APIRequestCallback;
import com.bespoke.commons.Commons;
import com.bespoke.network.CheckNetwork;
import com.bespoke.servercommunication.APIUtils;
import com.bespoke.servercommunication.CommunicatorNew;
import com.bespoke.servercommunication.ResponseParser;
import com.bespoke.utils.Utils;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.HashMap;

public class CategoryActivity extends AppCompatActivity implements APIRequestCallback, View.OnClickListener {
    private Toolbar mToolbar;
    /** context of current Activity */
    private Context mContext;
    private ProgressDialog loader = null;
    ArrayList<Category> categoryList;

    //Declare UI components.
    TextView tvSelectCategoryLbl,tvSelectCategory;
    EditText edtCategoryName,edtSubCategoryName;
    Button btnCreateCategory,btnCreateSubCategory;

    private long mLastClickTime = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);
        mContext = this;
        categoryList=new ArrayList<Category>();
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(getString(R.string.LeftPanelCategory));
        initializeComponents();
        //set loader
        loader = new ProgressDialog(this);
        loader.setMessage(getString(R.string.MessagePleaseWait));
        if (CheckNetwork.isInternetAvailable(mContext)) {
            loader.show();
            //Call API Request after check internet connection
            new CommunicatorNew(mContext, Request.Method.GET, APIUtils.METHOD_GET_ALL_CATEGORY, new HashMap<String,String>());
        } else {
            //Toast.makeText(mContext, mContext.getString(R.string.MessageNoInternetConnection), Toast.LENGTH_LONG).show();
            Utils.alertDialog(mContext,getResources().getString(R.string.Alert),getResources().getString(R.string.MessageNoInternetConnection));
        }
    }
    /**
     * Initialize the UI components.
     */
    public void initializeComponents() {
        tvSelectCategoryLbl = (TextView) findViewById(R.id.tvSelectCategoryLbl);
        tvSelectCategory = (TextView) findViewById(R.id.tvSelectCategory);
        tvSelectCategoryLbl.setOnClickListener(this);
        tvSelectCategory.setOnClickListener(this);
        edtCategoryName = (EditText) findViewById(R.id.edtCategoryName);
        edtSubCategoryName = (EditText) findViewById(R.id.edtSubCategoryName);
        btnCreateCategory = (Button) findViewById(R.id.btnCreateCategory);
        btnCreateSubCategory = (Button) findViewById(R.id.btnCreateSubCategory);
        btnCreateCategory.setOnClickListener(this);
        btnCreateSubCategory.setOnClickListener(this);
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
     * This method will validate Category name when user create New Category.
     * @return
     */
    public boolean validateCategoryItem()
    {
        boolean valid = true;
        String strCategoryName = edtCategoryName.getText().toString();
        if (strCategoryName.isEmpty()) {
            edtCategoryName.setError(getString(R.string.EnterCategoryName));
            valid = false;
        } else {
            edtCategoryName.setError(null);
        }
        return valid;
    }

    /**
     * This method will validate Category name when user create New Category.
     * @return
     */
    public boolean validateSubCategoryItem()
    {
        boolean valid = true;
        String strCategoryName = tvSelectCategory.getText().toString();
        if (strCategoryName.equalsIgnoreCase(getResources().getString(R.string.Categorytxt))) {
            Utils.alertDialog(mContext, getResources().getString(R.string.Alert), getResources().getString(R.string.SelectCategoryAlert));
            return false;
        }
        String strSubCategoryName = edtSubCategoryName.getText().toString();
        if (strSubCategoryName.isEmpty()) {
            edtSubCategoryName.setError(getString(R.string.EnterCategoryName));
            valid = false;
        } else {
            edtSubCategoryName.setError(null);
        }
        return valid;
    }

    /**
     * Overridden method will execute when user click on back button of device.
     */
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        this.finish();
    }

    /**
     *  Overridden method to handle clicks of UI components
     *  @param v
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.tvSelectCategoryLbl:
                showCategoryDialog();
                break;
            case R.id.tvSelectCategory:
                showCategoryDialog();
                break;
            case R.id.btnCreateCategory:
                if (!validateCategoryItem()) return;
                String strCategoryName = edtCategoryName.getText().toString().trim();
                //This will handle  multiple click on button at same time.
                if (SystemClock.elapsedRealtime() - mLastClickTime < Commons.THRESHOLD_TIME_POST_SCREEN) {
                    return;
                }
                //This will handle  multiple click on button at same time.
                mLastClickTime = SystemClock.elapsedRealtime();
                if (CheckNetwork.isInternetAvailable(mContext)) {
                    loader.show();
                    HashMap<String, String> requestMap = new HashMap<>();
                    requestMap.put(APIUtils.CATEGORY_NAME, strCategoryName);
                    //Call API Request after check internet connection
                    new CommunicatorNew(mContext, Request.Method.POST, APIUtils.METHOD_CREATE_CATEGORY, requestMap);
                } else {
                    Utils.alertDialog(mContext, mContext.getString(R.string.Alert), getResources().getString(R.string.MessageNoInternetConnection));
                }
                break;
            case R.id.btnCreateSubCategory:
                if (!validateSubCategoryItem()) return;
                String strSubCategoryName = edtSubCategoryName.getText().toString().trim();
                if (SystemClock.elapsedRealtime() - mLastClickTime < Commons.THRESHOLD_TIME_POST_SCREEN) {
                    return;
                }
                //This will handle  multiple click on button at same time.
                mLastClickTime = SystemClock.elapsedRealtime();
                if (CheckNetwork.isInternetAvailable(mContext)) {
                    loader.show();
                    HashMap<String, String> requestMap = new HashMap<>();
                    requestMap.put(APIUtils.SUB_CATEGORY_NAME, strSubCategoryName);
                    requestMap.put(APIUtils.CATEGORY_ID, String.valueOf(selectedCategoryId));
                    //Call API Request after check internet connection
                    new CommunicatorNew(mContext, Request.Method.POST, APIUtils.METHOD_CREATE_SUB_CATEGORY, requestMap);
                } else {
                    Utils.alertDialog(mContext, mContext.getString(R.string.Alert), getResources().getString(R.string.MessageNoInternetConnection));
                }
                break;
            default:
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
        if (APIUtils.METHOD_GET_ALL_CATEGORY.equalsIgnoreCase(name)) {
            try {
               final JSONObject responseObject = new JSONObject(object.toString());
                String error = responseObject.optString("error");
                if (TextUtils.isEmpty(error)) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                categoryList = ResponseParser.parseCategoryResponse(responseObject);
                            }
                        });
                }
                else {
                    // Show message in case of any failure in Server API call.
                    final String message = responseObject.optString("message");
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                           // Toast.makeText(mContext, ""+message, Toast.LENGTH_SHORT).show();
                            Utils.alertDialog(mContext,getResources().getString(R.string.ErrorTitle),message);
                        }
                    });
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        if (APIUtils.METHOD_CREATE_CATEGORY.equalsIgnoreCase(name)) {
            try {
                final JSONObject responseObject = new JSONObject(object.toString());
                String error = responseObject.optString("error");
                if (TextUtils.isEmpty(error)) {
                    String success = responseObject.optString("success");
                    if (success.equalsIgnoreCase("true")) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                categoryCreatedSucessDialog(getResources().getString(R.string.CategoryCreatedSuccessfully));
                            }
                        });
                    }
                    else
                    {
                        // Show Error message in case of any failure in Server API call.
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Utils.alertDialog(mContext,getResources().getString(R.string.ErrorTitle),getResources().getString(R.string.ErrorInCreatingTicket));
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
        if (APIUtils.METHOD_CREATE_SUB_CATEGORY.equalsIgnoreCase(name)) {
            try {
                final JSONObject responseObject = new JSONObject(object.toString());
                String error = responseObject.optString("error");
                if (TextUtils.isEmpty(error)) {
                    String success = responseObject.optString("success");
                    if (success.equalsIgnoreCase("true")) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                subCategoryCreatedSucessDialog(getResources().getString(R.string.SubCategoryCreatedSuccessfully));
                            }
                        });
                    }
                    else
                    {
                        // Show Error message in case of any failure in Server API call.
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Utils.alertDialog(mContext,getResources().getString(R.string.ErrorTitle),getResources().getString(R.string.ErrorInCreatingTicket));
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
                if(loader!=null) {
                    loader.dismiss();
                }
                Utils.alertDialog(mContext,getResources().getString(R.string.ErrorTitle),getResources().getString(R.string.SomethingWentWrong));
            }
        });
    }

    int selectedCategoryId;
    int selectedCategory;

    /**
     *  It will show category list in a dialog.
     */
    public void showCategoryDialog() {
        final Dialog dialog = new Dialog(this);
        View view = getLayoutInflater().inflate(R.layout.list_dialog_layout, null);
        ListView lv = (ListView) view.findViewById(R.id.lstCategory);
        CategoryListDialogAdapter applianceListDialogAdapter = new CategoryListDialogAdapter(CategoryActivity.this, categoryList);
        lv.setAdapter(applianceListDialogAdapter);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                final Category  selectedCategory = categoryList.get(position);
                selectedCategoryId = selectedCategory.getCat_id();
                tvSelectCategory.setText(selectedCategory.getCategory());
                dialog.dismiss();
            }
        });
        dialog.setContentView(view);
        dialog.show();

    }

    /**
     * To display alert dialog for Category created Successfully
     * @param message (msg to display)
     */
    public void categoryCreatedSucessDialog(String message) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        // set dialog message
        alertDialogBuilder
                .setMessage(message)
                .setCancelable(false)
                .setPositiveButton(getResources().getString(R.string.CommonOK), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                 dialog.cancel();
                 edtCategoryName.setText("");
                 if (CheckNetwork.isInternetAvailable(mContext)) {
                 loader.show();
                 //Call API Request after check internet connection
                 new CommunicatorNew(mContext, Request.Method.GET, APIUtils.METHOD_GET_ALL_CATEGORY, new HashMap<String, String>());
                 } else {
                 //Toast.makeText(mContext, mContext.getString(R.string.MessageNoInternetConnection), Toast.LENGTH_LONG).show();
                 Utils.alertDialog(mContext, getResources().getString(R.string.Alert), getResources().getString(R.string.MessageNoInternetConnection));
                 }
                    }
                });
        final AlertDialog alertDialog = alertDialogBuilder.create();
        // show it
        alertDialog.show();
    }

    /**
     * To display alert dialog for SubCategory created Successfully
     *
     * @param message (msg to display)
     */
    public void subCategoryCreatedSucessDialog(String message) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        // set dialog message
        alertDialogBuilder
                .setMessage(message)
                .setCancelable(false)
                .setPositiveButton(getResources().getString(R.string.CommonOK), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        edtSubCategoryName.setText("");
                        dialog.cancel();
                    }
                });
        final AlertDialog alertDialog = alertDialogBuilder.create();
        // show it
        alertDialog.show();
    }
}
