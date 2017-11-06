package com.cwave.calculation.console;

import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.cwave.calculation.R;
import com.cwave.calculation.service.Question;

import java.util.List;

public class QuestionHolder extends RecyclerView.ViewHolder {
  private static final String TAG = "QuestionHolder";

  private final TextView questionField;
  private final TextView timeField;
  private boolean correctness;

  private String id;

  public QuestionHolder(View itemView) {
    super(itemView);
    questionField = itemView.findViewById(R.id.question_message_text);
    timeField = itemView.findViewById(R.id.question_message_time);
  }

  public void bind(Question question, int position) {
    questionField.setText(question.getQuestion());
    timeField.setText(String.valueOf(question.getTime()));
    correctness = question.getIsCorrect();

    if (!correctness) {
      questionField.setBackgroundColor(Color.RED);
    }
  }
}
