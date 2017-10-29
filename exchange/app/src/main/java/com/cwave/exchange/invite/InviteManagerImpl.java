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

  private Map<String, Object> meta = new HashMap<>();

  public InviteManagerImpl() {}

  @Override
  public void writeInvite(InviteMessage inviteMessage) {
    Log.d(TAG, "writeInvite " + inviteMessage.getPost().getId() + " : " + inviteMessage.getPost().getUid());

    FirebaseFirestore.getInstance()
        .collection(buildPath(inviteMessage))
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
        .collection(buildPath(inviteMessage))
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
    CollectionReference collectionReference = FirebaseFirestore.getInstance().collection(buildPath(inviteMessage));
    collectionReference.addSnapshotListener(eventListener);
  }

  private static String buildPath(InviteMessage inviteMessage) {
    PostMessage postMessage = inviteMessage.getPost();
    return CollectionName.INVITES + SEPERATOR
        + postMessage.getId() + SEPERATOR
        + postMessage.getUid();
  }
}
