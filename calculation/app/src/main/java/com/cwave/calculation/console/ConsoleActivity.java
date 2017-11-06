package com.cwave.calculation.console;

import static com.google.common.base.Preconditions.checkNotNull;

import android.app.AlertDialog;
import android.app.Fragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build.VERSION_CODES;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.design.widget.TabLayout;
import android.support.design.widget.TabLayout.Tab;
import android.support.design.widget.TabLayout.TabLayoutOnPageChangeListener;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import com.cwave.calculation.R;
import com.cwave.calculation.dagger.MathApplication;
import com.squareup.otto.Bus;
import javax.inject.Inject;

/** Class to display debug information. */
public class ConsoleActivity extends AppCompatActivity {
  private static final String TAG = ConsoleActivity.class.getSimpleName();

  private static final String USER_NAME = "user_name";
  private static final String YOUR_NAME = "Your name";

  @Inject Bus bus;

  private ConsolePagerAdapter consolePagerAdapter;
  private TabLayout tabLayout;
  private ActionBar actionBar;
  private ViewPager viewPager;

  private Grade grade = Grade.preK;

  /** Starts a new {@link ConsoleActivity} instance. */
  public static void startActivity(Context context) {
    checkNotNull(context);
    context.startActivity(new Intent(context, ConsoleActivity.class));
  }

  @RequiresApi(api = VERSION_CODES.GINGERBREAD)
  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    ((MathApplication) getApplication()).getMathComponent().inject(this);
    setContentView(R.layout.console_activity);

    Toolbar toolbar = (Toolbar) findViewById(R.id.console_toolbar);
    setSupportActionBar(toolbar);

    actionBar = checkNotNull(getSupportActionBar());
    // actionBar.setHomeAsUpIndicator();
    // actionBar.setDisplayHomeAsUpEnabled(true);
    String name = null;
    if (savedInstanceState == null) {
      name = YOUR_NAME;
    } else {
      name = savedInstanceState.getString(USER_NAME);
      if (name == null) {
        name = YOUR_NAME;
      }
    }
    actionBar.setTitle(name);
    actionBar.setSubtitle(grade.toString());

    // final int abTitleId = getResources().getIdentifier(name, "console_toolbar",
    // "com.cwave.calculation.console");
    final int abTitleId = getResources().getIdentifier("console_toolbar", "id", getPackageName());
    findViewById(abTitleId)
        .setOnClickListener(
            new View.OnClickListener() {
              @Override
              public void onClick(View v) {
                changeName(v);
              }
            });

    Log.d(TAG, "Create page adapter");
    consolePagerAdapter = new ConsolePagerAdapter(getSupportFragmentManager());
    viewPager = (ViewPager) findViewById(R.id.console_view_pager);
    viewPager.setAdapter(consolePagerAdapter);

    tabLayout = (TabLayout) findViewById(R.id.console_tab_layout);
    tabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);
    tabLayout.setupWithViewPager(viewPager);
    viewPager.addOnPageChangeListener(
        new TabLayoutOnPageChangeListener(tabLayout) {
          @Override
          public void onPageSelected(int position) {
            Log.d(TAG, "onPageSelected " + position);
            super.onPageSelected(position);
            updateTabDescriptions();
          }
        });
  }

  @Override
  protected void onResume() {
    super.onResume();
    bus.register(this);
  }

  @Override
  protected void onPause() {
    bus.unregister(this);
    super.onPause();
  }

  @Override
  protected void onSaveInstanceState(Bundle outState) {
    outState.putString(USER_NAME, actionBar.getTitle().toString());
    super.onSaveInstanceState(outState);
  }

  @Override
  public boolean onCreateOptionsMenu(android.view.Menu menu) {
    getMenuInflater().inflate(R.menu.levels, menu);
    return super.onCreateOptionsMenu(menu);
  }

  @RequiresApi(api = VERSION_CODES.GINGERBREAD)
  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    int id = item.getItemId();
    if (id == R.id.pre_k) {
      grade = Grade.preK;
      actionBar.setSubtitle(grade.toString());
      updateFragmentGrade();
      return true;
    } else if (id == R.id.kinder) {
      grade = Grade.kinder;
      actionBar.setSubtitle(grade.toString());
      updateFragmentGrade();
      return true;
    } else if (id == R.id.first_grade) {
      grade = Grade.first_grade;
      actionBar.setSubtitle(grade.toString());
      updateFragmentGrade();
      return true;
    } else if (id == R.id.second_grade) {
      grade = Grade.second_grade;
      actionBar.setSubtitle(grade.toString());
      updateFragmentGrade();
      return true;
    } else if (id == R.id.third_grade) {
      grade = Grade.third_grade;
      actionBar.setSubtitle(grade.toString());
      updateFragmentGrade();
      return true;
    } else {
      return super.onOptionsItemSelected(item);
    }
  }

  private void updateFragmentGrade() {
    QuestionFragment questionFragment = (QuestionFragment) getSupportFragmentManager().findFragmentById(R.id.fragement_questions);
    questionFragment.setTotalQuestions();
  }

  /** Sets the content descriptions of the tabs for accessibility. */
  @RequiresApi(api = VERSION_CODES.GINGERBREAD)
  private void updateTabDescriptions() {
    int tabCount = tabLayout.getTabCount();
    for (int i = 0; i < tabCount; i++) {
      Tab tab = tabLayout.getTabAt(i);
      if (tab == null || consolePagerAdapter.getCount() < tabCount) {
        break;
      }
      tab.setContentDescription(
          getString(
              tab.isSelected()
                  ? R.string.tab_content_description_selected
                  : R.string.tab_content_description_unselected,
              consolePagerAdapter.getPageTitle(i),
              i + 1,
              tabCount));
    }
  }

  public Grade getGrade() {
    return grade;
  }

  private void changeName(View view) {
    LayoutInflater layoutInflater = LayoutInflater.from(getApplicationContext());
    View nameView = layoutInflater.inflate(R.layout.name, null);

    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(ConsoleActivity.this);

    alertDialogBuilder.setView(nameView);

    final EditText userInput = (EditText) nameView.findViewById(R.id.name_input);

    // set dialog message
    alertDialogBuilder
        .setCancelable(false)
        .setPositiveButton(
            "OK",
            new DialogInterface.OnClickListener() {
              public void onClick(DialogInterface dialog, int id) {
                actionBar.setTitle(userInput.getText().toString());
              }
            })
        .setNegativeButton(
            "Cancel",
            new DialogInterface.OnClickListener() {
              public void onClick(DialogInterface dialog, int id) {
                dialog.cancel();
              }
            });

    AlertDialog alertDialog = alertDialogBuilder.create();
    alertDialog.show();
  }
}
