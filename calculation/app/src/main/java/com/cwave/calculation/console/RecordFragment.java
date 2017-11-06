package com.cwave.calculation.console;

import static com.cwave.calculation.console.MathEvents.createMathLogDataRequestedEvent;

import android.os.Build.VERSION_CODES;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.AppBarLayout.OnOffsetChangedListener;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.Adapter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.cwave.calculation.R;
import com.cwave.calculation.dagger.MathApplication;
import com.cwave.calculation.service.Exercise;
import com.cwave.calculation.service.Question;
import com.cwave.calculation.service.QuestionStore;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.firebase.ui.firestore.SnapshotParser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.Query.Direction;
import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.otto.Bus;
import java.util.List;
import javax.inject.Inject;

/** Fragment that displays question log. */
public class RecordFragment extends Fragment {
  private static final String TAG = "RecordFragment";

  private RecyclerView recyclerView;
  private RecyclerView.LayoutManager logLayoutManager;

  private Adapter adapter;
  private FirestoreRecyclerAdapter firestoreRecyclerAdapter;

  CollectionReference questionCollection;
  Query questionQuery;

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

    recyclerView = (RecyclerView) view.findViewById(R.id.console_recycler_view);

    logLayoutManager = new LinearLayoutManager(getActivity());
    recyclerView.setLayoutManager(logLayoutManager);

    //attachRecyclerViewAdapter();
    getLatestCollection();

    return view;
  }

  @Override
  public void onResume() {
    Log.d(TAG, "onResume");
    super.onResume();
    bus.register(this);
  }

  @Override
  public void onPause() {
    Log.d(TAG, "onPause");
    bus.unregister(this);
    super.onPause();
  }

  private void getLatestCollection() {
    CollectionReference collection = FirebaseFirestore.getInstance().collection(CollectionNames.EXERCISES);
    Query query = collection
        .orderBy("date", Direction.DESCENDING)
        .limit(1);

    query.addSnapshotListener(new EventListener<QuerySnapshot>() {
      @Override
      public void onEvent(QuerySnapshot documentSnapshots, FirebaseFirestoreException e) {
        List<DocumentChange> changes = documentSnapshots.getDocumentChanges();
        List<DocumentSnapshot> snapshots = documentSnapshots.getDocuments();
        String id = null;
        // size must be 1 here.
        for(DocumentChange change : changes) {
          id = change.getDocument().getId();
        }
        for(DocumentSnapshot snapshot : snapshots) {
        }

        questionCollection = FirebaseFirestore.getInstance().collection(CollectionNames.EXERCISES + "/" + id + "/" + CollectionNames.QUESTIONS);
        questionQuery = questionCollection.limit(100);

        attachRecyclerViewAdapter();
      }
    });
  }

  private void attachRecyclerViewAdapter() {

    adapter = newAdapter(questionQuery);

    // Scroll to bottom on new messages.
    adapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
      @Override
      public void onItemRangeInserted(int positionStart, int itemCount) {
        recyclerView.smoothScrollToPosition(adapter.getItemCount());
      }
    });

    recyclerView.setAdapter(adapter);
  }

  protected RecyclerView.Adapter newAdapter(Query query) {
    FirestoreRecyclerOptions<Question> options =
        new FirestoreRecyclerOptions.Builder<Question>()
            .setQuery(query, Question.class)
            .build();

    firestoreRecyclerAdapter =
        new FirestoreRecyclerAdapter<Question, QuestionHolder>(options) {
      @Override
      public QuestionHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new QuestionHolder(
            LayoutInflater.from(parent.getContext()).inflate(R.layout.question_list, parent, false));
      }

      @Override
      protected void onBindViewHolder(QuestionHolder holder, int position, Question question) {
        holder.bind(question, position);
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
