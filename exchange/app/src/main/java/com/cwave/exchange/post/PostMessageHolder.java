package com.cwave.exchange.post;

import android.graphics.PorterDuff;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.RotateDrawable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cwave.exchange.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Calendar;

public class PostMessageHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
  private static final String TAG = "PostMessageHolder";

  private final TextView nameField;
  private final TextView uidField;
  private final TextView fromField;
  private final TextView toField;
  private final FrameLayout leftArrow;
  private final FrameLayout rightArrow;
  private final RelativeLayout messageContainer;
  private final LinearLayout message;
  private final int mGreen300;
  private final int mGray300;

  private String id;

  private PostMessageClickListener postMessageClickListener;

  /** {@link PostMessageHolder} callback interface. */
  public interface PostMessageClickListener {
    void onPostMessageClick(PostMessage post);
  }

  public PostMessageHolder(View itemView, PostMessageClickListener listener) {
    super(itemView);
    nameField = itemView.findViewById(R.id.post_message_name);
    uidField = itemView.findViewById(R.id.post_message_uid);
    fromField = itemView.findViewById(R.id.post_message_from_currency);
    toField = itemView.findViewById(R.id.post_message_to_currency);
    leftArrow = itemView.findViewById(R.id.left_arrow);
    rightArrow = itemView.findViewById(R.id.right_arrow);
    messageContainer = itemView.findViewById(R.id.post_message_container);
    message = itemView.findViewById(R.id.post_message);
    mGreen300 = ContextCompat.getColor(itemView.getContext(), R.color.material_green_300);
    mGray300 = ContextCompat.getColor(itemView.getContext(), R.color.material_gray_300);

    itemView.setOnClickListener(this);
    this.postMessageClickListener = listener;
  }

  public void bind(PostMessage post) {
    nameField.setText(post.getName());
    uidField.setText(post.getUid());
    fromField.setText(post.getFrom());
    toField.setText(post.getTo());

    id = post.getId();

    FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
    setIsSender(currentUser != null && post.getUid().equals(currentUser.getUid()));
  }

  private void setIsSender(boolean isSender) {
    final int color;
    if (isSender) {
      color = mGreen300;
      leftArrow.setVisibility(View.GONE);
      rightArrow.setVisibility(View.VISIBLE);
      messageContainer.setGravity(Gravity.END);
    } else {
      color = mGray300;
      leftArrow.setVisibility(View.VISIBLE);
      rightArrow.setVisibility(View.GONE);
      messageContainer.setGravity(Gravity.START);
    }

    ((GradientDrawable) message.getBackground()).setColor(color);
    ((RotateDrawable) leftArrow.getBackground()).getDrawable()
        .setColorFilter(color, PorterDuff.Mode.SRC);
    ((RotateDrawable) rightArrow.getBackground()).getDrawable()
        .setColorFilter(color, PorterDuff.Mode.SRC);
  }

  @Override
  public void onClick(View v) {
    Log.d(TAG, "onclicked" + " name:" + nameField.getText().toString()
        + " uid:" + uidField.getText().toString()
        + " from:" + fromField.getText().toString()
        + " to:" + toField.getText().toString());
    postMessageClickListener.onPostMessageClick(
        PostMessage.builder()
            .setId(id)
            .setName(nameField.getText().toString())
            .setUid(uidField.getText().toString())
            .setDate(Calendar.getInstance().getTime())
            .setFrom(fromField.getText().toString())
            .setTo(toField.getText().toString())
            .build());
  }
}
