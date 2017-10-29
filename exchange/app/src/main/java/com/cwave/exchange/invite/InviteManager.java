package com.cwave.exchange.invite;

import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.QuerySnapshot;

public interface InviteManager {

  void writeInvite(InviteMessage inviteMessage);
  void deleteInvite(InviteMessage inviteMessage);
  void listenToInvite(InviteMessage inviteMessage, final EventListener<QuerySnapshot> eventListener);
}
