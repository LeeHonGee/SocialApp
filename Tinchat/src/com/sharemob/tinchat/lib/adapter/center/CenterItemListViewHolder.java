package com.sharemob.tinchat.lib.adapter.center;

import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.sharemob.tinchat.R;
import com.sharemob.tinchat.component.AdvanceDecoration;

final class CenterItemListViewHolder extends RecyclerView.ViewHolder {
    View rootView;
    RecyclerView item_list;
//    PullLoadMoreRecyclerView item_list;
    public CenterItemListViewHolder(View view) {
        super(view);
        rootView = view;
//        item_list = (PullLoadMoreRecyclerView)view.findViewById(R.id.fragment_center_item_recyclerview);
//        item_list.RecyclerView().setHasFixedSize(true);
//        item_list.setLinearLayout();
//        StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL);
//        item_list.RecyclerView().addItemDecoration(new AdvanceDecoration(rootView.getContext(), OrientationHelper.VERTICAL));
//        item_list.RecyclerView().setLayoutManager(layoutManager);
        
        item_list = (RecyclerView)view.findViewById(R.id.fragment_center_item_recyclerview);
        item_list.addItemDecoration(new AdvanceDecoration(view.getContext(), OrientationHelper.HORIZONTAL));
        item_list.setLayoutManager(new GridLayoutManager(view.getContext(),1));
    }
}