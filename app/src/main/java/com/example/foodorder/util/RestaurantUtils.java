package com.example.foodorder.util;

import android.os.Bundle;

import com.example.foodorder.model.MenuItem;
import com.example.foodorder.model.Order;
import com.example.foodorder.model.Restaurant;

import java.util.UUID;

public class RestaurantUtils {
    public static final String ARG_RESTAURANT = "restaurant";
    public static final String ARG_RESTAURANT_ID = "restaurant_id";


    public static Bundle getRestaurantBundle(Restaurant restaurant) {
        Bundle bundle = new Bundle();
        bundle.putSerializable(ARG_RESTAURANT, restaurant);
        return bundle;
    }

    public static Bundle getRestaurantBundle(UUID id) {
        Bundle bundle = new Bundle();
        bundle.putString(ARG_RESTAURANT_ID, String.valueOf(id));
        return bundle;
    }
}
