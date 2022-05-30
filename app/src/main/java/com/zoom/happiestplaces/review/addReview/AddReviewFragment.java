package com.zoom.happiestplaces.review.addReview;

import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.zoom.happiestplaces.R;
import com.zoom.happiestplaces.databinding.AddReviewFragmentBinding;
import com.zoom.happiestplaces.model.Order;
import com.zoom.happiestplaces.model.Restaurant;
import com.zoom.happiestplaces.model.response.OrderResponse;
import com.zoom.happiestplaces.util.AppConstants;
import com.zoom.happiestplaces.util.AppUtils;
import com.zoom.happiestplaces.util.DateUtil;
import com.zoom.happiestplaces.util.OrderUtils;
import com.zoom.happiestplaces.util.RestaurantUtils;

import java.util.UUID;

import dagger.hilt.android.AndroidEntryPoint;

//It receives input of order Id and restaurant Id to function
@AndroidEntryPoint
public class AddReviewFragment extends Fragment {
    private AddReviewFragmentBinding mBinding;
    private AddReviewViewModel mViewModel;
    UUID mOrderId,mRestaurantId;
    private MenuItemReviewAdapter mAdapter;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(getArguments()!=null && getArguments().containsKey(AppConstants.KEY_ORDER_ID))
        {
            mOrderId= UUID.fromString(getArguments().getString(AppConstants.KEY_ORDER_ID));
            mRestaurantId=UUID.fromString(getArguments().getString(AppConstants.KEY_RESTAURANT_ID));
        }
        else {
            scanAgain();
        }
    }
    private void scanAgain() {
        NavHostFragment.findNavController(this).navigate(R.id.scanFragment);
    }
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        mBinding= AddReviewFragmentBinding.inflate(inflater,container,false);
        View view=mBinding.getRoot();
        initViewModel();
        initRecyclerView();
        return view;
    }

    private void initViewModel() {
        mViewModel = new ViewModelProvider(this).get(AddReviewViewModel.class);
        showOrderForReview(mOrderId);
    }
    private void initRecyclerView() {
        mBinding.recyclerView.setLayoutManager(
                new LinearLayoutManager(getContext()));
        mAdapter=new MenuItemReviewAdapter(getContext(),mViewModel.reviewRepo);
        mBinding.recyclerView.setAdapter(mAdapter);
    }

    //order is filled
    private void initUI(OrderResponse order) {
        mBinding.restaurantName.setText(order.getRestaurant().getName());
        mBinding.totalPrice.setText("Rs "+order.getTotal_price().intValue());
        mBinding.time.setText(DateUtil.getReviewOrderDateFormat(order.getTime()));
        mBinding.postButton.setOnClickListener(view -> {
            String reviewText=mBinding.reviewText.getText().toString().trim();
            if(!TextUtils.isEmpty(reviewText))
            mViewModel.setRestaurantReviewText(reviewText);
            mViewModel.submitReview();
            viewReview(mRestaurantId); }
        );
        mBinding.ratingBarRestaurant.setOnRatingBarChangeListener((ratingBar, v, b) -> mViewModel.setRestoRating(v));
    }

    private void viewReview(UUID restaurant) {
        NavHostFragment.findNavController(getParentFragment()).navigate(R.id.displayReviewFragment,
                RestaurantUtils.getRestaurantBundle(restaurant) );
    }

    private void displayLoader() {
        mBinding.viewLoader.rootView.setVisibility(View.VISIBLE);
        mBinding.layoutGroup.setVisibility(View.GONE);
    }
    private void hideLoader() {
        mBinding.viewLoader.rootView.setVisibility(View.GONE);
        mBinding.layoutGroup .setVisibility(View.VISIBLE);
    }

    private void getRestaurant(OrderResponse order){
        displayLoader();
        mViewModel.getRestaurant(mRestaurantId).observe(getViewLifecycleOwner(), restaurant -> {

            if(restaurant==null) {
                if(!AppUtils.isNetworkAvailableAndConnected(getContext()))
                    AppUtils.showSnackbar(getView(),getString(R.string.network_err));
            }
            else {
                hideLoader();
                mViewModel.setRestaurant(restaurant);
                initUI(order);
            }
        });
    }

    private void showOrderForReview(UUID orderId) {
        displayLoader();
        mViewModel.getOrder(orderId).observe(getViewLifecycleOwner(), order -> {

            if(order==null) {
                if(!AppUtils.isNetworkAvailableAndConnected(getContext()))
                    AppUtils.showSnackbar(getView(),getString(R.string.network_err));
            }
            else {
                hideLoader();
                mViewModel.startReview(order);
                mAdapter.setList(order.getMenuItems());
                getRestaurant(order);
            }
        });
    }
}