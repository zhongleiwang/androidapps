package com.cwave.calculation.console;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.util.Log;

/** {@link FragmentPagerAdapter} to display debug information. */
public class ConsolePagerAdapter extends FragmentPagerAdapter {
  private static final String TAG = ConsolePagerAdapter.class.getSimpleName();

  private enum PagerType {
    QUESTION(0),
    RECORD(1);

    private final int value;

    PagerType(final int newValue) {
      value = newValue;
    }

    public int getValue() {
      return value;
    }
  }

  public ConsolePagerAdapter(FragmentManager fm) {
    super(fm);
  }

  @Override
  public int getCount() {
    return PagerType.values().length;
  }

  @Override
  public Fragment getItem(int position) {
    Log.d(TAG, "fragment: " + position);

    if (position == PagerType.QUESTION.getValue()) {
      return new QuestionFragment();
    }

    return new LogFragment();
  }

  @Override
  public CharSequence getPageTitle(int position) {
    Log.d(TAG, "getPageTitle " + position);
    if (position == PagerType.QUESTION.getValue()) {
      return PagerType.QUESTION.toString();
    }

    return PagerType.RECORD.toString();
  }
}
