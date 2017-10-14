package com.cwave.calculation.drawermenu;

import static com.google.common.base.Preconditions.checkNotNull;

import android.accounts.Account;
import android.app.Activity;
import android.content.Intent;
import android.os.Build.VERSION_CODES;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.design.widget.NavigationView;
import android.support.design.widget.NavigationView.OnNavigationItemSelectedListener;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import com.cwave.calculation.R;

/** Wrapper for {@link DrawerLayout}. */
public final class DrawerMenu {
  private static final String TAG = DrawerMenu.class.getSimpleName();

  private static final int DELAY_MILLI_SEC = 300;

  /** Navigation options. */
  public enum MenuField {
    HOME,
    DEBUG;

    public int itemId() {
      switch (this) {
        case HOME:
          return R.id.action_home;
        case DEBUG:
          return R.id.action_debug;
      }
      return 0;
    }
  }

  private static final String BASE_PATH = "com.cwave.calculation";
  private static final String MATH_LIST_PATH = BASE_PATH + ".math.MathListActivity";
  private static final String DEBUG_PATH = BASE_PATH + ".console.ConsoleActivity";

  private final DrawerLayout drawerLayout;
  private final MenuField field;

  private NavigationView drawerMenuView;
  private TextView menuHeaderEmail;

  /** Constructor. */
  public DrawerMenu(DrawerLayout drawerLayout, MenuField field) {
    this.drawerLayout = checkNotNull(drawerLayout);
    this.field = checkNotNull(field);
  }

  /**
   * Prepares the navigation menu.
   *
   * @param account the account.
   * @param activity the parent activity.
   */
  @RequiresApi(api = VERSION_CODES.DONUT)
  public void startMenu(@Nullable Account account, final Activity activity) {
    drawerMenuView = (NavigationView) drawerLayout.findViewById(R.id.drawer_menu_view);

    View headerLayout = drawerMenuView.inflateHeaderView(R.layout.drawer_menu_header);
    menuHeaderEmail = (TextView) headerLayout.findViewById(R.id.drawer_layout_header_email);
    if (account != null) {
      menuHeaderEmail.setText(account.name);
      menuHeaderEmail.setContentDescription(
          activity.getString(R.string.signed_in_description, account.name));
    }
    drawerMenuView.setNavigationItemSelectedListener(
        new OnNavigationItemSelectedListener() {
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
  @RequiresApi(api = VERSION_CODES.CUPCAKE)
  public void openMenu() {
    drawerMenuView.getMenu().getItem(field.ordinal()).setChecked(true);
    drawerLayout.openDrawer(GravityCompat.START);
  }

  /** Returns whether is the navigation menu is shown. */
  public boolean isOpen() {
    return drawerLayout.isDrawerOpen(Gravity.START);
  }

  @RequiresApi(api = VERSION_CODES.DONUT)
  protected boolean handleDrawerMenuItemSelected(MenuItem menuItem, final Activity activity) {
    drawerLayout.closeDrawer(GravityCompat.START);
    final int itemId = menuItem.getItemId();
    if (itemId == field.itemId()) {
      return true;
    }

    // Delaying the drawer menu handling allows for a smooth transition and a clean screenshot
    // when submitting feedback.
    drawerLayout.postDelayed(
        new Runnable() {
          @Override
          public void run() {
            if (itemId == R.id.action_home) {
              activity.startActivity(createIntent(activity, MATH_LIST_PATH));
            } else if (itemId == R.id.action_debug) {
              activity.startActivity(createIntent(activity, DEBUG_PATH));
            }
          }
        },
        DELAY_MILLI_SEC);
    return true;
  }

  private static Intent createIntent(Activity activity, String className) {
    return new Intent().setClassName(activity, className);
  }
}
