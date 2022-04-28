package com.zoom.happiestplaces.util;

public interface AppConstants {
    public static final String RESTAURANT_NAME="HARD ROCK CAFE";
    public static final String RESTAURANT_NUM="7600722180";
    public static final String TAG="FoodDebug";
    String KEY_ORDER_ID ="order_id" ;
    String KEY_RESTAURANT_NAME = "restaurant_name";
    int NEW_USER_POINTS = 100;
    int NOT_MINS =60 ;
    String KEY_RESTAURANT_ID = "restaurant_id";
    public static final String CURRENCY = "Rs";
    String SHARED_FILE = "happiest";

    enum Status{Delivered,Paid,Placed,Cancel};
    int PARTY_POPPER_UNICODE=0x1F389;


    public static final String BASE_URL ="https://happiestplace.herokuapp.com/" ;
}
