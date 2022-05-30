package com.zoom.happiestplaces.model.response;

import com.google.gson.annotations.SerializedName;
import com.zoom.happiestplaces.model.MenuItem;
import com.zoom.happiestplaces.model.Restaurant;
import com.zoom.happiestplaces.model.Table;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;
//After placing order the api response
public class OrderResponse {
    UUID id;
    String status;
    Table table;
    Double total_price;
    Double net_price;
    Double points;
    Date time;
    int current_pts;
    @SerializedName("order_dish_set")
    List<OrderDishResponse> orderDishResponses;
    //UUID table_id;
    UUID customer;
    Restaurant restaurant;

    public List<MenuItem> getMenuItems()
    {
        List<MenuItem> menuItems=new ArrayList<>();
        for(OrderDishResponse orderDish:orderDishResponses)
            menuItems.add(orderDish.getMenuItem());
        return menuItems;
    }

    public double getCurrent_pts() {
        return current_pts;
    }

    public Restaurant getRestaurant() {
        return restaurant;
    }

    public void setRestaurant(Restaurant restaurant) {
        this.restaurant = restaurant;
    }

    public UUID getId() {
        return id;
    }

    public String getStatus() {
        return status;
    }

    public Table getTable() {
        return table;
    }

    public Double getTotal_price() {
        return total_price;
    }

    public Double getNet_price() {
        return net_price;
    }

    public Double getPoints() {
        return points;
    }

    public Date getTime() {
        return time;
    }

    public List<OrderDishResponse> getOrderDishResponses() {
        return orderDishResponses;
    }

    public UUID getCustomer() {
        return customer;
    }
}
