package com.cwave.calculation.splash;

import android.content.SharedPreferences;
import android.os.Build.VERSION_CODES;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.cwave.calculation.R;
import com.cwave.calculation.signin.SignInActivity;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.firebase.crash.FirebaseCrash;

/** Shows welcome screen at first time use. */
public class SplashActivity extends AppCompatActivity {

  private static final String TAG = "Splash";

  private static final int PERMISSION_REQUEST_CODE = 999;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    final SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
    FirebaseCrash.logcat(Log.INFO, TAG, "First time login ");

    if (GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(this) != ConnectionResult.SUCCESS) {
      GoogleApiAvailability.getInstance().makeGooglePlayServicesAvailable(this);
      // TODO: return error.
      FirebaseCrash.logcat(Log.ERROR, TAG, "No Google play service");
      return;
    }

    if (Permissions.ifAllPermissionGranted(this)) {
      startActivity();
    }

    Permissions.grantRuntimePermissions(this, PERMISSION_REQUEST_CODE);

    setContentView(R.layout.splash);
    Button button = (Button) findViewById(R.id.startBtn);
    button.setOnClickListener(
        new View.OnClickListener() {
          @Override
          public void onClick(View view) {
            startActivity();
          }
        });
  }

  private void startActivity() {
    SignInActivity.startActivity(this);
    this.finish();
  }
}
