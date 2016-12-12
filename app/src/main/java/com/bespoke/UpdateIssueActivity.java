package com.bespoke;

import android.app.ProgressDialog;
import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.bespoke.Model.TicketModel;

public class UpdateIssueActivity extends AppCompatActivity {
    private Toolbar mToolbar;
    private Context mContext;
    EditText edtShortDescription, edtDescription, edtAssignedTo;
    TextView tvSelectCategory, tvSelectCategoryLbl, tvSelectAffectedArea, tvSelectAffectedAreaLbl, tvTicketStatusLbl, tvTicketStatusValue;
    TextView tvAssignedTo, tvAssignedToLbl, tvSelectIssueOpenDateLbl, tvSelectIssueOpenDateVal,tvUserName;
    Button btnUpdateTicket, btnEmail;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_issue);
    }
}
