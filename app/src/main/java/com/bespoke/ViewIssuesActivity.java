//===============================================================================
// (c) 2016 Basecamp Startups Pvt. Ltd.  All rights reserved.
// Original Author: Ankur Sharma
// Original Date: 09/12/2016
//===============================================================================
package com.bespoke;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.bespoke.Model.IssueModel;
import com.bespoke.Model.TicketModel;
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

public class ViewIssuesActivity extends AppCompatActivity implements APIRequestCallback, View.OnTouchListener{
    private Toolbar mToolbar;
    /** context of current Activity */
    private Context mContext;
    ListView lstIssues;
    private ProgressDialog loader = null;
    ArrayList<IssueModel> ticketList;
    EditText edtSearchText;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_issues);
        mContext=this;
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(getString(R.string.LeftPanelViewIssues));
        lstIssues=(ListView)findViewById(R.id.lstIssues);
        edtSearchText=(EditText) findViewById(R.id.edtSearch);
        edtSearchText.addTextChangedListener(filterTextWatcher);

        Utils.hideSoftKeyboard(mContext,edtSearchText);
        edtSearchText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                   IssueModel searchedTicket = manageSearchClick(edtSearchText.getText().toString().trim());
                    if(searchedTicket!=null)
                    {
                        ArrayList<IssueModel> searchModels=new ArrayList<IssueModel>();
                        searchModels.add(searchedTicket);
                        IssueListAdapter adapter=new IssueListAdapter(mContext,searchModels);
                        lstIssues.setAdapter(adapter);
                    }
                    Utils.hideSoftKeyboard(mContext,edtSearchText);
                    return true;
                }
                return false;
            }
        });
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Click action
                Intent intent = new Intent(ViewIssuesActivity.this, AddIssueActivity.class);
                startActivityForResult(intent, 1);
            }
        });
        //set loader
        loader = new ProgressDialog(this);
        loader.setMessage(getString(R.string.MessagePleaseWait));
        loader.setCancelable(false);
        if (CheckNetwork.isInternetAvailable(mContext)) {
            loader.show();
            //Call API Request after check internet connection
            new CommunicatorNew(mContext, Request.Method.GET, APIUtils.METHOD_GET_ALL_ISSUES, new HashMap<String,String>());
        } else {
            Toast.makeText(mContext, mContext.getString(R.string.MessageNoInternetConnection), Toast.LENGTH_LONG).show();
        }

        lstIssues.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                IssueModel model=ticketList.get(position);
                Intent i=new Intent(ViewIssuesActivity.this,IssueDetailActivity.class);
                i.putExtra("SelectedModel", model);
                startActivityForResult(i,1);
            }
        });
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode==1)
        {
            if (CheckNetwork.isInternetAvailable(mContext)) {
                loader.show();
                //Call API Request after check internet connection
                new CommunicatorNew(mContext, Request.Method.GET, APIUtils.METHOD_GET_ALL_ISSUES, new HashMap<String,String>());
            } else {
                Toast.makeText(mContext, mContext.getString(R.string.MessageNoInternetConnection), Toast.LENGTH_LONG).show();
            }
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

        if (APIUtils.METHOD_GET_ALL_ISSUES.equalsIgnoreCase(name)) {
            try {
                final JSONObject responseObject = new JSONObject(object.toString());
                String error = responseObject.optString("error");
                if (TextUtils.isEmpty(error)) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            ticketList=ResponseParser.parseTicketsResponse(responseObject);
                            IssueListAdapter adapter=new IssueListAdapter(mContext,ticketList);
                            lstIssues.setAdapter(adapter);
                        }
                    });
                }
                else {
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

    @Override
    protected void onResume() {
        super.onResume();

    }



    public IssueModel manageSearchClick(String id)
    {

        IssueModel searchedTicketModel=null;
        for(int i=0;i<ticketList.size();i++)
        {
            if(ticketList.get(i).getTicket_id().equalsIgnoreCase(id))
            {
                //searchedTicketModel=new TicketModel();
                searchedTicketModel=ticketList.get(i);
            }
        }
       return searchedTicketModel;
    }


    private TextWatcher filterTextWatcher = new TextWatcher() {
        public void afterTextChanged(Editable s) {
            System.out.println("AfterTextChanged");
        }

        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            System.out.println("BeforeTextChanged");
        }

        public void onTextChanged(CharSequence s, int start, int before, int count) {
            final String str = s.toString().trim();
            Log.i("Paging", "onTextChanged str     " + str);

            Log.i("Paging", "onTextChanged postDelayed str     " + str);
            if (!TextUtils.isEmpty(str)) {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        IssueListAdapter adapter=new IssueListAdapter(mContext,ticketList);
                        lstIssues.setAdapter(adapter);
                    }
                }, 500);

            }
            if (!"".equals(edtSearchText.getText().toString())) {
                edtSearchText.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_search_black_24dp, 0, R.drawable.ic_clear_black_24dp, 0);
                edtSearchText.setOnTouchListener(ViewIssuesActivity.this);
            } else {
                edtSearchText.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_search_black_24dp, 0, 0, 0);
                edtSearchText.setOnTouchListener(null);
            }

        }
    };

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
          switch (view.getId()) {
            case R.id.edtSearch:
                if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                    if (motionEvent.getRawX() >= (edtSearchText.getRight() - edtSearchText.getCompoundDrawables()[Utils.DRAWABLE_RIGHT].getBounds().width())) {
                        edtSearchText.setText("");
                        edtSearchText.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_search_black_24dp, 0, 0, 0);
                        IssueListAdapter adapter=new IssueListAdapter(mContext,ticketList);
                        lstIssues.setAdapter(adapter);

                        return true;
                    } else {
                        Utils.showSoftKeyboard(mContext, edtSearchText);
                        return false;
                    }
                }
                return false;

            default:
                if (edtSearchText != null) {
                    Utils.hideSoftKeyboardWithoutReq(mContext, edtSearchText);
                }
                return false;
        }
    }
}
