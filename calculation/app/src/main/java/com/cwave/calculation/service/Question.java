package com.cwave.calculation.service;

import com.google.auto.value.AutoValue;

/** Represents a question. */
@AutoValue
public abstract class Question {

  public abstract String getQuestion();

  public abstract long getAnswer();

  public abstract long getTime();

  public abstract boolean getIsCorrect();

  Question() {}

  public static Builder builder() {
    // Set up default value
    return new AutoValue_Question.Builder()
        .setQuestion("")
        .setAnswer(0)
        .setTime(0)
        .setIsCorrect(false);
  }

  /** Builder for {@code Question} instances. */
  @AutoValue.Builder
  public abstract static class Builder {
    public abstract Builder setQuestion(String question);

    public abstract Builder setAnswer(long answer);

    public abstract Builder setTime(long time);

    public abstract Builder setIsCorrect(boolean isCorrect);

    public abstract Question build();

    Builder() {}
  }
}
