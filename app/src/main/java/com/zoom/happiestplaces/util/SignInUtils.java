package com.zoom.happiestplaces.util;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.zoom.happiestplaces.R;
import com.zoom.happiestplaces.order.OrderRepository;

import javax.inject.Inject;

public class SignInUtils {

    public static GoogleSignInOptions getSignInOptions()
    {
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        return gso;
    }


}
