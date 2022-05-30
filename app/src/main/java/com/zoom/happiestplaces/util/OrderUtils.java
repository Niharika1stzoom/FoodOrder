package com.zoom.happiestplaces.util;

import android.content.Context;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.widget.Toast;

import androidx.work.Data;

import com.zoom.happiestplaces.R;
import com.zoom.happiestplaces.model.MenuItem;
import com.zoom.happiestplaces.model.Order;
import com.zoom.happiestplaces.model.Restaurant;


import java.util.List;
import java.util.UUID;

public class OrderUtils {
    public static final String ARG_ORDER = "order" ;
    public static final String ARG_ORDER_ID = "orderId";


    public  static Bundle getOrderBundle(Order order){
        Bundle bundle=new Bundle();
        bundle.putSerializable(ARG_ORDER,order);
        return bundle;
    }

    private static String getSmsMsg(Context context) {
      Order order=SharedPrefUtils.getOrder(context);
        String title="Order from Table num: "+order.getTable();
        String orderItems="";
     /*   List<MenuItem> orderList=SharedPrefUtils.getListView(context);
        for(MenuItem item:orderList)
            orderItems+="\n"+item.getName()+"   X "+item.getQty();*/
        String total="\nTotal Pay: Rs "+SharedPrefUtils.showTotal(context);
        return title+orderItems+total;

    }

    public static Data getNotificationData(UUID orderId, UUID restaurantID,String restaurant) {
        return new Data.Builder()
                .putString(AppConstants.KEY_ORDER_ID, String.valueOf(orderId))
                .putString(AppConstants.KEY_RESTAURANT_ID, String.valueOf(restaurantID))
                .putString(AppConstants.KEY_RESTAURANT_NAME, String.valueOf(restaurant))
                .build();

    }

    public static Bundle getOrderIDBundle(UUID orderId) {
        Bundle bundle=new Bundle();
        bundle.putString(ARG_ORDER_ID, String.valueOf(orderId));
        return bundle;
    }
    public static Bundle getOrderRestoBundle(UUID orderId, UUID restId) {
        Bundle bundle=new Bundle();
        bundle.putString(AppConstants.KEY_ORDER_ID, String.valueOf(orderId));
        bundle.putString(AppConstants.KEY_RESTAURANT_ID, String.valueOf(restId));
        return bundle;
    }
}
