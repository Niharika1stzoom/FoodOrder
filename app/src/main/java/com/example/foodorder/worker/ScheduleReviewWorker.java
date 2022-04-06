package com.example.foodorder.worker;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.example.foodorder.util.AppConstants;
import com.example.foodorder.util.NotificationUtils;

import java.util.UUID;

public class ScheduleReviewWorker extends Worker {

    public ScheduleReviewWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
    }

    @NonNull
    @Override
    public Result doWork() {
        String restaurant= getInputData().getString(AppConstants.KEY_RESTAURANT_NAME);
        UUID orderId= UUID.fromString(getInputData().getString(AppConstants.KEY_ORDER_ID));

        NotificationUtils.remindUserReview(getApplicationContext(),orderId,restaurant);
        return null;
    }
}
