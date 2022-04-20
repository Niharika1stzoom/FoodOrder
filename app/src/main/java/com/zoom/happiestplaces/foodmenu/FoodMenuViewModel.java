package com.zoom.happiestplaces.foodmenu;

import android.app.Application;
import android.content.Context;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.zoom.happiestplaces.model.MenuItem;
import com.zoom.happiestplaces.model.Restaurant;
import com.zoom.happiestplaces.util.SharedPrefUtils;

import java.util.List;
import java.util.UUID;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class FoodMenuViewModel extends AndroidViewModel {
    Context mContext;
    private MutableLiveData<List<MenuItem>> mMenuList;
    private MutableLiveData<Restaurant> mRestaurantLiveData;
    @Inject
    FoodMenuRepository mMenuRepository;

    @Inject
    public FoodMenuViewModel(Application application) {
        super(application);
        mContext=application.getApplicationContext();
        mMenuList = new MutableLiveData();
        mRestaurantLiveData=new MutableLiveData<>();
    }

    public LiveData<Restaurant> getMenuList(UUID qrcode) {
        mMenuRepository.getMenuList(mRestaurantLiveData,qrcode);
        return mRestaurantLiveData;
    }


   /* public LiveData<Restaurant> getRestaurant(UUID mRestaurantId) {
        mMenuRepository.getRestaurant(mRestaurantId,mRestaurantLiveData);
        return mRestaurantLiveData;
    }*/

    public Restaurant getRestaurant() {
        return SharedPrefUtils.getOrder(mContext).getRestaurant();
    }
}