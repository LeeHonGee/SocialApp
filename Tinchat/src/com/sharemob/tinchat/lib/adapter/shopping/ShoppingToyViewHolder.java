package com.sharemob.tinchat.lib.adapter.shopping;

import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;
import android.widget.TextView;

import com.sharemob.tinchat.R;
import com.sharemob.tinchat.component.AdvanceDecoration;
import com.sharemob.tinchat.component.BannerSlideView;
import com.sharemob.tinchat.lib.photoview.NoScrollGridView;

final class ShoppingToyViewHolder extends RecyclerView.ViewHolder {
    BannerSlideView banner;
    RecyclerView recyclerview;
    View rootView;
    NoScrollGridView shopping_toy_gridview;
    TextView title;
    
    public ShoppingToyViewHolder(View view) {
        super(view);
        rootView = view;
        title = (TextView)view.findViewById(R.id.shopping_toy_title);
        banner = (BannerSlideView)view.findViewById(R.id.shopping_toy_banner);
        recyclerview = (RecyclerView)view.findViewById(R.id.shopping_toy_recyclerview);
        
        recyclerview.setHasFixedSize(false);
        StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(0x3, StaggeredGridLayoutManager.VERTICAL);
        recyclerview.addItemDecoration(new AdvanceDecoration(rootView.getContext(), OrientationHelper.HORIZONTAL));
        recyclerview.setLayoutManager(layoutManager);
    }
}