package com.cwave.exchange.util;

import com.cwave.exchange.invite.InviteMessage;
import com.cwave.exchange.post.PostMessage;
import com.cwave.exchange.trading.CollectionName;

public class ChatRoom {
  private static final String SEPERATOR = "/";
  private static final String POST_ID = "post_id";
  private static final String POST_UID = "post_uid";
  private static final String UID = "uid";

  // where write an {@link InviteMessage} as the meta data. */
  public synchronized static String getMetaPath(InviteMessage inviteMessage) {
    PostMessage postMessage = inviteMessage.getPost();
    return CollectionName.CHATS + SEPERATOR
        + POST_ID + SEPERATOR
        + postMessage.getId() + SEPERATOR
        + POST_UID + SEPERATOR
        + postMessage.getUid();
  }

  public synchronized static String getRoomPath(InviteMessage inviteMessage) {
    return getMetaPath(inviteMessage) + SEPERATOR
        + UID + SEPERATOR
        + inviteMessage.getUid();
  }
}
