package com.example.foodorder.order;

import android.app.Application;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.ViewModel;

import com.example.foodorder.util.AppUtils;
import com.example.foodorder.util.OrderUtils;
import com.example.foodorder.util.SharedPrefUtils;

public class OrderViewModel extends AndroidViewModel {
    private Context mContext;
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
}