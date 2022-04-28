package com.zoom.happiestplaces.model;

import com.google.gson.annotations.SerializedName;

public class Address {
    String street,code,city,country;
    @SerializedName("phone_number")
    String phone_number;

    public String getStreet() {
        return street;
    }

    public String getCode() {
        return code;
    }

    public Address(String street, String code, String city, String country) {
        this.street = street;
        this.code = code;
        this.city = city;
        this.country = country;
    }

    public String getCity() {
        return city;
    }

    public String getCountry() {
        return country;
    }

    public String getPhone_number() {
        return phone_number;
    }

    public void setPhone_number(String phone_number) {
        this.phone_number = phone_number;
    }
}
