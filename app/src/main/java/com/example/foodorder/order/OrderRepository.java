package com.example.foodorder.order;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.foodorder.model.MenuItem;
import com.example.foodorder.model.Order;
import com.example.foodorder.network.RestaurantApi;

import java.util.List;
import java.util.UUID;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OrderRepository {
    private RestaurantApi mApiInterface;

    public OrderRepository( RestaurantApi apiInterface) {
        mApiInterface = apiInterface;
    }

    public void getOrder(MutableLiveData<Order> liveData, UUID order_id) {
        //pass order id here
        Call<Order> call = mApiInterface.getOrder();
        call.enqueue(new Callback<Order>() {
            @Override
            public void onResponse(@NonNull Call<Order> call,
                                   @NonNull Response<Order> response) {
                if (response.isSuccessful()) {
                    liveData.postValue(response.body());
                    Log.d("FoodDebug","Success"+response.body().foodItemsList.size());
                } else {
                    liveData.postValue(null);
                    Log.d("FoodDebug","Not success"+response.message());
                }
            }
            @Override
            public void onFailure(@NonNull Call<Order> call, @NonNull Throwable t) {
                Log.d("FoodDebug","Failure"+t.getLocalizedMessage());
                liveData.postValue(null);
            }
        });
    }
    }

