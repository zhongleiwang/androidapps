package com.cwave.exchange.invite;

import com.cwave.exchange.post.PostMessage;
import com.google.firebase.firestore.ServerTimestamp;

import java.util.Calendar;
import java.util.Date;
/**
 * Presents a post.
 *
 *<p>Define this POJO, rather using protobuf or auto value.
 */
public class InviteMessage {
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
}
