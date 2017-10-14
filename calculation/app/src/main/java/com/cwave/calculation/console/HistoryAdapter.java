package com.cwave.calculation.console;

import android.os.Build.VERSION_CODES;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.RecyclerView;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.cwave.calculation.R;
import java.util.List;
import org.joda.time.Instant;

/** Hisotry {@link RecyclerView} adapter. */
public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.ViewHolder> {
  private static final String TAG = HistoryAdapter.class.getSimpleName();

  private final List<Pair<String, Instant>> history;

  /** Class to hold math name and meal time. */
  public static class ViewHolder extends RecyclerView.ViewHolder {
    public final TextView mathView;
    public final TextView timeView;

    public ViewHolder(View view) {
      super(view);
      mathView = (TextView) view.findViewById(R.id.history_math);
      timeView = (TextView) view.findViewById(R.id.history_time);
    }
  }

  /**
   * Constructor.
   *
   * @param history a list of meal history. Each entry represents math name and meal time.
   */
  public HistoryAdapter(List<Pair<String, Instant>> history) {
    this.history = history;
  }

  @Override
  public HistoryAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    RelativeLayout layout =
        (RelativeLayout)
            LayoutInflater.from(parent.getContext())
                .inflate(R.layout.history_item_view, parent, false);
    return new ViewHolder(layout);
  }

  @RequiresApi(api = VERSION_CODES.ECLAIR)
  @Override
  public void onBindViewHolder(ViewHolder holder, int position) {
    holder.mathView.setText(history.get(position).first);
    holder.timeView.setText(history.get(position).second.toString());
  }

  @Override
  public int getItemCount() {
    return history.size();
  }
}
