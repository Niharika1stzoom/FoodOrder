package com.example.foodorder.order;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.SavedStateHandle;
import androidx.lifecycle.ViewModelProvider;

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
import com.example.foodorder.databinding.OrderFragmentBinding;
import com.example.foodorder.foodmenu.FoodMenuAdapter;
import com.example.foodorder.model.MenuItem;
import com.example.foodorder.model.Order;
import com.example.foodorder.util.AppUtils;
import com.example.foodorder.util.SharedPrefUtils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class OrderFragment extends Fragment {

    private OrderViewModel mViewModel;
    private OrderFragmentBinding mBinding;
    private OrderAdapter mAdapter;
       public static OrderFragment newInstance() {
        return new OrderFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        mBinding = OrderFragmentBinding.inflate(inflater, container, false);
        mViewModel = new ViewModelProvider(this).get(OrderViewModel.class);
        View view = mBinding.getRoot();

        //If there is no order and order executed is false then handle the back
        if (mViewModel.checkOrderEmpty()
                && !mViewModel.isOrderTrue()) {
            NavHostFragment.findNavController(this).navigate(R.id.foodMenuFragment);
        }
        initOrderView();
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    private void initOrderView() {
       // setTitle();
        setButtonListeners();
        initRecyclerView();
    }

    private void setButtonListeners() {
        if (mViewModel.isOrderTrue()) {
            showExecutedOrderView();
        }
        mBinding.cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new AlertDialog.Builder(getContext())
                        .setTitle(R.string.alert_dialog_cancel_title)
                        .setMessage(R.string.alert_cancel_confirm_msg)
                        .setNegativeButton(R.string.no, null)
                        .setPositiveButton(R.string.yes, (dialog, which) -> {
                            mViewModel.clearOrder();
                            NavHostFragment.findNavController(getParentFragment()).navigate(R.id.foodMenuFragment);
                        })
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .show();
            }
        });
        mBinding.placeOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new AlertDialog.Builder(getContext())
                        .setTitle(R.string.alert_dialog_order_title)
                        .setMessage(R.string.alert_order_confirm_msg)
                        .setNegativeButton(R.string.no, null)
                        .setPositiveButton(R.string.place_order, (dialog, which) -> {
                            mBinding.buttonsGroup.setVisibility(View.INVISIBLE);
                            if (mViewModel.placeOrder()) {
                                mBinding.successMsg.setVisibility(View.VISIBLE);
                                mViewModel.setOrderExecuted();
                            } else
                                mBinding.buttonsGroup.setVisibility(View.VISIBLE);
                            ;
                        })
                        .setIcon(R.drawable.ic_action_place_order)
                        .show();
            }
        });
    }

    private void showExecutedOrderView() {
        mBinding.buttonsGroup.setVisibility(View.INVISIBLE);
        mBinding.successMsg.setVisibility(View.VISIBLE);
    }

    private void showTotal() {
        Double total = mViewModel.getTotal();
        mBinding.grandTotalValue.setText(total.toString());
    }

    private void setTitle() {
        if (((AppCompatActivity) getActivity()).getSupportActionBar() != null)
            ((AppCompatActivity) getActivity()).getSupportActionBar()
                    .setTitle(getString(R.string.table_num)
                            + SharedPrefUtils.getOrder(getActivity().getApplicationContext()).getRestaurant().getTableNum());
    }

    @Override
    public void onResume() {
        super.onResume();
        setTitle();
    }


    @Override
    public void onStop() {
        super.onStop();
       }

    private void initRecyclerView() {
        mBinding.recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mAdapter = new OrderAdapter(getContext());
        mBinding.recyclerView.setAdapter(mAdapter);
        mAdapter.setList(SharedPrefUtils.getListView(getActivity().getApplicationContext()));
        showTotal();
    }


}