package com.sharemob.tinchat.lib.adapter.shopping;

import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;

import com.sharemob.tinchat.R;
import com.sharemob.tinchat.component.AdvanceDecoration;
import com.sharemob.tinchat.component.BannerSlideView;

final class TopBannerViewHolder extends RecyclerView.ViewHolder {
    BannerSlideView banner;
    RecyclerView icon_gridview;
    View rootView;
    RecyclerView shopping_saletime;
    
    public TopBannerViewHolder(View view) {
        super(view);
        rootView = view;
        banner = (BannerSlideView)view.findViewById(R.id.shopping_top_banner);
        
        icon_gridview = (RecyclerView)view.findViewById(R.id.shopping_icon_gridview);
        icon_gridview.setHasFixedSize(false);
        
        StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(0x4, StaggeredGridLayoutManager.VERTICAL);
        icon_gridview.setLayoutManager(layoutManager);
        
        shopping_saletime = (RecyclerView)view.findViewById(R.id.shopping_saletime);
        shopping_saletime.setHasFixedSize(false);
        shopping_saletime.addItemDecoration(new AdvanceDecoration(view.getContext(), OrientationHelper.HORIZONTAL));
        shopping_saletime.setLayoutManager(new StaggeredGridLayoutManager(0x1, StaggeredGridLayoutManager.HORIZONTAL));
    }
}