//===============================================================================
// (c) 2016 Basecamp Startups Pvt. Ltd.  All rights reserved.
// Original Author: Ankur Sharma
// Original Date: 06/12/2016
//===============================================================================
package com.bespoke;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

public class SettingsActivity extends AppCompatActivity implements View.OnClickListener {
    private Toolbar mToolbar;
    /** context of current Activity */
    private Context mContext;
    ImageView ivSwitchBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        mContext=this;

        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(getString(R.string.LeftPanelSettings));
        ivSwitchBtn=(ImageView)findViewById(R.id.ivSwitchBtn);
        ivSwitchBtn.setOnClickListener(this);
        ivSwitchBtn.setTag(false);
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

    public void showSettingDialog(String msg) {

        final Dialog dialog = new Dialog(this);
        View view = getLayoutInflater().inflate(R.layout.setting_dialog_layout, null);
        Button btnCancel = (Button) view.findViewById(R.id.btnCancel);
        EditText edtPasscode = (EditText) view.findViewById(R.id.edtPasscode);
        TextView txtHeading = (TextView) view.findViewById(R.id.tvDialogHeading);
        txtHeading.setText(msg);
        btnCancel.setOnClickListener(new View.OnClickListener() {
            /**
             *  Overridden method to handle clicks of UI components
             *  @param v
             */
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.setContentView(view);
        dialog.show();


    }

    /**
     *  Overridden method to handle clicks of UI components
     *  @param v
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ivSwitchBtn:
                if ((boolean)ivSwitchBtn.getTag()){
                    ivSwitchBtn.setImageResource(R.drawable.off_btn);
                    ivSwitchBtn.setTag(false);
                    showSettingDialog(getResources().getString(R.string.DisablePasscodeMsg));

                } else {
                    ivSwitchBtn.setImageResource(R.drawable.on_btn);
                    ivSwitchBtn.setTag(true);
                    showSettingDialog(getResources().getString(R.string.EnablePasscodeMsg));
                }
            default:

        }
    }
}
