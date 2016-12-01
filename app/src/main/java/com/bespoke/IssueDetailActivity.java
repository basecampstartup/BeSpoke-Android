package com.bespoke;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.bespoke.Model.TicketModel;

public class IssueDetailActivity extends AppCompatActivity implements View.OnClickListener{
    private Toolbar mToolbar;
    private Context mContext;
    Button btnCloseTicket,btnEmailUser;
    private TextView tvIdValue,tvShortDescriptionValue,tvDescriptionValue,tvCategoryValue,tvAffectedAreaValue,tvUserValue,tvIssueOpenDateValue,tvAssignedToValue,tvStatusValue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_issue_detail);
        mContext=this;
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(getString(R.string.IssuesDetail));
        initializeComponents();
        Intent intent = getIntent();
        TicketModel model = (TicketModel) intent.getExtras().getSerializable("SelectedModel");
        setDataOnComponents(model);

    }


    public void setDataOnComponents(TicketModel model)
    {
      tvIdValue.setText(model.getTicket_id());
        tvShortDescriptionValue.setText(model.getShortdesc());
        tvDescriptionValue.setText(model.getDescription());
        tvCategoryValue.setText(model.getCategoryName());
        tvAffectedAreaValue.setText(model.getSubCategoryName());
        tvUserValue.setText(model.getUserName());
        tvIssueOpenDateValue.setText(model.getCreateddate());
        tvAssignedToValue.setText(model.getAssignedto());
        tvStatusValue.setText(model.getTicketstatus());
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

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        this.finish();
    }

}
