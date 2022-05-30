package com.zoom.happiestplaces.order;


import androidx.activity.OnBackPressedCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.emoji2.text.EmojiCompat;
import androidx.lifecycle.ViewModelProvider;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.material.chip.ChipGroup;
import com.google.firebase.iid.FirebaseInstanceIdReceiver;

import com.google.firebase.messaging.FirebaseMessaging;
import com.zoom.happiestplaces.R;
import com.zoom.happiestplaces.databinding.OrderFragmentBinding;
import com.zoom.happiestplaces.model.Customer;
import com.zoom.happiestplaces.model.response.OrderResponse;
import com.zoom.happiestplaces.util.AppConstants;
import com.zoom.happiestplaces.util.AppUtils;
import com.zoom.happiestplaces.util.CustomerUtils;
import com.zoom.happiestplaces.util.SharedPrefUtils;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.zoom.happiestplaces.util.SignInUtils;

import java.util.UUID;

import dagger.hilt.android.AndroidEntryPoint;
import com.google.firebase.messaging.FirebaseMessagingService;

@AndroidEntryPoint
public class OrderFragment extends Fragment {
    private OrderViewModel mViewModel;
    private OrderFragmentBinding mBinding;
    private OrderAdapter mAdapter;
    private ActivityResultLauncher<Intent> mStartForResult;
    private GoogleSignInClient mGoogleSignInClient;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //if mOrderId is there observe flow otherwise normal flow use OrderResponse ow use Order
        mStartForResult = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(result.getData());
                        handleSignInResult(task);
                    }
                });
    }

    @Override
    public void onStart() {
        super.onStart();
        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(getActivity());
        updateUI(account);
    }

    private void updateUI(GoogleSignInAccount account) {
        //check whether viewmodel is created
        if(account!=null && mViewModel.getCustomer()!=null)
        {
           // mBinding.signInButton.setVisibility(View.GONE);
            getRedeemPoints();
            //TODO:if using redeem check
            mBinding.signInGroup.setVisibility(View.GONE);
        }
        else
        {
            if(account!=null && mViewModel.getCustomer()==null)
            {
                logOut();
            }
        }
    }



    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        mBinding = OrderFragmentBinding.inflate(inflater, container, false);
        View view = mBinding.getRoot();
        mViewModel = new ViewModelProvider(this).get(OrderViewModel.class);
        //Here check whether mOrderId exist if so observe and else check the other part
        //If there is no order and order executed is false then handle the back
        if (SharedPrefUtils.checkOrderIsEmpty(getActivity().getApplicationContext())
                && !mViewModel.isOrderTrue()) {
            NavHostFragment.findNavController(getParentFragment()).navigate(R.id.foodMenuFragment);
        }
        initOrderView();

        //TODO:if account is null
        initSignIn();
        return view;
    }
    void setRedeemPoints(Double points)
    {

       // mBinding.signinLabel.setText("Yay! You have "+points+ " rewards points");
       //TODO:Below if using checkbox

        mBinding.redeemGroup.setVisibility(View.VISIBLE);
       if(points<=0)
           mBinding.rewardCheckbox.setEnabled(false);
         mBinding.rewardCheckbox.setText(getString(R.string.reward_pts)+points);
        mBinding.ptsBalance.setText(getString(R.string.available)+points);


    }



    private void getRedeemPoints() {
        mViewModel.getRedeemPoints(mViewModel.getCustomer()).observe(getViewLifecycleOwner(), customer -> {
            if(customer==null) {
                if(!AppUtils.isNetworkAvailableAndConnected(getActivity().getApplicationContext()))
                    setRedeemPoints(mViewModel.getCustomer().getCurrent_pts());
            }
            else {
                mViewModel.saveCustomer(customer);
             setRedeemPoints(mViewModel.getCustomer().getCurrent_pts());
            }
        });
    }

    private void initSignIn() {
        Log.d(AppConstants.TAG,"in sign in");
        GoogleSignInOptions gso= SignInUtils.getSignInOptions();
        mGoogleSignInClient = GoogleSignIn.getClient(getActivity(), gso);
        mBinding.signInButton.setSize(SignInButton.SIZE_WIDE);
        mBinding.signInButton.setOnClickListener(view -> {
            Log.d(AppConstants.TAG,"Clicked sign in");
            Intent intent = mGoogleSignInClient.getSignInIntent();
            mStartForResult.launch(intent);
        });
    }
    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);
            Customer cust=CustomerUtils.getCustomerAccount(account,getActivity().getApplicationContext());
            Log.d(AppConstants.TAG,"NEW Customer is "+cust.getFcmToken()+" Address "+cust.getReferralId());
            mViewModel.addCustomer(CustomerUtils.getCustomerAccount(account,getActivity().getApplicationContext())).observe(getViewLifecycleOwner(), customer -> {
                if(customer==null) {
                    AppUtils.showSnackbar(getView(),getString(R.string.sign_in_err));
                    logOut();
                }
                else {
                    mViewModel.saveCustomer(customer);
                    //this will happen only once
                    updateUI(account);
                    AppUtils.showSnackbar(getView(),getString(R.string.sign_in)+customer.getEmailId());
                }
            });
        } catch (ApiException e) {
            // The ApiException status code indicates the detailed failure reason.
            Log.d(AppConstants.TAG, "signInResult:failed code=" + e.getStatusCode());
            updateUI(null);
        }
    }

    private void initOrderView() {
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
               AlertDialog alert= new AlertDialog.Builder(getContext())
                        .setTitle(R.string.alert_dialog_cancel_title)
                        .setMessage(R.string.alert_cancel_confirm_msg)
                        .setNegativeButton(R.string.no, null)
                        .setPositiveButton(R.string.yes, (dialog, which) -> {
                            mViewModel.clearOrder();
                            NavHostFragment.findNavController(getParentFragment()).navigate(R.id.foodMenuFragment);
                        })
                        .setIcon(android.R.drawable.ic_dialog_alert).show();
               alert.getButton(DialogInterface.BUTTON_POSITIVE)
                       .setTextColor(Color.BLACK);
                alert.getButton(DialogInterface.BUTTON_NEGATIVE)
                        .setTextColor(Color.BLACK);
            }
        });
        mBinding.placeOrder.setOnClickListener(view -> {
            AlertDialog alert=new AlertDialog.Builder(getContext())
                    .setTitle(R.string.alert_dialog_order_title)
                    .setMessage(R.string.alert_order_confirm_msg)
                    .setNegativeButton(R.string.no, null)
                    .setPositiveButton(R.string.place_order, (dialog, which) -> {
                        mBinding.buttonsGroup.setVisibility(View.INVISIBLE);
                        if(mBinding.rewardCheckbox.isChecked())
                        {
                            mViewModel.setRedeemPointsOrder();
                        }
                        //when order is placed customer is added
                       mViewModel.placeOrder().observe(getViewLifecycleOwner(), order -> {
                            if(order==null) {
                                mBinding.buttonsGroup.setVisibility(View.VISIBLE);
                                AppUtils.showSnackbar(getView(),getString(R.string.order_fail));
                            }
                            else {
                                Log.d(AppConstants.TAG,"OrderId "+order.getId());
                                mBinding.successMsg.setVisibility(View.VISIBLE);
                                String s="You will earn "+order.getPoints()+" points "+
                                        new String(Character.toChars(AppConstants.PARTY_POPPER_UNICODE));
                                mBinding.successMsg.
                                        setText(getString(R.string.success_msg)+s);
                                mViewModel.setOrderExecuted();
                                getRedeemPoints();
                                //Clearing the order in Food menu
                                //SharedPrefUtils.cancelOrder(getActivity().getApplicationContext());
                            }
                        });
                    })
                    .setIcon(R.mipmap.ic_launcher_happiest)
                    .show();

            alert.getButton(DialogInterface.BUTTON_POSITIVE)
                    .setTextColor(Color.BLACK);
            alert.getButton(DialogInterface.BUTTON_NEGATIVE)
                    .setTextColor(Color.BLACK);
        });
        mBinding.rewardCheckbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b) {
                    Double redeem = mViewModel.getRedeemRs();
                    mBinding.ptsBalance.setText(getString(R.string.remaining) + mViewModel.getRemainingPoints());
                    showTotal(redeem);
                    Log.d(AppConstants.TAG,"Redeem points for this order"+mViewModel.getRedeemPointsOrder());

                }
                else {
                    mBinding.ptsBalance.setText(getString(R.string.available)
                            + mViewModel.getCustomer().getCurrent_pts());
                    showTotal(0.0);
                }
            }
        });
    }
    private void showTotal(Double redeemRs) {

        Double total = mViewModel.getTotal() - redeemRs;
        mBinding.grandTotalValue.setText(String.format("%.2f",total));
    }

    private void showExecutedOrderView() {
        mBinding.buttonsGroup.setVisibility(View.INVISIBLE);
        mBinding.successMsg.setVisibility(View.VISIBLE);
        mBinding.rewardCheckbox.setEnabled(false);
    }



    private void setTitle() {
       if(mViewModel.getCurrentOrder().getRestaurant()!=null)
        if (((AppCompatActivity) getActivity()).getSupportActionBar() != null)
            ((AppCompatActivity) getActivity()).getSupportActionBar()
                    .setTitle(getString(R.string.table_num)
                            + mViewModel.getCurrentOrder().getRestaurant().getTable().getNumber());
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

    private void logOut() {
        mGoogleSignInClient.signOut()
                .addOnCompleteListener(getActivity(), task -> mViewModel.delCustomer());
    }

    private void initRecyclerView() {
        mBinding.recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mAdapter = new OrderAdapter(getContext());
        mBinding.recyclerView.setAdapter(mAdapter);
        mAdapter.setList(SharedPrefUtils.getOrder(getActivity().getApplicationContext()).getItemsList());
        showTotal(0.0);
    }
}