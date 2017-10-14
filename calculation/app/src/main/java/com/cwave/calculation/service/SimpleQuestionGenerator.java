package com.cwave.calculation.service;

import android.util.Log;
import com.cwave.calculation.console.Grade;
import java.util.Date;
import java.util.Random;

/** Implementation of {@link QuestionStore}. */
public class SimpleQuestionGenerator implements QuestionGenerator {

  private static final String TAG = "SQG";
  private static int FIX_LEVEL = 3;

  Random random = new Random();

  public SimpleQuestionGenerator() {
    Date date = new Date();
    random.setSeed(date.getTime());
  }

  @Override
  public Question generate(Grade grade) {
    int a;
    int b;
    int num = 0;

    if (grade == Grade.preK || grade == Grade.kinder) {
      num = 0;
    }
    if (grade == Grade.first_grade || grade == Grade.second_grade || grade == Grade.third_grade) {
      num = 2;
    }

    int operation = getRandomNumber(1, num + FIX_LEVEL); // prek = 0

    Log.d(TAG, "operation: " + operation);
    int upperA = 0;
    int upperB = 0;
    if (operation == 1) {
      if (grade == Grade.preK || grade == Grade.kinder) {
        upperA = 9;
        upperB = 9;
      }
      if (grade == Grade.first_grade || grade == Grade.second_grade || grade == Grade.third_grade) {
        upperA = 19;
        upperB = 19;
      }
      a = getRandomNumber(0, upperA);
      b = getRandomNumber(1, upperB);
      int sum = a + b;
      return Question.builder().setQuestion(String.format("%d + %d ", a, b)).setAnswer(sum).build();

    } else if (operation == 2) {
      if (grade == Grade.preK || grade == Grade.kinder) {
        upperA = 9;
        upperB = 9;
      }
      if (grade == Grade.first_grade || grade == Grade.second_grade || grade == Grade.third_grade) {
        upperA = 19;
        upperB = 19;
      }
      a = getRandomNumber(0, upperA);
      b = getRandomNumber(1, upperB);
      int sum = a + b;
      return Question.builder().setQuestion(String.format("%d - %d ", sum, b)).setAnswer(a).build();
    } else if (operation == 3) {
      if (grade == Grade.preK || grade == Grade.kinder) {
        upperA = 9;
        upperB = 9;
      }
      if (grade == Grade.first_grade || grade == Grade.second_grade) {
        upperA = 19;
        upperB = 19;
      }
      if (grade == Grade.third_grade) {
        upperA = 39;
        upperB = 39;
      }

      a = getRandomNumber(0, upperA);
      b = getRandomNumber(1, upperB);
      int result = a * b;
      return Question.builder()
          .setQuestion(String.format("%d X %d ", a, b))
          .setAnswer(result)
          .build();
    } else if (operation == 4) {
      if (grade == Grade.preK || grade == Grade.kinder) {
        upperA = 9;
        upperB = 9;
      }
      if (grade == Grade.first_grade || grade == Grade.second_grade) {
        upperA = 19;
        upperB = 19;
      }
      if (grade == Grade.third_grade) {
        upperA = 39;
        upperB = 39;
      }
      a = getRandomNumber(1, upperA);
      b = getRandomNumber(1, upperB);
      int result = a * b;
      return Question.builder()
          .setQuestion(String.format("%d / %d ", result, b))
          .setAnswer(a)
          .build();
    }
    return null;
  }

  private int getRandomNumber(int from, int to) {
    return random.nextInt(to - from) + from;
  }

  private Question buildQuestion(String question, int a, int b, long result, long answer) {
    return Question.builder()
        .setQuestion(String.format("%d / %d ", result, b))
        .setAnswer(answer)
        .setIsCorrect(false)
        .setTime(0)
        .build();
  }
}
