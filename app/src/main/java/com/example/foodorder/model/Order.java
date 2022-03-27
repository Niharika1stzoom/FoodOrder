package com.example.foodorder.model;

import java.util.HashMap;

public class Order {

    private Restaurant restaurant;
    private HashMap<String,MenuItem> foodItems;

    public Boolean getOrderExecuted() {
        return orderExecuted;
    }

    public void setOrderExecuted(Boolean orderExecuted) {
        this.orderExecuted = orderExecuted;
    }

    private Boolean orderExecuted=false;
    public Order(Restaurant restaurant) {
        foodItems=new HashMap<>();
        this.restaurant = restaurant;
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
    public void incrementQty(String id)
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
}

