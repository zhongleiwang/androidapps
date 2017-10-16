package com.cwave.exchange.signin;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

@Module
public abstract class SignInActivityModule {
  @ContributesAndroidInjector
  abstract SignInActivity contributeSignInActivityInjector();
}
