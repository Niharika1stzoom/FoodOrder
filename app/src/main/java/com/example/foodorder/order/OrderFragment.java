package com.example.foodorder.order;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.SavedStateHandle;
import androidx.lifecycle.ViewModelProvider;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.util.Log;
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
import com.example.foodorder.util.NotificationUtils;
import com.example.foodorder.util.OrderUtils;
import com.example.foodorder.util.SharedPrefUtils;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

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
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        if (result.getResultCode() == Activity.RESULT_OK) {
                            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(result.getData());
                            handleSignInResult(task);
                        }
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

        if(account!=null)
        {
            mBinding.signInButton.setVisibility(View.GONE);
            mBinding.redeemLabelWelcome.setVisibility(View.GONE);
            mBinding.redeemLabel.setText("Yay! You have 100 rewards points");
            Log.d("FoodDebug","Account info"+account.getEmail()+account.getDisplayName()+account.getId());
        }
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
        initSignIn();
        return view;
    }

    private void initSignIn() {
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(getActivity(), gso);
        mBinding.signInButton.setSize(SignInButton.SIZE_STANDARD);
        mBinding.signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = mGoogleSignInClient.getSignInIntent();
                mStartForResult.launch(intent);
            }
            });
    }
    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);

            // Signed in successfully, show authenticated UI.
            updateUI(account);
        } catch (ApiException e) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            Log.d("OffDebug", "signInResult:failed code=" + e.getStatusCode());
            updateUI(null);
        }
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
                                setReviewButton();
                            } else
                                mBinding.buttonsGroup.setVisibility(View.VISIBLE);
                        })
                        .setIcon(R.drawable.ic_action_place_order)
                        .show();
            }
        });
    }

    private void setReviewButton() {
            mViewModel.scheduleNotification(mViewModel.getCurrentOrder().getId(),
                    mViewModel.getCurrentOrder().getRestaurant().getName());
           mBinding.reviewButton.setVisibility(View.VISIBLE);
           mBinding.reviewButton.setOnClickListener(new View.OnClickListener() {
               @Override
               public void onClick(View view) {
                   NavHostFragment.findNavController(getParentFragment()).navigate(R.id.addReviewFragment,
                           OrderUtils.getOrderBundle(mViewModel.getCurrentOrder()));
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
        logOut();
       }

    private void logOut() {
        mGoogleSignInClient.signOut()
                .addOnCompleteListener(getActivity(), new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Log.d("OffDebug","Logging out.....");
                    }
                });
    }

    private void initRecyclerView() {
        mBinding.recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mAdapter = new OrderAdapter(getContext());
        mBinding.recyclerView.setAdapter(mAdapter);
        mAdapter.setList(SharedPrefUtils.getListView(getActivity().getApplicationContext()));
        showTotal();
    }
}