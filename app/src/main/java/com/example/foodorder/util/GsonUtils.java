package com.example.foodorder.util;

import com.example.foodorder.model.Order;
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
}
