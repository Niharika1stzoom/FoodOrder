package com.example.foodorder.model;

import java.util.UUID;

public class RestaurantReview {
    UUID id;
    float num_of_stars;
    String textReview;

    public RestaurantReview(){
    generateId();
}

void generateId()
{
    id=UUID.randomUUID();
}
    public RestaurantReview(float num_of_stars) {
        generateId();
        this.num_of_stars = num_of_stars;
    }
    public RestaurantReview(float num_of_stars, String textReview) {
        generateId();
        this.num_of_stars = num_of_stars;
        this.textReview = textReview;
    }
    public float getNum_of_stars() {
        return num_of_stars;
    }

    public void setNum_of_stars(float num_of_stars) {
        this.num_of_stars = num_of_stars;
    }

    public String getTextReview() {
        return textReview;
    }

    public void setTextReview(String textReview) {
        this.textReview = textReview;
    }


}
