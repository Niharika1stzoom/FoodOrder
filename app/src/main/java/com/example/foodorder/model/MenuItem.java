package com.example.foodorder.model;

public class MenuItem {
    private String id;
    private String name;
    private String imageUrl;

    public MenuItem(String id, String name, String imageUrl, String description, Double price) {
        this.id = id;
        this.name = name;
        this.imageUrl = imageUrl;
        this.description = description;
        this.price = price;;
    }

    private String description;
    private Double price;
    private int Qty;

    public void setQty(int qty) {
        Qty = qty;
    }
    public int getQty() {
        return Qty;
    }

    public String getId() {
        return id;
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
