package com.zoom.happiestplaces.util;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.zoom.happiestplaces.R;
import com.zoom.happiestplaces.model.MenuItem;
import com.google.android.material.snackbar.Snackbar;

public class AppUtils {


    public static void setImage(Context context, String url, ImageView imageView) {
        Glide.with(context)
                .load(url)
                .placeholder(R.drawable.ic_placeholder)
                .centerCrop()
                .into(imageView);
    }


    public static void shareFoodMenu(Context context, MenuItem foodMenu) {
        String foodDesc=foodMenu.getDescription();
        if(foodDesc==null)
            foodDesc="";
        else
            foodDesc=foodDesc+"\n";
        String foodDetail=foodMenu.getName()+"\n"+foodDesc;
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("text/plain");
        shareIntent.putExtra(Intent.EXTRA_SUBJECT, context.getString(R.string.app_name));
        shareIntent.putExtra(Intent.EXTRA_TEXT, foodDetail);
        context.startActivity(Intent.createChooser(shareIntent, "Choose one"));
    }
    public static void showSnackbar(View view, String msg){
        Snackbar.make(view, msg, Snackbar.LENGTH_SHORT).show();
    }
    public static boolean isNetworkAvailableAndConnected(Context context) {
        ConnectivityManager cm =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null && activeNetwork.isConnectedOrConnecting();
        return isConnected;

    }

    public static String getCustomerNickName() {
        return "Anonymous User";
    }
}
