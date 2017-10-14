package com.cwave.calculation.service;

import com.cwave.calculation.console.Grade;

/** Interface for question generator. */
public interface QuestionGenerator {

  Question generate(Grade grade);
}
