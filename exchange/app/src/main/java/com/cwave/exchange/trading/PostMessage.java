package com.cwave.exchange.trading;

import com.google.firebase.firestore.ServerTimestamp;

import java.util.Date;

public class PostMessage extends AbstractChat {

  private String name;
  private String uid;
  private Date timestamp;
  private String from; // from currency
  private String to; // to currency
  private float rate;

  public PostMessage() {} // Needed for Firebase

  public PostMessage(String name, String uid, Date timestamp,
                     String from, String to, float rate) {
    this.name = name;
    this.uid = uid;
    this.timestamp = timestamp;
    this.from = from;
    this.to = to;
    this.rate = rate;
  }

  @Override
  public String getName() {
    return name;
  }

  @Override
  public String getUid() { return uid; }

  public Date getDate() {
    return timestamp;
  }

  public String getFrom() {
    return from;
  }

  public String getTo() {
    return to;
  }

  public float getRate() {
    return rate;
  }

  public void setName(String name) { this.name = name; }

  public void setUid(String uid) { this.uid = uid; }

  public void setDate(Date timestamp) {
    this.timestamp = timestamp;
  }

  public void setFrom(String from) {
    this.from = from;
  }

  public void setTo(String to) {
    this.to = to;
  }

  public void setRate(float rate) {
    this.rate = rate;
  }

  @ServerTimestamp
  public Date getTimestamp() { return timestamp; }

  public void setTimestamp(Date timestamp) { this.timestamp = timestamp; }
}
