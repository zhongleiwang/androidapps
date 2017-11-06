package com.cwave.calculation.signin;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.cwave.calculation.R;
import com.cwave.calculation.console.ConsoleActivity;
import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.ErrorCodes;
import com.firebase.ui.auth.IdpResponse;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Arrays;

import javax.inject.Inject;

import dagger.android.AndroidInjection;

import static com.google.common.base.Preconditions.checkNotNull;

/** Shows sign in screen at first time use. */
public class SignInActivity extends AppCompatActivity {

  private static final String TAG = "SignInActivity";

  private static final String PREF_SIGN_IN = "perf_sign_in";

  private static final int AUTH_REQUEST_CODE = 9999;

  private AuthUI authUI;
  private FirebaseAuth auth;

  /** Starts a new {@link ConsoleActivity} instance. */
  public static void startActivity(Context context) {
    checkNotNull(context);
    context.startActivity(new Intent(context, SignInActivity.class));
  }

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    authUI = AuthUI.getInstance();
    auth = FirebaseAuth.getInstance();

    Log.d(TAG, "onCreate");
    final SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);

    // TODO: Need to check signed in user id.
    if (!sharedPreferences.getBoolean(PREF_SIGN_IN, true) && isSignedIn()) {
      Log.d(TAG, "sign in already");
      startActivity();
      return;
    } else {
      Log.d(TAG, "signing in");
      sharedPreferences.edit().putBoolean(PREF_SIGN_IN, false).apply();
      sign();
    }
  }

  @Override
  protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    super.onActivityResult(requestCode, resultCode, data);
    Log.d(TAG, "onActivityResult request: " + requestCode + " result: " + resultCode);
    if (requestCode == AUTH_REQUEST_CODE) {
      IdpResponse response = IdpResponse.fromResultIntent(data);
      
      if (resultCode == RESULT_OK) {
        startActivity();
        finish();
      } else {
        // Sign in failed
        if (response == null) {
          // User pressed back button
          showSnackbar("Sign in cancelled");
          return;
        }

        if (response.getErrorCode() == ErrorCodes.NO_NETWORK) {
          showSnackbar("No internet connection");
          return;
        }

        if (response.getErrorCode() == ErrorCodes.UNKNOWN_ERROR) {
          showSnackbar("Unknown error");
          return;
        }
      }
    }
  }

  private void sign() {
    sign(this, AUTH_REQUEST_CODE);
  }

  private void startActivity() {
    ConsoleActivity.startActivity(this);
    this.finish();
  }

  private void showSnackbar(String msg) {
    Snackbar.make(this.getCurrentFocus(), msg, Snackbar.LENGTH_LONG).setAction("Action", null).show();
  }

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

  private boolean isSignedIn() {
    return auth.getCurrentUser() != null;
  }
}
