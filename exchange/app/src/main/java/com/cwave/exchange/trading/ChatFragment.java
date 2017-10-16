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

/** Fragment that displays chat. */
public class ChatFragment extends Fragment {
  private static final String TAG = "ChatFragment";

  private static final CollectionReference chatCollection =
      FirebaseFirestore.getInstance().collection(CollectionName.USERS);
  /** Get the last 50 chat messages ordered by timestamp . */
  private static final Query chatQuery = chatCollection.orderBy("timestamp").limit(50);

  private RecyclerView recyclerView;
  private LayoutManager layoutManager;
  private TextView emptyListMessage;
  
  @Override
  public void onCreate(Bundle savedInstanceState) {
    Log.d(TAG, "onCreate");
    super.onCreate(savedInstanceState);
  }

  @RequiresApi(api = VERSION_CODES.GINGERBREAD)
  @Override
  public View onCreateView(
      LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    Log.d(TAG, "onCreateView");
    View view = inflater.inflate(R.layout.post_frame_layout, container, false);

    recyclerView = (RecyclerView) view.findViewById(R.id.frame_recycler_view);

    layoutManager = new LinearLayoutManager(getActivity());
    recyclerView.setLayoutManager(layoutManager);

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

  private void attachRecyclerViewAdapter() {
    final RecyclerView.Adapter adapter = newAdapter();

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
    FirestoreRecyclerOptions<ChatMessage> options =
        new FirestoreRecyclerOptions.Builder<ChatMessage>()
            .setQuery(chatQuery, ChatMessage.class)
            .setLifecycleOwner(this)
            .build();

    return new FirestoreRecyclerAdapter<ChatMessage, ChatMessageHolder>(options) {
      @Override
      public ChatMessageHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ChatMessageHolder(LayoutInflater.from(parent.getContext())
            .inflate(R.layout.chat_message, parent, false));
      }

      @Override
      protected void onBindViewHolder(ChatMessageHolder holder, int position, ChatMessage model) {
        holder.bind(model);
      }

      @Override
      public void onDataChanged() {
        // If there are no chat messages, show a view that invites the user to add a message.
        emptyListMessage.setVisibility(getItemCount() == 0 ? View.VISIBLE : View.GONE);
      }
    };
  }
}
