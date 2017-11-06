package com.cwave.calculation.service;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.firebase.firestore.ServerTimestamp;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;

/**
 * Presents an Exercise.
 */
public class Exercise implements Parcelable {
  private String id; // Exericese unique ID.
  private String name;
  private String uid;
  private Date date;
  private List<Question> questions;

  public Exercise() {}

  public Exercise(String id,
                  String name,
                  String uid,
                  Date date,
                  List<Question> questions) {
    this.id = id;
    this.name = name;
    this.uid = uid;
    this.date = date;
    this.questions = questions;
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

  public List<Question> getQuestions() {
    return questions;
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

  public void setQuestion(List<Question> questions) {
    this.questions = questions;
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
    dest.writeTypedList(questions);
  }

  public static final Parcelable.Creator<Exercise> CREATOR
      = new Parcelable.Creator<Exercise>() {
    public Exercise createFromParcel(Parcel in) {
      return new Exercise(in);
    }

    public Exercise[] newArray(int size) {
      return new Exercise[size];
    }
  };

  public Exercise(Parcel in) {
    id = in.readString();
    name = in.readString();
    uid = in.readString();
    date = (Date)in.readSerializable();
    ArrayList<Question> qs = new ArrayList<Question>();
    in.readTypedList(qs, null);
    Collections.copy(questions, qs);
  }

  public static class Builder {
    private String id;
    private String name;
    private String uid;
    private Date date;
    private List<Question> questions;
    
    public Exercise build() {
      return new Exercise(id,
          name,
          uid,
          date,
          questions);
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
    
    public Builder setQuestion(List<Question> questions) {
      this.questions = questions;
      return this;
    }
  }
}
