package com.cwave.exchange.trading;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.cwave.exchange.R;
import com.cwave.exchange.chat.ChatFragment;
import com.cwave.exchange.drawermenu.DrawerMenu;
import com.cwave.exchange.drawermenu.DrawerMenu.MenuField;
import com.cwave.exchange.post.PostFragment;
import com.cwave.exchange.post.PostMessage;
import com.cwave.exchange.signin.SignInActivity;
import com.cwave.exchange.trading.OfferDialogFragment.OfferListener;
import com.cwave.firebase.Auth;
import com.cwave.firebase.Database;
import com.cwave.firebase.Store;
import com.google.firebase.auth.FirebaseAuth;

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
    OfferListener {

  private static final String TAG = "TradingActivity";

  //private DrawerLayout drawer;
  private DrawerMenu drawerMenu;
  private RecyclerView recyclerView;

  @Inject
  DispatchingAndroidInjector<Fragment> fragmentInjector;

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
  public AndroidInjector<Fragment> fragmentInjector() {
    return fragmentInjector;
  }

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    AndroidInjection.inject(this);
    super.onCreate(savedInstanceState);
    setContentView(layout.activity_trading);
    Toolbar toolbar = (Toolbar) findViewById(id.toolbar);
    setSupportActionBar(toolbar);

    switchToRootFragement();

    //drawer = (DrawerLayout) findViewById(id.drawer_layout);
    //toggle = new ActionBarDrawerToggle(
    //    this, drawer, toolbar, string.navigation_drawer_open, string.navigation_drawer_close);
    //drawer.addDrawerListener(toggle);

    //NavigationView navigationView = (NavigationView) findViewById(id.nav_view);
    //navigationView.setNavigationItemSelectedListener(this);
    drawerMenu = new DrawerMenu((DrawerLayout) findViewById(R.id.drawer_layout), MenuField.SEARCH);
    drawerMenu.startMenu(this);

    FirebaseAuth.getInstance().addAuthStateListener(this);

    startPostFragment();
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

  private void startPostFragment() {
    PostFragment postFragment = new PostFragment();
    getFragmentManager().beginTransaction()
        .add(id.fragment_container, postFragment).commit();
  }

  private void replaceWithPostFragment() {
    PostFragment postFragment = new PostFragment();
    getFragmentManager().beginTransaction()
        .replace(id.fragment_container, postFragment).commit();
  }

  @Override
  public void onOfferSelectedClick(PostMessage post) {
    store.write(CollectionName.POSTS, post);
  }

  @Override
  public void onOfferCancelClick() {
    // do nothing.
  }
}
