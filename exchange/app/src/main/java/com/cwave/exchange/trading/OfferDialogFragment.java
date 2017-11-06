package com.cwave.exchange.trading;

import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.cwave.exchange.R;
import com.cwave.exchange.post.PostMessage;
import com.cwave.exchange.util.Uuid;

import java.util.Calendar;

public class OfferDialogFragment extends DialogFragment {

  public static final String USER_KEY = "User: ";
  public static final String UID_KEY = "uid: ";
  public static final String REQUEST_TYPE = "request";

  private static final String TAG = OfferDialogFragment.class.getSimpleName();

  private static final String NO_MENU = "No menu available.";

  private View dialogView;
  private TextView nameView;
  private TextView uidView;
  private EditText fromView;
  private EditText toView;

  /**
   * {@link OfferDialogFragment} callback interface.
   */
  public interface OfferListener {
    void onOfferSelectedClick(PostMessage postMessage);
    void onOfferCancelClick();
  }

  private OfferListener offerListener;

  @Override
  public void onAttach(Context context) {
    Log.d(TAG, "onAttach");
    super.onAttach(context);
    try {
      offerListener = (OfferListener) context;
    } catch (ClassCastException e) {
      throw new ClassCastException(context + " must implement " + OfferListener.class.getSimpleName());
    }
  }

  @Override
  public Dialog onCreateDialog(Bundle savedInstanceState) {
    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
    LayoutInflater inflater = getActivity().getLayoutInflater();
    dialogView = inflater.inflate(R.layout.offer_dialog, null);
    builder = builder.setView(dialogView);

    nameView = dialogView.findViewById(R.id.offer_dialog_name);
    uidView = dialogView.findViewById(R.id.offer_dialog_uid);
    fromView = dialogView.findViewById(R.id.offer_dialog_from);
    toView = dialogView.findViewById(R.id.offer_dialog_to);

    Bundle bundle = getArguments();
    nameView.setText(bundle.getString(USER_KEY));
    uidView.setText(bundle.getString(UID_KEY));

    builder.setTitle("New Request");

    builder.setPositiveButton(
        "OK",
        new DialogInterface.OnClickListener() {
          @Override
          public void onClick(DialogInterface dialog, int id) {
            offerListener.onOfferSelectedClick(buildPostMessage());
          }
        })
        .setNegativeButton(
            "Cancel",
            new DialogInterface.OnClickListener() {
              @Override
              public void onClick(DialogInterface dialog, int id) {
                OfferDialogFragment.this.getDialog().cancel();
                offerListener.onOfferCancelClick();
              }
            });
    return builder.create();
  }

  private PostMessage buildPostMessage() {
    return PostMessage.builder()
        .setId(Uuid.getUuid())
        .setDate(Calendar.getInstance().getTime())
        .setName(nameView.getText().toString())
        .setUid(uidView.getText().toString())
        .setFrom(fromView.getText().toString())
        .setTo(toView.getText().toString())
        .build();
  }
}
