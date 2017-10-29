package com.cwave.exchange.util;

import java.util.UUID;

public class Uuid {
  public synchronized static String getUuid() {
    return UUID.randomUUID().toString();
  }
}
