package com.cwave.exchange.util;

public class ChatRoom {
  private static final String SEPERATOR = "/";

  public synchronized static String getPath(String postId, String postUid, String uid) {
    return postId + ":" + postUid + "-" + uid;
  }
}
