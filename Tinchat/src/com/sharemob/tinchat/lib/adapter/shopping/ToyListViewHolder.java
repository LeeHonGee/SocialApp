package com.sharemob.tinchat.lib.adapter.shopping;

import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.sharemob.tinchat.R;
import com.sharemob.tinchat.lib.refreashtabview.extras.PullLoadMoreRecyclerView;

final class ToyListViewHolder extends RecyclerView.ViewHolder {
    View rootView;
    PullLoadMoreRecyclerView toy_list;
    
    public ToyListViewHolder(View view) {
        super(view);
        rootView = view;
        toy_list = (PullLoadMoreRecyclerView)view.findViewById(R.id.shopping_toylist_recyclerview);
        toy_list.setGridLayout(0x1);
        toy_list.RecyclerView().setHasFixedSize(false);
    }
}