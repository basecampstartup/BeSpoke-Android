//===============================================================================
// (c) 2016 Basecamp Startups Pvt. Ltd.  All rights reserved.
// Original Author: Ankur Sharma
// Original Date: 15/12/2016
//===============================================================================
package com.bespoke;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class WebViewActivity extends AppCompatActivity {

    private Toolbar mToolbar;
    /** context of current Activity */
    private Context mContext;
    private ProgressDialog loader = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_view);
        mContext=this;
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(getString(R.string.Documents));
        Intent intent = getIntent();
        String path = intent.getExtras().getString("FilePath");
        String name = intent.getExtras().getString("FileName");
        loader = new ProgressDialog(this);
        loader.setMessage(getString(R.string.MessagePleaseWait));
        loader.setCancelable(false);
        WebView urlWebView = (WebView)findViewById(R.id.webView);
        if(name.contains(".png")||name.contains(".jpg")||name.contains(".jpeg"))
        {
            urlWebView.setWebViewClient(new AppWebViewClients());
            urlWebView.getSettings().setJavaScriptEnabled(true);
            urlWebView.getSettings().setUseWideViewPort(true);
            urlWebView.loadUrl(path);
        }
        else{
            urlWebView.setWebViewClient(new AppWebViewClients());
            urlWebView.getSettings().setJavaScriptEnabled(true);
            urlWebView.getSettings().setUseWideViewPort(true);
            urlWebView.loadUrl("http://docs.google.com/gview?embedded=true&url="
                    + path);
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

    /**
     * Overridden method will execute when user click on back button of device.
     */
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        this.finish();
    }

    /**
     * Class that will handle web view.
     */
    public class AppWebViewClients extends WebViewClient {

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            // TODO Auto-generated method stub
            view.loadUrl(url);
            return true;
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            // TODO Auto-generated method stub
            super.onPageFinished(view, url);

        }

    }
}
