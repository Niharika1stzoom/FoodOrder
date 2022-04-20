package com.zoom.happiestplaces.model.response;

import com.google.gson.annotations.SerializedName;
import com.zoom.happiestplaces.model.Table;

import java.util.Date;
import java.util.List;
import java.util.UUID;

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

    public double getCurrent_pts() {
        return current_pts;
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
