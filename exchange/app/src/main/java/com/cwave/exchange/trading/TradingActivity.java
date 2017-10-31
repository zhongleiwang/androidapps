package com.cwave.exchange.trading;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.app.Notification;
import android.app.Notification.Builder;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build.VERSION;
import android.os.Build.VERSION_CODES;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.cwave.exchange.BuildConfig;
import com.cwave.exchange.R;
import com.cwave.exchange.chat.ChatFragment;
import com.cwave.exchange.drawermenu.DrawerMenu;
import com.cwave.exchange.drawermenu.DrawerMenu.MenuField;
import com.cwave.exchange.invite.InviteManager;
import com.cwave.exchange.invite.InviteMessage;
import com.cwave.exchange.post.PostFragment;
import com.cwave.exchange.post.PostMessage;
import com.cwave.exchange.signin.SignInActivity;
import com.cwave.exchange.trading.OfferDialogFragment.OfferListener;
import com.cwave.firebase.Auth;
import com.cwave.firebase.Database;
import com.cwave.firebase.Store;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.crash.FirebaseCrash;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.perf.metrics.AddTrace;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings;

import javax.inject.Inject;

import dagger.android.AndroidInjection;
import dagger.android.AndroidInjector;
import dagger.android.DispatchingAndroidInjector;
import dagger.android.HasFragmentInjector;

import static com.cwave.exchange.R.drawable;
import static com.cwave.exchange.R.id;
import static com.cwave.exchange.R.layout;
import static com.google.common.base.Preconditions.checkNotNull;

