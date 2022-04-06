package com.example.foodorder.order;

import android.app.Application;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.ViewModel;
import androidx.work.Data;
import androidx.work.ExistingWorkPolicy;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;

import com.example.foodorder.model.Order;
import com.example.foodorder.util.AppConstants;
import com.example.foodorder.util.AppUtils;
import com.example.foodorder.util.NotificationUtils;
import com.example.foodorder.util.OrderUtils;
import com.example.foodorder.util.SharedPrefUtils;
import com.example.foodorder.worker.ScheduleReviewWorker;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

public class OrderViewModel extends AndroidViewModel {
    private Context mContext;
    WorkManager mWorkManager;
    public OrderViewModel(@NonNull Application application) {
        super(application);
        mContext=application.getApplicationContext();
    }


    public Boolean placeOrder() {
        return OrderUtils.sendSms(mContext, SharedPrefUtils.getOrder(mContext));
    }

    public void clearOrder() {
    SharedPrefUtils.cancelOrder(mContext);
    }

    public boolean isOrderTrue() {
        return SharedPrefUtils.checkOrderExecuted(mContext);

    }

    public void setOrderExecuted() {
        SharedPrefUtils.setOrderExecuted(mContext);
    }

    public boolean checkOrderEmpty() {
        return SharedPrefUtils.checkOrderIsEmpty(mContext);
    }

    public Double getTotal() {
        return SharedPrefUtils.showTotal(mContext);
    }

    public Order getCurrentOrder() {
        return SharedPrefUtils.getOrder(mContext);
    }
    public void scheduleNotification(UUID orderId,String restaurant)
    {
        mWorkManager=WorkManager.getInstance(mContext);
        OneTimeWorkRequest schedulerWorkRequest =
                new OneTimeWorkRequest.Builder(ScheduleReviewWorker.class)
                       // .setInitialDelay(NotificationUtils.NOTIFICATION_HR, TimeUnit.MINUTES)
                        .setInputData(OrderUtils.getNotificationData(orderId,restaurant))
                        .build();
        mWorkManager.enqueue(
                schedulerWorkRequest);

    }
}