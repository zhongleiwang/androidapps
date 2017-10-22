package com.cwave.exchange.post;

import com.google.firebase.firestore.ServerTimestamp;

import java.util.Calendar;
import java.util.Date;
/**
 * Presents a post.
 *
 *<p>Define this POJO, rather using protobuf and auto value.
 * Firebase doesn't support protobuf.
 * autovalue might be a good option, but seems Firebase might not have good support.
 * Just go with POJO.
 */
public class PostMessage {
  private String name;
  private String uid;
  private Date date;
  private String from; // from currency
  private String to; // to currency
  private float rate;
  private float fromAmount;
  private float toAmount;
  private boolean multiple;

  public PostMessage() {
    name = "";
    uid = "";
    date = Calendar.getInstance().getTime();
    from = "";
    to = "";
    rate = 0.0f;
    fromAmount = 0.0f;
    toAmount = 0.0f;
    multiple = false;
  }

  public PostMessage(String name,
              String uid,
              Date date,
              String from,
              String to,
              float rate,
              float fromAmount,
              float toAmount,
              boolean multiple) {
    this.name = name;
    this.uid = uid;
    this.date = date;
    this.from = from;
    this.to = to;
    this.rate = rate;
    this.fromAmount = fromAmount;
    this.toAmount = toAmount;
    this.multiple = multiple;
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

  public String getFrom() {
    return from;
  }

  public String getTo() {
    return to;
  }

  public float getRate() {
    return rate;
  }

  public float getFromAmount() {
    return fromAmount;
  }

  public float getToAmount() {
    return toAmount;
  }

  public boolean isMultiple() {
    return multiple;
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

  public void setFrom(String from) {
    this.from = from;
  }

  public void setTo(String to) {
    this.to = to;
  }

  public void setRate(float rate) {
    this.rate = rate;
  }

  public void getFromAmount(float fromAmount) {
      this.fromAmount = fromAmount;
  }

  public void setToAmount(float toAmount) {
    this.toAmount = toAmount;
  }

  public void isMultiple(boolean multiple) {
    this.multiple = multiple;
  }

  public static Builder builder() {
    return new Builder();
  }
  
  public static class Builder {
    private String name;
    private String uid;
    private Date date;
    private String from; // from currency
    private String to; // to currency
    private float rate;
    private float fromAmount;
    private float toAmount;
    private boolean multiple;
    
    public PostMessage build() {
      return new PostMessage(name,
                             uid,
                             date,
                             from,
                             to,
                             rate,
                             fromAmount,
                             toAmount,
                             multiple);
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
    
    public Builder setFrom(String from) {
      this.from = from;
      return this;
    }
    
    public Builder setTo(String to) {
      this.to = to;
      return this;
    }
    
    public Builder setRate(float rate) {
      this.rate = rate;
      return this;
    }
    
    public Builder getFromAmount(float fromAmount) {
      this.fromAmount = fromAmount;
      return this;
    }
    
    public Builder setToAmount(float toAmount) {
      this.toAmount = toAmount;
      return this;
    }
    
    public Builder isMultiple(boolean multiple) {
      this.multiple = multiple;
      return this;
    }
  }
}
