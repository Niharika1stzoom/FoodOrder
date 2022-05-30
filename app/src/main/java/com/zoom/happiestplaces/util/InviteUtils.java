package com.zoom.happiestplaces.util;

import android.content.Context;

import com.zoom.happiestplaces.R;
import com.zoom.happiestplaces.model.Customer;

public class InviteUtils {

    public static String getShareLink(Context context)
    {
        //String custID="";
        Customer customer=SharedPrefUtils.getCustomer(context);
        String custId=customer!=null?customer.getId().toString():"";
        String sharelinktext  = AppConstants.FDYNAMIC_URL+"?"+
                "link="+AppConstants.LINK_URL+custId+
                "&apn="+ context.getApplicationContext().getPackageName()+
                "&st="+context.getString(R.string.app_name)+
                "&sd="+AppConstants.TAG_LINE+
                "&si="+AppConstants.LOGO_URL;
        return sharelinktext;
    }

    public static String getShareText(){
        String inviteText="I'm inviting you to try this hassle free food ordering app and earn reward points with each order \n";
        return inviteText;
    }
}
