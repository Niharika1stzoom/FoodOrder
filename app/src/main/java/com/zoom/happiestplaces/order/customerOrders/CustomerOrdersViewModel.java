package com.zoom.happiestplaces.order.customerOrders;

import android.app.Application;
import android.content.Context;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.zoom.happiestplaces.model.response.OrderResponse;
import com.zoom.happiestplaces.order.OrderRepository;

import java.util.List;
import java.util.UUID;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class CustomerOrdersViewModel extends AndroidViewModel {
    private final Context mContext;
    private MutableLiveData<List<OrderResponse>> mOrders;
    @Inject
    OrderRepository orderRepository;
    @Inject
    public CustomerOrdersViewModel(Application application) {
        super(application);
        mContext=application.getApplicationContext();
        mOrders = new MutableLiveData();

    }


    public LiveData<List<OrderResponse>> getOrders(UUID id) {
        orderRepository.getOrdersCustomer(id,mOrders);
        return mOrders;
    }

}