package com.zoom.happiestplaces.settings;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.navigation.fragment.NavHostFragment;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.zoom.happiestplaces.R;
import com.zoom.happiestplaces.util.AppConstants;
import com.zoom.happiestplaces.util.SharedPrefUtils;

public class SettingsFragment extends PreferenceFragmentCompat {
    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.root_preferences, rootKey);
        checkSignIn();
        findPreference(getString(R.string.profile)).setOnPreferenceClickListener(preference -> {
            NavHostFragment.findNavController(this).navigate(R.id.profileFragment);

            return true;
        });
    }

    private void checkSignIn() {
        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(getActivity());
        if(account==null)
        {
            findPreference(getString(R.string.profile)).setVisible(false);
            findPreference(getString(R.string.sign_in_settings)).setVisible(true);
        }
    }
       /* if(SharedPrefUtils.getCustomer(getActivity().getApplicationContext())==null) {
            Log.d(AppConstants.TAG,"Customer null");
            findPreference(getString(R.string.profile)).setVisible(false);
        }
        else
            Log.d(AppConstants.TAG,"Customer not null"+
                    SharedPrefUtils.getCustomer(getActivity().getApplicationContext()).getName());


    }*/


}