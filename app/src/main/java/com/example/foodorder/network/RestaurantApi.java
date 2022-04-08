package com.example.foodorder.network;

import com.example.foodorder.model.MenuItem;
import com.example.foodorder.model.Order;
import com.example.foodorder.model.Restaurant;
import com.example.foodorder.model.RestaurantReview;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface RestaurantApi {
    @GET("0f867f87-231c-4b47-9379-bf405d453aff")
    Call<List<MenuItem>> getMenu();

    @GET("2658cd7d-53b3-4f8e-9143-fcfaa7e45aa0")
    Call<Order> getOrder();

    @GET("10ff9934-969a-438b-8750-d2a4245da61f")
    Call<List<RestaurantReview>> getRestaurantReview();

    @GET("13efb8ec-06fe-4623-8cf6-9bd81bdd0dc1")
    Call<Restaurant> getRestaurant();
}
