package com.bespoke;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.Request;
import com.bespoke.Model.Category;
import com.bespoke.adapter.CategoryListAdapter;
import com.bespoke.adapter.IssueListAdapter;
import com.bespoke.callback.APIRequestCallback;
import com.bespoke.network.CheckNetwork;
import com.bespoke.servercommunication.APIUtils;
import com.bespoke.servercommunication.CommunicatorNew;
import com.bespoke.servercommunication.ResponseParser;
import com.bespoke.utils.Utils;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class DocumentsActivity extends AppCompatActivity implements APIRequestCallback,
        View.OnTouchListener, AdapterView.OnItemClickListener{
    private Toolbar mToolbar;
    private ProgressDialog loader = null;
    Context mContext;
    ListView lstCategory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_documents);
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        lstCategory = (ListView) findViewById(R.id.lstCategory);
        lstCategory.setOnItemClickListener(this);
        mContext = this;
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(getString(R.string.LeftPanelDocuments));
        setAllCategory();
    }


    private void setAllCategory(){
        loader = new ProgressDialog(this);
        loader.setMessage(getString(R.string.MessagePleaseWait));
        loader.setCancelable(false);
        if (CheckNetwork.isInternetAvailable(mContext)) {
            loader.show();
            //Call API Request after check internet connection
            new CommunicatorNew(mContext, Request.Method.GET, APIUtils.METHOD_GET_ALL_CATEGORY, new HashMap<String,String>());
        } else {
            Toast.makeText(mContext, mContext.getString(R.string.MessageNoInternetConnection), Toast.LENGTH_LONG).show();
            loader.dismiss();
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
    public void onBackPressed() {
        super.onBackPressed();
        this.finish();
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        return false;
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
                            ArrayList<Category> categories= ResponseParser.parseCategoryResponse(responseObject);
                            CategoryListAdapter adapter=new CategoryListAdapter(mContext, categories);
                            lstCategory.setAdapter(adapter);
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
                Utils.alertDialog(mContext,getResources().getString(R.string.ErrorTitle), getResources().getString(R.string.SomethingWentWrong));
            }
        });
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Category category = (Category) parent.getItemAtPosition(position);
        int categoryId = category.getCat_id();
//        Intent intent = new Intent(this, );
//        startActivity(intent);
    }
}
