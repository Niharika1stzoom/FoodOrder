package com.zoom.happiestplaces.order;


import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
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

import com.zoom.happiestplaces.R;
import com.zoom.happiestplaces.databinding.OrderFragmentBinding;
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

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class OrderFragment extends Fragment {

    private OrderViewModel mViewModel;
    private OrderFragmentBinding mBinding;
    private OrderAdapter mAdapter;
    private ActivityResultLauncher<Intent> mStartForResult;
    private GoogleSignInClient mGoogleSignInClient;

    public static OrderFragment newInstance() {
        return new OrderFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
        if(account!=null)
        {
            mBinding.signInButton.setVisibility(View.GONE);
            setRedeemPoints(SharedPrefUtils.getCustomer(getContext()).getCurrent_pts());
        }
    }
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        mBinding = OrderFragmentBinding.inflate(inflater, container, false);
        View view = mBinding.getRoot();
        mViewModel = new ViewModelProvider(this).get(OrderViewModel.class);
        //If there is no order and order executed is false then handle the back
        if (mViewModel.checkOrderEmpty()
                && !mViewModel.isOrderTrue()) {
            NavHostFragment.findNavController(this).navigate(R.id.foodMenuFragment);
        }
        initOrderView();
        //TODO:if account is null
        initSignIn();
        return view;
    }
    void setRedeemPoints(int points)
    {
        mBinding.redeemLabelWelcome.setVisibility(View.GONE);
        mBinding.redeemLabel.setText("Yay! You have "+points+ " rewards points");
    }

    private void getRedeemPoints() {
        //For this to work u will need cust id from api
        mViewModel.getRedeemPoints(mViewModel.getCustomer().getId()).observe(getViewLifecycleOwner(), customer -> {
            if(customer==null) {
            }
            else {
                mViewModel.saveCustomer(customer);
             setRedeemPoints(mViewModel.getCustomer().getCurrent_pts());
            }
        });
    }

    private void initSignIn() {
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(getActivity(), gso);
        mBinding.signInButton.setSize(SignInButton.SIZE_WIDE);
        mBinding.signInButton.setOnClickListener(view -> {
            Intent intent = mGoogleSignInClient.getSignInIntent();
            mStartForResult.launch(intent);
        });
    }
    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);
            AppUtils.showSnackbar(getView(),getString(R.string.sign_in)+account.getEmail());
            mViewModel.addCustomer(account).observe(getViewLifecycleOwner(), customer -> {
                //TODO:check this line
                if(customer==null) {
                    //For testing as no api
                    mViewModel.saveCustomer(CustomerUtils.getCustomerAccount(account));
                    updateUI(account);
                    //logOut();
                }
                else {
                    mViewModel.saveCustomer(customer);
                    //this will happen only once
                    updateUI(account);
                }
            });
        } catch (ApiException e) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            Log.d(AppConstants.TAG, "signInResult:failed code=" + e.getStatusCode());
            updateUI(null);
        }
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
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
        mBinding.placeOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog alert=new AlertDialog.Builder(getContext())
                        .setTitle(R.string.alert_dialog_order_title)
                        .setMessage(R.string.alert_order_confirm_msg)
                        .setNegativeButton(R.string.no, null)
                        .setPositiveButton(R.string.place_order, (dialog, which) -> {
                            mBinding.buttonsGroup.setVisibility(View.INVISIBLE);
                            if (mViewModel.placeOrder()) {
                                //TODO:add customer in order
                                mBinding.successMsg.setVisibility(View.VISIBLE);
                                mViewModel.setOrderExecuted();
                                setReviewButton();
                            } else
                                mBinding.buttonsGroup.setVisibility(View.VISIBLE);
                        })
                        .setIcon(R.mipmap.ic_launcher_happiest)
                        .show();

                alert.getButton(DialogInterface.BUTTON_POSITIVE)
                        .setTextColor(Color.BLACK);
                alert.getButton(DialogInterface.BUTTON_NEGATIVE)
                        .setTextColor(Color.BLACK);
            }
        });
    }

    private void setReviewButton() {
            mViewModel.scheduleNotification(mViewModel.getCurrentOrder().getId(),
                    mViewModel.getCurrentOrder().getRestaurant().getName());
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
                            + mViewModel.getCurrentOrder().getTable());
    }

    @Override
    public void onResume() {
        super.onResume();
        setTitle();
    }


    @Override
    public void onStop() {
        super.onStop();
        //logOut();
       }

    private void logOut() {
        mGoogleSignInClient.signOut()
                .addOnCompleteListener(getActivity(), task -> mViewModel.delCustomer());
    }

    private void initRecyclerView() {
        mBinding.recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mAdapter = new OrderAdapter(getContext());
        mBinding.recyclerView.setAdapter(mAdapter);
        mAdapter.setList(SharedPrefUtils.getListView(getActivity().getApplicationContext()));
        showTotal();
    }
}