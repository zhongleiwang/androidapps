package com.cwave.calculation.console;

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
import com.squareup.otto.Bus;
import javax.inject.Inject;

/** Fragment that display meal history. */
public class HistoryFragment extends Fragment
    implements OnRefreshListener, OnOffsetChangedListener {
  private static final String TAG = HistoryFragment.class.getSimpleName();

  private SwipeRefreshLayout swipeRefreshLayout;
  private RecyclerView historyView;
  private RecyclerView.LayoutManager historyLayoutManager;

  @Inject Bus bus;

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

    historyView = (RecyclerView) view.findViewById(R.id.console_recycler_view);

    historyLayoutManager = new LinearLayoutManager(getActivity());
    historyView.setLayoutManager(historyLayoutManager);

    return view;
  }

  @RequiresApi(api = VERSION_CODES.GINGERBREAD)
  @Override
  public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
    swipeRefreshLayout.setEnabled(verticalOffset == 0);
  }

  @RequiresApi(api = VERSION_CODES.GINGERBREAD)
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
  public void onRefresh() {
    refreshHistory();
  }

  @RequiresApi(api = VERSION_CODES.GINGERBREAD)
  private void refreshHistory() {
    swipeRefreshLayout.setRefreshing(true);
    // TODO.
    swipeRefreshLayout.setRefreshing(false);
  }
}
