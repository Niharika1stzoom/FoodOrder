package com.example.foodorder.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.widget.ListView;

import com.example.foodorder.model.MenuItem;
import com.example.foodorder.model.Order;
import com.example.foodorder.model.Restaurant;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class SharedPrefUtils {
    public static final String KEY_ORDER = "current_order";

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
    synchronized public static void createOrder(Context context, String table) {
        Order order=new Order(new Restaurant(table,AppConstants.RESTAURANT_NUM,AppConstants.RESTAURANT_NAME));
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
        order.setOrderExecuted(true);
        SharedPrefUtils.setOrder(context,order);
    }
}
