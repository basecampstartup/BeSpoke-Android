package com.bespoke;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.bespoke.Model.IssueModel;
import com.bespoke.Model.TicketModel;
import com.bespoke.callback.APIRequestCallback;
import com.bespoke.servercommunication.APIUtils;
import com.bespoke.utils.TicketStatus;

import org.json.JSONObject;

public class RequestDetailActivity extends AppCompatActivity implements View.OnClickListener, APIRequestCallback {
    private Toolbar mToolbar;
    private Context mContext;
    Button btnCloseTicket,btnEmailUser;
    private TextView tvIdValue,tvShortDescriptionValue,tvDescriptionValue,tvCategoryValue,tvAffectedAreaValue,tvUserValue,tvIssueOpenDateValue,tvAssignedToValue,tvStatusValue;
    private ProgressDialog loader = null;
    IssueModel model;

    boolean isRecordUpdated=false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request_detail);
        mContext=this;
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(getString(R.string.IssuesDetail));
        loader = new ProgressDialog(this);
        loader.setMessage(getString(R.string.MessagePleaseWait));
        loader.setCancelable(false);
        initializeComponents();
        Intent intent = getIntent();
        model= (IssueModel) intent.getExtras().getSerializable("SelectedModel");
        setDataOnComponents(model);
    }
    /**
     * Initialize the UI components.
     */
    public void initializeComponents() {
        btnCloseTicket = (Button) findViewById(R.id.btnEmailUser);
        btnCloseTicket.setOnClickListener(this);
        btnEmailUser = (Button) findViewById(R.id.btnCloseTicket);
        btnEmailUser.setOnClickListener(this);
        tvIdValue=(TextView)findViewById(R.id.tvIDValue);
        tvShortDescriptionValue=(TextView)findViewById(R.id.tvShortDescriptionValue);
        tvDescriptionValue=(TextView)findViewById(R.id.tvDescriptionValue);
        tvCategoryValue=(TextView)findViewById(R.id.tvCategoryValue);
        tvAffectedAreaValue=(TextView)findViewById(R.id.tvAffectedAreaValue);
        tvUserValue=(TextView)findViewById(R.id.tvUserValue);
        tvIssueOpenDateValue=(TextView)findViewById(R.id.tvIssueOpenDateValue);
        tvAssignedToValue=(TextView)findViewById(R.id.tvAssignedToValue);
        tvStatusValue=(TextView)findViewById(R.id.tvTicketStatusValue);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.issue_detail_menu, menu);
        (menu.findItem(R.id.menuUpdate)).setVisible(true);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            case R.id.menuUpdate:
                Intent i=new Intent(RequestDetailActivity.this,UpdateIssueActivity.class);
                i.putExtra("SelectedModel", model);
                startActivityForResult(i,1);
                return  true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnCloseTicket:
                break;
            default:
                break;
        }
    }

    @Override
    public void onBackPressed() {
        if(isRecordUpdated)
            setResult(1);
            finish();
    }
    @Override
    public void onSuccess(String name, Object object) {

    }

    @Override
    public void onFailure(String name, Object object) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (loader != null) {
                    loader.dismiss();
                }
            }
        });
    }
    public void setDataOnComponents(IssueModel model)
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
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode==1)
        {
            String updatedAssignedToName=data.getStringExtra("AssignedToName");
            String updatedStatus=data.getStringExtra("TicketStatus");
            tvAssignedToValue.setText(updatedAssignedToName);
            tvStatusValue.setText(updatedStatus);
            isRecordUpdated=true;

        }
    }

}
