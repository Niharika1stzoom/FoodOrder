package com.zoom.happiestplaces.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.UUID;

public class MenuItem implements Serializable {
    private UUID id;
    private String name;
    @SerializedName("image1")
    private String imageUrl;
    private String description;
    private Double price;
    Ratings ratings;


    public MenuItem(UUID id, String name, String imageUrl, String description, Double price) {
        this.id = id;
        this.name = name;
        this.imageUrl = imageUrl;
        this.description = description;
        this.price = price;;
    }


    public UUID getId() {
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

    public Ratings getRatings() {
        return ratings;
    }
}
