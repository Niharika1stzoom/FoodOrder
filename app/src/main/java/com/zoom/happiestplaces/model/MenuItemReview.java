package com.zoom.happiestplaces.model;

import java.util.UUID;

public class MenuItemReview {
    private UUID id;
    private UUID menuItemId;
    private String reviewText;
    private float numStars;

    public UUID getId() {
        return id;
    }

    public UUID getMenuItemId() {
        return menuItemId;
    }

    public String getReviewText() {
        return reviewText;
    }

    public float getNumStars() {
        return numStars;
    }



    public MenuItemReview(String reviewText, float numStars, UUID menuItemId) {
        id=UUID.randomUUID();
        this.reviewText = reviewText;
        this.numStars = numStars;
        this.menuItemId = menuItemId;
    }

    public void setNumStars(float stars) {
        numStars=stars;
    }
}
