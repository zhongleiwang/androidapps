package com.cwave.exchange.splash;

import android.content.SharedPreferences;
import android.os.Build.VERSION_CODES;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import com.cwave.exchange.R;
import com.cwave.exchange.signin.SignInActivity;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;

/** Shows welcome screen at first time use. */
public class SplashActivity extends AppCompatActivity {

  private static final String PREF_FIRST_TIME_USE = "perf_first_time_use";

  private static final int PERMISSION_REQUEST_CODE = 999;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    final SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);

    if (!sharedPreferences.getBoolean(PREF_FIRST_TIME_USE, true)) {
      startActivity();
      return;
    }
    if (GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(this) != ConnectionResult.SUCCESS) {
      GoogleApiAvailability.getInstance().makeGooglePlayServicesAvailable(this);
      return;
    }
    setContentView(R.layout.splash);
    Button button = (Button) findViewById(R.id.startBtn);
    button.setOnClickListener(
        new View.OnClickListener() {
          @Override
          public void onClick(View view) {
            sharedPreferences.edit().putBoolean(PREF_FIRST_TIME_USE, false).apply();
            startActivity();
          }
        });
    Permissions.grantRuntimePermissions(this, PERMISSION_REQUEST_CODE);
  }

  private void startActivity() {
    SignInActivity.startActivity(this);
    this.finish();
  }
}
