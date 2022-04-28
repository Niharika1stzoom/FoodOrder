package com.zoom.happiestplaces.model;

import com.zoom.happiestplaces.model.response.OrderResponse;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Review {
    OrderResponse order;
    List<MenuItemReview> reviewItems;
    RestaurantReview restaurantReview;
    public void setOrder(OrderResponse order) {
        this.order = order;
    }


    public void setRestaurantReview(RestaurantReview restaurantReview) {
        this.restaurantReview = restaurantReview;
    }

    public Review()
    {
        reviewItems=new ArrayList<>();
    }

    public OrderResponse getOrder() {
        return order;
    }

    public List<MenuItemReview> getReviewItems() {
        return reviewItems;
    }

    public RestaurantReview getRestaurantReview() {
        return restaurantReview;
    }

    public void addReviewMenuItem(UUID menuItemId, float stars) {
    MenuItemReview menuItemReview=getMenuItemReview(menuItemId);
    if(menuItemReview==null)
        reviewItems.add(new MenuItemReview("",stars,menuItemId));
    else
        menuItemReview.setNumStars(stars);
    }


    public MenuItemReview getMenuItemReview(UUID menuItemId) {
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

