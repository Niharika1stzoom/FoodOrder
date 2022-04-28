package com.zoom.happiestplaces.profile;

import androidx.lifecycle.ViewModelProvider;

import android.accounts.Account;
import android.app.Activity;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;

import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.zoom.happiestplaces.R;
import com.zoom.happiestplaces.databinding.OrderFragmentBinding;
import com.zoom.happiestplaces.databinding.ProfileFragmentBinding;
import com.zoom.happiestplaces.model.Address;
import com.zoom.happiestplaces.model.Customer;
import com.zoom.happiestplaces.order.OrderViewModel;
import com.zoom.happiestplaces.util.AppConstants;
import com.zoom.happiestplaces.util.AppUtils;
import com.zoom.happiestplaces.util.SharedPrefUtils;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
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
                                    navigate(R.id.scanFragment); });
            }
        });
        mBinding.save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!validate())
                    return;
                Customer customer=mViewModel.getCustomer();
                boolean edit=false;
                if(!TextUtils.isEmpty(mBinding.street.getText().toString().trim()))
                {
                    customer.setAddress(new Address(mBinding.street.getText().toString().trim(),
                            mBinding.code.getText().toString().trim(),
                            mBinding.city.getText().toString().trim(),
                            mBinding.country.getText().toString().trim()));
                    if(!TextUtils.isEmpty(mBinding.phone.getText().toString().trim()))
                    {
                        customer.getAddress().setPhone_number(mBinding.phone.getText().toString().trim());
                        edit=true;
                    }
                    edit=true;
                }
                if(!TextUtils.isEmpty(mBinding.itemTitle.getText().toString().trim()))
                {
                    customer.setName(mBinding.itemTitle.getText().toString());
                    edit=true;
                }
                if(edit) {

                    mViewModel.updateCustomer(customer).observe(getViewLifecycleOwner(), customerResponse -> {
                        if(customerResponse==null) {
                            if(!AppUtils.isNetworkAvailableAndConnected(getContext()))
                                AppUtils.showSnackbar(getView(),getString(R.string.network_err));
                            Log.d(AppConstants.TAG,"Customer response null");
                        }
                        else {
                            mViewModel.saveCustomer(customerResponse);
                            AppUtils.showSnackbar(getView(),getString(R.string.data_updated));
                            NavHostFragment.findNavController(getParentFragment()).navigate(R.id.settingsFragment);
                        }
                    });
                }
            }
        });
    }

    private boolean validate() {
        boolean validate=true;
        if(TextUtils.isEmpty(mBinding.street.getText())
                && TextUtils.isEmpty(mBinding.code.getText())
        && TextUtils.isEmpty(mBinding.city.getText())
        && TextUtils.isEmpty(mBinding.country.getText())
    && TextUtils.isEmpty(mBinding.phone.getText()))
        {
            return validate;
        }
        if(TextUtils.isEmpty(mBinding.street.getText())) {
            mBinding.street.setError(getString(R.string.textEmptyError));
            validate=false;
        }
        if(TextUtils.isEmpty(mBinding.code.getText())) {
            mBinding.code.setError(getString(R.string.textEmptyError));
            validate=false;
        }
        if(TextUtils.isEmpty(mBinding.city.getText())) {
            mBinding.city.setError(getString(R.string.textEmptyError));
            validate=false;
        }
        if(TextUtils.isEmpty(mBinding.country.getText())) {
            mBinding.country.setError(getString(R.string.textEmptyError));
            validate=false;
        }
        if(TextUtils.isEmpty(mBinding.phone.getText())) {
            mBinding.phone.setError(getString(R.string.textEmptyError));
            validate=false;
        }

        return validate;
    }

    private void updateUI() {
        Customer customer=mViewModel.getCustomer();

        if(!TextUtils.isEmpty(customer.getName()))
        mBinding.itemTitle.setText(customer.getName().toUpperCase());
        if(!TextUtils.isEmpty(customer.getEmailId()))
            mBinding.email.setText(customer.getEmailId());
        if(customer.getAddress()!=null)
       { mBinding.code.setText(customer.getAddress().getCode());
           mBinding.city.setText(customer.getAddress().getCity());
           mBinding.street.setText(customer.getAddress().getStreet());
           mBinding.country.setText(customer.getAddress().getCountry());
           if(!TextUtils.isEmpty(customer.getAddress().getPhone_number()))
               mBinding.phone.setText(customer.getAddress().getPhone_number());
       }
        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(getActivity());
        mBinding.rewardsPts.setText(getString(R.string.total_points)+" "+customer.getCurrent_pts());
        AppUtils.setImage(getContext(),customer.getPhotoURL(),mBinding.itemProfileImg);
    }

}