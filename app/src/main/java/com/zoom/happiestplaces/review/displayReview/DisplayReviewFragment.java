package com.zoom.happiestplaces.review.displayReview;

import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.zoom.happiestplaces.R;
import com.zoom.happiestplaces.databinding.AddReviewFragmentBinding;
import com.zoom.happiestplaces.databinding.DisplayReviewFragmentBinding;
import com.zoom.happiestplaces.model.Restaurant;
import com.zoom.happiestplaces.util.AppUtils;
import com.zoom.happiestplaces.util.RestaurantUtils;
import com.zoom.happiestplaces.util.ReviewUtils;

import java.util.UUID;

import dagger.hilt.android.AndroidEntryPoint;

//Works with Restaurant input
@AndroidEntryPoint
public class DisplayReviewFragment extends Fragment {

    private DisplayReviewViewModel mViewModel;
    private DisplayReviewFragmentBinding mBinding;
    private UUID mRestaurantId;
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
    private void displayLoader() {
        mBinding.viewLoader.rootView.setVisibility(View.VISIBLE);
        mBinding.layoutGroup.setVisibility(View.GONE);
    }
    private void hideLoader() {
        mBinding.viewLoader.rootView.setVisibility(View.GONE);
        mBinding.layoutGroup .setVisibility(View.VISIBLE);
    }

    private void getReviewDetails() {
        mViewModel.getRestaurant(mRestaurantId).observe(getViewLifecycleOwner(), restaurant -> {
            if(restaurant==null) {
                if(!AppUtils.isNetworkAvailableAndConnected(getContext()))
                    AppUtils.showSnackbar(getView(),getString(R.string.network_err));

            }
            else {
                initUI(restaurant);
            }
        });

        mViewModel.getRestaurantReviews(mRestaurantId).observe(getViewLifecycleOwner(), reviewList -> {
            displayLoader();
            if(reviewList==null) {
                if(!AppUtils.isNetworkAvailableAndConnected(getContext()))
                    AppUtils.showSnackbar(getView(),getString(R.string.network_err));
            }
            else {
                hideLoader();
                mAdapter.setList(reviewList);
            }
        });
    }
}