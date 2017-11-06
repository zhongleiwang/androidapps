package com.cwave.calculation.console;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.cwave.calculation.R;
import com.cwave.calculation.service.Exercise;
import com.cwave.calculation.service.Question;

import java.util.List;

public class ExerciseHolder extends RecyclerView.ViewHolder {
  private static final String TAG = "ExerciseHolder";

  private final TextView idField;
  private final TextView nameField;
  private final TextView uidField;
  private final TextView questionField;
  private final TextView timeField;
  private boolean correctness;
  private List<Question> questions;

  private String id;

  public ExerciseHolder(View itemView) {
    super(itemView);
    idField = itemView.findViewById(R.id.question_message_id);
    nameField = itemView.findViewById(R.id.question_message_name);
    uidField = itemView.findViewById(R.id.question_message_uid);
    questionField = itemView.findViewById(R.id.question_message_text);
    timeField = itemView.findViewById(R.id.question_message_time);
  }

  public void bind(Exercise exercise, int position) {
    idField.setText(exercise.getId());
    nameField.setText(exercise.getName());
    uidField.setText(exercise.getUid());
    questionField.setText("");
    timeField.setText("");
  }
}
