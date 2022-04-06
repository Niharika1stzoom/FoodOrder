package com.example.foodorder.review;

import android.os.Bundle;

import com.example.foodorder.model.MenuItem;
import com.example.foodorder.model.Order;

public class MenuItemUtil {
    public static final String ARG_MENU_ITEM = "menu_item";

    public static Bundle getMenuItemBundle(MenuItem menuItem) {
        Bundle bundle = new Bundle();
        bundle.putSerializable(ARG_MENU_ITEM, menuItem);
        return bundle;
    }
}
