package com.cwave.calculation.service;

import java.util.List;

/** Interface for question store. */
public interface QuestionStore {

  /** Lists questions. */
  List<Question> get();

  /** Gets number of questions. */
  int getNumberOfQuestions();

  /** Gets number of wrong answers. */
  int getWrongAnswers();

  /** Adds a new question. */
  void add(String question, long time, boolean correct);

  /** Cleans up the list. */
  void cleanUp();
}
