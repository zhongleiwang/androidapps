package com.cwave.exchange.trading;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.cwave.exchange.R;
import com.cwave.exchange.signin.SignInActivity;
import com.cwave.firebase.Auth;
import com.cwave.firebase.Database;
import com.cwave.firebase.Store;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Calendar;
import java.util.Date;

import javax.inject.Inject;

import dagger.android.AndroidInjection;

import static com.google.common.base.Preconditions.checkNotNull;

//import com.cwave.firebase.Auth;

public class TradingActivity extends AppCompatActivity
    implements NavigationView.OnNavigationItemSelectedListener, FirebaseAuth.AuthStateListener {

  private static final String TAG = "????TradingActivity";

  private DrawerLayout drawer;
  private ActionBarDrawerToggle toggle;
  private RecyclerView recyclerView;

  @Inject
  Database database;

  @Inject
  Store store;

  @Inject
  Auth auth;

  /** Starts a new {@link TradingActivity} instance. */
  public static void startActivity(Context context) {
    checkNotNull(context);
    context.startActivity(new Intent(context, TradingActivity.class));
  }

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    AndroidInjection.inject(this);
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_trading);
    Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
    setSupportActionBar(toolbar);

    FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
    fab.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
          createPost(view);
        }
      });

    drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
    toggle = new ActionBarDrawerToggle(
        this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
    drawer.addDrawerListener(toggle);

    NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
    navigationView.setNavigationItemSelectedListener(this);

    FirebaseAuth.getInstance().addAuthStateListener(this);

    if (findViewById(R.id.fragment_container) != null) {
      PostFragment postFragment = new PostFragment();

      getSupportFragmentManager().beginTransaction()
          .add(R.id.fragment_container, postFragment).commit();
    }
  }

  @Override
  public void onDestroy() {
    super.onDestroy();
    FirebaseAuth.getInstance().removeAuthStateListener(this);
    drawer.removeDrawerListener(toggle);
  }

  @Override
  public void onBackPressed() {
    DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
    if (drawer.isDrawerOpen(GravityCompat.START)) {
      drawer.closeDrawer(GravityCompat.START);
    } else {
      super.onBackPressed();
    }
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

    //noinspection SimplifiableIfStatement
    if (id == R.id.action_settings) {
      return true;
    }

    return super.onOptionsItemSelected(item);
  }

  @SuppressWarnings("StatementWithEmptyBody")
  @Override
  public boolean onNavigationItemSelected(MenuItem item) {
    // Handle navigation view item clicks here.
    int id = item.getItemId();

    if (id == R.id.nav_camera) {
      // Handle the camera action
    } else if (id == R.id.nav_gallery) {

    } else if (id == R.id.nav_slideshow) {

    } else if (id == R.id.nav_manage) {

    } else if (id == R.id.nav_share) {

    } else if (id == R.id.nav_send) {

    }

    DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
    drawer.closeDrawer(GravityCompat.START);
    return true;
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

  private void createPost(View view) {
    Log.d(TAG, "data store read/write test");
    database.read();
    database.write();
    store.write();

    Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
        .setAction("Action", null).show();

    Post post1 = new Post("wang", "123345", getCurrentTime(),
        "RMB", "Dollar", 1.0f, 100.0f, 100.0f, true);
    store.write(CollectionName.POSTS, post1);

    Post post3 = Post.builder()
        .setName("my")
        .setUid(auth.getCurrentUser().getUid())
        .setRate(10)
        .setDate(getCurrentTime())
        .setFrom("Yen")
        .build();
    store.write(CollectionName.POSTS, post3);
  }

  private static Date getCurrentTime() {
    return Calendar.getInstance().getTime();
  }
}
