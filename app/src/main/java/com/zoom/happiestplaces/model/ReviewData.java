package com.zoom.happiestplaces.model;

import java.util.UUID;
//Used to post restaurant review text/comment to api
public class ReviewData {
    String text;
    UUID customer;
    public ReviewData(String text, UUID customer) {
        this.text = text;
        this.customer = customer;
    }

    public String getText() {
        return text;
    }

    public UUID getCustomer() {
        return customer;
    }
}
