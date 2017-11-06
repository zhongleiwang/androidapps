package com.cwave.exchange.post;

import android.Manifest.permission;
import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.Adapter;
import android.support.v7.widget.RecyclerView.LayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.cwave.exchange.R;
import com.cwave.exchange.invite.InviteManager;
import com.cwave.exchange.invite.InviteMessage;
import com.cwave.exchange.post.PostMessageHolder.PostMessageClickListener;
import com.cwave.exchange.trading.CollectionName;
import com.cwave.exchange.trading.OfferDialogFragment;
import com.cwave.exchange.trading.TradingActivity;
import com.cwave.firebase.Auth;
import com.cwave.firebase.Database;
import com.cwave.firebase.Store;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.perf.FirebasePerformance;
import com.google.firebase.perf.metrics.Trace;

import java.util.Calendar;
import java.util.Date;

import javax.inject.Inject;

import dagger.android.AndroidInjection;

/** Fragment that displays posts. */
public class PostFragment extends Fragment implements PostMessageClickListener,
    ConnectionCallbacks, OnConnectionFailedListener, LocationListener {
  private static final String TAG = "PostFragment";

  private static final CollectionReference postCollection =
      FirebaseFirestore.getInstance().collection(CollectionName.POSTS);
  // Get the last 50 chat messages ordered by timestamp .
  private static final Query postQuery = postCollection.orderBy("date").limit(50);

  private static final int NOTIFICATION_ID = 999;

  private Activity activity;
  private RecyclerView recyclerView;
  private LayoutManager layoutManager;
  private Adapter adapter;
  private FirestoreRecyclerAdapter firestoreRecyclerAdapter;
  private LocationManager locationManager;
  private Location location;

  @Inject
  Store store;

  @Inject
  Database database;

  @Inject
  Auth auth;

  @Inject
  InviteManager inviteManager;

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

    Trace trace = FirebasePerformance.getInstance().newTrace("TradingActivity");
    trace.start();

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

    locationManager = (LocationManager) activity.getApplicationContext().getSystemService(Context.LOCATION_SERVICE);
    if (ActivityCompat.checkSelfPermission(activity, permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
        && ActivityCompat.checkSelfPermission(activity, permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
      return view;
    }
    Criteria criteria = new Criteria();
    criteria.setAccuracy(Criteria.ACCURACY_COARSE);
    location = locationManager.getLastKnownLocation(locationManager.getBestProvider(criteria, true));

    trace.stop();

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

    String uid = auth.getCurrentUser().getUid();
    String name = auth.getCurrentUser().getDisplayName();
    String postUid = postMessage.getUid();

    InviteMessage inviteMessage = InviteMessage.builder()
        .setId(uid)
        .setName(name)
        .setPost(postMessage)
        .build();

    if (postUid.equals(uid)) {
      Toast.makeText(activity.getApplicationContext(), "It's your post", Toast.LENGTH_LONG).show();
      return;
    }

    inviteManager.writeInvite(inviteMessage);

    Bundle bundle = new Bundle();
    bundle.putParcelable(CollectionName.INVITE_MESSAGE_KEY, inviteMessage);
    ((TradingActivity) activity).startChatFragment(bundle);
  }

  @Override
  public void onLocationChanged(Location location) {
  }

  @Override
  public void onStatusChanged(String provider, int status, Bundle extras) {

  }

  @Override
  public void onProviderEnabled(String provider) {

  }

  @Override
  public void onProviderDisabled(String provider) {

  }

  @Override
  public void onConnected(@Nullable Bundle bundle) {
    //LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
  }

  @Override
  public void onConnectionSuspended(int i) {

  }

  @Override
  public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

  }
}
