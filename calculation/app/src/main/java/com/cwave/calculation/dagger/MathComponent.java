package com.cwave.calculation.dagger;

import com.cwave.calculation.console.ConsoleComponent;

import dagger.Component;
import javax.inject.Singleton;

/** Dagger {@link Component} for application-scoped injections. */
@Singleton
@Component(modules = MathModule.class)
public interface MathComponent extends ConsoleComponent {

  void inject(MathApplication target);
}
