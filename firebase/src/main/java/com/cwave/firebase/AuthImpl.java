package com.cwave.firebase;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;

import com.firebase.ui.auth.AuthUI;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Arrays;

public class AuthImpl implements Auth {

  private static final String TAG = "AuthImpl";

  private final AuthUI authUI;
  private final FirebaseAuth auth;

  public AuthImpl() {
    authUI = AuthUI.getInstance();
    auth = FirebaseAuth.getInstance();
  }

  @Override
  public void sign(Activity activity, int request) {
    // Sign in with FirebaseUI
    Intent intent = authUI.createSignInIntentBuilder()
        .setAvailableProviders(
            Arrays.asList(
                new AuthUI.IdpConfig.Builder(AuthUI.EMAIL_PROVIDER).build(),
                new AuthUI.IdpConfig.Builder(AuthUI.PHONE_VERIFICATION_PROVIDER).build(),
                new AuthUI.IdpConfig.Builder(AuthUI.GOOGLE_PROVIDER).build()))
        .setIsSmartLockEnabled(false)
        .build();
    
    activity.startActivityForResult(intent, request);
  }

  @Override
  public FirebaseUser getCurrentUser() {
    return auth.getCurrentUser();
  }

  @Override
  public boolean isSignedIn() {
    return auth.getCurrentUser() != null;
  }
}
