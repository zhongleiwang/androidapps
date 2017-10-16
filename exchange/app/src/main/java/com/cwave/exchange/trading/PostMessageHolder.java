package com.cwave.exchange.trading;

import android.graphics.PorterDuff;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.RotateDrawable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cwave.exchange.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class PostMessageHolder extends RecyclerView.ViewHolder {
  private final TextView nameField;
  private final TextView textField;
  private final FrameLayout leftArrow;
  private final FrameLayout rightArrow;
  private final RelativeLayout messageContainer;
  private final LinearLayout message;
  private final int mGreen300;
  private final int mGray300;

  public PostMessageHolder(View itemView) {
    super(itemView);
    nameField = itemView.findViewById(R.id.name);
    textField = itemView.findViewById(R.id.from_currency);
    leftArrow = itemView.findViewById(R.id.left_arrow);
    rightArrow = itemView.findViewById(R.id.right_arrow);
    messageContainer = itemView.findViewById(R.id.post_message_container);
    message = itemView.findViewById(R.id.post_message);
    mGreen300 = ContextCompat.getColor(itemView.getContext(), R.color.material_green_300);
    mGray300 = ContextCompat.getColor(itemView.getContext(), R.color.material_gray_300);
  }

  public void bind(AbstractChat post) {
    setName(post.getName());
    setText(post.getUid());

    FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
    setIsSender(currentUser != null && post.getUid().equals(currentUser.getUid()));
  }

  private void setName(String name) {
    nameField.setText(name);
  }

  private void setText(String text) {
    textField.setText(text);
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
}
