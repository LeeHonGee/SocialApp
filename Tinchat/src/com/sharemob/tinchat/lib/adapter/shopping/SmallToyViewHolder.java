package com.sharemob.tinchat.lib.adapter.shopping;

import android.support.v7.widget.RecyclerView;
import android.widget.ImageView;
import android.view.View;
import android.widget.TextView;

import com.sharemob.tinchat.R;
import com.sharemob.tinchat.lib.LocalUtils;

final class SmallToyViewHolder extends RecyclerView.ViewHolder {
    ImageView image;
    View rootView;
    TextView subtitle;
    TextView title;
    
    public SmallToyViewHolder(View view) {
        super(view);
        rootView = view;
        title = LocalUtils.getFindViewById(view, R.id.item_title);
        subtitle = LocalUtils.getFindViewById(view, R.id.item_subtitle);
        image = (ImageView)view.findViewById(R.id.item_img);
    }
}