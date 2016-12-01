package com.bespoke;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.bespoke.Model.Category;
import com.bespoke.Model.UserModel;
import com.bespoke.adapter.CategoryListDialogAdapter;
import com.bespoke.callback.APIRequestCallback;
import com.bespoke.network.CheckNetwork;
import com.bespoke.servercommunication.APIUtils;
import com.bespoke.servercommunication.CommunicatorNew;
import com.bespoke.servercommunication.ResponseParser;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class CategoryActivity extends AppCompatActivity implements APIRequestCallback, View.OnClickListener {
    private Toolbar mToolbar;
    private Context mContext;
    private ProgressDialog loader = null;
    ArrayList<Category> categoryList;
    TextView tvCategory;
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
        tvCategory=(TextView)findViewById(R.id.tvSelectCategory);
        tvCategory.setOnClickListener(this);
        //set loader
        loader = new ProgressDialog(this);
        loader.setMessage(getString(R.string.MessagePleaseWait));
        if (CheckNetwork.isInternetAvailable(mContext)) {
            loader.show();
            //Call API Request after check internet connection
            new CommunicatorNew(mContext, Request.Method.GET, APIUtils.METHOD_GET_ALL_CATEGORY, new HashMap<String,String>());
        } else {
            Toast.makeText(mContext, mContext.getString(R.string.MessageNoInternetConnection), Toast.LENGTH_LONG).show();
        }
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

            case R.id.tvSelectCategory:
            showCategoryDialog();
                break;

            default:
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        this.finish();
    }


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
                    final String message = responseObject.optString("message");
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
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
                Toast.makeText(mContext, "On Failure of Category", Toast.LENGTH_SHORT).show();
            }
        });
    }

    int selectedCategoryId;
    int selectedCategory;
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
                //    Toast.makeText(AddScheduleTaskActivity.this, "Item :" + selectedAppliance, Toast.LENGTH_SHORT).show();
                tvCategory.setText(selectedCategory.getCategory());
                dialog.dismiss();
            }
        });
        dialog.setContentView(view);
        dialog.show();


    }

}
