package com.cwave.calculation.service;

import static com.google.common.base.Preconditions.checkNotNull;

import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/** Implementation of {@link NetworkConnectionService}. */
public final class NetworkConnectionServiceImpl implements NetworkConnectionService {
  private final ConnectivityManager connectivityManager;

  public NetworkConnectionServiceImpl(ConnectivityManager connectivityManager) {
    this.connectivityManager = checkNotNull(connectivityManager);
  }

  @Override
  public boolean isConnected() {
    NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
    return activeNetworkInfo != null && activeNetworkInfo.isConnected();
  }
}
