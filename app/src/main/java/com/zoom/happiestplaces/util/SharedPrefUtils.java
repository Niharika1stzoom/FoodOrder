package com.zoom.happiestplaces.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.text.TextUtils;

import com.zoom.happiestplaces.model.Customer;
import com.zoom.happiestplaces.model.MenuItem;
import com.zoom.happiestplaces.model.Order;
import com.zoom.happiestplaces.model.OrderMenuItem;
import com.zoom.happiestplaces.model.Restaurant;

import java.util.List;
import java.util.UUID;

public class SharedPrefUtils {
    public static final String KEY_ORDER = "current_order";
    private static final String KEY_CUSTOMER ="customer" ;
    private static final String KEY_FCM_TOKEN = "fcm_token";
    private static final String KEY_BLUETOOTH_ADDRESS = "address";
    private static final String KEY_REFERRAL = "referral";
    private static final String KEY_FIRST_USER = "first_time_user";

    synchronized private static void setOrder(Context context, Order order) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(KEY_ORDER,GsonUtils.getGsonObject(order));
        editor.apply();
    }
    public static Order getOrder(Context context) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        String order = prefs.getString(KEY_ORDER, "");
        return GsonUtils.getModelObject(order) ;
    }
    synchronized public static void createOrder(Context context, UUID table, Restaurant restaurant) {
        Order order=new Order(table,restaurant);
        SharedPrefUtils.setOrder(context,order);
    }
    synchronized public static void createOrder(Context context, UUID table) {
        Order order=new Order(table);
        SharedPrefUtils.setOrder(context,order);
    }
    synchronized public static void setRestaurant(Context context, Restaurant restaurant) {
        Order order=SharedPrefUtils.getOrder(context);
        order.setRestaurant(restaurant);
        SharedPrefUtils.setOrder(context,order);
    }


    synchronized public static void addItem(Context context, MenuItem item) {
        Order order=SharedPrefUtils.getOrder(context);
        order.addMenuItem(item);
        SharedPrefUtils.setOrder(context,order);
    }

    synchronized public static void incrementQty(Context context, MenuItem item) {
        Order order=SharedPrefUtils.getOrder(context);
        order.incrementQty(item.getId());
        SharedPrefUtils.setOrder(context,order);
    }
    synchronized public static void decrementQty(Context context, MenuItem item) {
        Order order=SharedPrefUtils.getOrder(context);
        order.decrementQty(item.getId());
        SharedPrefUtils.setOrder(context,order);
    }
    synchronized public static void removeItem(Context context, MenuItem item) {
        Order order=SharedPrefUtils.getOrder(context);
        order.removeMenuItem(item.getId());
        SharedPrefUtils.setOrder(context,order);
    }

    public static OrderMenuItem containsItem(Context context, UUID id) {
        Order order=SharedPrefUtils.getOrder(context);
        return order.getMenuItem(id);
    }

    synchronized public static void addRedeemPoints(Context context, Double points) {
        Order order=SharedPrefUtils.getOrder(context);
        order.setRedeem_points(points);
        SharedPrefUtils.setOrder(context,order);
    }
    synchronized public static void cancelOrder(Context context) {
        Order order=SharedPrefUtils.getOrder(context);
        order.cancelOrder();
        order.setOrderExecuted(false);
        SharedPrefUtils.setOrder(context,order);
    }

    public static List<OrderMenuItem> getListView(Context context) {
     //   Collection<MenuItem> list= SharedPrefUtils.getOrder(context).getFoodItems().values();
       // List orderItems=new ArrayList<>(list);
        return SharedPrefUtils.getOrder(context).getItemsList();
    }

    public static Double showTotal(Context context) {

       /* List<MenuItem>menuItems =getListView(context);
        Double total=0.0;
        for(MenuItem item:menuItems)
        {
            total+=item.getQty()*item.getPrice();
        }*/
        return  SharedPrefUtils.getOrder(context).getTotalPrice();
    }

    public static boolean checkOrderIsEmpty(Context context) {
        Order order=SharedPrefUtils.getOrder(context);
        return order.isOrderEmpty();
    }

    public static boolean checkOrderExecuted(Context context) {
        Order order=getOrder(context);
        return order.getOrderExecuted();
    }

    synchronized public static void setOrderExecuted(Context context) {
        Order order=getOrder(context);
        order.setTime();
        order.setOrderExecuted(true);
        SharedPrefUtils.setOrder(context,order);
    }

    synchronized public static void setCustomer(Context context, Customer customer) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(KEY_CUSTOMER,GsonUtils.getGsonObject(customer));
        editor.apply();
    }

    public static Customer getCustomer(Context context) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        String customer = prefs.getString(KEY_CUSTOMER, "");
        if(TextUtils.isEmpty(customer))
            return null;
        else
        return GsonUtils.getModelObjectCustomer(customer) ;
    }

    synchronized public static void delCustomer(Context context) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = prefs.edit();
        editor.remove(KEY_CUSTOMER);
        editor.apply();
    }

    public static void addCustomerOrder(Context context) {
        //if customer is logged in will add customer to order this will happen when user places order
        Customer customer=SharedPrefUtils.getCustomer(context);
        if(customer==null)
            return;
        Order order=SharedPrefUtils.getOrder(context);
               order.setCustomer(customer);
               order.setCustomerId(customer.getId());
               SharedPrefUtils.setOrder(context,order);
    }

    public synchronized static void saveFcmToken(Context context,String token) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(KEY_FCM_TOKEN,token);
        editor.apply();
    }
    public synchronized static String getFcmToken(Context context) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        String token=prefs.getString(KEY_FCM_TOKEN,"");
       return token;
    }

    public static void saveReferral(Context context, String custID) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(KEY_REFERRAL,custID);
        editor.apply();
    }
    public synchronized static String getReferral(Context context) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        String referral=prefs.getString(KEY_REFERRAL,"");
        return referral;
    }
    public static void setFirstTimeUser(Context context, boolean b) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putBoolean(KEY_FIRST_USER,b);
        editor.apply();
    }
    public synchronized static Boolean getFirstTimeUser(Context context) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        return prefs.getBoolean(KEY_FIRST_USER,false);
    }
}
