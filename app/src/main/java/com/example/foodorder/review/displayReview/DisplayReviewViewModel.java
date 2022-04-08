package com.example.foodorder.review.displayReview;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.foodorder.foodmenu.FoodMenuRepository;
import com.example.foodorder.model.Order;
import com.example.foodorder.model.Restaurant;
import com.example.foodorder.model.RestaurantReview;
import com.example.foodorder.order.OrderRepository;
import com.example.foodorder.review.ReviewRepository;

import java.util.List;
import java.util.UUID;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class DisplayReviewViewModel extends AndroidViewModel {
    @Inject
    ReviewRepository reviewRepo;
    @Inject
    FoodMenuRepository menuRepository;
    MutableLiveData<List<RestaurantReview>> reviewLiveData;
    private MutableLiveData<Restaurant> restaurantLiveData;

    @Inject
    public DisplayReviewViewModel(@NonNull Application application) {
        super(application);
        reviewLiveData=new MutableLiveData<>();
        restaurantLiveData=new MutableLiveData<>();
    }
    public LiveData<List<RestaurantReview>> getRestaurantReviews(UUID mRestaurantId) {
        reviewRepo.getRestaurantReviews(mRestaurantId,reviewLiveData);
        return reviewLiveData;
    }

    public LiveData<Restaurant> getRestaurant(UUID restaurantId) {
        menuRepository.getRestaurantReview(restaurantId,restaurantLiveData);
        return restaurantLiveData;
    }
}