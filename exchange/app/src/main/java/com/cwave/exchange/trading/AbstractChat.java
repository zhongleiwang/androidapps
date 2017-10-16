package com.cwave.exchange.trading;

/**
 * Common interface for chat messages, helps share code between RTDB and Firestore examples.
 */
public abstract class AbstractChat {

  public abstract String getName();

  public abstract String getUid();

}
