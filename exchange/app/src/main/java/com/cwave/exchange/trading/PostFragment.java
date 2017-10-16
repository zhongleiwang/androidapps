package com.cwave.exchange.trading;

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
import android.support.v7.widget.RecyclerView.Adapter;
import android.support.v7.widget.RecyclerView.LayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.cwave.exchange.R;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

/** Fragment that displays posts. */
public class PostFragment extends Fragment {
  private static final String TAG = "?????PostFragment";

  private static final CollectionReference postCollection =
      FirebaseFirestore.getInstance().collection(CollectionName.POSTS);
  /** Get the last 50 chat messages ordered by timestamp . */
  private static final Query postQuery = postCollection.orderBy("name").limit(50);

  private RecyclerView recyclerView;
  private LayoutManager layoutManager;
  private Adapter adapter;
  private FirestoreRecyclerAdapter firestoreRecyclerAdapter;

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
  }

  @Override
  public View onCreateView(
      LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    FirebaseFirestore.setLoggingEnabled(true);
    View view = inflater.inflate(R.layout.post_frame_layout, container, false);

    recyclerView = (RecyclerView) view.findViewById(R.id.frame_recycler_view);

    layoutManager = new LinearLayoutManager(getActivity());
    recyclerView.setLayoutManager(layoutManager);

    attachRecyclerViewAdapter();

    return view;
  }

  @Override
  public void onResume() {
    super.onResume();
  }

  @Override
  public void onPause() {
    super.onPause();
  }

  @Override
  public void onStop() {
    super.onStop();
    firestoreRecyclerAdapter.stopListening();
  }

  private void attachRecyclerViewAdapter() {
    adapter = newAdapter();

    // Scroll to bottom on new messages
    adapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
      @Override
      public void onItemRangeInserted(int positionStart, int itemCount) {
        recyclerView.smoothScrollToPosition(adapter.getItemCount());
      }
    });

    recyclerView.setAdapter(adapter);
  }

  protected RecyclerView.Adapter newAdapter() {
    FirestoreRecyclerOptions<PostMessage> options =
        new FirestoreRecyclerOptions.Builder<PostMessage>()
            .setQuery(postQuery, PostMessage.class)
            .setLifecycleOwner(this)
            .build();

    FirestoreRecyclerAdapter firestoreRecyclerAdapter =
        new FirestoreRecyclerAdapter<PostMessage, PostMessageHolder>(options) {
      @Override
      public PostMessageHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new PostMessageHolder(LayoutInflater.from(parent.getContext())
            .inflate(R.layout.post_message, parent, false));
      }

      @Override
      protected void onBindViewHolder(PostMessageHolder holder, int position, PostMessage message) {
        Log.d(TAG, message.getName());
        holder.bind(message);
      }

      @Override
      public void onDataChanged() {
        // If there are no chat messages, show a view that invites the user to add a message.
        // emptyListMessage.setVisibility(getItemCount() == 0 ? View.VISIBLE : View.GONE);
      }
    };

    firestoreRecyclerAdapter.startListening();
    return firestoreRecyclerAdapter;
  }
}
