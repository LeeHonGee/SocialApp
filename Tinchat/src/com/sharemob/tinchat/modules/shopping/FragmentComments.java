package com.sharemob.tinchat.modules.shopping;

import java.util.HashMap;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.sharemob.tinchat.R;
import com.sharemob.tinchat.component.AdvanceDecoration;
import com.sharemob.tinchat.lib.LocalUtils;
import com.sharemob.tinchat.lib.adapter.shopping.GoodsCommentsAdapter;
import com.sharemob.tinchat.lib.common.AsyncHttpRequest;

public class FragmentComments extends Fragment {
    private RecyclerView comments;
    private View rootView;
    
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_goods_comments, container, false);
        return rootView;
    }
    
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        comments = (RecyclerView)rootView.findViewById(R.id.shopping_goods_comments);
        comments.addItemDecoration(new AdvanceDecoration(rootView.getContext(),OrientationHelper.HORIZONTAL));
        comments.setLayoutManager(new StaggeredGridLayoutManager(0x1, StaggeredGridLayoutManager.VERTICAL));
    }
    
    public void onResume() {
        super.onResume();
        loadData();
    }
    
    public void loadData() {
        HashMap<String, Object> map = new HashMap<String, Object>();
        LocalUtils.requestHttp(10035, map, new AsyncHttpRequest.RequestCallback() {
            public void requestDidFinished(String json) {
                System.out.println(json);
                GoodsCommentsAdapter commentsAdapter = new GoodsCommentsAdapter(getActivity());
                commentsAdapter.addArray(json);
                comments.setAdapter(commentsAdapter);
            }
            
            public void requestDidFailed() {
            }
        });
    }
}