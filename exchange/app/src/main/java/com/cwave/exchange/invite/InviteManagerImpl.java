package com.cwave.exchange.invite;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.util.Log;

import com.cwave.exchange.post.PostMessage;
import com.cwave.exchange.trading.CollectionName;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.Map;

public class InviteManagerImpl implements InviteManager {

  private static final String TAG = "InviteManager";

  private static final String SEPERATOR = "/";
  private static final String POST_ID = "post_id";
  private static final String POST_UID = "post_uid";
  private static final String UID = "uid";

  public InviteManagerImpl() {}

  @Override
  public void writeInvite(InviteMessage inviteMessage) {
    Log.d(TAG, "writeInvite " + inviteMessage.getPost().getId() + " : " + inviteMessage.getPost().getUid());

    FirebaseFirestore.getInstance()
        .collection(getInvitePath(inviteMessage))
        .document(inviteMessage.getUid())
        .set(inviteMessage)
        .addOnSuccessListener(new OnSuccessListener<Void>() {
          @Override
          public void onSuccess(Void aVoid) {

          }
        })
        .addOnFailureListener(new OnFailureListener() {
          @Override
          public void onFailure(@NonNull Exception e) {
            Log.e(TAG, "Write invite failed " + e);
          }
        });
  }

  @Override
  public void deleteInvite(InviteMessage inviteMessage) {
    Log.d(TAG, "writeInvite " + inviteMessage.getPost().getId() + " : " + inviteMessage.getPost().getUid());

    FirebaseFirestore.getInstance()
        .collection(getInvitePath(inviteMessage))
        .document(inviteMessage.getUid())
        .delete()
        .addOnSuccessListener(new OnSuccessListener<Void>() {
          @Override
          public void onSuccess(Void aVoid) {

          }
        })
        .addOnFailureListener(new OnFailureListener() {
          @Override
          public void onFailure(@NonNull Exception e) {
            Log.e(TAG, "Delete invite failed " + e);
          }
        });
  }

  @Override
  public void listenToInvite(final InviteMessage inviteMessage, final EventListener<QuerySnapshot> eventListener) {
    FirebaseFirestore.getInstance()
        .collection(getInvitePath(inviteMessage))
        .addSnapshotListener(eventListener);
  }

  private static String getInvitePath(InviteMessage inviteMessage) {
    PostMessage postMessage = inviteMessage.getPost();
    return CollectionName.INVITES + SEPERATOR
        + POST_ID + SEPERATOR
        + postMessage.getId() + SEPERATOR
        + POST_UID + SEPERATOR
        + postMessage.getUid();
  }
}
