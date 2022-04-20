package com.zoom.happiestplaces.foodmenu;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.GridLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.zoom.happiestplaces.R;
import com.zoom.happiestplaces.databinding.FoodMenuFragmentBinding;
import com.zoom.happiestplaces.model.MenuItem;
import com.zoom.happiestplaces.model.Restaurant;
import com.zoom.happiestplaces.util.AppUtils;
import com.zoom.happiestplaces.util.QRUtils;
import com.zoom.happiestplaces.util.RestaurantUtils;
import com.zoom.happiestplaces.util.SharedPrefUtils;
import java.util.List;
import java.util.UUID;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class FoodMenuFragment extends Fragment {
    private FoodMenuFragmentBinding mBinding;
    private FoodMenuViewModel mViewModel;
    private FoodMenuAdapter mAdapter;
    private UUID mRestaurantId, mQRcode;

    public static FoodMenuFragment newInstance() {
        return new FoodMenuFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        mBinding= FoodMenuFragmentBinding.inflate(inflater,container,false);
        View view=mBinding.getRoot();
        //checkOrderStatus();
        initViewModel();
        initRecyclerView();
        return view;

    }

    private void initViewModel() {
        mViewModel = new ViewModelProvider(this).get(FoodMenuViewModel.class);
        getRestaurantMenu();
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
        mBinding.viewOrderButton.setOnClickListener(view -> {
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
        });
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //get restaurant
        if (getArguments() != null) {
            /*if (getArguments().containsKey(RestaurantUtils.ARG_RESTAURANT_ID)) {
                mRestaurantId = UUID.fromString(getArguments().getString(RestaurantUtils.ARG_RESTAURANT_ID));
            }*/
            if (getArguments().containsKey(RestaurantUtils.ARG_QRCode_ID)) {
                mQRcode = UUID.fromString(getArguments().getString(RestaurantUtils.ARG_QRCode_ID));
                SharedPrefUtils.createOrder(getActivity().getApplicationContext(),mQRcode);
            }
        }

        else
        {
                //it should be already there
        }
    }


    @Override
    public void onResume() {
        super.onResume();
        setTitle();
    }

    private void getRestaurantMenu() {
        //TODO:Save restaurant details in menu this has to be done once
        mViewModel.getMenuList(mQRcode)
                .observe(getViewLifecycleOwner(), restaurant -> {
                    displayLoader();
                    if(restaurant==null || restaurant.getMenuList().size()==0) {

                        //SharedPrefUtils.createOrder(getActivity().getApplicationContext(),mQRcode,restaurant);
                        if(AppUtils.isNetworkAvailableAndConnected(getContext()))
                            displayEmptyView();
                        else
                            AppUtils.showSnackbar(getView(),getString(R.string.network_err));
                        mBinding.recyclerView.setVisibility(View.INVISIBLE);
                    }
                    else {
                        SharedPrefUtils.setRestaurant(getActivity().getApplicationContext(),restaurant);
                        setTitle();
                        displayDataView(restaurant.getMenuList());
                    }
                });
       /* mViewModel.getMenuList(mRestaurantId)
                .observe(getViewLifecycleOwner(), menuList -> {
                    displayLoader();
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
    });*/
}
    private void setTitle() {
        if(mViewModel.getRestaurant()!=null)
        if (((AppCompatActivity) getActivity()).getSupportActionBar() != null)
            ((AppCompatActivity) getActivity()).getSupportActionBar()
                    .setTitle(mViewModel.getRestaurant().getName());
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