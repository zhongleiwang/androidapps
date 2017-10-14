package com.cwave.calculation.console;

/** Events that are used to comminucate between compoents. */
public class MathEvents {

  /** Event to be published when data have been retrieved for displaying. */
  public static class MathLogDataRetrievedEvent {}

  /** Event to be published when data could not be retrieved. */
  public static class MathLogDataRetrieveFailedEvent {}

  /** Event to be published when data should be refreshed. */
  public static class MathLogDataRefreshEvent {}

  /** Event to be published when data are requested. */
  public static class MathLogDataRequestedEvent {}

  /** Helper function to create {@link MathLogDataRequestedEvent}. */
  public static MathLogDataRequestedEvent createMathLogDataRequestedEvent() {
    return new MathLogDataRequestedEvent();
  }
}
