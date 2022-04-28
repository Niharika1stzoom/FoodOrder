package com.zoom.happiestplaces.profile;

import android.app.Application;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.zoom.happiestplaces.model.Customer;
import com.zoom.happiestplaces.order.OrderRepository;
import com.zoom.happiestplaces.util.SharedPrefUtils;
import com.zoom.happiestplaces.util.SignInUtils;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class ProfileViewModel extends AndroidViewModel {
    Context mContext;
    private GoogleSignInClient mGoogleSignInClient;
    @Inject
    OrderRepository orderRepository;
    MutableLiveData<Customer> customerLiveData;

    @Inject
    public ProfileViewModel(@NonNull Application application) {
        super(application);
        mContext=application.getApplicationContext();
        mGoogleSignInClient= GoogleSignIn.getClient(mContext, SignInUtils.getSignInOptions());
        customerLiveData=new MutableLiveData<>();
    }
    public void logOut() {
        SharedPrefUtils.delCustomer(mContext);
    }

    public GoogleSignInClient getGoogleClient() {
        return mGoogleSignInClient;
    }

    public LiveData<Customer> updateCustomer(Customer customer) {
        orderRepository.addCustomer(customer,customerLiveData);
        return customerLiveData;
    }
    public void saveCustomer(Customer customer) {

        orderRepository.saveCustomer(mContext,customer);
    }
    public Customer getCustomer() {
        return SharedPrefUtils.getCustomer(mContext);
    }
}