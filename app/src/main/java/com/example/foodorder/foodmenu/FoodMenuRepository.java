package com.example.foodorder.foodmenu;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.example.foodorder.model.MenuItem;
import com.example.foodorder.network.RestaurantApi;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FoodMenuRepository {
    private RestaurantApi mApiInterface;

    public FoodMenuRepository( RestaurantApi apiInterface) {
        mApiInterface = apiInterface;
    }

    public void getMenuList(MutableLiveData<List<MenuItem>> liveData, String restaurantName) {
        Call<List<MenuItem>> call = mApiInterface.getMenu();
        call.enqueue(new Callback<List<MenuItem>>() {
            @Override
            public void onResponse(@NonNull Call<List<MenuItem>> call,
                                   @NonNull Response<List<MenuItem>> response) {
                if (response.isSuccessful()) {
                    liveData.postValue(response.body());
                } else {
                    liveData.postValue(null);
                }
            }
            @Override
            public void onFailure(@NonNull Call<List<MenuItem>> call, @NonNull Throwable t) {
                liveData.postValue(null);
            }
        });
    }
}

