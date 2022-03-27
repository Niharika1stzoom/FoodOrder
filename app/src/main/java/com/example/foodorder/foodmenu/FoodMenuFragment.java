package com.example.foodorder.foodmenu;

import androidx.appcompat.app.AlertDialog;
import androidx.lifecycle.ViewModelProvider;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.foodorder.R;
import com.example.foodorder.databinding.FoodMenuFragmentBinding;
import com.example.foodorder.databinding.ScanFragmentBinding;
import com.example.foodorder.model.MenuItem;
import com.example.foodorder.util.AppUtils;
import com.example.foodorder.util.SharedPrefUtils;

import java.util.ArrayList;
import java.util.List;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class FoodMenuFragment extends Fragment {
    private FoodMenuFragmentBinding mBinding;
    private FoodMenuViewModel mViewModel;
    private FoodMenuAdapter mAdapter;

    public static FoodMenuFragment newInstance() {
        return new FoodMenuFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        mBinding= FoodMenuFragmentBinding.inflate(inflater,container,false);
        View view=mBinding.getRoot();
        mViewModel = new ViewModelProvider(this).get(FoodMenuViewModel.class);
        checkOrderStatus();
        observeMenuList();
        initRecyclerView();
        return view;

    }

    public void checkOrderStatus() {
        if(SharedPrefUtils.checkOrderExecuted(getActivity().getApplicationContext())) {
            SharedPrefUtils.cancelOrder(getActivity().getApplicationContext());
        }
    }


    private void initRecyclerView() {
        mBinding.recyclerView.setLayoutManager(
                new GridLayoutManager(getContext(), 2));
        mAdapter=new FoodMenuAdapter(getContext());
        mBinding.recyclerView.setAdapter(mAdapter);
        mBinding.viewOrderButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(SharedPrefUtils.checkOrderIsEmpty(getActivity().getApplicationContext()))
                {
                    new AlertDialog.Builder(getContext())
                            .setTitle(R.string.alert_dialog_add_title)
                            .setMessage(R.string.alert_no_items_msg)
                            .setCancelable(false)
                            .setPositiveButton(R.string.ok, null)
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .show();
                }
                else
                NavHostFragment.findNavController(getParentFragment()).navigate(R.id.orderFragment);
            }
        });
    }

    private void observeMenuList() {
        mViewModel.getMenuList(SharedPrefUtils.getOrder(
                getActivity().getApplicationContext()).getRestaurant().getName())
                .observe(getViewLifecycleOwner(), menuList -> {
        if(menuList==null || menuList.size()==0) {
           if(AppUtils.isNetworkAvailableAndConnected(getContext()))
                displayEmptyView();
            else
                AppUtils.showSnackbar(getView(),getString(R.string.network_err));
            mBinding.recyclerView.setVisibility(View.INVISIBLE);
        }
        else {
            displayDataView(menuList);
        }
    });
}
    private void displayLoader() {
        mBinding.viewLoader.rootView.setVisibility(View.VISIBLE);
        mBinding.recyclerView.setVisibility(View.GONE);
    }
    private void displayDataView(List<MenuItem> menuList) {
        hideLoader();
        mBinding.recyclerView.setVisibility(View.VISIBLE);
        mBinding.viewEmpty.emptyContainer.setVisibility(View.GONE);
        mAdapter.setList(menuList);
        mBinding.recyclerView.scheduleLayoutAnimation();
    }
    private void hideLoader() {
       mBinding.viewLoader.rootView.setVisibility(View.GONE);
    }
    private void displayEmptyView() {
        hideLoader();
        mBinding.recyclerView.setVisibility(View.GONE);
        mBinding.viewEmpty.emptyContainer.setVisibility(View.VISIBLE);
    }
}