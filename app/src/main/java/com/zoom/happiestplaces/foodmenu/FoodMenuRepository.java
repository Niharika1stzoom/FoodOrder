package com.zoom.happiestplaces.foodmenu;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.zoom.happiestplaces.model.MenuItem;
import com.zoom.happiestplaces.model.Restaurant;
import com.zoom.happiestplaces.network.RestaurantApi;
import com.zoom.happiestplaces.util.AppConstants;

import java.util.List;
import java.util.UUID;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FoodMenuRepository {
    private RestaurantApi mApiInterface;

    public FoodMenuRepository( RestaurantApi apiInterface) {
        mApiInterface = apiInterface;
    }

    public void getMenuList(MutableLiveData<Restaurant> liveData, UUID tableId) {
        //Call<List<MenuItem>> call = mApiInterface.getMenu();
        Call<Restaurant> call = mApiInterface.getRestaurantDishes(tableId);
        call.enqueue(new Callback<Restaurant>() {
            @Override
            public void onResponse(@NonNull Call<Restaurant> call,
                                   @NonNull Response<Restaurant> response) {
                if (response.isSuccessful()) {
                    liveData.postValue(response.body());
                } else {
                    liveData.postValue(null);
                }
            }
            @Override
            public void onFailure(@NonNull Call<Restaurant> call, @NonNull Throwable t) {
                liveData.postValue(null);
            }
        });
    }

    public void getRestaurant(UUID restaurantId, MutableLiveData<Restaurant> liveData) {
        //pass restId
        Call<Restaurant> call = mApiInterface.getRestaurant();
        call.enqueue(new Callback<Restaurant>() {
            @Override
            public void onResponse(@NonNull Call<Restaurant> call,
                                   @NonNull Response<Restaurant> response) {
                if (response.isSuccessful()) {
                    liveData.postValue(response.body());
                } else {
                    liveData.postValue(null);
                }
            }
            @Override
            public void onFailure(@NonNull Call<Restaurant> call, @NonNull Throwable t) {
                liveData.postValue(null);
            }
        });
    }
}

