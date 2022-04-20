package com.zoom.happiestplaces.profile;

import androidx.lifecycle.ViewModelProvider;

import android.app.Activity;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.zoom.happiestplaces.R;
import com.zoom.happiestplaces.databinding.OrderFragmentBinding;
import com.zoom.happiestplaces.databinding.ProfileFragmentBinding;
import com.zoom.happiestplaces.model.Customer;
import com.zoom.happiestplaces.order.OrderViewModel;
import com.zoom.happiestplaces.util.AppConstants;
import com.zoom.happiestplaces.util.AppUtils;
import com.zoom.happiestplaces.util.SharedPrefUtils;

public class ProfileFragment extends Fragment {

    private ProfileViewModel mViewModel;
    ProfileFragmentBinding mBinding;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(ProfileViewModel.class);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        mBinding = ProfileFragmentBinding.inflate(inflater, container, false);
        View view = mBinding.getRoot();
        updateUI();
        addClickListener();
        return view;
    }

    private void addClickListener() {
        mBinding.logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mViewModel.getGoogleClient().signOut()
                        .addOnCompleteListener(getActivity(), task -> {Log.d(AppConstants.TAG,"Delete...");
                mViewModel.logOut();
                            NavHostFragment.findNavController(getParentFragment()).
                                    navigate(R.id.scanFragment);
                        });
            }
        });
    }

    private void updateUI() {
        Customer customer= SharedPrefUtils.getCustomer(getActivity().getApplicationContext());
        if(!TextUtils.isEmpty(customer.getName()))
        mBinding.itemTitle.setText(customer.getName().toUpperCase());
        if(!TextUtils.isEmpty(customer.getEmailId()))
            mBinding.email.setText(customer.getEmailId());
        if(!TextUtils.isEmpty(customer.getPhone()))
            mBinding.phone.setText(customer.getPhone());
       // else
       //     mBinding.phone.setVisibility(View.GONE);
        if(!TextUtils.isEmpty(customer.getAddress()))
            mBinding.address.setText(customer.getAddress());
       // else
       //     mBinding.address.setVisibility(View.GONE);
        mBinding.rewardsPts.setText(getString(R.string.total_points)+" "+customer.getCurrent_pts());
        Log.d(AppConstants.TAG,"url"+customer.getPhotoURL());
        AppUtils.setImage(getContext(),customer.getPhotoURL(),mBinding.itemProfileImg);
    }


}