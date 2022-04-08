package com.example.foodorder.review;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.example.foodorder.model.MenuItemReview;
import com.example.foodorder.model.Order;
import com.example.foodorder.model.RestaurantReview;
import com.example.foodorder.network.RestaurantApi;
import com.example.foodorder.util.DateUtil;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.UUID;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ReviewRepository {
    private RestaurantApi mApiInterface;
    private ReviewSingleton sReviewSingleton;
    public ReviewRepository( RestaurantApi apiInterface,ReviewSingleton review) {
        sReviewSingleton=review;
        mApiInterface = apiInterface;
    }

    public void addReviewMenuItem(String menuItemId, float stars) {
        sReviewSingleton.addReviewMenuItem(menuItemId,stars);
    }

    public MenuItemReview getMenuItemReview(String menuItemId) {
        return sReviewSingleton.getMenuItemReview(menuItemId);
    }

    public void startReview(Order order) {
        sReviewSingleton.setOrder(order);
    }

    public void submitReview() {
        //Make a post request to submit review to api take from singleton
        //clear singleton
        Log.d("FoodDebug",""+Integer.toString(sReviewSingleton.getReviewItems().size()));
        sReviewSingleton.clear();
    }
    //if review exist get ow create
    public RestaurantReview getRestoReview()
    {
        RestaurantReview review=sReviewSingleton.getRestaurantReview();
        if(review==null) {
            review = new RestaurantReview();
            sReviewSingleton.setRestaurantReview(review);
        }
        return review;
    }

    public void setRestoRating(float v) {
        getRestoReview().setNum_of_stars(v);
    }

    public void setRestoReviewText(String reviewText) {
        getRestoReview().setTextReview(reviewText);
    }

    public String getOrderTime() {
       //because
        sReviewSingleton.getOrder().setTime();
        return DateUtil.getReviewOrderDateFormat(sReviewSingleton.getOrder().getTime());
    }

    public void getRestaurantReviews(UUID mRestaurantId, MutableLiveData<List<RestaurantReview>> liveData) {
        //pass restaurant id here
        Call<List<RestaurantReview>> call = mApiInterface.getRestaurantReview();
        call.enqueue(new Callback<List<RestaurantReview>>() {
            @Override
            public void onResponse(@NonNull Call<List<RestaurantReview>> call,
                                   @NonNull Response<List<RestaurantReview>> response) {
                if (response.isSuccessful()) {
                    liveData.postValue(response.body());
                    Log.d("FoodDebug","Success"+response.body().size());
                } else {
                    liveData.postValue(null);
                    Log.d("FoodDebug","Not success"+response.message());
                }
            }
            @Override
            public void onFailure(@NonNull Call<List<RestaurantReview>> call, @NonNull Throwable t) {
                Log.d("FoodDebug","Failure"+t.getLocalizedMessage());
                liveData.postValue(null);
            }
        });
    }
}
