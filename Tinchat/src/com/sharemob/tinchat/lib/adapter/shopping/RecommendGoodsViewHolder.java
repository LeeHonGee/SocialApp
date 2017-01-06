package com.sharemob.tinchat.lib.adapter.shopping;

import android.support.v7.widget.RecyclerView;
import android.widget.ImageView;
import android.widget.TextView;
import android.view.View;

import com.sharemob.tinchat.R;
import com.sharemob.tinchat.lib.LocalUtils;

final class RecommendGoodsViewHolder extends RecyclerView.ViewHolder {
	View rootView;
    ImageView icon;
    TextView price;
    TextView title;
    
    public RecommendGoodsViewHolder(View view) {
        super(view);
        rootView = view;
        title = LocalUtils.getFindViewById(view, R.id.recommend_goods_title);
        price = LocalUtils.getFindViewById(view, R.id.recommend_goods_price);
        icon = (ImageView)view.findViewById(R.id.recommend_goods_icon);
    }
}