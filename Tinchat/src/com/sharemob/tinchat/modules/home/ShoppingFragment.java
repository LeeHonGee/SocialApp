package com.sharemob.tinchat.modules.home;

import java.util.HashMap;
import java.util.Map;

import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.sharemob.tinchat.R;
import com.sharemob.tinchat.component.BannerSlideView;
import com.sharemob.tinchat.component.LazyFragment;
import com.sharemob.tinchat.lib.LocalUtils;
import com.sharemob.tinchat.lib.adapter.shopping.ShoppingAdapter;
import com.sharemob.tinchat.lib.common.AsyncHttpRequest;
import com.sharemob.tinchat.lib.refreashtabview.extras.PullLoadMoreRecyclerView;

public class ShoppingFragment extends LazyFragment {
    private ShoppingAdapter adapter;
    private PullLoadMoreRecyclerView pullToRefresh;
    
    public void initBanner(BannerSlideView bannerSlideView, float banner_size) {
        LinearLayout.LayoutParams linearParams = (LinearLayout.LayoutParams)bannerSlideView.getLayoutParams();
        linearParams.height = (int)(linearParams.height * banner_size);
        bannerSlideView.setLayoutParams(linearParams);
    }
    
    public View initView(LayoutInflater inflater, ViewGroup container) {
        View view = inflater.inflate(R.layout.layout_shopping_fragment, container, false);
        pullToRefresh = (PullLoadMoreRecyclerView)view.findViewById(R.id.shopping_recyclerview);
        pullToRefresh.setLinearLayout();
        adapter = new ShoppingAdapter(getActivity());
        pullToRefresh.setAdapter(adapter);
        pullToRefresh.setOnPullLoadMoreListener(new PullLoadMoreRecyclerView.PullLoadMoreListener() {
            public void onRefresh() {
                new GetDataTask().execute();
            }
            
            public void onLoadMore() {
                new GetDataTask().execute();
            }
        });
        return view;
    }
    
    public void initData() {
        Map<String, Object> map = new HashMap<String, Object>();
        LocalUtils.requestHttpForApp(2008, map, new AsyncHttpRequest.RequestCallback() {
            public void requestDidFinished(String body) {
                adapter.addArray(body);
                adapter.notifyDataSetChanged();
            }
            public void requestDidFailed() {}
        });
    }
    
	@Override
	protected void onInvisible() {
		initData();
	}
	
	@Override
	public void onResume() {
		super.onResume();
		initData();
	}
	
    class GetDataTask extends AsyncTask<Void, Void, String> {
    	protected String doInBackground(Void... params) {
            try {
                Thread.sleep(1000L);
                initData();
            } catch(InterruptedException localInterruptedException1) {
            }
            return null;
        }
        
        protected void onPostExecute(String result) {
            pullToRefresh.setPullLoadMoreCompleted();
            super.onPostExecute(result);
        }
    }
}