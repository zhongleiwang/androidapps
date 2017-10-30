package com.cwave.exchange.post;

import android.os.Parcel;
import android.os.Parcelable;

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
public class PostMessage implements Parcelable {
  private String id; // Post unique ID.
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

  public PostMessage(String id,
                     String name,
                     String uid,
                     Date date,
                     String from,
                     String to,
                     float rate,
                     float fromAmount,
                     float toAmount,
                     boolean multiple) {
    this.id = id;
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

  public String getId() {return id; }

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

  public void setId(String id) { this.id = id; }

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

  @Override
  public int describeContents() {
    return 0;
  }

  @Override
  public void writeToParcel(Parcel dest, int flags) {
    dest.writeString(id);
    dest.writeString(name);
    dest.writeString(uid);
    dest.writeSerializable(date);
    dest.writeString(from);
    dest.writeString(to);
    dest.writeFloat(rate);
    dest.writeFloat(fromAmount);
    dest.writeFloat(toAmount);
    dest.writeByte((byte)(multiple ? 1 : 0));
  }

  public static final Parcelable.Creator<PostMessage> CREATOR
      = new Parcelable.Creator<PostMessage>() {
    public PostMessage createFromParcel(Parcel in) {
      return new PostMessage(in);
    }

    public PostMessage[] newArray(int size) {
      return new PostMessage[size];
    }
  };

  private PostMessage(Parcel in) {
    id = in.readString();
    name = in.readString();
    uid = in.readString();
    date = (Date)in.readSerializable();
    from = in.readString();
    to = in.readString();
    rate =  in.readFloat();
    fromAmount = in.readFloat();
    toAmount = in.readFloat();
    multiple = (in.readByte() == 1) ? true : false;
  }

  public static class Builder {
    private String id;
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
      return new PostMessage(id,
          name,
          uid,
          date,
          from,
          to,
          rate,
          fromAmount,
          toAmount,
          multiple);
    }

    public Builder setId(String id) {
      this.id = id;
      return this;
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
