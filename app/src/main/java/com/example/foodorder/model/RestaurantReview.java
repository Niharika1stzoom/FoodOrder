package com.example.foodorder.model;

import java.util.Calendar;
import java.util.Date;
import java.util.UUID;

public class RestaurantReview {
    UUID id;
    float num_of_stars;
    String textReview;
    Customer customer;
    Date time;
    public RestaurantReview(){
    generateId();
}
    public Customer getCustomer() {
        return customer;
    }
    public void setCustomer(Customer customer) {
        this.customer = customer;
    }
    void generateId()
{
    id=UUID.randomUUID();
}

public RestaurantReview(float num_of_stars) {
        generateId();
        time= Calendar.getInstance().getTime();
        this.num_of_stars = num_of_stars;
    }
    public RestaurantReview(float num_of_stars, String textReview) {
        generateId();
        time= Calendar.getInstance().getTime();
        this.num_of_stars = num_of_stars;
        this.textReview = textReview;
    }
    public Date getTime() {
        return time;
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
