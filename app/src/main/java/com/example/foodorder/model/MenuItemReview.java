package com.example.foodorder.model;

import java.util.UUID;

public class MenuItemReview {
    private UUID id;
    private String menuItemId;
    private String reviewText;
    private float numStars;

    public UUID getId() {
        return id;
    }

    public String getMenuItemId() {
        return menuItemId;
    }

    public String getReviewText() {
        return reviewText;
    }

    public float getNumStars() {
        return numStars;
    }



    public MenuItemReview(String reviewText, float numStars, String menuItemId) {
        id=UUID.randomUUID();
        this.reviewText = reviewText;
        this.numStars = numStars;
        this.menuItemId = menuItemId;
    }

    public void setNumStars(float stars) {
        numStars=stars;
    }
}
