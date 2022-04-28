package com.zoom.happiestplaces.review;

import android.text.TextUtils;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.zoom.happiestplaces.model.MenuItemReview;
import com.zoom.happiestplaces.model.Order;
import com.zoom.happiestplaces.model.RateData;
import com.zoom.happiestplaces.model.Restaurant;
import com.zoom.happiestplaces.model.RestaurantReview;
import com.zoom.happiestplaces.model.ReviewData;
import com.zoom.happiestplaces.model.response.OrderResponse;
import com.zoom.happiestplaces.model.response.ReviewDataResponse;
import com.zoom.happiestplaces.network.RestaurantApi;
import com.zoom.happiestplaces.util.AppConstants;
import com.zoom.happiestplaces.util.DateUtil;

import org.w3c.dom.Text;

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

    public void addReviewMenuItem(UUID menuItemId, float stars) {
        sReviewSingleton.addReviewMenuItem(menuItemId,stars);
    }

    public MenuItemReview getMenuItemReview(UUID menuItemId) {
        return sReviewSingleton.getMenuItemReview(menuItemId);
    }

    public void startReview(OrderResponse order) {
        sReviewSingleton.setOrder(order);
    }

    public void submitReview() {
       //post rating restaurant

        if(sReviewSingleton.getRestaurantReview()!=null && sReviewSingleton.getRestaurantReview().getNum_of_stars()>0) {
            Call<RateData> call = mApiInterface.postRatingRestaurant(new RateData(sReviewSingleton.
                    getRestaurantReview().getNum_of_stars()),sReviewSingleton.getOrder().getRestaurant().getId(),
                    sReviewSingleton.getOrder().getCustomer());
            call.enqueue(new Callback<RateData>() {
                @Override
                public void onResponse(@NonNull Call<RateData> call,
                                       @NonNull Response<RateData> response) {
                    if (response.isSuccessful()) {
                        //liveData.postValue(response.body());

                    } else {
                        //liveData.postValue(null);
                    }
                }

                @Override
                public void onFailure(@NonNull Call<RateData> call, @NonNull Throwable t) {
                    Log.d(AppConstants.TAG, "Submit Review Failure" + t.getLocalizedMessage());
                    //liveData.postValue(null);
                }
            });

        }
        if(sReviewSingleton.getReviewItems()!=null && sReviewSingleton.getReviewItems().size()>0) {
            List<MenuItemReview> itemReviews = sReviewSingleton.getReviewItems();
            for (MenuItemReview item : itemReviews) {
                Call<String> call = mApiInterface.postRatingDish(new RateData(item.getNumStars()),
                        item.getMenuItemId(), sReviewSingleton.getOrder().getCustomer());
                call.enqueue(new Callback<String>() {
                    @Override
                    public void onResponse(@NonNull Call<String> call,
                                           @NonNull Response<String> response) {
                        if (response.isSuccessful()) {
                            //liveData.postValue(response.body());

                        } else {
                            //liveData.postValue(null);
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<String> call, @NonNull Throwable t) {
                        Log.d(AppConstants.TAG, "Failure Submit MenuItem" + t.getLocalizedMessage());
                        //liveData.postValue(null);
                    }
                });
            }
            }
        if(sReviewSingleton.getRestaurantReview()!=null && !TextUtils.isEmpty(sReviewSingleton.getRestaurantReview().getTextReview())) {
            Call<ReviewData> call = mApiInterface.postRestaurantComment(new ReviewData(sReviewSingleton.
                            getRestaurantReview().getTextReview(),sReviewSingleton.getOrder().getCustomer()),sReviewSingleton.getOrder().getRestaurant().getId());

            call.enqueue(new Callback<ReviewData>() {
                @Override
                public void onResponse(@NonNull Call<ReviewData> call,
                                       @NonNull Response<ReviewData> response) {
                    if (response.isSuccessful()) {
                        Log.d(AppConstants.TAG, "Review Restaurant success" + response.body());
                        //liveData.postValue(response.body());

                    } else {
                        Log.d(AppConstants.TAG, "Review Restaurant issue" + response.code()+response.message());
                        //liveData.postValue(null);
                    }
                }

                @Override
                public void onFailure(@NonNull Call<ReviewData> call, @NonNull Throwable t) {
                    Log.d(AppConstants.TAG, "Review Failure" + t.getLocalizedMessage());
                    //liveData.postValue(null);
                }
            });

        }
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
        return DateUtil.getReviewOrderDateFormat(sReviewSingleton.getOrder().getTime());
    }

    public void getRestaurantReviews(UUID mRestaurantId, MutableLiveData<List<ReviewDataResponse>> liveData) {
        //pass restaurant id here
        Call<List<ReviewDataResponse>> call = mApiInterface.getRestaurantComments(mRestaurantId);
        call.enqueue(new Callback<List<ReviewDataResponse>>() {
            @Override
            public void onResponse(@NonNull Call<List<ReviewDataResponse>> call,
                                   @NonNull Response<List<ReviewDataResponse>> response) {
                if (response.isSuccessful()) {
                    liveData.postValue(response.body());

                } else {
                    liveData.postValue(null);
                }
            }
            @Override
            public void onFailure(@NonNull Call<List<ReviewDataResponse>> call, @NonNull Throwable t) {
                Log.d(AppConstants.TAG,"Failure"+t.getLocalizedMessage());
                liveData.postValue(null);
            }
        });
    }

    public void setRestaurant(Restaurant restaurant) {
        sReviewSingleton.getOrder().setRestaurant(restaurant);
    }

    public void getRestaurant(UUID restaurantId, MutableLiveData<Restaurant> liveData) {
        Call<Restaurant> call = mApiInterface.getRestaurant(restaurantId);
        call.enqueue(new Callback<Restaurant>() {
            @Override
            public void onResponse(@NonNull Call<Restaurant> call,
                                   @NonNull Response<Restaurant> response) {
                if (response.isSuccessful()) {
                    liveData.postValue(response.body());

                } else {
                    liveData.postValue(null);
                }
            }
            @Override
            public void onFailure(@NonNull Call<Restaurant> call, @NonNull Throwable t) {
                Log.d(AppConstants.TAG,"Failure Restaurant"+t.getLocalizedMessage());
                liveData.postValue(null);
            }
        });

    }
}
