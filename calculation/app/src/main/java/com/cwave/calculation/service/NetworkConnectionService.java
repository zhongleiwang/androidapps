package com.cwave.calculation.service;

/** Interface for checking network connection. */
public interface NetworkConnectionService {

  /** Returns whether or not there are any connected networks. */
  boolean isConnected();
}
