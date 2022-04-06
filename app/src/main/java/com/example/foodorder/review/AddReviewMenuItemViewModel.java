package com.example.foodorder.review;

import androidx.lifecycle.ViewModel;

import com.example.foodorder.model.MenuItemReview;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class AddReviewMenuItemViewModel extends ViewModel {
    @Inject
    ReviewRepository mRepository;
    public void addReview(String menuItemId, float stars) {
            mRepository.addReviewMenuItem(menuItemId,stars);
    }

    @Inject
    public AddReviewMenuItemViewModel() {
    }

    public MenuItemReview getMenuItemReview(String menuItemId) {
        return mRepository.getMenuItemReview(menuItemId);
    }
}