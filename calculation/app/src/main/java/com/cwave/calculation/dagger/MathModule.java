package com.cwave.calculation.dagger;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import com.cwave.calculation.R;
import com.cwave.calculation.console.DiagLogReader;
import com.cwave.calculation.console.LogReader;
import com.cwave.calculation.service.InMemoryQuestionStoreImpl;
import com.cwave.calculation.service.NetworkConnectionService;
import com.cwave.calculation.service.NetworkConnectionServiceImpl;
import com.cwave.calculation.service.QuestionGenerator;
import com.cwave.calculation.service.QuestionStore;
import com.cwave.calculation.service.SimpleQuestionGenerator;
import com.squareup.otto.Bus;
import dagger.Module;
import dagger.Provides;
import java.util.Calendar;
import java.util.GregorianCalendar;
import javax.inject.Singleton;

/** Dagger {@link Module} for application-scoped injections. */
@Module
public class MathModule {

  private Application application;

  public MathModule(Application application) {
    this.application = application;
  }

  @Provides
  @Singleton
  Application provideApplication() {
    return application;
  }

  @Provides
  @Singleton
  static LogReader provideLogReader(Application application) {
    return new DiagLogReader(application);
  }

  @Provides
  @Singleton
  static QuestionGenerator provideQuestionGenerator() {
    return new SimpleQuestionGenerator();
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
  static NetworkConnectionService provideNetworkConnectionChecker(Application application) {
    return new NetworkConnectionServiceImpl(
        (ConnectivityManager) application.getSystemService(Context.CONNECTIVITY_SERVICE));
  }

  @Provides
  @Singleton
  static QuestionStore provideQuestionStore() {
    return new InMemoryQuestionStoreImpl();
  }
}
