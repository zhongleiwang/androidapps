package com.cwave.exchange.trading;

import java.time.Instant;
import java.util.Date;
/**
 * Presents a post.
 *
 *<p>Define this POJO, rather using protobuf and auto value.
 * Firebase doesn't support protobuf.
 * autovalue might be a good option, but seems Firebase might not have good support.
 * Just go with POJO.
 */
public class Post {
  private String name;
  private String uid;
  private Date date;
  private String from; // from currency
  private String to; // to currency
  private float rate;
  private float fromAmount;
  private float toAmount;
  private boolean multiple;

  public Post(String name,
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

    public Post build() {
      return new Post(name,
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
