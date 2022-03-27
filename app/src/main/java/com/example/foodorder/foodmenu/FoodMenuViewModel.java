package com.example.foodorder.foodmenu;

import android.content.Context;
import android.view.Menu;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.foodorder.model.MenuItem;
import com.example.foodorder.util.SharedPrefUtils;

import java.util.List;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class FoodMenuViewModel extends ViewModel {
    private MutableLiveData<List<MenuItem>> mMenuList;
    @Inject
    FoodMenuRepository mMenuRepository;

    @Inject
    public FoodMenuViewModel() {
        mMenuList = new MutableLiveData();
    }

    public LiveData<List<MenuItem>> getMenuList(String restaurant) {
        mMenuRepository.getMenuList(mMenuList,restaurant);
        return mMenuList;
    }


}