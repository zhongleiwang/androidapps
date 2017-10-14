package com.cwave.calculation.util;

import android.os.Handler;
import android.os.Looper;

/** A helper class to handle thread-related operations. */
public final class ThreadUtils {

  private static final Thread uiThread = Looper.getMainLooper().getThread();

  public static final Handler uiThreadHandler = new Handler(Looper.getMainLooper());

  /**
   * Checks if the running thread is the UI thread.
   *
   * @return true if this is the UI thread otherwise false.
   */
  public static boolean isUiThread() {
    return Thread.currentThread().equals(uiThread);
  }

  /**
   * Runs the given {@link Runnable} on the UI thread: <br>
   * Run immediately if this is the UI thread, post it on the UI thread otherwise.
   */
  public static void runOnUiThread(Runnable r) {
    if (Looper.myLooper() == Looper.getMainLooper()) {
      r.run();
    } else {
      uiThreadHandler.post(r);
    }
  }

  /** Posts the given runnable on the UI thread. */
  public static void postOnUiThread(Runnable r) {
    uiThreadHandler.post(r);
  }
}
