package com.bespoke;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.Request;
import com.bespoke.Model.Category;
import com.bespoke.Model.TicketModel;
import com.bespoke.adapter.IssueListAdapter;
import com.bespoke.callback.APIRequestCallback;
import com.bespoke.network.CheckNetwork;
import com.bespoke.servercommunication.APIUtils;
import com.bespoke.servercommunication.CommunicatorNew;
import com.bespoke.servercommunication.ResponseParser;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class ViewIssuesActivity extends AppCompatActivity implements APIRequestCallback{
    private Toolbar mToolbar;
    private Context mContext;
    ListView lstIssues;
    private ProgressDialog loader = null;
    ArrayList<TicketModel> ticketList;
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
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Click action
                Intent intent = new Intent(ViewIssuesActivity.this, AddIssueActivity.class);
                startActivity(intent);
            }
        });
        //set loader
        loader = new ProgressDialog(this);
        loader.setMessage(getString(R.string.MessagePleaseWait));
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
                TicketModel model=ticketList.get(position);
                Intent i=new Intent(ViewIssuesActivity.this,IssueDetailActivity.class);
                i.putExtra("SelectedModel", model);
                startActivity(i);
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
                Toast.makeText(mContext, "On Failure of IssueList", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
