package com.zoom.happiestplaces.settings;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.zoom.happiestplaces.R;
import com.zoom.happiestplaces.order.OrderViewModel;
import com.zoom.happiestplaces.util.AppConstants;
import com.zoom.happiestplaces.util.AppUtils;
import com.zoom.happiestplaces.util.CustomerUtils;
import com.zoom.happiestplaces.util.SharedPrefUtils;
import com.zoom.happiestplaces.util.SignInUtils;

public class SettingsFragment extends PreferenceFragmentCompat {
    private ActivityResultLauncher<Intent> mStartForResult;
    private OrderViewModel mViewModel;
    private GoogleSignInClient mGoogleSignInClient;

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.root_preferences, rootKey);
        checkSignIn();
        mStartForResult = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        Task<GoogleSignInAccount> task =
                                GoogleSignIn.getSignedInAccountFromIntent(result.getData());
                        handleSignInResult(task);
                    }
                });
        findPreference(getString(R.string.profile)).setOnPreferenceClickListener(preference -> {
            NavHostFragment.findNavController(this).navigate(R.id.profileFragment);
            return true;
        });
        findPreference(getString(R.string.sign_in_settings)).setOnPreferenceClickListener(preference -> {
            mViewModel = new ViewModelProvider(getActivity()).get(OrderViewModel.class);
            signIn();
            return true;
        });
    }

    private void signIn() {

        mGoogleSignInClient = GoogleSignIn.getClient(getActivity(),  SignInUtils.getSignInOptions());
        Intent intent =mGoogleSignInClient.getSignInIntent();
        mStartForResult.launch(intent);
    }
    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);
            mViewModel.addCustomer(CustomerUtils.getCustomerAccount(account)).observe(getViewLifecycleOwner(), customer -> {
                //TODO:check this line
                if(customer==null) {
                    AppUtils.showSnackbar(getView(),getString(R.string.sign_in_err));
                    logOut();
                }
                else {
                    mViewModel.saveCustomer(customer);
                    AppUtils.showSnackbar(getView(),getString(R.string.sign_in)+customer.getEmailId());
                    checkSignIn();
                    //NavHostFragment.findNavController(this).navigate(R.id.foodMenuFragment);
                }
            });
        } catch (ApiException e) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            Log.d(AppConstants.TAG, "signInResult:failed code=" + e.getStatusCode());
            //updateUI(null);
        }
    }
    private void logOut() {
     if(mGoogleSignInClient!=null)
        mGoogleSignInClient.signOut()
                .addOnCompleteListener(getActivity(), task -> mViewModel.delCustomer());
    }

    private void checkSignIn() {

        if(SharedPrefUtils.getCustomer(getActivity().getApplicationContext())==null) {
            findPreference(getString(R.string.profile)).setVisible(false);
            findPreference(getString(R.string.sign_in_settings)).setVisible(true);
        }
        else {
            findPreference(getString(R.string.profile)).setVisible(true);
            findPreference(getString(R.string.sign_in_settings)).setVisible(false);

        }
    }


}