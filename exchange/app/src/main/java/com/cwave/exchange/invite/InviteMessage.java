package com.cwave.exchange.invite;

import android.os.Parcel;
import android.os.Parcelable;

import com.cwave.exchange.post.PostMessage;
import com.google.firebase.firestore.ServerTimestamp;

import java.util.Calendar;
import java.util.Date;
/**
 * Presents a post.
 *
 *<p>Define this POJO, rather using protobuf or auto value.
 */
public class InviteMessage implements Parcelable {
  private String name;
  private String uid;
  private PostMessage post;

  public InviteMessage() {}

  public InviteMessage(String name, String uid, PostMessage post) {
    this.name = name;
    this.uid = uid;
    this.post = post;
  }

  public String getName() {return name; }

  public String getUid() {return uid; }

  public PostMessage getPost() {
    return post;
  }

  public void setName(String name) { this.name = name; }

  public void setId(String uid) { this.uid = uid; }

  public void setPost(PostMessage post) {
    this.post = post;
  }

  public static Builder builder() {
    return new Builder();
  }
  
  public static class Builder {
    private String name;
    private String uid;
    private PostMessage post;
    
    public InviteMessage build() {
      return new InviteMessage(name, uid, post);
    }

    public Builder setName(String name) {
      this.name = name;
      return this;
    }

    public Builder setId(String uid) {
      this.uid = uid;
      return this;
    }

    public Builder setPost(PostMessage post) {
      this.post = post;
      return this;
    }
  }

  public int describeContents() {
    return 0;
  }

  @Override
  public void writeToParcel(Parcel out, int flags) {
    out.writeString(name);
    out.writeString(uid);
    out.writeParcelable(post, flags);
  }

  public static final Parcelable.Creator<InviteMessage> CREATOR
      = new Parcelable.Creator<InviteMessage>() {
    public InviteMessage createFromParcel(Parcel in) {
      return new InviteMessage(in);
    }

    public InviteMessage[] newArray(int size) {
      return new InviteMessage[size];
    }
  };

  private InviteMessage(Parcel in) {
    name = in.readString();
    uid = in.readString();
    post = in.readParcelable(PostMessage.class.getClassLoader());
  }
}
