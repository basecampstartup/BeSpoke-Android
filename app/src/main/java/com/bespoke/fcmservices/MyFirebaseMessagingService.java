//===============================================================================
// (c) 2016 Basecamp Startups Pvt. Ltd.  All rights reserved.
// Original Author: Ankur Sharma
// Original Date: 12/12/2016.
//===============================================================================
package com.bespoke.fcmservices;

import android.util.Log;

import com.bespoke.R;
import com.bespoke.sprefs.AppSPrefs;
import com.bespoke.utils.DisplayNotification;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        Log.v("PUSH", "Push Receive");
        String msg = remoteMessage.getData().get("message");
        //String title = remoteMessage.getData().get("contentTitle");
        //Map<String, String> params = remoteMessage.getData();
        Log.e("PushREceive", "PushREceive");
        if (AppSPrefs.isAlreadyLoggedIn()) {
            DisplayNotification.getInstance(getApplicationContext()).sendNotification(getResources().getString(R.string.app_name), msg, "");
        }
    }

}
