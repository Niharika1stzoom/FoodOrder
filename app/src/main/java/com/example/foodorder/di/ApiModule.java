package com.example.foodorder.di;

import com.example.foodorder.foodmenu.FoodMenuRepository;
import com.example.foodorder.model.Review;
import com.example.foodorder.network.RestaurantApi;
import com.example.foodorder.order.OrderRepository;
import com.example.foodorder.review.ReviewRepository;
import com.example.foodorder.review.ReviewSingleton;

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
    public ReviewSingleton getReviewInstance() {
        return ReviewSingleton.getReview();
    }

    @Singleton
    @Provides
    FoodMenuRepository provideMenuRepository(RestaurantApi apiInterface){
        return new FoodMenuRepository(apiInterface);
    }
    @Provides
    OrderRepository provideOrderRepository(RestaurantApi apiInterface){
        return new OrderRepository(apiInterface);
    }
    @Provides
    ReviewRepository provideRepository(RestaurantApi apiInterface,ReviewSingleton reviewSingleton){
        return new ReviewRepository(apiInterface,reviewSingleton);
    }
}
