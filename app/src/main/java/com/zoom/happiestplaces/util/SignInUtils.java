package com.zoom.happiestplaces.util;

import com.google.android.gms.auth.api.signin.GoogleSignInOptions;

public class SignInUtils {
    public static GoogleSignInOptions getSignInOptions()
    {
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        return gso;
    }
}
