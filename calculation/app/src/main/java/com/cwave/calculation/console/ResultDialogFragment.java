package com.cwave.calculation.console;

import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.cwave.calculation.R;

public class ResultDialogFragment extends DialogFragment {

  public static final String WRONG_NUMBER_KEY = "wrong_number_key";

  private static final String TAG = ResultDialogFragment.class.getSimpleName();

  private View dialogView;
  private TextView numberView;

  @Override
  public Dialog onCreateDialog(Bundle savedInstanceState) {
    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
    LayoutInflater inflater = getActivity().getLayoutInflater();
    dialogView = inflater.inflate(R.layout.result_dialog, null);
    builder = builder.setView(dialogView);

    numberView = dialogView.findViewById(R.id.wrong_answers);

    Bundle bundle = getArguments();
    numberView.setText(String.valueOf(bundle.getInt(WRONG_NUMBER_KEY)));

    builder.setTitle("Results");

    builder.setPositiveButton("OK", null);
    return builder.create();
  }
}
