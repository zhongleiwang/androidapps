package com.cwave.firebase;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity implements
    FirebaseAuth.AuthStateListener {

  private static final String TAG = "MainActivity";

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onDestroy();
  }

  @Override
  public void onDestroy() {
    super.onDestroy();
  }

  @Override
  public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {

  }
}
