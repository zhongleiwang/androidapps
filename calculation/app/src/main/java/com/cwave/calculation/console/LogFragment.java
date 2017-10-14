package com.cwave.calculation.console;

import static com.cwave.calculation.console.MathEvents.createMathLogDataRequestedEvent;

import android.os.Build.VERSION_CODES;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.AppBarLayout.OnOffsetChangedListener;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.cwave.calculation.R;
import com.cwave.calculation.dagger.MathApplication;
import com.cwave.calculation.service.Question;
import com.cwave.calculation.service.QuestionStore;
import com.squareup.otto.Bus;
import java.util.List;
import javax.inject.Inject;

/** Fragment that displays meal log. */
public class LogFragment extends Fragment implements OnRefreshListener, OnOffsetChangedListener {
  private static final String TAG = "LogFragment";

  private SwipeRefreshLayout swipeRefreshLayout;
  private RecyclerView logView;
  private RecyclerView.LayoutManager logLayoutManager;

  @Inject LogReader logReader;

  @Inject Bus bus;

  @Inject QuestionStore questionStore;

  @Override
  public void onCreate(Bundle savedInstanceState) {
    Log.d(TAG, "onCreate");
    super.onCreate(savedInstanceState);
    ((MathApplication) getActivity().getApplication()).getMathComponent().inject(this);
  }

  @RequiresApi(api = VERSION_CODES.GINGERBREAD)
  @Override
  public View onCreateView(
      LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    Log.d(TAG, "onCreateView");
    View view = inflater.inflate(R.layout.console_fragment_pager, container, false);

    AppBarLayout appBarLayout =
        (AppBarLayout) getActivity().findViewById(R.id.console_app_bar_layout);
    appBarLayout.addOnOffsetChangedListener(this);

    swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.console_swipe_refresh_layout);
    swipeRefreshLayout.setColorSchemeResources(R.color.swipe_to_refresh);
    swipeRefreshLayout.setOnRefreshListener(this);

    logView = (RecyclerView) view.findViewById(R.id.console_recycler_view);

    logLayoutManager = new LinearLayoutManager(getActivity());
    logView.setLayoutManager(logLayoutManager);

    return view;
  }

  @Override
  public void onResume() {
    super.onResume();
    bus.register(this);

    onRefresh();
  }

  @Override
  public void onPause() {
    bus.unregister(this);
    super.onPause();
  }

  @RequiresApi(api = VERSION_CODES.GINGERBREAD)
  @Override
  public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
    swipeRefreshLayout.setEnabled(verticalOffset == 0);
  }

  @RequiresApi(api = VERSION_CODES.GINGERBREAD)
  @Override
  public void onRefresh() {
    refreshLog();
    bus.post(createMathLogDataRequestedEvent());
  }

  @RequiresApi(api = VERSION_CODES.GINGERBREAD)
  private void refreshLog() {
    Log.d(TAG, "refresh");
    List<Question> questions = questionStore.get();
    Log.d(TAG, "refresh newwww");
    LogAdapter adapter = new LogAdapter(questions);
    swipeRefreshLayout.setRefreshing(true);
    Log.d(TAG, "refresh set adapter");
    logView.setAdapter(adapter); // new LogAdapter(questionStore.get()));
    swipeRefreshLayout.setRefreshing(false);
  }
}
