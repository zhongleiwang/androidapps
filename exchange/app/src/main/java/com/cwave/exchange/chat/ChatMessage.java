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
  private String name;
  private String uid;
  private Date date;
  private String message;

  public ChatMessage() {
    name = "";
    uid = "";
    date = Calendar.getInstance().getTime();
    message = "";
  }

  public ChatMessage(String name,
              String uid,
              Date date,
              String message) {
    this.name = name;
    this.uid = uid;
    this.date = date;
    this.message = message;
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

  public void setName(String name) {
    this.name = name;
  }

  public void setDate(Date date) {
      this.date = date;
  }

  public void setDate() {
    date = Calendar.getInstance().getTime();
  }
     
  public void setUid(String uid) {
    this.uid = uid;
  }

  public void setMessage(String message) {
    this.message = message;
  }

  public static Builder builder() {
    return new Builder();
  }
  
  public static class Builder {
    private String name;
    private String uid;
    private Date date;
    private String message;
    
    public ChatMessage build() {
      return new ChatMessage(name,
                             uid,
                             date,
                             message);
    }
    
    public Builder setName(String name) {
      this.name = name;
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
    
    public Builder setUid(String uid) {
      this.uid = uid;
      return this;
    }
    
    public Builder setMessage(String message) {
      this.message = message;
      return this;
    }
  }
}
