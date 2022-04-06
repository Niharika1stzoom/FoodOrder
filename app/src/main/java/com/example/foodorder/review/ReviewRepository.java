package com.example.foodorder.review;

import android.util.Log;

import com.example.foodorder.model.MenuItemReview;
import com.example.foodorder.model.Order;
import com.example.foodorder.model.RestaurantReview;
import com.example.foodorder.network.RestaurantApi;

import java.text.SimpleDateFormat;

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
       SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd LLL HH:mm");
        String dateTime = simpleDateFormat.format(sReviewSingleton.getOrder().getTime());
        return dateTime;
    }
}
