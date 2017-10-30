package com.cwave.exchange.chat;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.NonNull;
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
import android.widget.Toast;

import com.cwave.exchange.R;
import com.cwave.exchange.invite.InviteMessage;
import com.cwave.exchange.post.PostMessage;
import com.cwave.exchange.trading.CollectionName;
import com.cwave.exchange.util.ChatRoom;
import com.cwave.firebase.Auth;
import com.cwave.firebase.Database;
import com.cwave.firebase.Store;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import javax.inject.Inject;

import dagger.android.AndroidInjection;

/** Fragment that displays chat. */
public class ChatFragment extends Fragment {
  private static final String TAG = "ChatFragment";

  private RecyclerView recyclerView;
  private LayoutManager layoutManager;
  private ImageButton inputButton;
  private EditText inputMessage;
  private Activity activity;
  private Query chatQuery;
  private CollectionReference chatCollection;
  private FirestoreRecyclerAdapter<ChatMessage, ChatMessageHolder> firestoreRecyclerAdapter;
  private String postId;
  private String postName;
  private String postUid;
  private String name;
  private String uid;

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

    recyclerView = (RecyclerView) view.findViewById(R.id.chat_frame_recycler_view);
    layoutManager = new LinearLayoutManager(getActivity());
    recyclerView.setLayoutManager(layoutManager);

    Bundle arguments = getArguments();
    InviteMessage inviteMessage = arguments.getParcelable(CollectionName.INVITE_MESSAGE_KEY);
    PostMessage postMessage = inviteMessage.getPost();
    postId = postMessage.getId();
    postName = postMessage.getName();
    postUid = postMessage.getUid();
    uid = inviteMessage.getUid();
    name = inviteMessage.getName();

    // chat room in structure
    // chats/post_id/post_uid/uid/
    //                           /inviteMessage (doc)
    //                           /messages
    writeInviteMessage(inviteMessage);

    chatCollection = FirebaseFirestore.getInstance().collection(ChatRoom.getRoomPath(inviteMessage));
    chatQuery = chatCollection.orderBy("date").limit(100);
    attachRecyclerViewAdapter();

    inputButton = (ImageButton) view.findViewById(R.id.chat_message_input_button);
    inputMessage = (EditText) view.findViewById(R.id.chat_message_input_message);

    inputButton.setOnClickListener(new OnClickListener() {
      @Override
      public void onClick(View v) {
        sendMessage(v);
      }
    });

    return view;
  }

  public void writeInviteMessage(InviteMessage inviteMessage) {
    Log.d(TAG, "writeInvite " + inviteMessage.getPost().getId() + " : " + inviteMessage.getPost().getUid());

    FirebaseFirestore.getInstance()
        .collection(ChatRoom.getMetaPath(inviteMessage))
        .document(inviteMessage.getUid())
        .set(inviteMessage)
        .addOnSuccessListener(new OnSuccessListener<Void>() {
          @Override
          public void onSuccess(Void aVoid) {
          }
        })
        .addOnFailureListener(new OnFailureListener() {
          @Override
          public void onFailure(@NonNull Exception e) {
            Log.e(TAG, "Write invite failed " + e);
          }
        });
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
    super.onPause();
    if (firestoreRecyclerAdapter != null) {
      firestoreRecyclerAdapter.stopListening();
    }
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
    Log.d(TAG, "send message");

    chatCollection
        .add(ChatMessage.builder()
            .setPostName(postName)
            .setPostUid(postUid)
            .setName(auth.getCurrentUser().getDisplayName())
            .setUid(auth.getCurrentUser().getUid())
            .setMessage(inputMessage.getText().toString())
            .build())
        .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
            @Override
            public void onSuccess(DocumentReference documentReference) {
              Log.d(TAG, "user DocumentSnapshot added with ID: " + documentReference.getId());
              inputMessage.setText("");
            }
          })
        .addOnFailureListener(new OnFailureListener() {
          @Override
          public void onFailure(@NonNull Exception e) {
            Log.e(TAG, "failed to send message " + e);
            Toast.makeText(activity.getApplicationContext(), "Failed to send message", Toast.LENGTH_LONG).show();
          }
        });

  }
}
