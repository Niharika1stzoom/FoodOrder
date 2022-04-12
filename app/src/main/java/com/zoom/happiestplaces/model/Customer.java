package com.zoom.happiestplaces.model;

import java.util.UUID;

public class Customer {
    UUID id;
    String name;
    String emailId;
    String credentials;
    int current_pts;
    int lifetime_pts;
    String photoURL;

    public Customer(String name, String emailId, String credentials,String photoURL) {
        id=UUID.randomUUID();
        this.name = name;
        this.emailId = emailId;
        this.credentials = credentials;
        this.photoURL=photoURL;
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

    public int getCurrent_pts() {
        return current_pts;
    }

    public int getLifetime_pts() {
        return lifetime_pts;
    }




}