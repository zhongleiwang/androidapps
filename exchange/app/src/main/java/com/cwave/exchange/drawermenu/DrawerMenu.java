package com.cwave.exchange.drawermenu;

import android.app.Activity;
import android.content.Intent;
import android.support.design.widget.NavigationView;
import android.support.design.widget.NavigationView.OnNavigationItemSelectedListener;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;

import com.cwave.exchange.R;

import static com.google.common.base.Preconditions.checkNotNull;

/** Wrapper for {@link DrawerLayout}. */
public final class DrawerMenu {
  private static final String TAG = "DrawerMenu";

  private static final int DELAY_MILLI_SEC = 300;
  private final DrawerLayout drawerLayout;
  private final MenuField field;
  private NavigationView drawerMenuView;

  /** Constructor. */
  public DrawerMenu(DrawerLayout drawerLayout, MenuField field) {
    this.drawerLayout = checkNotNull(drawerLayout);
    this.field = checkNotNull(field);
  }

  private static Intent createIntent(Activity activity, String className) {
    return new Intent().setClassName(activity, className);
  }

  /**
   * Prepares the navigation menu.
   *
   * @param activity the parent activity.
   */
  public void startMenu(final Activity activity) {
    drawerMenuView = (NavigationView) drawerLayout.findViewById(R.id.nav_view);
    drawerMenuView.setNavigationItemSelectedListener(new OnNavigationItemSelectedListener() {
      @Override
      public boolean onNavigationItemSelected(MenuItem menuItem) {
        return handleDrawerMenuItemSelected(menuItem, activity);
      }
    });
  }

  /** Closes the navigation menu. */
  public void closeMenu() {
    drawerLayout.closeDrawer(GravityCompat.START);
  }

  /** Shows the navigation menu. */
  public void openMenu() {
    //drawerMenuView.getMenu().getItem(field.ordinal()).setChecked(true);
    drawerLayout.openDrawer(GravityCompat.START);
  }

  /** Returns whether is the navigation menu is shown. */
  public boolean isOpen() {
    return drawerLayout.isDrawerOpen(Gravity.START);
  }

  protected boolean handleDrawerMenuItemSelected(MenuItem menuItem, final Activity activity) {
    drawerLayout.closeDrawer(GravityCompat.START);
    final int id = menuItem.getItemId();
    if (id == field.itemId()) {
      return true;
    }

    // Delaying the drawer menu handling allows for a smooth transition and a clean screenshot
    // when submitting feedback.
    drawerLayout.postDelayed(
        new Runnable() {
          @Override
          public void run() {

            if (id == R.id.nav_camera) {
              // Handle the camera action
            } else if (id == R.id.nav_gallery) {
              Log.d(TAG, "galley");
            } else if (id == R.id.nav_slideshow) {
              // activity.startActivity(createIntent(activity, PATH));
              Log.d(TAG, "slideshow");
            } else if (id == R.id.nav_manage) {
              Log.d(TAG, "manage");
            } else if (id == R.id.nav_share) {
              Log.d(TAG, "share");
            } else if (id == R.id.nav_send) {
              Log.d(TAG, "send");
            }
          }
        },
        DELAY_MILLI_SEC);
    return true;
  }

  /** Navigation options. */
  public enum MenuField {
    SEARCH, SETTINGS;

    public int itemId() {
      switch (this) {
        case SEARCH:
          return R.id.action_search;
        case SETTINGS:
          return R.id.action_settings;
      }
      return 0;
    }
  }
}
