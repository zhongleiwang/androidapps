package com.cwave.calculation.console;

public enum Grade {
  preK(0),
  kinder(1),
  first_grade(2),
  second_grade(3),
  third_grade(4);

  private final int grade;

  Grade(int g) {
    grade = g;
  }

  public int getGrade() {
    return grade;
  }
}
