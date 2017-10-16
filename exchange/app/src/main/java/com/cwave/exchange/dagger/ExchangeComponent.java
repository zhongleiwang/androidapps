package com.cwave.exchange.dagger;

import com.cwave.exchange.signin.SignInActivityModule;
import com.cwave.exchange.trading.TradingActivityModule;

import javax.inject.Singleton;

import dagger.Component;
import dagger.android.AndroidInjectionModule;

@Singleton
@Component(
    modules = {
        AndroidInjectionModule.class,
        SignInActivityModule.class,
        TradingActivityModule.class,
        ExchangeModule.class})
interface ExchangeComponent {
  void inject(ExchangeApplication application);
}
