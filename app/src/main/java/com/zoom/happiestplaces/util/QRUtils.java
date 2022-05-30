package com.zoom.happiestplaces.util;

import android.text.TextUtils;

import java.util.UUID;
//Qrcode format 2*123e4567-e89b-12d3-a456-426614174000
public class QRUtils {
    public static final int start=0;


    public static String get_table_id(String qrcode_data){
        String parts[] = qrcode_data.split("/");
        return parts[parts.length -1];
    }

    public static boolean validateQRCODEURL(String url) {
        if(url.contains(AppConstants.QRCODE_URL))
            return true;
        else
            return false;
    }
}
