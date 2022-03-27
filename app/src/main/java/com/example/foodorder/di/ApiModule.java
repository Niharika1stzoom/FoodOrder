package com.example.foodorder.di;

import com.example.foodorder.foodmenu.FoodMenuRepository;
import com.example.foodorder.network.RestaurantApi;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import dagger.hilt.InstallIn;
import dagger.hilt.components.SingletonComponent;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

@Module
@InstallIn(SingletonComponent.class)
public class ApiModule {
    //String baseURL = "https://api.restaurant.com/";
    String baseURL="https://mocki.io/v1/";
    @Singleton
    @Provides
    public RestaurantApi getRestApiInterface(Retrofit retrofit) {
        return retrofit.create(RestaurantApi.class);
    }
    @Singleton
    @Provides
    public Retrofit getRetroInstance() {
        return new Retrofit.Builder()
                .baseUrl(baseURL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }
    @Singleton
    @Provides
    FoodMenuRepository provideRepository(RestaurantApi apiInterface){
        return new FoodMenuRepository(apiInterface);
    }
}
