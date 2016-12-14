
package com.bespoke.utils;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.util.Log;

import com.bespoke.HomeActivity;
import com.bespoke.R;

/*
 *This class handle the functionality related to display push.
 */
public class DisplayNotification {
    Context context;

    static DisplayNotification displayNotification=null;
    private int notificationId;


    //Constructor of this class takes context as parameter.
    public DisplayNotification(Context context) {
        this.context = context;

    }

    public static DisplayNotification getInstance(Context con) {

        if (displayNotification == null) {
            displayNotification = new DisplayNotification(con);
        }
        return displayNotification;
    }


    /*  public void sendNotification(String title, String message,String cData) {

        //Initialize EDPreference Class instance.

        //Create unique notification ID for display notification.
          int notificationId=(int)System.currentTimeMillis();
          android.support.v7.app.NotificationCompat.Builder nb= new android.support.v7.app.NotificationCompat.Builder(context);
          nb.setSmallIcon(R.drawable.ic_launcher);
          nb.setContentTitle("BeSpoke");
          nb.setContentText(title);
          nb.setTicker(title);

        //  ImageHelper.downloadProfileImages();
       *//*   Bitmap bitmap_image = BitmapFactory.decodeResource(context.getResources(), R.drawable.empty_photo);
          NotificationCompat.BigPictureStyle s = new NotificationCompat.BigPictureStyle().bigPicture(bitmap_image);
          s.setSummaryText(message);
          nb.setStyle(s);*//*

          Intent resultIntent = new Intent(context, HomeActivity.class);
          TaskStackBuilder tsb = TaskStackBuilder.create(context);
          tsb.addParentStack(HomeActivity.class);
          tsb.addNextIntent(resultIntent);
          PendingIntent resultPendingIntent = tsb.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
          nb.setContentIntent(resultPendingIntent);
          nb.setAutoCancel(true);
          NotificationManager mNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
          // mId allows you to update the notification later on.
          mNotificationManager.notify(11221, nb.build());
    }*/

   /* private int getNotificationIcon() {
        boolean useWhiteIcon = (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP);
        return useWhiteIcon ? R.mipmap.ic_launcher: R.mipmap.ic_launcher;
    }*/
    /**
     * Send simple notification using the NotificationCompat API.
     * This method  will show notification to user.
     */
    public void sendNotification(String title, String message, String json) {


        notificationId =(int)System.nanoTime();
        // Use NotificationCompat.Builder to set up our notification.
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context);
        //Redirecting to intermediate activity for handling redirection via deeplink.
        Intent intent = new Intent(context, HomeActivity.class);

        //Set pending intent this will fire on notification click
        PendingIntent contentIntent = PendingIntent.getActivity(context, notificationId, intent, 0);
        builder.setContentIntent(contentIntent);
        // builder.setStyle(new NotificationCompat.BigTextStyle().bigText(message));
        //Icon appears in device notification bar .
        //builder.setSmallIcon(R.drawable.ic_launcher);

        builder.setSmallIcon(getNotificationIcon());
        // Large icon appears on the left of the notification
        // builder.setLargeIcon(BitmapFactory.decodeResource(context.getResources(), R.drawable.ic_launcher));
        // Content title, which appears in large type at the top of the notification

        builder.setContentTitle(title);
        // ticker text appears in status bar
        builder.setTicker(message);
        //Set the text (second row) of the notification, in a standard notification.
        builder.setContentText(message);
        builder.setAutoCancel(true);
        //Set the relative priority for this notification.
        //Priority is an indication of how much of the user's valuable attention should be consumed by this notification.
        builder.setPriority(NotificationCompat.PRIORITY_HIGH);
        //Condition for banner like notification in lollipop
        if (Build.VERSION.SDK_INT >= 21) {   //this icon will show in Lolipop and higher version
            builder.setLargeIcon(BitmapFactory.decodeResource(context.getResources(), R.drawable.ic_launcher));
            //this line is for setting alerty type notification for Lollipop and higner version
            builder.setVibrate(new long[0]);
        }
        // Check user setting for app vibrate
        // We can vary this pattern de
        long[] pattern = {500, 500, 500, 500};
        builder.setVibrate(pattern);
        //Get system notification service reference Using NotificationManager.
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.cancel(notificationId);
        //Added notification ID to a list for trashing purpose .
        //Will display the notification in the notification bar
        notificationManager.notify(notificationId, builder.build());
    }

    private int getNotificationIcon() {
        boolean useWhiteIcon = (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP);
        return useWhiteIcon ? R.drawable.ic_launcher : R.drawable.ic_launcher;
    }
}