package com.cwave.firebase;

import android.support.annotation.NonNull;
import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.Map;

public class StoreImpl implements Store {

  private static final String TAG = "StoreImpl";

  private final FirebaseFirestore db;

  public StoreImpl() {
    // Enable firestore logging
    FirebaseFirestore.setLoggingEnabled(true);

    // Access a Cloud Firestore instance from your Activity
    db = FirebaseFirestore.getInstance();
  }

  @Override
  public void write() {
    writeUser();
  }

  @Override
  public void write(String collection, Object object) {
    // Add a new document with a generated ID
    db.collection(collection)
        .add(object)
        .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
          @Override
          public void onSuccess(DocumentReference documentReference) {
            Log.d(TAG, "user DocumentSnapshot added with ID: " + documentReference.getId());
          }
        })
        .addOnFailureListener(new OnFailureListener() {
          @Override
          public void onFailure(@NonNull Exception e) {
            Log.w(TAG, "user Error adding document", e);
          }
        });
    db.collection("users")
        .get()
        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
          @Override
          public void onComplete(@NonNull Task<QuerySnapshot> task) {
            if (task.isSuccessful()) {
              for (DocumentSnapshot document : task.getResult()) {
                Log.d(TAG, document.getId() + " => " + document.getData());
              }
            } else {
              Log.w(TAG, "Error getting documents.", task.getException());
            }
          }
        });
  }

  private void writeUser() {
    // Create a new user with a first and last name
    Map<String, Object> user = new HashMap<>();
    user.put("first", "Tom");
    user.put("middle", "Peterson");
    user.put("last", "Cat");
    user.put("born", 1815);

    // Add a new document with a generated ID
    db.collection("users")
        .add(user)
        .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
          @Override
          public void onSuccess(DocumentReference documentReference) {
            Log.d(TAG, "user DocumentSnapshot added with ID: " + documentReference.getId());
          }
        })
        .addOnFailureListener(new OnFailureListener() {
          @Override
          public void onFailure(@NonNull Exception e) {
            Log.w(TAG, "user Error adding document", e);
          }
        });
    db.collection("users")
        .get()
        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
          @Override
          public void onComplete(@NonNull Task<QuerySnapshot> task) {
            if (task.isSuccessful()) {
              for (DocumentSnapshot document : task.getResult()) {
                Log.d(TAG, document.getId() + " => " + document.getData());
              }
            } else {
              Log.w(TAG, "Error getting documents.", task.getException());
            }
          }
        });
  }
}
