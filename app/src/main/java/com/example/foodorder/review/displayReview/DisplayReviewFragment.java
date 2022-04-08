package com.example.foodorder.review.displayReview;

import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;

import com.example.foodorder.R;
import com.example.foodorder.databinding.AddReviewFragmentBinding;
import com.example.foodorder.databinding.DisplayReviewFragmentBinding;
import com.example.foodorder.model.Order;
import com.example.foodorder.model.Restaurant;
import com.example.foodorder.review.addReview.MenuItemReviewAdapter;
import com.example.foodorder.util.OrderUtils;
import com.example.foodorder.util.RestaurantUtils;
import com.example.foodorder.util.ReviewUtils;

import java.util.UUID;

import dagger.hilt.android.AndroidEntryPoint;

//Works with Restaurant input
@AndroidEntryPoint
public class DisplayReviewFragment extends Fragment {

    private DisplayReviewViewModel mViewModel;
    private DisplayReviewFragmentBinding mBinding;
    private UUID mRestaurantId;
    //private Restaurant mRestaurant;
    private DisplayReviewAdapter mAdapter;

    public static DisplayReviewFragment newInstance() {
        return new DisplayReviewFragment();
    }
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //get restaurant
        if(getArguments()!=null) {
            if (getArguments().containsKey(RestaurantUtils.ARG_RESTAURANT_ID)) {
                mRestaurantId = UUID.fromString(getArguments().getString(RestaurantUtils.ARG_RESTAURANT_ID));
                Log.d("FoodDebug", "Restaurant Fragment" + mRestaurantId);
            }
    /*  //in case dont want to use the net
        if (getArguments().containsKey(RestaurantUtils.ARG_RESTAURANT)) {
                mRestaurant= (Restaurant) getArguments().getSerializable(RestaurantUtils.ARG_RESTAURANT);
                mRestaurant.setTotal_reviews(222);
                mRestaurant.setNum_of_stars(2.5f);
                Log.d("FoodDebug", "Restaurant Fragment" + mRestaurant.getName());
            }*/
        }
        else {
            //TODO:err msg and navigate
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        mBinding= DisplayReviewFragmentBinding.inflate(inflater,container,false);
        View view=mBinding.getRoot();
        initViewModel();
        //initUI(mRestaurant);
        initRecyclerView();
        return view;
    }

    private void initRecyclerView() {
        mBinding.recyclerView.setLayoutManager(
                new LinearLayoutManager(getContext()));
        mAdapter=new DisplayReviewAdapter(getContext());
        mBinding.recyclerView.setAdapter(mAdapter);
    }

    private void initUI(Restaurant restaurant) {

        mBinding.restaurantName.setText(restaurant.getName());
        if(restaurant.getTotal_reviews()==0)
            mBinding.numReviews.setText(getString(R.string.no_reviews));
        else
            mBinding.numReviews.setText(ReviewUtils.displayNumReviews(restaurant.getTotal_reviews()));
        mBinding.ratingBarRestaurant.setRating(restaurant.getNum_of_stars());
        mBinding.totalRating.setText(Float.toString(restaurant.getNum_of_stars()));
    }

    void initViewModel(){
        mViewModel = new ViewModelProvider(this).get(DisplayReviewViewModel.class);
        getReviewDetails();
    }

    private void getReviewDetails() {
        mViewModel.getRestaurant(mRestaurantId).observe(getViewLifecycleOwner(), restaurant -> {
            if(restaurant==null) {
                Log.d("FoodDebug","Resto is null");
            }
            else {
                Log.d("FoodDebug","ReviewList is not null"+restaurant.getName());
                initUI(restaurant);
            }
        });

        mViewModel.getRestaurantReviews(mRestaurantId).observe(getViewLifecycleOwner(), reviewList -> {
            if(reviewList==null) {
                Log.d("FoodDebug","Review List is null");
            }
            else {
                Log.d("FoodDebug","ReviewList is not null"+reviewList.size());
                mAdapter.setList(reviewList);
            }
        });
    }


}