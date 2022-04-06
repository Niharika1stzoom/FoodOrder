package com.example.foodorder.model;

import java.util.ArrayList;
import java.util.List;

public class Review {
    public void setOrder(Order order) {
        this.order = order;
    }

    Order order;
    List<MenuItemReview> reviewItems;

    public void setRestaurantReview(RestaurantReview restaurantReview) {
        this.restaurantReview = restaurantReview;
    }

    RestaurantReview restaurantReview;
    public Review()
    {
        reviewItems=new ArrayList<>();
    }

    public Order getOrder() {
        return order;
    }

    public List<MenuItemReview> getReviewItems() {
        return reviewItems;
    }

    public RestaurantReview getRestaurantReview() {
        return restaurantReview;
    }

    public void addReviewMenuItem(String menuItemId, float stars) {
    MenuItemReview menuItemReview=getMenuItemReview(menuItemId);
    if(menuItemReview==null)
        reviewItems.add(new MenuItemReview("",stars,menuItemId));
    else
        menuItemReview.setNumStars(stars);
    }


    public MenuItemReview getMenuItemReview(String menuItemId) {
        for(MenuItemReview review:reviewItems) {
            if (review.getMenuItemId().equals(menuItemId))
                return review;
        }
        return null;
        }

    public void clear() {
        reviewItems.clear();
        restaurantReview=null;
        order=null;
    }
}