public class TradingActivity extends AppCompatActivity implements
    HasFragmentInjector,
    FirebaseAuth.AuthStateListener,
    EventListener<QuerySnapshot>,
    OfferListener {

  private static final String TAG = "TradingActivity";

  private static final int NOTIFICATION_ID = 999;

  private static final int PENDING_INTENT_REQUEST_CODE = 199;

  //private DrawerLayout drawer;
  private DrawerMenu drawerMenu;
  private RecyclerView recyclerView;
  private EventListener<QuerySnapshot> eventListener;

  private FirebaseRemoteConfig firebaseRemoteConfig;

  @Inject
  DispatchingAndroidInjector<Fragment> fragmentInjector;

  @Inject
  Database database;

  @Inject
  Store store;

  @Inject
  Auth auth;

  @Inject
  InviteManager inviteManager;

  /** Starts a new {@link TradingActivity} instance. */
  public static void startActivity(Context context) {
    checkNotNull(context);
    context.startActivity(new Intent(context, TradingActivity.class));
  }

  @Override
  public AndroidInjector<Fragment> fragmentInjector() {
    return fragmentInjector;
  }

  @Override
  @AddTrace(name = "TradingActivity", enabled = true)
  protected void onCreate(Bundle savedInstanceState) {
    AndroidInjection.inject(this);
    super.onCreate(savedInstanceState);
    setContentView(layout.activity_trading);

    String token = FirebaseInstanceId.getInstance().getToken();
    Log.d(TAG, "ID token: " + token);

    Toolbar toolbar = (Toolbar) findViewById(id.toolbar);
    setSupportActionBar(toolbar);

    drawerMenu = new DrawerMenu((DrawerLayout) findViewById(R.id.drawer_layout), MenuField.SEARCH);
    drawerMenu.startMenu(this);

    FirebaseAuth.getInstance().addAuthStateListener(this);

    processIntent(getIntent());

    loadRemotConfig();
  }

  @Override
  public void onDestroy() {
    super.onDestroy();
    FirebaseAuth.getInstance().removeAuthStateListener(this);
  }

  @Override
  public void onBackPressed() {
    if (drawerMenu.isOpen()) {
      drawerMenu.closeMenu();
      return;
    }
    super.onBackPressed();
  }

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    // Inflate the menu; this adds items to the action bar if it is present.
    getMenuInflater().inflate(R.menu.trading, menu);
    return true;
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    // Handle action bar item clicks here. The action bar will
    // automatically handle clicks on the Home/Up button, so long
    // as you specify a parent activity in AndroidManifest.xml.
    int id = item.getItemId();
    Log.d(TAG, "option item: " + id);

    if (id == android.R.id.home) {
      Fragment currentFragment = getFragmentManager().findFragmentById(R.id.fragment_container);
      if (currentFragment instanceof PostFragment) {
        drawerMenu.openMenu();
      } else if (currentFragment instanceof ChatFragment) {
        switchToRootFragement();
        replaceWithPostFragment();
      }
      return true;
    } else if (id == R.id.action_settings) {
      return true;
    } else if (id == R.id.action_search) {
      return true;
    }

    return super.onOptionsItemSelected(item);
  }

  @Override
  public void onAuthStateChanged(@NonNull FirebaseAuth auth) {
    Log.d(TAG, "onAuthStateChanged");
    if (auth.getCurrentUser() == null) {
      Log.d(TAG, "sign in again");
      startSigninActivity();
    }
  }

  private void startSigninActivity() {
    SignInActivity.startActivity(this);
    this.finish();
  }

  public void switchToChildFragement() {
    Log.d(TAG, "switch to child");
    ActionBar actionBar = getSupportActionBar();
    actionBar.setHomeAsUpIndicator(drawable.ic_arrow_back_white_24dp);
    actionBar.setDisplayHomeAsUpEnabled(true);
    if (drawerMenu.isOpen()) {
      drawerMenu.closeMenu();
    }
  }

  public void switchToRootFragement() {
    Log.d(TAG, "switch to root");
    ActionBar ab = getSupportActionBar();
    ab.setHomeAsUpIndicator(drawable.ic_menu_white_24dp);
    ab.setDisplayHomeAsUpEnabled(true);

    if (drawerMenu != null && drawerMenu.isOpen()) {
      drawerMenu.closeMenu();
    }
  }

  private void replaceWithPostFragment() {
    PostFragment postFragment = new PostFragment();
    getFragmentManager().beginTransaction()
        .replace(id.fragment_container, postFragment).commit();
  }

  @Override
  public void onOfferSelectedClick(PostMessage post) {
    addPost(CollectionName.POSTS, post);
  }

  @Override
  public void onOfferCancelClick() {
    // do nothing.
  }

  private void addPost(String collectionNmae, final PostMessage postMessage) {
    FirebaseFirestore.getInstance()
        .collection(collectionNmae)
        .document(postMessage.getId())
        .set(postMessage)
        .addOnSuccessListener(new OnSuccessListener<Void>() {
          @Override
          public void onSuccess(Void aVoid) {
            Log.d(TAG, "Add post " + postMessage.getId());
          }
        })
        .addOnFailureListener(new OnFailureListener() {
          @Override
          public void onFailure(@NonNull Exception e) {
            Log.e(TAG, "Add post failed, " + e);
            Toast.makeText(getApplicationContext(), "Add post failed: " + e,
                Toast.LENGTH_LONG).show();
          }
        });

    // We then listen to invite
    inviteManager.listenToInvite(InviteMessage.builder()
            .setId(auth.getCurrentUser().getUid())
            .setName(auth.getCurrentUser().getDisplayName())
            .setPost(postMessage)
            .build(),
        this);
  }

  @Override
  public void onEvent(QuerySnapshot documentSnapshots, FirebaseFirestoreException e) {
    Log.d(TAG, "invite on event");

    if (e != null) {
      Log.e(TAG, "onEvent error " + e);
      return;
    }
    for (DocumentChange change : documentSnapshots.getDocumentChanges()) {
      switch(change.getType()) {
        case ADDED:
          sendInvite(change);
          break;
        case MODIFIED:
          break;
        case REMOVED:
          break;
      }
    }
  }

  private void sendInvite(DocumentChange change) {
    String myUid = auth.getCurrentUser().getUid();
    InviteMessage inviteMessage = change.getDocument().toObject(InviteMessage.class);
    Log.d(TAG, "Invite from " + inviteMessage.getPost().getUid() + " from " + inviteMessage.getUid());
    PostMessage postMessage = inviteMessage.getPost();
    if (myUid.equals(postMessage.getUid())) {
      if (VERSION.SDK_INT >= VERSION_CODES.O) {
        sendNotificationChannelOnO(inviteMessage);
      } else {
        sendNotification(inviteMessage);
      }
    }

    // We then delete the invite.
    inviteManager.deleteInvite(inviteMessage);
  }

  private void sendNotification(InviteMessage inviteMessage) {
    Log.d(TAG, "sendNotification");

    NotificationManager notificationManager =
        (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

    Notification notification = new Builder(getApplication())
        .setContentTitle("Exchange invite")
        .setContentText("Invite from " + inviteMessage.getPost().getName())
        .setSmallIcon(R.drawable.ic_notifications_active_white_24dp)
        .setContentIntent(buildPendingIntent(inviteMessage))
        .setAutoCancel(true)
        .build();
    notificationManager.notify(NOTIFICATION_ID, notification);
  }

  @RequiresApi(api = VERSION_CODES.O)
  private void sendNotificationChannelOnO(InviteMessage inviteMessage) {
    Log.d(TAG, "sendNotification on O+");

    PostMessage postMessage = inviteMessage.getPost();

    NotificationManager notificationManager =
        (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
    // The id of the channel.
    String id = getString(R.string.default_notification_channel_id);
    // The user-visible name of the channel.
    CharSequence name = getString(R.string.default_notification_channel_name);
    // The user-visible description of the channel.
    String description = getString(R.string.default_notification_channel_description);
    int importance = NotificationManager.IMPORTANCE_HIGH;

    // Configure the notification channel.
    NotificationChannel channel = new NotificationChannel(id, name, importance);
    channel.setDescription(description);
    channel.enableLights(true);
    channel.setLightColor(Color.RED);
    channel.enableVibration(true);
    channel.setVibrationPattern(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400});
    notificationManager.createNotificationChannel(channel);

    Notification notification = new Builder(getApplication())
        .setContentTitle("Exchange invite")
        .setContentText("Invite from " + postMessage.getName())
        .setSmallIcon(R.drawable.ic_notifications_active_white_24dp)
        .setChannelId(id)
        .setContentIntent(buildPendingIntent(inviteMessage))
        .setAutoCancel(true)
        .build();
    notificationManager.notify(NOTIFICATION_ID, notification);
  }

  private void processIntent(Intent intent) {
    Log.d(TAG, "processIntent");
    if (intent != null && intent.getExtras() != null) {
      Bundle bundle = intent.getExtras();
      if (bundle != null) {

        switchToChildFragement();
        startChatFragment(bundle);
      }
    } else {
      switchToRootFragement();
      startPostFragment();
    }
  }

  private void startPostFragment() {
    PostFragment postFragment = new PostFragment();
    getFragmentManager().beginTransaction()
        .add(id.fragment_container, postFragment).commit();
  }

  public void startChatFragment(Bundle bundle) {
    ChatFragment chatFragment = new ChatFragment();
    chatFragment.setArguments(bundle);
    FragmentTransaction transaction = getFragmentManager().beginTransaction();
    transaction.replace(R.id.fragment_container, chatFragment);
    transaction.addToBackStack(null);
    transaction.commit();
  }

  private Bundle buildNotificationBundle(InviteMessage inviteMessage) {
    Bundle bundle = new Bundle();
    bundle.putParcelable(CollectionName.INVITE_MESSAGE_KEY, inviteMessage);
    return bundle;
  }

  private void firebaseLog() {
    FirebaseCrash.log("=== firebase ??? crash ??? log ===");
    FirebaseCrash.logcat(Log.ERROR, "TAG", " ??? logcat ???");
    FirebaseCrash.report(new Exception(" on create error ???"));
  }

  private PendingIntent buildPendingIntent(InviteMessage inviteMessage) {
    Intent intent = new Intent(this, TradingActivity.class);
    Bundle bundle = buildNotificationBundle(inviteMessage);
    intent.putExtras(bundle);
    return PendingIntent.getActivity(this,
        PENDING_INTENT_REQUEST_CODE,
        intent,
        PendingIntent.FLAG_UPDATE_CURRENT);
  }

  private void loadRemotConfig() {
    // Get Remote Config instance.
    firebaseRemoteConfig = FirebaseRemoteConfig.getInstance();
    FirebaseRemoteConfigSettings configSettings = new FirebaseRemoteConfigSettings.Builder()
        .setDeveloperModeEnabled(BuildConfig.DEBUG)
        .build();
    firebaseRemoteConfig.setConfigSettings(configSettings);
    firebaseRemoteConfig.setDefaults(R.xml.remote_config_defaults);

    final String key = "test_parameter";
    final String result = firebaseRemoteConfig.getString(key);

    long cacheExpiration = 3600; // 1 hour in seconds.
    // If your app is using developer mode, cacheExpiration is set to 0, so each fetch will
    // retrieve values from the service.
    if (firebaseRemoteConfig.getInfo().getConfigSettings().isDeveloperModeEnabled()) {
      cacheExpiration = 0;
    }

    // cacheExpirationSeconds is set to cacheExpiration here, indicating the next fetch request
    // will use fetch data from the Remote Config service, rather than cached parameter values,
    // if cached parameter values are more than cacheExpiration seconds old.
    // See Best Practices in the README for more information.
    firebaseRemoteConfig.fetch(cacheExpiration)
        .addOnCompleteListener(this, new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
              if (task.isSuccessful()) {
                Toast.makeText(TradingActivity.this, "Fetch Succeeded: " + result, Toast.LENGTH_SHORT).show();

                // After config data is successfully fetched,
                // it must be activated before newly fetched values are returned.
                firebaseRemoteConfig.activateFetched();
              } else {
                Toast.makeText(TradingActivity.this, "Fetch Failed", Toast.LENGTH_SHORT).show();
              }
            }
          });
  }
}
