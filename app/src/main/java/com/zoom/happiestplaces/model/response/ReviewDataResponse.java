package com.zoom.happiestplaces.model.response;

import com.google.gson.annotations.SerializedName;
import com.zoom.happiestplaces.model.Customer;

import java.util.Date;
import java.util.UUID;

//Response for getting comments/reviews of a restaurant
public class ReviewDataResponse {
    UUID id;
    String text;
    Customer customer;
    @SerializedName("time")
    Date date;
    Float rating;

    public UUID getId() {
        return id;
    }

    public Customer getCustomer() {
        return customer;
    }

    public Date getDate() {
        return date;
    }

    public String getText() {
        return text;
    }

    public Float getRating() {
        return rating;
    }
}
