package com.cwave.exchange.chat;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

@Module
public abstract class ChatFragmentModule {
    @ContributesAndroidInjector
    abstract ChatFragment contributeSignInActivityInjector();
}
