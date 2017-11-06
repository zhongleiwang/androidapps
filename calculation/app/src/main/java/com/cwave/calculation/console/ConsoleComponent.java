package com.cwave.calculation.console;

/** Dagger {@link dagger.Component} for console related classes. */
public interface ConsoleComponent {

  void inject(ConsoleActivity target);

  void inject(QuestionFragment target);

  void inject(RecordFragment target);
}
