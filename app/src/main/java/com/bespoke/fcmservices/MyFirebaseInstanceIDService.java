//===============================================================================
// (c) 2016 Basecamp Startups Pvt. Ltd.  All rights reserved.
// Original Author: Ankur Sharma
// Original Date: 12/12/2016.
//===============================================================================
package com.bespoke.fcmservices;
import android.content.Context;
import android.util.Log;
import com.android.volley.Request;
import com.bespoke.callback.APIRequestCallback;
import com.bespoke.commons.Commons;
import com.bespoke.servercommunication.APIUtils;
import com.bespoke.servercommunication.CommunicatorNew;
import com.bespoke.sprefs.AppSPrefs;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;
import java.util.HashMap;

public class MyFirebaseInstanceIDService extends FirebaseInstanceIdService implements APIRequestCallback {
    Context mContext;
    @Override
    public void onTokenRefresh() {
        super.onTokenRefresh();
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        Log.i("PUSH","MyFirebaseInstanceIDService class refereh token method");
        Log.i("PUSH","Token"+refreshedToken);
        AppSPrefs.setString(Commons.DEVICE_ID,refreshedToken);
        Log.i("Token","Refresh token after service call"+refreshedToken);
    }

    /**
     * This method will call to Server registration.  (not in use now)
     * @param token
     */
    private void sendRegistrationToServer(String token) {

        mContext=MyFirebaseInstanceIDService.this;
        HashMap<String, String> requestMap = new HashMap<>();
        requestMap.put(APIUtils.DEVICE_ID, "deviceID");
        requestMap.put(APIUtils.DEVICE_TYPE, "deviceType");
        requestMap.put(APIUtils.PARAM_USER_ID, AppSPrefs.getString(Commons.USER_ID));
        new CommunicatorNew(mContext, Request.Method.POST, APIUtils.METHOD_UPDATE_DEVICEID, requestMap);

    }

    /**
     * This is a overridden method to Handle API call response.
     * @param name   string call name returned from ajax response on success
     * @param object object returned from ajax response on success
     */
    @Override
    public void onSuccess(String name, Object object) {

    }

    @Override
    public void onFailure(String name, Object object) {

    }
}
