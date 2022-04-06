package com.example.foodorder.model;

public class Restaurant {
    String tableNum;
    String phoneNum;
    String name;


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
