package com.cwave.calculation.console;

import android.content.Context;
import android.util.Log;
import com.google.common.collect.ImmutableList;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/** Implementation of {@link LogReader} */
public class DiagLogReader implements LogReader {

  private static final String TAG = DiagLogReader.class.getSimpleName();
  private static final String LOGCAT_CMD = "logcat -d";

  public DiagLogReader(Context context) {}

  @Override
  public List<String> read() {
    List<String> list = new ArrayList<>();
    try {
      Process process = Runtime.getRuntime().exec(LOGCAT_CMD);
      BufferedReader bufferedReader =
          new BufferedReader(new InputStreamReader(process.getInputStream()));
      String line;
      while ((line = bufferedReader.readLine()) != null) {
        list.add(line);
      }
      return list;
    } catch (IOException e) {
      Log.e(TAG, "Read logcat error " + e);
      return ImmutableList.of("Cannot read logcat.");
    }
  }
}
