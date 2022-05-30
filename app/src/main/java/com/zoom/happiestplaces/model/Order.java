package com.zoom.happiestplaces.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.UUID;
//Used to create order in app
public class Order implements Serializable {
    private UUID id;
    @SerializedName("table_id")
    UUID table;
    @SerializedName("customer")
    UUID customerId;
    Date time;
    boolean status;
    private Boolean orderExecuted=false;
    private Customer customerObj;
    @SerializedName("redeem_pts")
    Double redeem_points;
    private Restaurant restaurant;
    @SerializedName("orders")
    public List<OrderMenuItem> foodItemsList;
    Double points;

    public Order(UUID table, Restaurant restaurant) {
        this.restaurant = restaurant;
        foodItemsList=new ArrayList<>();
        this.table=table;
    }

    public void setRedeem_points(Double redeem_points) {
        this.redeem_points = redeem_points;
    }

    public UUID getCustomerId() {
        return customerId;
    }

    public void setCustomerId(UUID customerId) {
        this.customerId = customerId;
    }

    public Order(UUID table) {
        foodItemsList=new ArrayList<>();
        this.table=table;
    }
    public Date getTime() {
        return time;
    }

    public Customer getCustomer() {
        return customerObj;
    }

    public void setCustomer(Customer customer) {
        this.customerObj = customer;
    }

    public Double getRedeem_points() {
        return redeem_points;
    }

    public int getPoints() {
        return points.intValue();
    }

    public Boolean getOrderExecuted() {
        return orderExecuted;
    }
    public void setOrderExecuted(Boolean orderExecuted) {
        this.orderExecuted = orderExecuted;
    }
    public void setRestaurant(Restaurant restaurant) {
        this.restaurant = restaurant;
    }
    public void setTime()
{
    time= Calendar.getInstance().getTime();
}
    public Restaurant getRestaurant() {
        return restaurant;
    }

    public OrderMenuItem getMenuItem(UUID id)
    {
        OrderMenuItem menuItem=null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            menuItem = foodItemsList.stream()
                    .filter(item -> id.equals(item.getDish_id()))
                    .findAny()
                    .orElse(null);
        }
        else
        {
            for(OrderMenuItem item:foodItemsList)
            {
                if(item.getDish_id().equals(id))
                {
                    return item;
                }
            }
        }
        return menuItem;
    }
    public void addMenuItem(MenuItem item)
    {
        OrderMenuItem orderMenuItem=new OrderMenuItem(item);
        orderMenuItem.setQty(1);
        foodItemsList.add(orderMenuItem);
    }
    public Double getTotalPrice(){
        Double total=0.0;

        for(OrderMenuItem item:foodItemsList)
        {
            total+=item.getQty()*item.getPrice();
        }
        return total;

    }
    public void incrementQty
            (UUID id)
    {
        int Qty=getMenuItem(id).getQty();
        getMenuItem(id).setQty(++Qty);
    }
    public void decrementQty(UUID id) {
        int Qty = getMenuItem(id).getQty();
        if (Qty > 1) {
            Qty--;
        } else{
            removeMenuItem(id);
        return;
    }
            getMenuItem(id).setQty(Qty);
        }

    public void removeMenuItem(UUID id) {
        OrderMenuItem item=getMenuItem(id);
        getItemsList().remove(item);
    }

    public void cancelOrder() {
        getItemsList().clear();
    }
    public Boolean isOrderEmpty() {
        if(getItemsList().isEmpty())
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

    public List<OrderMenuItem> getItemsList() {
        return foodItemsList;
    }

    public UUID getTable() {
        return table;
    }

    public void setTable(UUID table) {
        this.table = table;
    }
}

