package com.cwave.calculation.console;

import java.util.List;

/** Interface for tracking user's meal history. */
public interface LogReader {

  /** Reads contexts from a logger. */
  List<String> read();
}
