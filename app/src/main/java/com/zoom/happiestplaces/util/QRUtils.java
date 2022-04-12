package com.zoom.happiestplaces.util;

import android.text.TextUtils;

import java.util.UUID;
//Qrcode format 2*123e4567-e89b-12d3-a456-426614174000
public class QRUtils {
    public static final int start=0;
    public static boolean checkQRFormat(String QRcode) {
        if(QRcode.contains("*"))
        return true;
        return false;
    }

    public static String getTableQR(String qRcode) {
        int diff=qRcode.indexOf("*");
        if(start==diff-1)
            return String.valueOf(qRcode.charAt(start));
       // else
        return TextUtils.substring(qRcode,start,diff-1);
    }

    public static UUID getRestaurantIdQR(String qRcode) {
        int diff=qRcode.indexOf("*");
        //for testing
        return UUID.randomUUID();
        //return UUID.fromString(qRcode.substring(diff+1,qRcode.length()-1));


    }
}
