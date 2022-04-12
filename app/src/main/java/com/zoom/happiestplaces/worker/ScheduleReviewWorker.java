package com.zoom.happiestplaces.worker;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.zoom.happiestplaces.util.AppConstants;
import com.zoom.happiestplaces.util.NotificationUtils;

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
