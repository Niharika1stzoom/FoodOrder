package com.example.foodorder.review;

import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.foodorder.R;


import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;

import com.example.foodorder.R;
import com.example.foodorder.databinding.AddReviewFragmentBinding;
import com.example.foodorder.databinding.AddReviewMenuItemFragmentBinding;
import com.example.foodorder.databinding.OrderFragmentBinding;
import com.example.foodorder.model.MenuItem;
import com.example.foodorder.model.MenuItemReview;
import com.example.foodorder.order.OrderViewModel;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class AddReviewMenuItemFragment extends Fragment {
    MenuItem menuItem;

    AddReviewMenuItemFragmentBinding mBinding;
    private AddReviewMenuItemViewModel mViewModel;

    public static AddReviewMenuItemFragment newInstance(MenuItem menuItem) {
        AddReviewMenuItemFragment fragment = new AddReviewMenuItemFragment();
        fragment.setArguments(MenuItemUtil.getMenuItemBundle(menuItem));
        return fragment;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            menuItem= (MenuItem) getArguments().getSerializable(MenuItemUtil.ARG_MENU_ITEM);


            //  mParam1 = getArguments().getString(ARG_PARAM1);
            //    mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mBinding = AddReviewMenuItemFragmentBinding.inflate(inflater, container, false);
        View view = mBinding.getRoot();
        initViewModel();
        initView();
        return view;

    }

    private void initViewModel() {
        mViewModel = new ViewModelProvider(this).get(AddReviewMenuItemViewModel.class);
    }

    private void initView() {
        mBinding.itemName.setText(menuItem.getName());
        MenuItemReview itemReview=mViewModel.getMenuItemReview(menuItem.getId());
        if(itemReview!=null)
        mBinding.editTextTextMultiLine.setText(Float.toString(itemReview.getNumStars()));

       mBinding.ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
           @Override
           public void onRatingChanged(RatingBar ratingBar, float v, boolean b) {
               //adds or updates
               mViewModel.addReview(menuItem.getId(),v);
           }
       });
       /* mBinding.awesomeEmoji.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mViewModel.addReview(menuItem.getId(),3);
            }
        });*/

    }
}