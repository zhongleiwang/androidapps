package com.cwave.exchange.chat;

import com.google.firebase.firestore.ServerTimestamp;

import java.util.Calendar;
import java.util.Date;
/**
 * Presents a chat message.
 *
 *<p>Define this POJO, rather using protobuf and auto value.
 * Firebase doesn't support protobuf.
 * autovalue might be a good option, but seems Firebase might not have good support.
 * Just go with POJO.
 */
public class ChatMessage {
  private String postName;
  private String postUid;
  private String name;
  private String uid;
  private Date date;
  private String message;

  public ChatMessage() {}

  public ChatMessage(String postName,
                     String postUid,
                     String name,
                     String uid,
                     Date date,
                     String message) {
    this.postName = postName;
    this.postUid = postUid;
    this.name = name;
    this.uid = uid;
    this.date = date;
    this.message = message;
  }

  public String getPostName() {
    return postName;
  }

  public String getPostUid() {
    return postUid;
  }

  public String getName() {
    return name;
  }

  public String getUid() {
    return uid;
  }

  @ServerTimestamp
  public Date getDate() {
    return date;
  }

  public String getMessage() {
    return message;
  }

  public void setPostName(String postName) {
    this.postName = postName;
  }

  public void setPostUid(String postUid) {
    this.postUid = postUid;
  }

  public void setName(String name) {
    this.name = name;
  }

  public void setUid(String uid) {
    this.uid = uid;
  }

  public void setDate(Date date) {
      this.date = date;
  }

  public void setDate() {
    date = Calendar.getInstance().getTime();
  }
     
  public void setMessage(String message) {
    this.message = message;
  }

  public static Builder builder() {
    return new Builder();
  }
  
  public static class Builder {
    private String postName;
    private String postUid;
    private String name;
    private String uid;
    private Date date;
    private String message;
    
    public ChatMessage build() {
      return new ChatMessage(postName,
          postUid,
          name,
          uid,
          date,
          message);
    }

    public Builder setPostName(String name) {
      this.postName = name;
      return this;
    }

    public Builder setPostUid(String uid) {
      this.postUid = uid;
      return this;
    }

    public Builder setName(String name) {
      this.name = name;
      return this;
    }

    public Builder setUid(String uid) {
      this.uid = uid;
      return this;
    }

    public Builder setDate(Date date) {
      this.date = date;
      return this;
    }

    public Builder setDate() {
      this.date = Calendar.getInstance().getTime();
      return this;
    }
    
    public Builder setMessage(String message) {
      this.message = message;
      return this;
    }
  }
}
