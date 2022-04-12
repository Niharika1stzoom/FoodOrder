package com.zoom.happiestplaces.scan;

import android.app.Application;
import android.content.Context;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.zoom.happiestplaces.foodmenu.FoodMenuRepository;
import com.zoom.happiestplaces.model.Restaurant;

import java.util.UUID;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class ScanViewModel extends AndroidViewModel {
    private MutableLiveData<Restaurant> mRestaurantLiveData;
    Context mContext;
    @Inject
    FoodMenuRepository mMenuRepository;
    @Inject
    public ScanViewModel(Application application) {
        super(application);
        mContext=application.getApplicationContext();
        mRestaurantLiveData=new MutableLiveData<>();
    }
    public LiveData<Restaurant> getRestaurant(UUID mRestaurantId) {
        mMenuRepository.getRestaurant(mRestaurantId,mRestaurantLiveData);
        return mRestaurantLiveData;
    }

}