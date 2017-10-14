package com.cwave.exchange.trading;

import android.app.Activity;

import dagger.Binds;
import dagger.Module;
import dagger.android.ActivityKey;
import dagger.android.AndroidInjector;
import dagger.multibindings.IntoMap;

@Module(subcomponents = TradingActivitySubcomponent.class)
public abstract class TradingActivityModule {
    @Binds
    @IntoMap
    @ActivityKey(TradingActivity.class)
    abstract AndroidInjector.Factory<? extends Activity>
        bindYourActivityInjectorFactory(TradingActivitySubcomponent.Builder builder);
}
