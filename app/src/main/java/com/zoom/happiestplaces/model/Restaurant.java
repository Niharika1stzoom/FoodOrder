package com.zoom.happiestplaces.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;
import java.util.UUID;

public class Restaurant implements Serializable {
    UUID id;
    String phoneNum;
    String name;
    float num_of_stars;
    int total_reviews;
    Table table;
    @SerializedName("dish_set")
    List<MenuItem> menuList;

    public Restaurant(UUID id, String phoneNum, String name, float num_of_stars, int total_reviews) {
        this.id = id;
        this.phoneNum = phoneNum;
        this.name = name;
        this.num_of_stars = num_of_stars;
        this.total_reviews = total_reviews;
    }


    public Table getTable() {
        return table;
    }

    public List<MenuItem> getMenuList() {
        return menuList;
    }
    public void setTotal_reviews(int total_reviews) {
        this.total_reviews = total_reviews;
    }
    public void setNum_of_stars(float num_of_stars) {
        this.num_of_stars = num_of_stars;
    }
    public float getNum_of_stars() {
        return num_of_stars;
    }

    public int getTotal_reviews() {
        return total_reviews;
    }

    public String getPhoneNum() {
        return phoneNum;
    }

    public String getName() {
        return name;
    }
}
