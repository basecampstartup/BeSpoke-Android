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
/**
 * Created by Ankur on 17/08/2016.
 */
public class MyFirebaseInstanceIDService extends FirebaseInstanceIdService implements APIRequestCallback {
    Context mContext;
    @Override
    public void onTokenRefresh() {
        super.onTokenRefresh();
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        Log.i("PUSH","MyFirebaseInstanceIDService class refereh token method");
        Log.i("PUSH","Token"+refreshedToken);
        AppSPrefs.setString(Commons.DEVICE_ID,refreshedToken);
        //sendRegistrationToServer(refreshedToken);
        Log.i("Token","Refresh token after service call"+refreshedToken);
    }

    private void sendRegistrationToServer(String token) {
        /*String selectedCityID= SharedPreferenceData.getSelectedCityId(getApplicationContext());
        String androidId= Utils.getAndroidScureId(Utils.getContext());*/
        mContext=MyFirebaseInstanceIDService.this;
        HashMap<String, String> requestMap = new HashMap<>();
        requestMap.put(APIUtils.DEVICE_ID, "deviceID");
        requestMap.put(APIUtils.DEVICE_TYPE, "deviceType");
        requestMap.put(APIUtils.PARAM_USER_ID, AppSPrefs.getString(Commons.USER_ID));
        new CommunicatorNew(mContext, Request.Method.POST, APIUtils.METHOD_UPDATE_DEVICEID, requestMap);

    }

    @Override
    public void onSuccess(String name, Object object) {

    }

    @Override
    public void onFailure(String name, Object object) {

    }
}
