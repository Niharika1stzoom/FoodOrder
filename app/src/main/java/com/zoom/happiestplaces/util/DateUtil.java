package com.zoom.happiestplaces.util;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtil {
    public static String getDisplayReviewDateFormat(Date date) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd LLL HH:mm");
        String dateTime = simpleDateFormat.format(date);
        return dateTime;
    }

    public static String getReviewOrderDateFormat(Date date) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd LLL HH:mm");
        String dateTime = simpleDateFormat.format(date);
        return dateTime;
    }
}
