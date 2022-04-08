package com.example.foodorder.review.addReview;

import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;

import com.example.foodorder.R;
import com.example.foodorder.databinding.AddReviewFragmentBinding;
import com.example.foodorder.model.Order;
import com.example.foodorder.model.Restaurant;
import com.example.foodorder.util.OrderUtils;
import com.example.foodorder.util.RestaurantUtils;

import java.util.UUID;

import dagger.hilt.android.AndroidEntryPoint;

//It receives input of order Id to function
@AndroidEntryPoint
public class AddReviewFragment extends Fragment {
    private AddReviewFragmentBinding mBinding;
    private AddReviewViewModel mViewModel;
    FragmentStateAdapter adapter;
    UUID mOrderId;
    private MenuItemReviewAdapter mAdapter;

    public static AddReviewFragment newInstance() {
        return new AddReviewFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(getArguments()!=null && getArguments().containsKey(OrderUtils.ARG_ORDER_ID))
        {
            mOrderId= UUID.fromString(getArguments().getString(OrderUtils.ARG_ORDER_ID));
            Log.d("FoodDebug","Review Fragment"+mOrderId);
        }
        else {
            //TODO:err msg and navigate
        }
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

    private void initRecyclerView() {
        mBinding.recyclerView.setLayoutManager(
                new LinearLayoutManager(getContext()));
        mAdapter=new MenuItemReviewAdapter(getContext(),mViewModel.reviewRepo);
        mBinding.recyclerView.setAdapter(mAdapter);
    }

    //order is filled
    private void initUI(Order order) {
        mBinding.restaurantName.setText(order.getRestaurant().getName());
        mBinding.totalPrice.setText("Rs "+order.getTotalPrice().intValue());
        mBinding.time.setText(mViewModel.getOrderTime());
        mBinding.postButton.setOnClickListener(view -> {
            String reviewText=mBinding.reviewText.getText().toString().trim();
            if(!TextUtils.isEmpty(reviewText))
            mViewModel.setRestaurantReviewText(reviewText);
            mViewModel.submitReview();
            viewReview(order.getRestaurant());
        }
        );
        mBinding.ratingBarRestaurant.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float v, boolean b) {
                mViewModel.setRestoRating(v);
            }
        });

        //init listener for restaurant rating bar
    }

    private void viewReview(Restaurant restaurant) {
        RestaurantUtils.getRestaurantBundle(restaurant);
        NavHostFragment.findNavController(getParentFragment()).navigate(R.id.displayReviewFragment,
                RestaurantUtils.getRestaurantBundle(restaurant) );
    }

    private void initButtonListener() {

    }

    private void showOrderForReview(UUID orderId) {
        mViewModel.getOrder(orderId).observe(getViewLifecycleOwner(), order -> {
            if(order==null) {
                Log.d("FoodDebug","Order is null");
            }
            else {
                Log.d("FoodDebug","Order is not null"+order.getItemsList().size());
                //mBinding.orderDetail.setText(order.getRestaurant().getName());
                mViewModel.startReview(order);
                mAdapter.setList(order.getItemsList());
                initUI(order);
                //set rest details in review

            }
        });
    }



    private void initViewModel() {
        mViewModel = new ViewModelProvider(this).get(AddReviewViewModel.class);
        showOrderForReview(mOrderId);
    }



}