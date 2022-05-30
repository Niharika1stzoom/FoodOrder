package com.zoom.happiestplaces.di;

import com.zoom.happiestplaces.foodmenu.FoodMenuRepository;
import com.zoom.happiestplaces.network.RestaurantApi;
import com.zoom.happiestplaces.order.OrderRepository;
import com.zoom.happiestplaces.repository.IssueRepository;
import com.zoom.happiestplaces.review.ReviewRepository;
import com.zoom.happiestplaces.review.ReviewSingleton;

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
    String baseURL = "https://restaurants.happiestplaces.com/api/";
    //String baseURL="http://20.106.72.196/api/";
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
    @Provides
    IssueRepository provideIssueRepository(RestaurantApi apiInterface){
        return new IssueRepository(apiInterface);
    }
}
