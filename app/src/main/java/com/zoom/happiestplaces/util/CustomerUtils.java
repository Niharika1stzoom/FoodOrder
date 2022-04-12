package com.zoom.happiestplaces.util;

import com.zoom.happiestplaces.model.Customer;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;

public class CustomerUtils {
    public static Customer getCustomerAccount(GoogleSignInAccount account) {
        String url="";
        if(account.getPhotoUrl()!=null)
         url=account.getPhotoUrl().getPath();
        Customer customer= new Customer(account.getDisplayName(),
                account.getEmail(),account.getId(), url);
        customer.setCurrent_pts(AppConstants.NEW_USER_POINTS);
        return customer;
    }
}
