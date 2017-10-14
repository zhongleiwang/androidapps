package com.cwave.calculation.console;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import com.cwave.calculation.R;

/** Shows welcome screen at first time use. */
public class SplashActivity extends AppCompatActivity {

  private static final String PREF_FIRST_TIME_USE = "perf_first_time_use";

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    final SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);

    if (!sharedPreferences.getBoolean(PREF_FIRST_TIME_USE, true)) {
      startActivity();
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
  }

  private void startActivity() {
    ConsoleActivity.startActivity(this);
    this.finish();
  }
}
