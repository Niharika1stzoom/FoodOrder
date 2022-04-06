package com.example.foodorder.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class Order implements Serializable {
    private UUID id;

    public Date getTime() {
        return time;
    }

    //private Customer customer;
    //int redeem_points;
    //boolean status
    Date time;

    private Restaurant restaurant;
    @SerializedName("array")
    public List<MenuItem> foodItemsList;

    private HashMap<String,MenuItem> foodItems;
    public Boolean getOrderExecuted() {
        return orderExecuted;
    }
    public void setOrderExecuted(Boolean orderExecuted) {
        this.orderExecuted = orderExecuted;
    }

    private Boolean orderExecuted=false;
    public Order(Restaurant restaurant) {
        id=UUID.randomUUID();
        foodItems=new HashMap<>();
        this.restaurant = restaurant;

    }
public void setTime()
{
    time= Calendar.getInstance().getTime();
}
    public Restaurant getRestaurant() {
        return restaurant;
    }
    public HashMap<String, MenuItem> getFoodItems() {
        return foodItems;
    }
    public MenuItem getMenuItem(String id)
    {
        return foodItems.get(id);
    }
    public MenuItem addMenuItem(MenuItem item)
    {
        item.setQty(1);
        return foodItems.put(item.getId(),item);
    }
    public Double getTotalPrice(){
        Double total=0.0;

        for(MenuItem item:foodItemsList)
        {
            total+=item.getQty()*item.getPrice();
        }
        return total;

    }
    public void incrementQty
            (String id)
    {
        int Qty=getMenuItem(id).getQty();
        getMenuItem(id).setQty(++Qty);
    }
    public void decrementQty(String id) {
        int Qty = getMenuItem(id).getQty();
        if (Qty > 1) {
            Qty--;
        } else{
            removeMenuItem(id);
        return;
    }
            getMenuItem(id).setQty(Qty);
        }

    public void removeMenuItem(String id) {
        getFoodItems().remove(id);
    }

    public void cancelOrder() {
        getFoodItems().clear();
    }
    Boolean isOrderEmpty() {
        if(getFoodItems().isEmpty())
            return true;
        else
            return false;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public List<MenuItem> getItemsList() {
        return foodItemsList;
    }
}

