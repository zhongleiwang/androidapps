package com.cwave.exchange.post;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.Adapter;
import android.support.v7.widget.RecyclerView.LayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cwave.exchange.R;
import com.cwave.exchange.chat.ChatFragment;
import com.cwave.exchange.post.PostMessageHolder.PostMessageClickListener;
import com.cwave.exchange.trading.CollectionName;
import com.cwave.exchange.trading.OfferDialogFragment;
import com.cwave.exchange.trading.TradingActivity;
import com.cwave.firebase.Auth;
import com.cwave.firebase.Database;
import com.cwave.firebase.Store;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.util.Calendar;
import java.util.Date;

import javax.inject.Inject;

import dagger.android.AndroidInjection;

/** Fragment that displays posts. */
public class PostFragment extends Fragment implements PostMessageClickListener {
  private static final String TAG = "PostFragment";

  private static final CollectionReference postCollection =
      FirebaseFirestore.getInstance().collection(CollectionName.POSTS);
  // Get the last 50 chat messages ordered by timestamp .
  private static final Query postQuery = postCollection.orderBy("date").limit(50);

  private Activity activity;
  private RecyclerView recyclerView;
  private LayoutManager layoutManager;
  private Adapter adapter;
  private FirestoreRecyclerAdapter firestoreRecyclerAdapter;

  @Inject
  Store store;

  @Inject
  Database database;

  @Inject
  Auth auth;

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
  }

  @Override
  public void onAttach(Activity activity) {
    AndroidInjection.inject(this);
    super.onAttach(activity);
    this.activity = activity;
  }

  @Override
  public View onCreateView(
      LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    FirebaseFirestore.setLoggingEnabled(true);
    View view = inflater.inflate(R.layout.post_frame_layout, container, false);

    recyclerView = (RecyclerView) view.findViewById(R.id.post_frame_recycler_view);

    layoutManager = new LinearLayoutManager(getActivity());
    recyclerView.setLayoutManager(layoutManager);

    attachRecyclerViewAdapter();

    FloatingActionButton fab = (FloatingActionButton) view.findViewById(R.id.fab);
    fab.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        createPost(view);
      }
    });

    return view;
  }

  @Override
  public void onResume() {
    super.onResume();
    if (firestoreRecyclerAdapter != null) {
      firestoreRecyclerAdapter.startListening();
    }
  }

  @Override
  public void onPause() {
    if (firestoreRecyclerAdapter != null) {
      firestoreRecyclerAdapter.stopListening();
    }
    super.onPause();
  }

  @Override
  public void onStop() {
    if (firestoreRecyclerAdapter != null) {
      firestoreRecyclerAdapter.stopListening();
    }
    super.onStop();
  }

  private void attachRecyclerViewAdapter() {
    adapter = newAdapter();

    // Scroll to bottom on new messages.
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
            .build();

    firestoreRecyclerAdapter =
        new FirestoreRecyclerAdapter<PostMessage, PostMessageHolder>(options) {
      @Override
      public PostMessageHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new PostMessageHolder(
            LayoutInflater.from(parent.getContext()).inflate(R.layout.post_message, parent, false),
            PostFragment.this);
      }

      @Override
      protected void onBindViewHolder(PostMessageHolder holder, int position, PostMessage message) {
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

  private void createPost(View view) {
    OfferDialogFragment offerDialogFragment = new OfferDialogFragment();
    Bundle bundle = new Bundle();
    bundle.putString(OfferDialogFragment.USER_KEY, auth.getCurrentUser().getDisplayName());
    bundle.putString(OfferDialogFragment.UID_KEY, auth.getCurrentUser().getUid());
    bundle.putSerializable(OfferDialogFragment.REQUEST_TYPE, "type");
    offerDialogFragment.setArguments(bundle);
    offerDialogFragment.show(getFragmentManager(), "");
  }

  private static Date getCurrentTime() {
    return Calendar.getInstance().getTime();
  }

  @Override
  public void onPostMessageClick(PostMessage postMessage) {
    Log.d(TAG, "onPostMessageClick");

    TradingActivity tradingActivity = (TradingActivity)activity;
    tradingActivity.switchToChildFragement();

    ChatFragment chatFragment = new ChatFragment();
    Bundle args = new Bundle();
    args.putString(ChatFragment.COLLECTION_KEY, createCollectionName(postMessage));
    chatFragment.setArguments(args);

    FragmentTransaction transaction = getFragmentManager().beginTransaction();
    // Replace whatever is in the fragment_container view with this fragment,
    // and add the transaction to the back stack so the user can navigate back
    transaction.replace(R.id.fragment_container, chatFragment);
    transaction.addToBackStack(null);
    transaction.commit();
  }

  private String createCollectionName(PostMessage post) {
    return auth.getCurrentUser().getUid() + ":" + post.getUid();
  }
}
