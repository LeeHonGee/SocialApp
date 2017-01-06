/**
 * 
 */
package com.sharemob.tinchat.modules.home;

import java.util.HashMap;
import java.util.Map;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;

import com.sharemob.tinchat.R;
import com.sharemob.tinchat.component.LazyFragment;
import com.sharemob.tinchat.lib.LocalUtils;
import com.sharemob.tinchat.lib.adapter.center.CenterItemAdapter;
import com.sharemob.tinchat.lib.common.AsyncHttpRequest;
import com.sharemob.tinchat.lib.refreashtabview.extras.PullLoadMoreRecyclerView;
import com.sharemob.tinchat.lib.refreashtabview.extras.PullLoadMoreRecyclerView.PullLoadMoreListener;
import com.sharemob.tinchat.modules.dao.cache.CacheManager;

/**
 * @author lihangjie
 * 
 */
public class FragmentCenter extends LazyFragment {

	private PullLoadMoreRecyclerView recyclerView;
	private CenterItemAdapter adapter;
	private View view;
	
	protected View initView(LayoutInflater inflater, ViewGroup container) {
		view = inflater.inflate(R.layout.fragment_center, container, false);
		
		view.findViewById(R.id.title_back).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
//				LocalUtils.enterActivity(view.getContext(), MessageActivity.class.getName());
				
				Intent intent = new Intent();
				intent.setClass(view.getContext(), MessageActivity.class);
				view.getContext().startActivity(intent);
				((Activity) view.getContext()).overridePendingTransition(R.anim.in_from_right,R.anim.animi_remain);
			}
		});
		
		adapter = new CenterItemAdapter(this.getActivity());
		recyclerView = (PullLoadMoreRecyclerView) view.findViewById(R.id.fragment_center_recyclerview);
		recyclerView.setGridLayout(1);
		recyclerView.setAdapter(adapter);
		recyclerView.scrollToTop();
		recyclerView.setPushRefreshEnable(false);
		recyclerView.setOnPullLoadMoreListener(new PullLoadMoreListener() {
			@Override
			public void onRefresh() {
				new GetDataTask().execute();
			}

			@Override
			public void onLoadMore() {
				new GetDataTask().execute();
			}
		});
		initData();
		return view;
	}
	
	@Override
	public void initData() {
//		Map<String, Object> map=new HashMap<String, Object>();
//		map.put("", CacheManager.getInstance().userInfo.uid);
		
		LocalUtils.requestHttpForApp(2009, new HashMap<String, Object>(),
				new AsyncHttpRequest.RequestCallback() {
					public void requestDidFinished(String body) {
						adapter.addArray(body);
						adapter.notifyDataSetChanged();
					}
					public void requestDidFailed() {}
		});
	}
	
	@Override
	public void onResume() {
		super.onResume();
		adapter.notifyItemChanged(0);
	}

	private class GetDataTask extends AsyncTask<Void, Void, String> {
		@Override
		protected String doInBackground(Void... params) {
			try {
				Thread.sleep(1000);
				initData();
			} catch (InterruptedException e) {
			}
			String str = "Added after refresh...I add";
			return str;
		}

		protected void onPostExecute(String result) {
			recyclerView.setPullLoadMoreCompleted();
			super.onPostExecute(result);
		}
	}
}
