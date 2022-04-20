package com.zoom.happiestplaces.profile;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.ViewModel;

import com.google.android.gms.auth.api.identity.SignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.zoom.happiestplaces.util.AppConstants;
import com.zoom.happiestplaces.util.SharedPrefUtils;
import com.zoom.happiestplaces.util.SignInUtils;

public class ProfileViewModel extends AndroidViewModel {
    Context mContext;
    private GoogleSignInClient mGoogleSignInClient;

    public ProfileViewModel(@NonNull Application application) {
        super(application);
        mContext=application.getApplicationContext();
        mGoogleSignInClient= GoogleSignIn.getClient(mContext, SignInUtils.getSignInOptions());
    }

    public void logOut() {
        SharedPrefUtils.delCustomer(mContext);


    }

    public GoogleSignInClient getGoogleClient() {
        return mGoogleSignInClient;
    }
}