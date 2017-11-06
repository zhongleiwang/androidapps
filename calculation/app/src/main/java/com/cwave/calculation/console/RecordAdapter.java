package com.cwave.calculation.console;

import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Build.VERSION_CODES;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.cwave.calculation.R;
import com.cwave.calculation.service.Question;
import com.google.common.base.Preconditions;
import java.util.List;

/** Log {@link RecyclerView} adapter. */
public class RecordAdapter extends RecyclerView.Adapter<RecordAdapter.ViewHolder> {

  private static final String TAG = "RecordAdapter";

  private final List<Question> questions;

  /** Class to hold math name and meal time. */
  public static class ViewHolder extends RecyclerView.ViewHolder {
    public final TextView questionView;
    public final TextView timeView;

    public ViewHolder(View view) {
      super(view);
      questionView = (TextView) view.findViewById(R.id.question_content);
      timeView = (TextView) view.findViewById(R.id.question_time);
    }
  }

  /** Constructor. */
  public RecordAdapter(List<Question> questions) {
    Log.d(TAG, "create size: " + questions.size());
    this.questions = Preconditions.checkNotNull(questions);
  }

  @Override
  public RecordAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    RelativeLayout layout =
        (RelativeLayout)
            LayoutInflater.from(parent.getContext())
                .inflate(R.layout.question_item_view, parent, false);
    return new ViewHolder(layout);
  }

  @RequiresApi(api = VERSION_CODES.ECLAIR)
  @Override
  public void onBindViewHolder(final ViewHolder holder, final int position) {
    Question question = questions.get(position);
    Log.d(TAG, "pos: " + position + " question: " + question);
    holder.questionView.setText(question.getQuestion());
    holder.timeView.setText(String.valueOf(question.getTime()));

    if (!question.getIsCorrect()) {
      holder.questionView.setTypeface(Typeface.DEFAULT_BOLD, 0);
      holder.timeView.setTypeface(Typeface.DEFAULT_BOLD, 0);
      holder.questionView.setBackgroundColor(Color.RED);
      holder.timeView.setBackgroundColor(Color.RED);
    }

    holder.questionView.setOnClickListener(
        new OnClickListener() {
          @Override
          public void onClick(View view) {
            onQuestionClick(holder.questionView, position);
          }
        });
  }

  @Override
  public int getItemCount() {
    Log.d(TAG, "size: " + questions.size());
    return questions.size();
  }

  private void onQuestionClick(View view, int position) {
    TextView log = (TextView) view;
    Log.d(TAG, "log " + log.getText() + " onClick: " + position);
  }
}
