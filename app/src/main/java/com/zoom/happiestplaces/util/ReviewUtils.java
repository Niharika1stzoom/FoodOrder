package com.zoom.happiestplaces.util;

public class ReviewUtils {
    public static String displayNumReviews(int total_reviews) {
        return "Based on "+total_reviews+ " reviews";
    }

    public static String getRatingStringFormat(Float rating) {
        return String.format("%.1f",rating);
    }
}
