package com.cwave.calculation.service;

import android.os.Build.VERSION_CODES;
import android.support.annotation.RequiresApi;
import android.util.Log;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/** Implementation of {@link QuestionStore} based on SQLite database. */
public class InMemoryQuestionStoreImpl implements QuestionStore {

  private static final String TAG = "InMQSImpl";

  private List<Question> questions = new ArrayList<>();

  public InMemoryQuestionStoreImpl() {}

  private static final Comparator<Question> QUESTION_COMPARATOR =
      new Comparator<Question>() {
        @RequiresApi(api = VERSION_CODES.KITKAT)
        @Override
        public int compare(Question l, Question r) {
          final int BEFORE = -1;
          final int AFTER = 1;

          if (!l.getIsCorrect() && !r.getIsCorrect()) {
            return Long.compare(r.getTime(), l.getTime());
          } else if (!l.getIsCorrect()) {
            return BEFORE;
          } else if (!r.getIsCorrect()) {
            return AFTER;
          }
          return Long.compare(r.getTime(), l.getTime());
        }
      };

  @Override
  public List<Question> get() {
    Log.d(TAG, "get size: " + questions.size());
    return questions;
  }

  @Override
  public int getWrongAnswers() {
    int num = 0;
    for (Question question : questions) {
      if (!question.getIsCorrect()) {
        num++;
      }
    }
    return num;
  }

  @Override
  public int getNumberOfQuestions() {
    return questions.size();
  }

  @Override
  public void add(String question, long time, boolean correct) {
    Log.d(TAG, "add " + question);
    questions.add(new Question(question, 0, time, correct));
    Collections.sort(questions, QUESTION_COMPARATOR);

    display();
  }

  @Override
  public void cleanUp() {
    Log.d(TAG, "cleanup");
    questions.clear();
  }

  private void display() {
    Log.d(TAG, "display size: " + questions.size());
    for (Question q : questions) {
      Log.d(TAG, "Display " + q.getIsCorrect() + " " + q.getQuestion() + " " + q.getTime());
    }
  }
}
