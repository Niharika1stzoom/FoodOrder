package com.example.foodorder.util;

import android.content.Context;
import android.telephony.SmsManager;
import android.widget.Toast;

import com.example.foodorder.R;
import com.example.foodorder.model.MenuItem;
import com.example.foodorder.model.Order;


import java.util.List;

public class OrderUtils {
    public static boolean sendSms(Context context, Order order) {
        String phone_Num = order.getRestaurant().getPhoneNum();
       String send_msg =OrderUtils.getSmsMsg(context);
       try {
            SmsManager sms = SmsManager.getDefault();
            sms.sendTextMessage(phone_Num, null, send_msg, null, null); // adding number and text
        } catch (Exception e) {
            Toast.makeText(context, "Your order is not placed.Unable to send order msg."+e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            e.printStackTrace();
            return false;
       }
        return true;
    }

    private static String getSmsMsg(Context context) {
        Order order=SharedPrefUtils.getOrder(context);
        String title="Order from Table num: "+order.getRestaurant().getTableNum();
        String orderItems="";
        List<MenuItem> orderList=SharedPrefUtils.getListView(context);
        for(MenuItem item:orderList)
            orderItems+="\n"+item.getName()+"   X "+item.getQty();
        String total="\nTotal Pay: Rs "+SharedPrefUtils.showTotal(context);
        return title+orderItems+total;

    }
}
