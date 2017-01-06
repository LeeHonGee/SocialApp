/**
 *  文件名:DynamicFragment.java
 *  修改人:lihangjie
 *  创建时间:2016-4-15 下午11:51:56
 */
package com.sharemob.tinchat.modules.dynamic;

import java.util.HashMap;
import java.util.Map;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.sharemob.tinchat.R;
import com.sharemob.tinchat.component.LazyFragment;
import com.sharemob.tinchat.lib.LocalUtils;
import com.sharemob.tinchat.lib.adapter.DynamicArticleItemAdapter;
import com.sharemob.tinchat.lib.common.AsyncHttpRequest.RequestCallback;
import com.sharemob.tinchat.lib.refreashtabview.extras.PullLoadMoreRecyclerView;
import com.sharemob.tinchat.lib.refreashtabview.extras.PullLoadMoreRecyclerView.PullLoadMoreListener;
import com.sharemob.tinchat.modules.dao.cache.CacheManager;

/**
 * 
 * <一句话功能简述>
 * 
 * @author lihangjie version [版本号,2016-4-15 下午11:51:56]
 * @see [相关类/方法]
 * @since [产品/模块版本]
 * 
 */
public class NearbyDynamicFragment extends LazyFragment {
	private PullLoadMoreRecyclerView pullToRefresh;
	private DynamicArticleItemAdapter adapter;
	private View view;
	
	private void initDynamic() {
		pullToRefresh = (PullLoadMoreRecyclerView) view.findViewById(R.id.recyclerview_dynamic);
		pullToRefresh.setGridLayout(1);
		adapter=new DynamicArticleItemAdapter(view.getContext());
		pullToRefresh.setAdapter(adapter);
		adapter.setPullLoadMoreRecyclerView(pullToRefresh);
		
		pullToRefresh.setOnPullLoadMoreListener(new PullLoadMoreListener() {
			public void onRefresh() {
				new GetDataTask().execute();
			}
			public void onLoadMore() {
				new GetDataTask().execute();
			}
		});
	}
	
	public void loadData(){
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("uid", CacheManager.getInstance().getUserInfoUid());
		map.put("currentPage",adapter.currentPage);
		     
		LocalUtils.requestHttp(10056,map,new RequestCallback() {
				public void requestDidFinished(String body) {
					try {
						JSONObject object=new JSONObject(body);
						if(object.has("body")){
							JSONArray array=object.getJSONArray("body");
							String json=array.toString();
							adapter.addArray(json);
							adapter.notifyDataSetChanged();
							adapter.currentPage++;
						}
					} catch (JSONException e) {
						e.printStackTrace();
					}
				}
				public void requestDidFailed() {}
			});
	}

	private class GetDataTask extends AsyncTask<Void, Void, String> {
		@Override
		protected String doInBackground(Void... params) {
			try {
				Thread.sleep(1000);
				loadData();
			} catch (InterruptedException e) {
			}
			return null;
		}

		protected void onPostExecute(String result) {
			pullToRefresh.setPullLoadMoreCompleted();
			super.onPostExecute(result);
		}
	}

	@Override
	public void initData() {
		loadData();
	}

	@Override
	protected View initView(LayoutInflater inflater, ViewGroup container) {
		view = inflater.inflate(R.layout.fragment_nearby_dynamic, container,false);
		initDynamic();
		return view;
	}

}
