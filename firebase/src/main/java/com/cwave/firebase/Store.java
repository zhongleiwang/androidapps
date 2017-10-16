package com.cwave.firebase;

public interface Store {
  void write();

  void write(String collection, Object object);
}
