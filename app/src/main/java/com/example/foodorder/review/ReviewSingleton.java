package com.example.foodorder.review;

import android.content.Context;

import com.example.foodorder.model.Review;

import javax.inject.Singleton;

@Singleton
public class ReviewSingleton extends Review{
    private static ReviewSingleton sInstance;
    private static final Object LOCK = new Object();
    public static ReviewSingleton getReview() {
        if (sInstance == null) {
            synchronized (LOCK) {
                sInstance = new ReviewSingleton();
            }
        }
        return sInstance;
    }


}
