package com.cwave.exchange.post;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

@Module
public abstract class PostFragmentModule {
    @ContributesAndroidInjector
    abstract PostFragment contributeSignInActivityInjector();
}
