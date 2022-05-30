package com.zoom.happiestplaces.worker;

import android.util.Log;

import androidx.navigation.fragment.NavHostFragment;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.zoom.happiestplaces.R;
import com.zoom.happiestplaces.util.AppConstants;
import com.zoom.happiestplaces.util.NotificationUtils;
import com.zoom.happiestplaces.util.OrderUtils;

import java.util.Map;
import java.util.UUID;

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    private static final String TAG = "MyFirebaseMsgService";
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {

        if (remoteMessage.getData().size() > 0) {

            Map<String,String> data=remoteMessage.getData();
            if (data.containsKey(AppConstants.KEY_ORDER_STATUS)) {
                String status = data.get(AppConstants.KEY_ORDER_STATUS);
                if (status.equals(AppConstants.Status.Paid.toString())) {
                    String orderId = data.get(AppConstants.KEY_ORDER_ID);
                    String restId = data.get(AppConstants.KEY_RESTAURANT_ID);
                    String restaurant = data.get(AppConstants.KEY_RESTAURANT_NAME);

                    NotificationUtils.remindUserReview(getApplicationContext(),
                            UUID.fromString(orderId),
                            UUID.fromString(restId),restaurant);
                }
                else
                    {
                        //A notification
                        NotificationUtils.orderStatusChangeNotification(
                                getApplicationContext(),data.get(
                                        AppConstants.KEY_ORDER_STATUS),
                                data.get(AppConstants.KEY_RESTAURANT_NAME),
                                data.get(AppConstants.KEY_TABLE_NUM));
                    }
            }
            else
            if (data.containsKey(AppConstants.KEY_REFERRED_BY)) {
                if(remoteMessage.getNotification() != null)
                NotificationUtils.sendNotification(remoteMessage.getNotification().getTitle(),
                        remoteMessage.getNotification().getBody(),getApplicationContext());
            }
            else
            if (data.containsKey(AppConstants.KEY_REFERRED_USER)) {
                NotificationUtils.sendNotification(remoteMessage.getNotification().getTitle(),
                        remoteMessage.getNotification().getBody(),getApplicationContext());
            }
        }
        // Check if message contains a notification payload.
        if (remoteMessage.getNotification() != null) {
        }
    }

    @Override
    public void onNewToken(String token) {
       // Log.d(AppConstants.TAG, "Refreshed token: " + token);
    }
}