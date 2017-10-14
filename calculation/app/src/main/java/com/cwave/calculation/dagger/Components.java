package com.cwave.calculation.dagger;

import android.content.Context;

/** Utility functions related to Dagger components. */
public final class Components {

  @SuppressWarnings("unchecked")
  public static Object fromApplication(Context context) {
    return context.getApplicationContext();
  }

  private Components() {}
}
