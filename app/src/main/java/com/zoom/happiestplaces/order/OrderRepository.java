package com.zoom.happiestplaces.order;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.zoom.happiestplaces.model.Customer;
import com.zoom.happiestplaces.model.Order;
import com.zoom.happiestplaces.model.response.OrderResponse;
import com.zoom.happiestplaces.network.RestaurantApi;
import com.zoom.happiestplaces.util.AppConstants;
import com.zoom.happiestplaces.util.CustomerUtils;
import com.zoom.happiestplaces.util.SharedPrefUtils;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;

import java.util.UUID;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OrderRepository {
    private RestaurantApi mApiInterface;

    public OrderRepository( RestaurantApi apiInterface) {
        mApiInterface = apiInterface;
    }

    public void getOrder(MutableLiveData<OrderResponse> liveData, UUID order_id) {
        Call<OrderResponse> call = mApiInterface.getOrder(order_id);
        call.enqueue(new Callback<OrderResponse>() {
            @Override
            public void onResponse(@NonNull Call<OrderResponse> call,
                                   @NonNull Response<OrderResponse> response) {
                if (response.isSuccessful()) {
                    liveData.postValue(response.body());
                } else {
                    liveData.postValue(null);
                }
            }
            @Override
            public void onFailure(@NonNull Call<OrderResponse> call, @NonNull Throwable t) {
                Log.d(AppConstants.TAG,"Failure order"+t.getLocalizedMessage());
                liveData.postValue(null);
            }
        });
    }

    public void addCustomer(Customer customer, MutableLiveData<Customer> liveData) {
        Call<Customer> call = mApiInterface.postCustomer(customer);
        call.enqueue(new Callback<Customer>() {
            @Override
            public void onResponse(@NonNull Call<Customer> call,
                                   @NonNull Response<Customer> response) {
                if (response.isSuccessful()) {
                    liveData.postValue(response.body());
                } else {
                    liveData.postValue(null);
                }
            }
            @Override
            public void onFailure(@NonNull Call<Customer> call, @NonNull Throwable t) {
               Log.d(AppConstants.TAG,"Failure"+t.getLocalizedMessage());
                liveData.postValue(null);
            }
        });
    }
    public void getCustomer(UUID id, MutableLiveData<Customer> liveData) {
        //pass id
        Call<Customer> call = mApiInterface.getCustomer(id);
        call.enqueue(new Callback<Customer>() {
            @Override
            public void onResponse(@NonNull Call<Customer> call,
                                   @NonNull Response<Customer> response) {
                if (response.isSuccessful()) {
                    liveData.postValue(response.body());
                } else {
                    liveData.postValue(null);
                }
            }

            @Override
            public void onFailure(@NonNull Call<Customer> call, @NonNull Throwable t) {
                Log.d(AppConstants.TAG,"Failure"+t.getLocalizedMessage());
                liveData.postValue(null);
            }
        });
    }


    public void saveCustomer(Context context, Customer customer) {
        SharedPrefUtils.setCustomer(context,customer);
    }

    public Customer getCustomer(Context mContext) {
        return SharedPrefUtils.getCustomer(mContext);
    }
    public void delCustomer(Context context) {
        SharedPrefUtils.delCustomer(context);
    }

    public void placeOrder(MutableLiveData<OrderResponse> liveData,Order order) {
        Call<OrderResponse> call = mApiInterface.placeOrder(order);
        call.enqueue(new Callback<OrderResponse>() {
            @Override
            public void onResponse(@NonNull Call<OrderResponse> call,
                                   @NonNull Response<OrderResponse> response) {
                if (response.isSuccessful()) {
                    liveData.postValue(response.body());
                } else {
                    liveData.postValue(null);
                }
            }
            @Override
            public void onFailure(@NonNull Call<OrderResponse> call, @NonNull Throwable t) {
                Log.d(AppConstants.TAG,"Failure"+t.getLocalizedMessage());
                liveData.postValue(null);
            }
        });
    }
}

