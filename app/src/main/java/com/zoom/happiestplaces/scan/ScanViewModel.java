package com.zoom.happiestplaces.scan;

import android.Manifest;
import android.app.Application;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.content.Context;
import android.content.pm.PackageManager;
import android.text.TextUtils;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.messaging.FirebaseMessaging;
import com.zoom.happiestplaces.MainActivity;
import com.zoom.happiestplaces.foodmenu.FoodMenuRepository;
import com.zoom.happiestplaces.model.Restaurant;
import com.zoom.happiestplaces.util.AppConstants;
import com.zoom.happiestplaces.util.SharedPrefUtils;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
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
        mContext = application.getApplicationContext();
        mRestaurantLiveData = new MutableLiveData<>();
    }

    public LiveData<Restaurant> getRestaurant(UUID mRestaurantId) {
        mMenuRepository.getRestaurant(mRestaurantId, mRestaurantLiveData);
        return mRestaurantLiveData;

    }

    void getRegistrationToken() {
        String token = SharedPrefUtils.getFcmToken(mContext);
        if (token == null || TextUtils.isEmpty(token))
            FirebaseMessaging.getInstance().getToken()
                    .addOnCompleteListener(new OnCompleteListener<String>() {
                        @Override
                        public void onComplete(@NonNull Task<String> task) {
                            if (!task.isSuccessful()) {
                                return;
                            }
                            // Get new FCM registration token
                            String token = task.getResult();
                            SharedPrefUtils.saveFcmToken(mContext, token);
                        }
                    });

    }
    public void saveReferral(String custID) {
        SharedPrefUtils.saveReferral(mContext,custID);
    }
}