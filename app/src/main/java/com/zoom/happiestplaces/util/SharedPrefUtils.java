package com.zoom.happiestplaces.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.zoom.happiestplaces.model.Customer;
import com.zoom.happiestplaces.model.MenuItem;
import com.zoom.happiestplaces.model.Order;
import com.zoom.happiestplaces.model.Restaurant;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class SharedPrefUtils {
    public static final String KEY_ORDER = "current_order";
    private static final String KEY_CUSTOMER ="customer" ;

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
    synchronized public static void createOrder(Context context, String table, Restaurant restaurant) {
      //  Order order=new Order(new Restaurant(
      //          table,AppConstants.RESTAURANT_NUM,AppConstants.RESTAURANT_NAME));
        Order order=new Order(table,restaurant);
        SharedPrefUtils.setOrder(context,order);
    }
    synchronized public static void setRestaurant(Context context, Restaurant restaurant) {
        Order order=getOrder(context);
        order.setRestaurant(restaurant);
        SharedPrefUtils.setOrder(context,order);
    }

    synchronized public static void addItem(Context context, MenuItem item) {
        Order order=getOrder(context);
        order.addMenuItem(item);
        SharedPrefUtils.setOrder(context,order);
    }

    synchronized public static void incrementQty(Context context, MenuItem item) {
        Order order=getOrder(context);
        order.incrementQty(item.getId());
        SharedPrefUtils.setOrder(context,order);
    }
    synchronized public static void decrementQty(Context context, MenuItem item) {
        Order order=getOrder(context);
        order.decrementQty(item.getId());
        SharedPrefUtils.setOrder(context,order);
    }
    synchronized public static void removeItem(Context context, MenuItem item) {
        Order order=getOrder(context);
        order.removeMenuItem(item.getId());
        SharedPrefUtils.setOrder(context,order);
    }

    public static MenuItem containsItem(Context context,String id) {
        Order order=getOrder(context);
        return order.getMenuItem(id);
    }

    synchronized public static void cancelOrder(Context context) {
        Order order=getOrder(context);
        order.cancelOrder();
        order.setOrderExecuted(false);
        SharedPrefUtils.setOrder(context,order);
    }

    public static List<MenuItem> getListView(Context context) {
        Collection<MenuItem> list= SharedPrefUtils.getOrder(context).getFoodItems().values();
        List orderItems=new ArrayList<>(list);
        return orderItems;
    }

    public static Double showTotal(Context context) {
        List<MenuItem>menuItems =getListView(context);
        Double total=0.0;
        for(MenuItem item:menuItems)
        {
            total+=item.getQty()*item.getPrice();
        }
        return total;
    }

    public static boolean checkOrderIsEmpty(Context context) {
        Order order=getOrder(context);
        return order.getFoodItems().isEmpty();


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
        return GsonUtils.getModelObjectCustomer(customer) ;
    }

    synchronized public static void delCustomer(Context context) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = prefs.edit();
        editor.remove(KEY_CUSTOMER);
    }
}
