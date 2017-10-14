package com.cwave.calculation.service;

import java.util.List;

/** Interface for question store. */
public interface QuestionStore {

  /** Lists questions. */
  List<Question> get();

  /** Add a new question. */
  void add(String question, long time, boolean correct);

  /** Cleans up the list. */
  void cleanUp();
}
