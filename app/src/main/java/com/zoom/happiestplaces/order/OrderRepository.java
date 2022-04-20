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

    public void getOrder(MutableLiveData<Order> liveData, UUID order_id) {
        //pass order id here
        Call<Order> call = mApiInterface.getOrder();
        call.enqueue(new Callback<Order>() {
            @Override
            public void onResponse(@NonNull Call<Order> call,
                                   @NonNull Response<Order> response) {
                if (response.isSuccessful()) {
                    liveData.postValue(response.body());
                } else {
                    liveData.postValue(null);
                    Log.d(AppConstants.TAG,"Not success"+response.message());
                }
            }
            @Override
            public void onFailure(@NonNull Call<Order> call, @NonNull Throwable t) {
                Log.d(AppConstants.TAG,"Failure"+t.getLocalizedMessage());
                liveData.postValue(null);
            }
        });
    }

    public void addCustomer(Customer customer, MutableLiveData<Customer> liveData) {
        getCustomer(UUID.fromString("2c7d0322-c45f-49fa-aeea-65855aba5319"),liveData);
      /*  Call<Customer> call = mApiInterface.postCustomer(customer);
        call.enqueue(new Callback<Customer>() {
            @Override
            public void onResponse(@NonNull Call<Customer> call,
                                   @NonNull Response<Customer> response) {
                if (response.isSuccessful()) {
                    //for testing as there is no api
                    liveData.postValue(customer);
                } else {
                    liveData.postValue(null);
                    Log.d(AppConstants.TAG,"Not success"+response.message());
                }
            }
            @Override
            public void onFailure(@NonNull Call<Customer> call, @NonNull Throwable t) {
               Log.d(AppConstants.TAG,"Failure"+t.getLocalizedMessage());
                liveData.postValue(null);
            }
        });*/
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
                    Log.d(AppConstants.TAG,"Success"+response.body());
                } else {
                    liveData.postValue(null);
                    Log.d(AppConstants.TAG,"Not success"+response.message());
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
       /* List<OrderMenuItem> list=new ArrayList<>();
        for(MenuItem menuItem: menuList)
        {
            OrderMenuItem item=new OrderMenuItem(menuItem.getId(), menuItem.getQty());
            list.add(item);
            Log.d(AppConstants.TAG,"Order item"+item.getDish_id()+item.getQty());
        }*/
        //CustomerOrder cOrder=new CustomerOrder(order.getTable(),null,order.getItemsList());
        Call<OrderResponse> call = mApiInterface.placeOrder(order);
        call.enqueue(new Callback<OrderResponse>() {
            @Override
            public void onResponse(@NonNull Call<OrderResponse> call,
                                   @NonNull Response<OrderResponse> response) {
                if (response.isSuccessful()) {
                    liveData.postValue(response.body());
                    Log.d(AppConstants.TAG,"Success"+response.body().getTime().toString());
                } else {
                    liveData.postValue(null);
                    Log.d(AppConstants.TAG,"Not success"+response.message()+response.errorBody()+"/n"+response.code());
                }
            }
            @Override
            public void onFailure(@NonNull Call<OrderResponse> call, @NonNull Throwable t) {
                Log.d(AppConstants.TAG,"Failure"+t.getLocalizedMessage());
                liveData.postValue(null);
            }
        });

       /* Call<Order> call = mApiInterface.placeOrder(order);
        call.enqueue(new Callback<Order>() {
            @Override
            public void onResponse(@NonNull Call<Order> call,
                                   @NonNull Response<Order> response) {
                if (response.isSuccessful()) {
                    liveData.postValue(response.body());
                    Log.d(AppConstants.TAG,"Success"+response.body().foodItemsList.size()+response.body().getId());
                } else {
                    liveData.postValue(null);
                    Log.d(AppConstants.TAG,"Not success"+response.message());
                }
            }
            @Override
            public void onFailure(@NonNull Call<Order> call, @NonNull Throwable t) {
                Log.d(AppConstants.TAG,"Failure"+t.getLocalizedMessage());
                liveData.postValue(null);
            }
        });*/
    }
}

