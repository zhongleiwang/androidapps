package com.cwave.firebase;

import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class DatabaseImpl implements Database {
  private static final String TAG = "DatabaseImpl";

  public void write() {
    Log.d(TAG, "firebase");
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference databaseReference = database.getReference("msg-test");
    databaseReference.setValue("Hello World ???");

    databaseReference.addValueEventListener(new ValueEventListener() {
        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
          Log.d(TAG, "onDataChange");
          Object object = dataSnapshot.getValue();
          if (object instanceof String){
            String value = dataSnapshot.getValue(String.class);
            Log.d(TAG, "Value is: " + value);
          }
        }

        @Override
        public void onCancelled(DatabaseError databaseError) {
          Log.w(TAG, "Failed to read value.", databaseError.toException());
        }
      });
  }

  public void read() {
    Log.d(TAG, "store user");
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference databaseReference = database.getReference("users");
    /*
      databaseReference.setValue("users id!");
    User user = User.newBuilder()
        .setId("123456")
        .setEmail("a@com")
        .setName("name")
        .build();

    databaseReference.child("users").child(user.getId()).setValue(user.getName());
    */
    Log.d(TAG, "store user end");
  }
}
