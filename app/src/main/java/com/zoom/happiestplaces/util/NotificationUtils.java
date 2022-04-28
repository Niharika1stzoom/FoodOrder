package com.zoom.happiestplaces.util;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.util.Log;

import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;
import androidx.navigation.NavDeepLinkBuilder;

import com.zoom.happiestplaces.R;

import java.util.Random;
import java.util.UUID;

public class NotificationUtils {

    public static final int NOTIFICATION_MIN = AppConstants.NOT_MINS;
    /*
     * This notification ID can be used to access our notification after we've displayed it. This
     * can be handy when we need to cancel the notification, or perhaps update it. This number is
     * arbitrary and can be set to whatever you like. 1138 is in no way significant.
     */
    public static final String  GROUP_KEY_REVIEW_NOTIFICATION = "group_review";

    private static final int REVIEW_REMINDER_NOTIFICATION_ID = 1138;
    /**
     * This pending intent id is used to uniquely reference the pending intent
     */
    private static final int REVIEW_REMINDER_PENDING_INTENT_ID = 3417;
    /**
     * This notification channel id is used to link notifications to this channel
     */
    public static NotificationCompat.InboxStyle inboxStyle = new NotificationCompat.InboxStyle();
    private static final String REVIEW_REMINDER_NOTIFICATION_CHANNEL_ID = "reminder_notification_channel";
    private static final int ACTION_REVIEW_PENDING_INTENT_ID = 1;
    private static final int ACTION_IGNORE_PENDING_INTENT_ID = 14;

    //  COMPLETED (1) Create a method to clear all notifications
    public static void clearAllNotifications(Context context) {
        NotificationManager notificationManager = (NotificationManager)
                context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.cancelAll();
    }

    public static void remindUserReview(Context context, UUID orderId, UUID restID,String restaurant) {
        Log.d(AppConstants.TAG,"In remind user ");
        NotificationManager notificationManager = (NotificationManager)
                context.getSystemService(Context.NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel mChannel = new NotificationChannel(
                    REVIEW_REMINDER_NOTIFICATION_CHANNEL_ID,
                    context.getString(R.string.main_notification_channel_name),
                    NotificationManager.IMPORTANCE_HIGH);
            notificationManager.createNotificationChannel(mChannel);
        }
        int unicode = 0x1F60B;
        NotificationCompat.Builder notificationBuilder =
                new NotificationCompat.Builder(context,REVIEW_REMINDER_NOTIFICATION_CHANNEL_ID)
                .setColor(ContextCompat.getColor(context, R.color.primaryColor))
                .setSmallIcon(R.mipmap.ic_launcher_happiest)
                .setLargeIcon(largeIcon(context))
                        .setGroup(GROUP_KEY_REVIEW_NOTIFICATION)
                .setContentTitle(new String(Character.toChars(unicode))+" "+context.getString(R.string.notification_content_title))
                .setContentText(restaurant+" - How was the food?")
                        .setDefaults(Notification.DEFAULT_VIBRATE).
                        setContentIntent(contentIntent(context,orderId,restID))
                .setAutoCancel(true);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN
                && Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
            notificationBuilder.setPriority(NotificationCompat.PRIORITY_HIGH);
        }
        notificationManager.notify(new Random().nextInt() , notificationBuilder.build());
        Log.d(AppConstants.TAG,"Notification sent ");
    }


    private static PendingIntent contentIntent(Context context, UUID orderId,UUID restId) {
        //Intent startActivityIntent = new Intent(context, MainActivity.class);
        PendingIntent startActivityIntent = new NavDeepLinkBuilder(context)
                .setGraph(R.navigation.nav_graph)
                .setDestination(R.id.addReviewFragment)
                .setArguments(OrderUtils.getOrderRestoBundle(orderId,restId))
                .createPendingIntent();
            return startActivityIntent;
        //startActivityIntent.putExtra(AppConstants.KEY_ORDER_ID, orderId);
        //startActivityIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

       /* return PendingIntent.getActivity(
                context,
                REVIEW_REMINDER_PENDING_INTENT_ID,
                startActivityIntent,
                PendingIntent.FLAG_UPDATE_CURRENT);*/
    }

    private static Bitmap largeIcon(Context context) {

        Resources res = context.getResources();
        Bitmap largeIcon = BitmapFactory.decodeResource(res,R.mipmap.ic_launcher_happiest);
        return largeIcon;
    }
}
