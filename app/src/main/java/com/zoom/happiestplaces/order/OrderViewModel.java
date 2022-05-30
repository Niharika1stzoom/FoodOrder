package com.zoom.happiestplaces.order;

import android.app.Application;
import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;

import com.zoom.happiestplaces.R;
import com.zoom.happiestplaces.model.Customer;
import com.zoom.happiestplaces.model.Order;
import com.zoom.happiestplaces.model.response.OrderResponse;
import com.zoom.happiestplaces.util.AppConstants;
import com.zoom.happiestplaces.util.AppUtils;
import com.zoom.happiestplaces.util.CustomerUtils;
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
    MutableLiveData<OrderResponse> mOrderLiveData;
    @Inject
    OrderRepository orderRepository;
    private MutableLiveData<Customer> mNewCustomerLiveData;

    @Inject
    public OrderViewModel(@NonNull Application application) {
        super(application);
        mContext=application.getApplicationContext();
        mCustomerLiveData=new MutableLiveData<>();
        mNewCustomerLiveData=new MutableLiveData<>();
        mOrderLiveData=new MutableLiveData<>();
    }

    //Adds customer to order
    public LiveData<OrderResponse> placeOrder() {
        addCustomerToOrder();
        orderRepository.placeOrder(mOrderLiveData,
                SharedPrefUtils.getOrder(mContext));
        return mOrderLiveData ;
    }

    private void addCustomerToOrder() {
        SharedPrefUtils.addCustomerOrder(mContext);
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

        public void scheduleNotification(UUID orderId,UUID restaurantID,String restaurant)
    {
        if(getCustomer()==null)
            return;
        mWorkManager=WorkManager.getInstance(mContext);
        //TODO:if notification is on
        OneTimeWorkRequest schedulerWorkRequest =
                new OneTimeWorkRequest.Builder(ScheduleReviewWorker.class)
                        .setInitialDelay(NotificationUtils.NOTIFICATION_MIN, TimeUnit.MINUTES)
                        .setInputData(OrderUtils.getNotificationData(orderId, restaurantID,restaurant))
                        .build();
        mWorkManager.enqueue(
                schedulerWorkRequest);
    }

    public LiveData<Customer> addCustomer(Customer customer) {
        orderRepository.addCustomer(customer,mNewCustomerLiveData);
        return mNewCustomerLiveData;
    }

    public void saveCustomer(Customer customer) {
        orderRepository.saveCustomer(mContext,customer);
        }

    public LiveData<Customer> getRedeemPoints(Customer customer) {
        orderRepository.addCustomer(customer,mCustomerLiveData);
        return mCustomerLiveData;
    }

    public Customer getCustomer() {
        return orderRepository.getCustomer(mContext);
    }
    public void delCustomer() {
        CustomerUtils.setFirebaseCrashylyticsDetails(mContext,"");
        orderRepository.delCustomer(mContext);
    }

    public Double getRedeemRs() {
        Double points=getCustomer().getCurrent_pts();
        Double redeem=points
                *getCurrentOrder().getRestaurant().getPoints_rate();
            Log.d(AppConstants.TAG,"Resto "+getCurrentOrder().getRestaurant().getPoints_rate());
        if(redeem>getTotal()) {
            redeem = getTotal();
        }

        if(redeem==getTotal())
    {
        //TODO:to be handled
    }
        Log.d(AppConstants.TAG,"Reedeem "+redeem);
       return redeem;

    }
    public Double getRedeemPointsOrder() {
        Double points=getCustomer().getCurrent_pts();
        Double redeem=points
                *getCurrentOrder().getRestaurant().getPoints_rate();
        if(redeem>=getTotal()) {
            redeem = getTotal();
            return redeem/getCurrentOrder().getRestaurant().getPoints_rate();
         }
        if(redeem==getTotal())
        {
            //TODO:to be handled
        }
       return points;

    }

    public Double getRemainingPoints() {
        Double remaining=getCustomer().getCurrent_pts()-getRedeemPointsOrder();
        Log.d(AppConstants.TAG,"Remaining "+remaining);
        return remaining;

       // return getCustomer().getCurrent_pts()-(getRedeemPoints()/ getCurrentOrder().getRestaurant().getPoints_rate());
    }

    public void setRedeemPointsOrder() {
        SharedPrefUtils.addRedeemPoints(mContext,getRedeemPointsOrder());
        //getCurrentOrder().setRedeem_points(getRedeemPointsOrder());
        Log.d(AppConstants.TAG,"Redeem points for this order"+getRedeemPointsOrder());
    }
}



















































































































































































