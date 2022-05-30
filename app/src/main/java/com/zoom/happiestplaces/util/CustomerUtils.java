package com.zoom.happiestplaces.util;

import android.content.Context;
import android.text.TextUtils;

import com.google.firebase.crashlytics.FirebaseCrashlytics;
import com.zoom.happiestplaces.model.Customer;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;

import java.util.UUID;

public class CustomerUtils {
    public static Customer getCustomerAccount(GoogleSignInAccount account, Context context) {
        String s="";
        if(account.getPhotoUrl()!=null )
        {
            s=account.getPhotoUrl().toString();
        }
        String token=SharedPrefUtils.getFcmToken(context);
                String referral=SharedPrefUtils.getReferral(context);
                UUID ref=referral==null || TextUtils.isEmpty(referral) ?null: UUID.fromString(referral);
        Customer customer= new Customer(account.getDisplayName(),
                account.getEmail(),account.getId(), s,token,ref);
        setFirebaseCrashylyticsDetails(context, account.getEmail());
        return customer;
    }
    public static void setFirebaseCrashylyticsDetails(Context context,String email) {
        FirebaseCrashlytics crashlytics = FirebaseCrashlytics.getInstance();
        crashlytics.setCustomKey(AppConstants.KEY_FUSER_EMAIL, email);
    }
}
