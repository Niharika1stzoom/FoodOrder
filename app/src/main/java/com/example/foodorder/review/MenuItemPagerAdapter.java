package com.example.foodorder.review;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.foodorder.model.MenuItem;

import java.util.List;

public class MenuItemPagerAdapter extends FragmentStateAdapter {
    List<MenuItem> menuItems;
    public MenuItemPagerAdapter(FragmentActivity fa,List<MenuItem> items) {
        super(fa);
        menuItems=items;
    }

    @Override
    public Fragment createFragment(int position) {
        return AddReviewMenuItemFragment.newInstance(menuItems.get(position));
    }

    @Override
    public int getItemCount() {
        return menuItems.size();
    }
}

