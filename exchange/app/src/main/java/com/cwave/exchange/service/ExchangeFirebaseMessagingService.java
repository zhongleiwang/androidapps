package com.cwave.exchange.service;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import com.cwave.exchange.R;
import com.cwave.exchange.trading.TradingActivity;
import com.firebase.jobdispatcher.Constraint;
import com.firebase.jobdispatcher.FirebaseJobDispatcher;
import com.firebase.jobdispatcher.GooglePlayDriver;
import com.firebase.jobdispatcher.Job;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

public class ExchangeFirebaseMessagingService extends FirebaseMessagingService {

  private static final String TAG = "EFMsgService";

  /**
   * Called when message is received.
   *
   * @param remoteMessage Object representing the message received from Firebase Cloud Messaging.
   */
  @Override
  public void onMessageReceived(RemoteMessage remoteMessage) {
    // [START_EXCLUDE]
    // There are two types of messages data messages and notification messages. Data messages are handled
    // here in onMessageReceived whether the app is in the foreground or background. Data messages are the type
    // traditionally used with GCM. Notification messages are only received here in onMessageReceived when the app
    // is in the foreground. When the app is in the background an automatically generated notification is displayed.
    // When the user taps on the notification they are returned to the app. Messages containing both notification
    // and data payloads are treated as notification messages. The Firebase console always sends notification
    // messages. For more see: https://firebase.google.com/docs/cloud-messaging/concept-options
    // [END_EXCLUDE]

    // TODO(developer): Handle FCM messages here.
    // Not getting messages here? See why this may be: https://goo.gl/39bRNJ
    Log.d(TAG, "From: " + remoteMessage.getFrom());

    // Check if message contains a data payload.
    if (remoteMessage.getData().size() > 0) {
      Log.d(TAG, "Message data payload: " + remoteMessage.getData());

      if (/* Check if data needs to be processed by long running job */ true) {
        // For long-running tasks (10 seconds or more) use Firebase Job Dispatcher.
        scheduleJob();
      } else {
        // Handle message within 10 seconds
        handleNow();
      }
    }

    // Check if message contains a notification payload.
    if (remoteMessage.getNotification() != null) {
      sendNotification("XXX");
      Log.d(TAG, "Message Notification Body: " + remoteMessage.getNotification().getBody());
    }
  }

  public void onDeletedMessages() {
    Log.d(TAG, "onDeletedMessages");
  }

  @android.support.annotation.WorkerThread
  public void onMessageSent(String s) {
    Log.d(TAG, "onMessageSent " + s);
  }

  @android.support.annotation.WorkerThread
  public void onSendError(String s, Exception e) {
    Log.d(TAG, "send " + s + " error: " + e);
  }
  
  /**
   * Schedule a job using FirebaseJobDispatcher.
   */
  private void scheduleJob() {
    FirebaseJobDispatcher dispatcher = new FirebaseJobDispatcher(new GooglePlayDriver(this));
    Job myJob = dispatcher.newJobBuilder()
        .setService(ExchangeJobService.class)
        .setTag("exchange-job-tag")
        .build();
    dispatcher.schedule(myJob);
  }

  /**
   * Handle time allotted to BroadcastReceivers.
   */
  private void handleNow() {
    Log.d(TAG, "Short lived task is done.");
  }

  /**
   * Create and show a simple notification containing the received FCM message.
   *
   * @param messageBody FCM message body received.
   */
  private void sendNotification(String messageBody) {
    Intent intent = new Intent(this, TradingActivity.class);
    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
    PendingIntent pendingIntent = PendingIntent.getActivity(
        this,
        0 /* Request code */,
        intent,
        PendingIntent.FLAG_ONE_SHOT);

    Log.d(TAG, "send notification");
    String channelId = getString(R.string.default_notification_channel_id);
    Uri defaultSoundUri= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
    NotificationCompat.Builder notificationBuilder =
        new NotificationCompat.Builder(this)
        .setSmallIcon(R.drawable.ic_notifications_active_black_24dp)
        .setContentTitle("FCM Message")
        .setContentText(messageBody);

    NotificationManager notificationManager =
        (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

    notificationManager.notify(0 /* ID of notification */, notificationBuilder.build());
  }
}
