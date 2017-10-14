package com.cwave.exchange.dagger;

import android.app.Activity;
import android.app.Application;
import java.net.CookieHandler;
import java.net.CookieManager;

import javax.inject.Inject;

import dagger.android.AndroidInjector;
import dagger.android.DispatchingAndroidInjector;
import dagger.android.HasActivityInjector;

/** Main entry point for the app. */
public final class ExchangeApplication extends Application implements HasActivityInjector {

  @Inject
  DispatchingAndroidInjector<Activity> dispatchingAndroidInjector;

  @Override
  public void onCreate() {
    super.onCreate();
    CookieHandler.setDefault(new CookieManager());

    DaggerExchangeComponent.builder()
        .exchangeModule(new ExchangeModule(this))
        .build()
        .inject(this);
  }

  @Override
  public AndroidInjector<Activity> activityInjector() {
    return dispatchingAndroidInjector;
  }
}
