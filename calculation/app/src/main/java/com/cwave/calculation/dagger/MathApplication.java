package com.cwave.calculation.dagger;

import android.app.Application;
import android.os.Build.VERSION_CODES;
import android.support.annotation.RequiresApi;
import android.support.multidex.MultiDexApplication;

import java.net.CookieHandler;
import java.net.CookieManager;

/** Main entry point for the app. */
public final class MathApplication extends MultiDexApplication {

  private MathComponent mathComponent;

  @RequiresApi(api = VERSION_CODES.GINGERBREAD)
  @Override
  public void onCreate() {
    super.onCreate();
    CookieHandler.setDefault(new CookieManager());

    mathComponent = DaggerMathComponent.builder().mathModule(new MathModule(this)).build();
  }

  public MathComponent getMathComponent() {
    return mathComponent;
  }
}
