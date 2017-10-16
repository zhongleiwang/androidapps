package com.cwave.firebase;

import android.app.Activity;

import com.google.firebase.auth.FirebaseUser;

public interface Auth {
  void sign(Activity activity, int code);
  FirebaseUser getCurrentUser();
  boolean isSignedIn();
}
