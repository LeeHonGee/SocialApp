package com.sharemob.tinchat.lib.adapter.center;

import android.support.v7.widget.RecyclerView;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.sharemob.tinchat.R;
import com.sharemob.tinchat.lib.LocalUtils;

final class CenterItemViewHolder extends RecyclerView.ViewHolder {
    View rootView;
    ImageView icon;
    TextView title;
    FrameLayout view;
    public CenterItemViewHolder(View view) {
        super(view);
        rootView = view;
        icon = (ImageView)view.findViewById(R.id.fragment_center_item_icon);
        title = LocalUtils.getFindViewById(view, R.id.fragment_center_item_title);
        this.view = (FrameLayout)view.findViewById( R.id.fragment_center_item_view);
        icon = (ImageView)view.findViewById(R.id.fragment_center_item_icon);
  
    }
}