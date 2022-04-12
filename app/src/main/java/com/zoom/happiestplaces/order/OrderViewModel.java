package com.zoom.happiestplaces.order;

import android.app.Application;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;

import com.zoom.happiestplaces.model.Customer;
import com.zoom.happiestplaces.model.Order;
import com.zoom.happiestplaces.util.NotificationUtils;
import com.zoom.happiestplaces.util.OrderUtils;
import com.zoom.happiestplaces.util.SharedPrefUtils;
import com.zoom.happiestplaces.worker.ScheduleReviewWorker;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class OrderViewModel extends AndroidViewModel{
    private Context mContext;
    WorkManager mWorkManager;
    MutableLiveData<Customer> mCustomerLiveData;
    @Inject
    OrderRepository orderRepository;
    private MutableLiveData<Customer> mNewCustomerLiveData;

    @Inject
    public OrderViewModel(@NonNull Application application) {
        super(application);
        mContext=application.getApplicationContext();
        mCustomerLiveData=new MutableLiveData<>();
        mNewCustomerLiveData=new MutableLiveData<>();
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
                        .setInitialDelay(NotificationUtils.NOTIFICATION_MIN, TimeUnit.MINUTES)
                        .setInputData(OrderUtils.getNotificationData(orderId,restaurant))
                        .build();
        mWorkManager.enqueue(
                schedulerWorkRequest);

    }

    public LiveData<Customer> addCustomer(GoogleSignInAccount account) {
        orderRepository.addCustomer(account,mNewCustomerLiveData);
        return mNewCustomerLiveData;
    }

    public void saveCustomer(Customer customer) {

        //add customer in place order to the
        orderRepository.saveCustomer(mContext,customer);
    }

    public LiveData<Customer> getRedeemPoints(UUID id) {
        orderRepository.getCustomer(id,mCustomerLiveData);
        return mCustomerLiveData;
    }

    public Customer getCustomer() {
        return orderRepository.getCustomer(mContext);
    }

    public void delCustomer() {
        orderRepository.delCustomer(mContext);
    }
}