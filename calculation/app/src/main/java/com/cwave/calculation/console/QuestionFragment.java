package com.cwave.calculation.console;

import android.graphics.Color;
import android.os.Build.VERSION_CODES;
import android.os.Bundle;
import android.support.annotation.NonNull;
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
import com.cwave.calculation.service.Exercise;
import com.cwave.calculation.service.Question;
import com.cwave.calculation.service.QuestionGenerator;
import com.cwave.calculation.service.QuestionStore;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.otto.Bus;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.UUID;

import javax.inject.Inject;

import static com.cwave.calculation.util.ThreadUtils.runOnUiThread;

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

  private int totalQuestions = TOTAL_QUESTIONS;

  private int tries;

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

    progressBar = (ProgressBar) view.findViewById(R.id.progressBar);
    progressBar.setProgress(0);
    progressBar.setMax(totalQuestions);

    setTotalQuestions();
    stopTimer();

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

  public void setTotalQuestions() {
    ConsoleActivity activity = (ConsoleActivity) getActivity();
    Grade grade = activity.getGrade();
    if (grade == Grade.preK || grade == Grade.kinder) {
      totalQuestions = TOTAL_QUESTIONS;
    }
    if (grade == Grade.first_grade || grade == Grade.second_grade || grade == Grade.third_grade) {
      totalQuestions = 5 * TOTAL_QUESTIONS;
    }
  }

  private void showResults(int num) {
    ResultDialogFragment resultDialogFragment = new ResultDialogFragment();
    Bundle bundle = new Bundle();
    bundle.putInt(ResultDialogFragment.WRONG_NUMBER_KEY, num);
    resultDialogFragment.setArguments(bundle);
    resultDialogFragment.show(getActivity().getFragmentManager(), "");
  }

  private void storeQuestions() {
    String uuid = UUID.randomUUID().toString();
    Exercise exercise = Exercise.builder()
        .setDate()
        .setId(uuid)
        .setName(FirebaseAuth.getInstance().getCurrentUser().getDisplayName())
        .setUid(FirebaseAuth.getInstance().getCurrentUser().getUid())
        .setQuestion(questionStore.get())
        .build();

    FirebaseFirestore.getInstance()
        .collection(CollectionNames.EXERCISES)
        .document(uuid)
        .set(exercise)
        .addOnSuccessListener(new OnSuccessListener<Void>() {
          @Override
          public void onSuccess(Void aVoid) {

          }
        })
        .addOnFailureListener(new OnFailureListener() {
          @Override
          public void onFailure(@NonNull Exception e) {
            Log.e(TAG, "Write invite failed " + e);
          }
        });

    // store in a collection
    List<Question> questionList = questionStore.get();
    CollectionReference collectionReference = FirebaseFirestore.getInstance()
        .collection(CollectionNames.EXERCISES)
        .document(uuid)
        .collection(CollectionNames.QUESTIONS);

    for (Question question : questionList) {
     collectionReference.add(question);
    }
  }

  private void reset() {
    stopTimer();

    // Send to database
    storeQuestions();

    // Gets the wrong answers before get cleared
    int wrongs = questionStore.getWrongAnswers();

    questionStore.cleanUp();
    progressBar.setProgress(0);
    progressBar.setMax(totalQuestions);

    tries = 0;
    answerText.clearComposingText();
    answerText.setText("");
    questionTextView.clearComposingText();
    questionTextView.setText("");
    numberTextView.clearComposingText();
    numberTextView.setText("");
    answerText.setBackgroundColor(Color.WHITE);

    nextButton.setText("Start");

    showResults(wrongs);
    return;
  }

  private void showNextQuestion() {
    int finished = questionStore.getNumberOfQuestions();
    Log.d(TAG, "showNextQuestion total:" + totalQuestions + " finished: " + finished);

    if (ifTimerStopped()) {
      prepareNextQuestion();
      startTimer();
    }

    String answer = answerText.getText().toString();
    if (answer.isEmpty()) {
      return;
    }

    int answered = toInt(answer);
    long expected = question.getAnswer();

    Log.d(TAG, "answered: " + answered + " expected: " + expected);

    if (answered == expected) {
      long questionFinishTime = System.currentTimeMillis();
      String q = questionTextView.getText().toString();
      questionStore.add(q,
          (questionFinishTime - questionStartTime) / 1000,
          tries == 0);

      finished = questionStore.getNumberOfQuestions();
      if (finished >= totalQuestions) {
        reset();
        return;
      }

      tries = 0;

      prepareNextQuestion();

      answerText.setText("");
      answerText.setBackgroundColor(Color.WHITE);
    } else {
      tries++;
      answerText.setText("");
      answerText.setBackgroundColor(Color.RED);
      Log.d(TAG, "wrong");
    }
  }

  private void prepareNextQuestion() {
    Log.d(TAG, "prepareNextQuestion");

    nextButton.setText("Next");
    numberTextView.setText(toString(questionStore.getNumberOfQuestions()));

    questionStartTime = System.currentTimeMillis();

    ConsoleActivity activity = (ConsoleActivity) getActivity();
    question = questionGenerator.generate(activity.getGrade());
    questionTextView.setText(question.getQuestion());

    answerText.setText("");
    answerText.setBackgroundColor(Color.WHITE);

    progressBar.incrementProgressBy(1);
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
    if (timer == null) {
      return;
    }
    timer.cancel();
    timer = null;
  }

  private boolean ifTimerStopped() {
    return timer == null;
  }

  public void onOffsetChanged(AppBarLayout appBarLayout, int i) {}
}
