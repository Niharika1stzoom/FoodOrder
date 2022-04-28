package com.zoom.happiestplaces.worker;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.zoom.happiestplaces.util.AppConstants;
import com.zoom.happiestplaces.util.NotificationUtils;
import com.zoom.happiestplaces.util.RestaurantUtils;

import java.util.UUID;

public class ScheduleReviewWorker extends Worker {

    public ScheduleReviewWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
    }

    @NonNull
    @Override
    public Result doWork() {
        UUID restaurantID= UUID.fromString(getInputData().getString(AppConstants.KEY_RESTAURANT_ID));
        UUID orderId= UUID.fromString(getInputData().getString(AppConstants.KEY_ORDER_ID));
        String restaurant= getInputData().getString(AppConstants.KEY_RESTAURANT_NAME);
        NotificationUtils.remindUserReview(getApplicationContext(),orderId,restaurantID,restaurant);
        return null;
    }
}
