package com.zoom.happiestplaces.util;

import com.zoom.happiestplaces.model.Customer;
import com.zoom.happiestplaces.model.Order;
import com.google.gson.Gson;

public class GsonUtils {
    public static String getGsonObject(Order order) {
        Gson gson = new Gson();
        return gson.toJson(order);
    }

    public static Order getModelObject(String order) {
        Gson gson = new Gson();
        return gson.fromJson(order, Order.class);

    }

    public static String getGsonObject(Customer customer) {
        Gson gson = new Gson();
        return gson.toJson(customer);
    }
    public static Customer getModelObjectCustomer(String customer) {
        Gson gson = new Gson();
        return gson.fromJson(customer, Customer.class);

    }
}
