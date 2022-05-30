package com.zoom.happiestplaces.util;

public interface AppConstants {
    public static final String RESTAURANT_NAME="HARD ROCK CAFE";
    public static final String RESTAURANT_NUM="7600722180";
    public static final String TAG="FoodDebug";
    String KEY_ORDER_ID ="orderId" ;
    String KEY_RESTAURANT_NAME = "restaurant";
    int NEW_USER_POINTS = 100;
    int NOT_MINS =60 ;
    String KEY_RESTAURANT_ID = "restId";
    public static final String CURRENCY = "Rs";
    String SHARED_FILE = "happiest";
    String KEY_ORDER_STATUS ="status" ;
    String KEY_TABLE_NUM ="table" ;
    int TASTY_UNICODE =0x1F60B;
     int SAD_UNICODE =0x1F641;
    int PREPARING_UNICODE =0x23F3;
    int READY_UNICODE =0x1F60A;
    int PARTY_POPPER_UNICODE=0x1F389;
    String FDYNAMIC_URL = "https://happiestplaces.page.link/" ;
    String LINK_URL = "http://www.happiestplaces.com/?invitedBy=";
    String TAG_LINE ="Good local food.No hype." ;
    String LOGO_URL = "https://play-lh.googleusercontent.com/Vd0Q7jsK2ZjKT_Ebc9Kx6dBjSRMqfYa14DL0xGkefXY_XrKez9ugGziNCsuFCeVWsNA=s360";
    String KEY_INVITED ="invitedBy" ;
    String[] ISSUES_ARRAY ={
            "Signing In",
            "Scanning QR Code",
            "Selecting dishes",
            "Placing order",
            "Reward points",
            "Others"
    } ;
    String KEY_FUSER_EMAIL ="user_email" ;
    String KEY_CUSTOMER = "customer";
    String KEY_REFERRED_BY ="referred_by" ;
    String KEY_REFERRED_USER ="referred_user" ;

    enum Status{Delivered,Paid,Placed,Cancel,Preparing,Ready};



    public static final String QRCODE_URL ="https://restaurants.happiestplaces.com" ;
}
