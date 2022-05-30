package com.zoom.happiestplaces.model;


import com.google.gson.annotations.SerializedName;

import java.util.UUID;

public class Customer {
    UUID id;
    String name;
    @SerializedName("email")
    String emailId;
    String credentials;
    @SerializedName("current_points")
    double current_pts;
    int lifetime_pts;
    String photoURL;
    String phone;
    Address address;
    String fcmToken;
    @SerializedName("redeem")
    UUID referralId;

    public UUID getReferralId() {
        return referralId;
    }

    public void setReferralId(UUID referralId) {
        this.referralId = referralId;
    }

    public Customer(String name, String emailId, String credentials, String photoURL, String token, UUID referral) {
        this.name = name;
        this.emailId = emailId;
        this.credentials = credentials;
        this.photoURL=photoURL;
        this.fcmToken=token;
        this.referralId=referral;
    }

    public String getFcmToken() {
        return fcmToken;
    }

    public void setFcmToken(String fcmToken) {
        this.fcmToken = fcmToken;
    }

    public String getPhone() {
        return phone;
    }

    public Address getAddress() {
        return address;
    }

    public UUID getId() {
        return id;
    }

    public String getPhotoURL() {
        return photoURL;
    }

    public void setCurrent_pts(int current_pts) {
        this.current_pts = current_pts;
    }

    public void setLifetime_pts(int lifetime_pts) {
        this.lifetime_pts = lifetime_pts;
    }

    public String getName() {
        return name;
    }

    public String getEmailId() {
        return emailId;
    }

    public String getCredentials() {
        return credentials;
    }

    public double getCurrent_pts() {
        return current_pts;
    }

    public int getLifetime_pts() {
        return lifetime_pts;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setAddress(Address address) {
        this.address = address;
    }
}
