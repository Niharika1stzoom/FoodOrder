package com.example.foodorder.network;

import com.example.foodorder.model.MenuItem;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface RestaurantApi {
    @GET("0f867f87-231c-4b47-9379-bf405d453aff")
    Call<List<MenuItem>> getMenu();
}
