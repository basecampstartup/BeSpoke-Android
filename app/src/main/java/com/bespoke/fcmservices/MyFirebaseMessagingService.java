package com.bespoke.fcmservices;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import com.bespoke.HomeActivity;
import com.bespoke.R;
import com.bespoke.commons.Commons;
import com.bespoke.sprefs.AppSPrefs;
import com.bespoke.utils.DisplayNotification;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.Map;


/**
 * Created by Ankur on 17/08/2016.
 */
public class MyFirebaseMessagingService  extends FirebaseMessagingService {

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
//        Toast.makeText(this,"push receive",Toast.LENGTH_LONG).show();
        Log.v("PUSH","Push Receive");
        String msg=remoteMessage.getData().get("message");
        String title=remoteMessage.getData().get("contentTitle");
        //Map<String, String> params = remoteMessage.getData();
        Log.e("PushREceive","PushREceive");
      //  String body= remoteMessage.getNotification().getBody();
       // Log.e("PushREceive","PushREceive body"+body);
        //sendNotification("Hello");
        if(AppSPrefs.isAlreadyLoggedIn()){
        DisplayNotification.getInstance(getApplicationContext()).sendNotification(getResources().getString(R.string.app_name),msg,"");
        }
    }

    //This method is only generating push notification
    //It is same as we did in earlier posts
    private void sendNotification(String messageBody) {
        Intent intent = new Intent(this, HomeActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent,
                PendingIntent.FLAG_ONE_SHOT);

        Uri defaultSoundUri= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle("Firebase Push Notification")
                .setContentText(messageBody)
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent);

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.notify(0, notificationBuilder.build());
    }
}
