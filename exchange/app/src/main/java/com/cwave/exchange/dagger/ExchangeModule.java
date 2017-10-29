package com.cwave.exchange.dagger;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

import com.cwave.exchange.R;
import com.cwave.exchange.invite.InviteManager;
import com.cwave.exchange.invite.InviteManagerImpl;
import com.cwave.firebase.Auth;
import com.cwave.firebase.AuthImpl;
import com.cwave.firebase.Database;
import com.cwave.firebase.DatabaseImpl;
import com.cwave.firebase.Store;
import com.cwave.firebase.StoreImpl;
import com.squareup.otto.Bus;

import java.util.Calendar;
import java.util.GregorianCalendar;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/** Dagger {@link Module} for application-scoped injections. */
@Module
public class ExchangeModule {

  private Application application;

  public ExchangeModule(Application application) {
    this.application = application;
  }

  @Provides
  @Singleton
  Application provideApplication() {
    return application;
  }

  @Provides
  @Singleton
  static Calendar provideCalendar() {
    return new GregorianCalendar();
  }

  @Provides
  @Singleton
  static Bus provideBus() {
    return new Bus();
  }

  @Provides
  @Singleton
  static SharedPreferences provideSharedPreferences(Application application) {
    return application.getSharedPreferences(
        application.getString(R.string.app_name), Context.MODE_MULTI_PROCESS);
  }

  @Provides
  @Singleton
  static Database provideFirebaseDatabase() {
    return new DatabaseImpl();
  }

  @Provides
  @Singleton
  static Store provideFirebaseStore() {
    return new StoreImpl();
  }

  @Provides
  @Singleton
  static Auth provideFirebaseAuth() {
    return new AuthImpl();
  }

  @Provides
  @Singleton
  static InviteManager provideInviteManagerImpl() {
    return new InviteManagerImpl();
  }
}
