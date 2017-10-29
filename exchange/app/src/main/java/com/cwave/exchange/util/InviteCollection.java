package com.cwave.exchange.util;

import com.cwave.exchange.invite.InviteMessage;
import com.cwave.exchange.post.PostMessage;
import com.cwave.exchange.trading.CollectionName;

public class InviteCollection {
  private static final String SEPERATOR = "/";
  
  public static String getPath(InviteMessage inviteMessage) {
    PostMessage postMessage = inviteMessage.getPost();
    return CollectionName.INVITES + SEPERATOR
        + postMessage.getId() + SEPERATOR
        + postMessage.getUid();
  }
}
