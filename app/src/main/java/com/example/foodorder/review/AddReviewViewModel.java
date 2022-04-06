package com.example.foodorder.review;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.foodorder.model.Order;
import com.example.foodorder.order.OrderRepository;

import java.util.UUID;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class AddReviewViewModel extends AndroidViewModel {
    @Inject
    ReviewRepository reviewRepo;
    @Inject
    OrderRepository orderRepo;
    MutableLiveData<Order> orderLiveData;

    @Inject
    public AddReviewViewModel(@NonNull Application application) {
        super(application);
        orderLiveData=new MutableLiveData<>();
    }

    public LiveData<Order> getOrder(UUID order_id) {
        orderRepo.getOrder(orderLiveData,order_id);
        return orderLiveData;
    }


    public void startReview(Order order) {
        reviewRepo.startReview(order);
    }

    public void submitReview() {
        reviewRepo.submitReview();
    }

    public void setRestoRating(float v) {
        reviewRepo.setRestoRating(v);
    }

    public void setRestaurantReviewText(String reviewText) {
        reviewRepo.setRestoReviewText(reviewText);
    }

    public String getOrderTime() {
        return reviewRepo.getOrderTime();
    }
}