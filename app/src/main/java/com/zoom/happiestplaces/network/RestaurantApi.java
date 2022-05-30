package com.zoom.happiestplaces.network;


import com.zoom.happiestplaces.model.Customer;
import com.zoom.happiestplaces.model.Issue;
import com.zoom.happiestplaces.model.MenuItem;
import com.zoom.happiestplaces.model.Order;
import com.zoom.happiestplaces.model.RateData;
import com.zoom.happiestplaces.model.Restaurant;
import com.zoom.happiestplaces.model.RestaurantReview;
import com.zoom.happiestplaces.model.ReviewData;
import com.zoom.happiestplaces.model.Topic;
import com.zoom.happiestplaces.model.response.OrderResponse;
import com.zoom.happiestplaces.model.response.ReviewDataResponse;

import java.util.List;
import java.util.UUID;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface RestaurantApi {
    @Headers("AUTHORIZATION: AUcufHNW2LKaP9c9uQT8krSMCwfrzA")
    @GET("qrcode/{tableId}")
    Call<Restaurant> getRestaurantDishes(@Path("tableId") UUID tableId);

   @Headers("AUTHORIZATION: AUcufHNW2LKaP9c9uQT8krSMCwfrzA")
    @POST("order/")
    Call<OrderResponse> placeOrder(@Body Order order);

    @Headers("AUTHORIZATION: AUcufHNW2LKaP9c9uQT8krSMCwfrzA")
    @POST("customer/")
    Call<Customer> postCustomer(@Body Customer customer);

    @Headers("AUTHORIZATION: AUcufHNW2LKaP9c9uQT8krSMCwfrzA")
    @GET("customer/{custId}")
    Call<Customer> getCustomer(@Path("custId") UUID custId);

    @Headers("AUTHORIZATION: AUcufHNW2LKaP9c9uQT8krSMCwfrzA")
    @GET("order/{orderId}")
    Call<OrderResponse> getOrder(@Path("orderId") UUID orderId);

    @Headers("AUTHORIZATION: AUcufHNW2LKaP9c9uQT8krSMCwfrzA")
    @GET("customer/order/{custId}")
    Call<List<OrderResponse>> getOrdersCustomer(@Path("custId") UUID custId);

    @Headers("AUTHORIZATION: AUcufHNW2LKaP9c9uQT8krSMCwfrzA")
    @GET("restaurant/{restId}")
    Call<Restaurant> getRestaurant(@Path("restId") UUID restId);

    @Headers("AUTHORIZATION: AUcufHNW2LKaP9c9uQT8krSMCwfrzA")
    @POST("ratings/dish/{dish_id}/{customer_id}/")
    Call<String> postRatingDish(@Body RateData rating,@Path("dish_id") UUID dish_id,
                                  @Path("customer_id") UUID customer_id);

    @Headers("AUTHORIZATION: AUcufHNW2LKaP9c9uQT8krSMCwfrzA")
    @POST("ratings/restaurant/{restaurant_id}/{customer_id}/")
    Call<RateData> postRatingRestaurant(@Body RateData rating,@Path("restaurant_id") UUID restaurant_id,
                                  @Path("customer_id") UUID customer_id);

 @Headers("AUTHORIZATION: AUcufHNW2LKaP9c9uQT8krSMCwfrzA")
 @POST("comments/restaurant/{restaurant_id}/")
 Call<ReviewData> postRestaurantComment(@Body ReviewData rating, @Path("restaurant_id") UUID restaurant_id);

    @Headers("AUTHORIZATION: AUcufHNW2LKaP9c9uQT8krSMCwfrzA")
    @GET("comments/restaurant/{restaurant_id}/")
    Call<List<ReviewDataResponse>> getRestaurantComments(@Path("restaurant_id") UUID restaurant_id);

    @Headers("AUTHORIZATION: AUcufHNW2LKaP9c9uQT8krSMCwfrzA")
    @GET("report/customer/")
    Call<Topic> getTopics();

    @Headers("AUTHORIZATION: AUcufHNW2LKaP9c9uQT8krSMCwfrzA")
    @POST("report/customer/")
    Call<Issue> sendIssue(@Body Issue issue);
}
