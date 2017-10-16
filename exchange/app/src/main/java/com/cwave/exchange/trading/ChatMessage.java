package com.cwave.exchange.trading;

import com.google.firebase.firestore.ServerTimestamp;

import java.util.Date;

public class ChatMessage extends AbstractChat {

  private String name;
  private String message;
  private String uid;
  private Date timestamp;

  public ChatMessage() {} // Needed for Firebase

  public ChatMessage(String name, String message, String uid) {
    this.name = name;
    this.message = message;
    this.uid = uid;
  }

  public String getName() { return name; }

  public String getMessage() { return message; }

  public String getUid() { return uid; }

  public void setMessage(String message) { this.message = message; }
  
  public void setName(String name) { this.name = name; }

  public void setUid(String uid) { this.uid = uid; }

  @ServerTimestamp
  public Date getTimestamp() { return timestamp; }

  public void setTimestamp(Date timestamp) { this.timestamp = timestamp; }
}
