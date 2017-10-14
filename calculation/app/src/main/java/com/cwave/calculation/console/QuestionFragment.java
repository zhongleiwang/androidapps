package com.cwave.calculation.console;

import static com.cwave.calculation.util.ThreadUtils.runOnUiThread;

import android.graphics.Color;
import android.os.Build.VERSION_CODES;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.design.widget.AppBarLayout;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import com.cwave.calculation.R;
import com.cwave.calculation.dagger.MathApplication;
import com.cwave.calculation.service.Question;
import com.cwave.calculation.service.QuestionGenerator;
import com.cwave.calculation.service.QuestionStore;
import com.cwave.proto.record.Proto.Record;
import com.squareup.otto.Bus;
import java.util.Timer;
import java.util.TimerTask;
import javax.inject.Inject;

/** Fragment that displays meal log. */
public class QuestionFragment extends Fragment {
  private static final String TAG = "QuestionFragment";

  private static final int TOTAL_QUESTIONS = 10;

  private TextView questionTextView;
  private EditText answerText;
  private Button nextButton;
  private TextView timerText;
  private TextView numberTextView;
  private ProgressBar progressBar;

  private int currentQuestionIndex = 0;
  private int totalCorrectQuestions = 0;
  private boolean answerIsWrong = false;

  private int totalQuestions = TOTAL_QUESTIONS;

  private Timer timer;

  long startTime = 0;
  long questionStartTime = 0;

  Question question;

  @Inject Bus bus;

  @Inject QuestionStore questionStore;

  @Inject QuestionGenerator questionGenerator;

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    ((MathApplication) getActivity().getApplication()).getMathComponent().inject(this);
  }

  @RequiresApi(api = VERSION_CODES.GINGERBREAD)
  @Override
  public View onCreateView(
      LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    Log.d(TAG, "onCreateView");

    View view = inflater.inflate(R.layout.questions, container, false);

    questionTextView = (TextView) view.findViewById(R.id.questionTextView);
    questionTextView.setText("Question");

    answerText = (EditText) view.findViewById(R.id.answerText);
    answerText.setText("");

    nextButton = (Button) view.findViewById(R.id.nextButton);
    nextButton.setText("Start");
    nextButton.setOnClickListener(
        new OnClickListener() {
          @Override
          public void onClick(View v) {
            showNextQuestion();
          }
        });

    timerText = (TextView) view.findViewById(R.id.timerTextView);
    timerText.setText("--:--");

    numberTextView = (TextView) view.findViewById(R.id.numberTextView);
    numberTextView.setText("");

    timer = new Timer();

    progressBar = (ProgressBar) view.findViewById(R.id.progressBar);
    progressBar.setProgress(0);
    progressBar.setMax(totalQuestions);

    return view;
  }

  @Override
  public void onResume() {
    super.onResume();
    bus.register(this);
  }

  @Override
  public void onPause() {
    bus.unregister(this);
    super.onPause();
  }

  private void showNextQuestion() {
    Log.d(TAG, "showNextQuestion");

    if (currentQuestionIndex == 0) {
      questionStore.cleanUp();
      ConsoleActivity activity = (ConsoleActivity) getActivity();
      Grade grade = activity.getGrade();
      if (grade == Grade.preK || grade == Grade.kinder) {
        totalQuestions = TOTAL_QUESTIONS;
      }
      if (grade == Grade.first_grade || grade == Grade.second_grade || grade == Grade.third_grade) {
        totalQuestions = 5 * TOTAL_QUESTIONS;
      }
      progressBar.setProgress(0);
      progressBar.setMax(totalQuestions);
      prepareNextQuestion();
      return;
    }

    int answered = toInt(answerText.getText().toString());
    long expected = question.getAnswer();

    Log.d(TAG, "answered: " + answered + " expected: " + expected);

    if (answered == expected) {
      long questionFinishTime = System.currentTimeMillis();
      questionStore.add(
          questionTextView.getText().toString(),
          (questionFinishTime - questionStartTime) / 1000,
          !answerIsWrong);

      if (answerIsWrong) {
        answerIsWrong = false;
      } else {
        totalCorrectQuestions++;
      }
      Log.d(TAG, "right");
      prepareNextQuestion();

      answerText.setText("");
      answerText.setBackgroundColor(Color.WHITE);
    } else {
      answerIsWrong = true;
      answerText.setText("");
      answerText.setBackgroundColor(Color.RED);
      Log.d(TAG, "wrong");
    }
  }

  private String toString(int in) {
    return String.valueOf(in);
  }

  private int toInt(String in) {
    // Default is always 0
    if (TextUtils.isEmpty(in)) {
      return 0;
    }
    return Integer.parseInt(in);
  }

  private void prepareNextQuestion() {
    if (currentQuestionIndex >= totalQuestions) {
      Log.d(TAG, "stop timer");
      currentQuestionIndex = 0;
      stopTimer();

      progressBar.setProgress(0);
      progressBar.setMax(totalQuestions);

      questionTextView.setText(String.format("Wrong: %d", totalQuestions - totalCorrectQuestions));
      nextButton.setText("Excellent!!! Restart?");
      return;
    }

    // User proto
    Record record = Record.newBuilder()
        .setName("name")
        .build();

    if (currentQuestionIndex == 0) {
      totalCorrectQuestions = 0;
      answerIsWrong = false;

      startTimer();
    }

    nextButton.setText("Next");
    numberTextView.setText(toString(currentQuestionIndex + 1));

    questionStartTime = System.currentTimeMillis();

    ConsoleActivity activity = (ConsoleActivity) getActivity();
    question = questionGenerator.generate(activity.getGrade());
    questionTextView.setText(question.getQuestion());

    currentQuestionIndex++;
    progressBar.incrementProgressBy(1);
  }

  /** Start local timer. */
  private void startTimer() {
    if (timer == null) {
      timer = new Timer();
    }
    startTime = System.currentTimeMillis();
    timer.scheduleAtFixedRate(
        new TimerTask() {
          @Override
          public void run() {
            runOnUiThread(
                new Runnable() {
                  @Override
                  public void run() {
                    long now = System.currentTimeMillis();
                    long seconds = (now - startTime) / 1000;
                    long mins = seconds / 60;
                    seconds = seconds % 60;
                    timerText.setText(String.format("%d\' : %d\" ", mins, seconds));
                  }
                });
          }
        },
        1000,
        1000);
  }

  private void stopTimer() {
    timer.cancel();
    timer = null;
  }

  public void onOffsetChanged(AppBarLayout appBarLayout, int i) {}
}
