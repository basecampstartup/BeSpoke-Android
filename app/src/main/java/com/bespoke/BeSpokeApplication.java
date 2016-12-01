package com.bespoke;

import android.app.Application;
import android.graphics.Typeface;
import android.text.TextUtils;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.bespoke.context.ContextHelper;

/**
 * Created by admin on 11/23/2016.
 */

public class BeSpokeApplication extends Application {
    public static final String TAG = BeSpokeApplication.class.getSimpleName();
    public static BeSpokeApplication beSpokeApplication;
    public static Typeface APP_FONT_TYPEFACE;
    private RequestQueue mRequestQueue;

    public static synchronized BeSpokeApplication getInstance() {
        return beSpokeApplication;
    }
    @Override
    public void onCreate() {
        super.onCreate();
        ContextHelper.setContext(this);
        beSpokeApplication=this;

    }
    public RequestQueue getRequestQueue() {
        if (mRequestQueue == null) {
            mRequestQueue = Volley.newRequestQueue(getApplicationContext());
        }

        return mRequestQueue;
    }
    public <T> void addToRequestQueue(Request<T> req, String tag) {
        // set the default tag if tag is empty
        req.setTag(TextUtils.isEmpty(tag) ? TAG : tag);
        getRequestQueue().add(req);
    }

    public <T> void addToRequestQueue(Request<T> req) {
        req.setTag(TAG);
        getRequestQueue().add(req);
    }

    public void cancelPendingRequests(Object tag) {
        if (mRequestQueue != null) {
            mRequestQueue.cancelAll(tag);
        }
    }
}
