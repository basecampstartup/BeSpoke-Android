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

public class ViewRequestsActivity extends AppCompatActivity implements APIRequestCallback, View.OnTouchListener{
    private Toolbar mToolbar;
    private Context mContext;
    private ProgressDialog loader = null;

    ArrayList<IssueModel> ticketList;
    ListView lstIssues;
    EditText edtSearchText;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_request);
        mContext=this;

        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(getString(R.string.LeftPanelViewRequests));

        lstIssues=(ListView)findViewById(R.id.lstIssues);
        edtSearchText=(EditText) findViewById(R.id.edtSearch);
        edtSearchText.addTextChangedListener(filterTextWatcher);
        loader = new ProgressDialog(this);
        loader.setMessage(getString(R.string.MessagePleaseWait));
        loader.setCancelable(false);
        if (CheckNetwork.isInternetAvailable(mContext)) {
            loader.show();
            //Call API Request after check internet connection
            new CommunicatorNew(mContext, Request.Method.GET, APIUtils.METHOD_GET_ALL_REQUEST, new HashMap<String,String>());
        } else {
            Toast.makeText(mContext, mContext.getString(R.string.MessageNoInternetConnection), Toast.LENGTH_LONG).show();
        }

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
                Intent intent = new Intent(ViewRequestsActivity.this, AddRequestActivity.class);
                startActivityForResult(intent, 1);
            }
        });

        lstIssues.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                IssueModel model=ticketList.get(position);
                Intent i=new Intent(ViewRequestsActivity.this,RequestDetailActivity.class);
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
                new CommunicatorNew(mContext, Request.Method.GET, APIUtils.METHOD_GET_ALL_REQUEST, new HashMap<String,String>());
            } else {
                Toast.makeText(mContext, mContext.getString(R.string.MessageNoInternetConnection), Toast.LENGTH_LONG).show();
            }
        }
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

        if (APIUtils.METHOD_GET_ALL_REQUEST.equalsIgnoreCase(name)) {
            try {
                final JSONObject responseObject = new JSONObject(object.toString());
                String error = responseObject.optString("error");
                if (TextUtils.isEmpty(error)) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            ticketList= ResponseParser.parseTicketsResponse(responseObject);
                            IssueListAdapter adapter=new IssueListAdapter(mContext,ticketList);
                            lstIssues.setAdapter(adapter);
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
                Utils.alertDialog(mContext,getResources().getString(R.string.ErrorTitle),getResources().getString(R.string.SomethingWentWrong));
            }
        });
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
                edtSearchText.setOnTouchListener(ViewRequestsActivity.this);
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
