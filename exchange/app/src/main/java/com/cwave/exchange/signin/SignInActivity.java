package com.cwave.exchange.signin;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.cwave.exchange.R;
import com.cwave.exchange.trading.TradingActivity;
import com.cwave.firebase.Auth;
import com.firebase.ui.auth.ErrorCodes;
import com.firebase.ui.auth.IdpResponse;

import javax.inject.Inject;

import dagger.android.AndroidInjection;

import static com.google.common.base.Preconditions.checkNotNull;

/** Shows sign in screen at first time use. */
public class SignInActivity extends AppCompatActivity {

  private static final String TAG = "SignInActivity";

  private static final String PREF_SIGN_IN = "perf_sign_in";

  private static final int AUTH_REQUEST_CODE = 9999;

  @Inject
  Auth auth;

  /** Starts a new {@link TradingActivity} instance. */
  public static void startActivity(Context context) {
    checkNotNull(context);
    context.startActivity(new Intent(context, SignInActivity.class));
  }

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    AndroidInjection.inject(this);
    super.onCreate(savedInstanceState);
    Log.d(TAG, "onCreate");
    final SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);

    // TODO: Need to check signed in user id.
    if (!sharedPreferences.getBoolean(PREF_SIGN_IN, true) && auth.isSignedIn()) {
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
          showSnackbar(R.string.sign_in_cancelled);
          return;
        }

        if (response.getErrorCode() == ErrorCodes.NO_NETWORK) {
          showSnackbar(R.string.no_internet_connection);
          return;
        }

        if (response.getErrorCode() == ErrorCodes.UNKNOWN_ERROR) {
          showSnackbar(R.string.unknown_error);
          return;
        }
      }
    }
  }

  private void sign() {
    auth.sign(this, AUTH_REQUEST_CODE);
  }

  private void startActivity() {
    TradingActivity.startActivity(this);
    this.finish();
  }

  private void showSnackbar(int id) {
    Snackbar.make(this.getCurrentFocus(), id, Snackbar.LENGTH_LONG).setAction("Action", null).show();
  }
}
