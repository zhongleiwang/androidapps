package com.cwave.exchange.trading;

import dagger.Subcomponent;
import dagger.android.AndroidInjector;

/** Dagger {@link dagger.Component} for trading related classes. */
@Subcomponent
public interface TradingActivitySubcomponent extends AndroidInjector<TradingActivity> {
  @Subcomponent.Builder
  public abstract class Builder extends AndroidInjector.Builder<TradingActivity> {
  }
}
