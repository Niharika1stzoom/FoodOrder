package com.zoom.happiestplaces.model.response;

import com.google.gson.annotations.SerializedName;
import com.zoom.happiestplaces.model.MenuItem;

import java.util.UUID;

public class OrderDishResponse {
    UUID id;
    int qty;
    @SerializedName("dish")
    MenuItem menuItem;

    public UUID getId() {
        return id;
    }

    public int getQty() {
        return qty;
    }

    public MenuItem getMenuItem() {
        return menuItem;
    }
}
