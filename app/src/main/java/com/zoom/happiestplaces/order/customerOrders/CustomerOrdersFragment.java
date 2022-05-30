package com.zoom.happiestplaces.order.customerOrders;

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
import com.zoom.happiestplaces.databinding.CustomerOrdersFragmentBinding;
import com.zoom.happiestplaces.order.OrderAdapter;
import com.zoom.happiestplaces.util.AppUtils;
import com.zoom.happiestplaces.util.SharedPrefUtils;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class CustomerOrdersFragment extends Fragment {

    private CustomerOrdersViewModel mViewModel;
    private CustomerOrdersFragmentBinding mBinding;
    private CustomerOrdersAdapter mAdapter;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        //check sign in otherwise go to food menu
        mBinding = CustomerOrdersFragmentBinding.inflate(inflater, container, false);
        View view = mBinding.getRoot();
        initViewModel();
        initRecyclerView();
        getOrders();
        return view;
       
    }

    private void getOrders() {

       displayLoader();
        //TODO:Save restaurant details in menu this has to be done once
        mViewModel.getOrders(SharedPrefUtils.getCustomer(getActivity().getApplicationContext()).getId())
                .observe(getViewLifecycleOwner(), orderList -> {
                    hideLoader();
                    if (orderList == null || orderList.size() == 0) {
                        if (!AppUtils.isNetworkAvailableAndConnected(getContext()))
                            AppUtils.showSnackbar(getView(), getString(R.string.network_err));
                            displayEmptyView();

                    } else {
                        mAdapter.setList(orderList);
                    }
                });

    }

    private void hideLoader() {
        mBinding.viewLoader.rootView.setVisibility(View.GONE);
        mBinding.recyclerView.setVisibility(View.VISIBLE);
    }
    private void displayEmptyView() {
        mBinding.recyclerView.setVisibility(View.GONE);
        mBinding.viewEmpty.emptyText.setText(getString(R.string.empty_order));
        mBinding.viewEmpty.emptyContainer.setVisibility(View.VISIBLE);
    }

    private void displayLoader() {
        mBinding.viewLoader.rootView.setVisibility(View.VISIBLE);
        mBinding.recyclerView.setVisibility(View.GONE);
    }
    private void initRecyclerView() {
        mBinding.recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mAdapter = new CustomerOrdersAdapter(getContext());
        mBinding.recyclerView.setAdapter(mAdapter);

    }

    private void initViewModel() {
        mViewModel = new ViewModelProvider(this).get(CustomerOrdersViewModel.class);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

   

}