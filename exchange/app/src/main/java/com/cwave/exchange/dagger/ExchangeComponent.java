package com.cwave.exchange.dagger;

import com.cwave.exchange.trading.TradingActivity;
import com.cwave.exchange.trading.TradingActivityModule;

import javax.inject.Singleton;

import dagger.Component;
import dagger.android.AndroidInjectionModule;

@Singleton
@Component(
    modules = {
        AndroidInjectionModule.class,
        TradingActivityModule.class,
        ExchangeModule.class})
interface ExchangeComponent {
  void inject(ExchangeApplication application);
}
