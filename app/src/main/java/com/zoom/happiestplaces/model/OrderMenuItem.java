package com.zoom.happiestplaces.model;

import com.google.gson.annotations.SerializedName;

import java.util.UUID;

public class OrderMenuItem {
    UUID dish_id;
    int qty;
    private String name;
    @SerializedName("image1")
    private String imageUrl;
    private String description;
    private Double price;

    public OrderMenuItem(UUID dish_id, int qty) {
        this.dish_id = dish_id;
        this.qty = qty;
    }
    public OrderMenuItem(MenuItem item) {
        this.dish_id = item.getId();
        this.name=item.getName();
        this.description=item.getDescription();
        this.imageUrl=item.getImageUrl();
        this.price=item.getPrice();
    }

    public void setQty(int qty) {
        this.qty = qty;
    }

    public UUID getDish_id() {
        return dish_id;
    }

    public int getQty() {
        return qty;
    }

    public String getName() {
        return name;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public String getDescription() {
        return description;
    }

    public Double getPrice() {
        return price;
    }
}
