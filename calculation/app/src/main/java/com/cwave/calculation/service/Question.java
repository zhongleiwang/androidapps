package com.cwave.calculation.service;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.auto.value.AutoValue;

/** Represents a question. */
public class Question implements Parcelable {

  private String question;
  private long answer;
  private long time;
  private boolean isCorrect;
  
  Question() {}
  
  Question(String question,
           long answer,
           long time,
           boolean isCorrect) {
    this.question = question;
    this.answer = answer;
    this.time = time;
    this.isCorrect = isCorrect;
  }

  public String getQuestion() {
    return question;
  }     

  public long getAnswer() {
    return answer;
  }

  public long getTime() {
    return time;
  }

  public boolean getIsCorrect() {
    return isCorrect;
  }

  public void setQuestion(String question) {
    this.question = question;
  }

  public void setAnswer(long answer) {
    this.answer = answer;
  }

  public void setTime(long time) {
    this.time = time;
  }

  public void setIsCorrect(boolean isCorrect) {
    this.isCorrect = isCorrect;
  }

  @Override
  public int describeContents() {
    return 0;
  }

  @Override
  public void writeToParcel(Parcel dest, int flags) {

  }

  public static Builder builder() {
    return new Builder();
  }

  public static class Builder {
    private String question;
    private long anawer;
    private long time;
    private boolean isCorrect;
    
    public Question build() {
      return new Question(question,
          anawer,
          time,
          isCorrect);
    }

    public Builder setQuestion(String question) {
      this.question = question;
      return this;
    }

    public Builder setAnswer(long answer) {
      this.anawer = answer;
      return this;
    }
    
    public Builder setTime(long time) {
      this.time = time;
      return this;
    }

    public Builder setIsCorrect(boolean isCorrect) {
      this.isCorrect = isCorrect;
      return this;
    }
  }
}
