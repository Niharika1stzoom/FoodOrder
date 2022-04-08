package com.example.foodorder.model;

import java.io.Serializable;
import java.util.UUID;

public class Restaurant implements Serializable {
    UUID id;
    String tableNum;
    String phoneNum;
    String name;
    float num_of_stars;
    int total_reviews;

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



    public Restaurant(String tableNum, String phoneNum, String name) {
        this.tableNum = tableNum;
        this.phoneNum = phoneNum;
        this.name = name;
    }

    public String getTableNum() {
        return tableNum;
    }

    public String getPhoneNum() {
        return phoneNum;
    }

    public String getName() {
        return name;
    }
}
