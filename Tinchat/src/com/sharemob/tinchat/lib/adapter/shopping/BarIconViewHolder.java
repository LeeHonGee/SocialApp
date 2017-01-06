package com.sharemob.tinchat.lib.adapter.shopping;

import android.support.v7.widget.RecyclerView;
import android.widget.ImageView;
import android.view.View;
import android.widget.TextView;

import com.sharemob.tinchat.R;
import com.sharemob.tinchat.lib.LocalUtils;

final class BarIconViewHolder extends RecyclerView.ViewHolder {
    ImageView icon;
    View rootView;
    TextView title;
    
    public BarIconViewHolder(View view) {
        super(view);
        rootView = view;
        title = LocalUtils.getFindViewById(view, R.id.shopping_icon_title);
        icon = (ImageView)view.findViewById(R.id.shopping_icon);
    }
}