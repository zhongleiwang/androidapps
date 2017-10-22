package com.cwave.exchange.chat;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.LayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;

import com.cwave.exchange.R;
import com.cwave.firebase.Auth;
import com.cwave.firebase.Database;
import com.cwave.firebase.Store;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import javax.inject.Inject;

import dagger.android.AndroidInjection;

/** Fragment that displays chat. */
public class ChatFragment extends Fragment {
  private static final String TAG = "ChatFragment";

  public static final String COLLECTION_KEY = "collection_key";

  private RecyclerView recyclerView;
  private LayoutManager layoutManager;
  private ImageButton inputButton;
  private EditText inputMessage;
  private Activity activity;
  private Query chatQuery;
  private CollectionReference chatCollection;
  private String collectionName;
  private FirestoreRecyclerAdapter<ChatMessage, ChatMessageHolder> firestoreRecyclerAdapter;

  @Inject
  Store store;

  @Inject
  Database database;

  @Inject
  Auth auth;
  
  @Override
  public void onCreate(Bundle savedInstanceState) {
    Log.d(TAG, "onCreate");
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
    Log.d(TAG, "onCreateView");
    View view = inflater.inflate(R.layout.chat_frame_layout, container, false);

    Bundle arguments = getArguments();
    collectionName = arguments.getString(COLLECTION_KEY);
    Log.d(TAG, "collection name: " + collectionName);

    chatCollection = FirebaseFirestore.getInstance().collection(collectionName);
    chatQuery = chatCollection.orderBy("date").limit(100);

    inputButton = (ImageButton) view.findViewById(R.id.chat_message_input_button);
    inputMessage = (EditText) view.findViewById(R.id.chat_message_input_message);

    inputButton.setOnClickListener(new OnClickListener() {
      @Override
      public void onClick(View v) {
        sendMessage(v);
      }
    });

    recyclerView = (RecyclerView) view.findViewById(R.id.chat_frame_recycler_view);

    layoutManager = new LinearLayoutManager(getActivity());
    recyclerView.setLayoutManager(layoutManager);
    attachRecyclerViewAdapter();

    return view;
  }

  @Override
  public void onResume() {
    super.onResume();
    firestoreRecyclerAdapter.startListening();
  }

  @Override
  public void onPause() {
    super.onPause();
    firestoreRecyclerAdapter.stopListening();
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
            .build();

    firestoreRecyclerAdapter = new FirestoreRecyclerAdapter<ChatMessage, ChatMessageHolder>(options) {
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
      }
    };

    firestoreRecyclerAdapter.startListening();
    return firestoreRecyclerAdapter;
  }

  private void sendMessage(View view) {
    store.write(collectionName,
        ChatMessage.builder()
        .setName(auth.getCurrentUser().getDisplayName())
        .setUid(auth.getCurrentUser().getUid())
        .setDate()
        .setMessage(inputMessage.getText().toString())
        .build());
    inputMessage.setText("");
  }
}
