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
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.sharemob.tinchat.R;
import com.sharemob.tinchat.component.LazyFragment;
import com.sharemob.tinchat.lib.LocalUtils;
import com.sharemob.tinchat.lib.adapter.NearbyUserAdapter;
import com.sharemob.tinchat.lib.common.AsyncHttpRequest.RequestCallback;
import com.sharemob.tinchat.lib.refreashtabview.extras.PullLoadMoreRecyclerView;
import com.sharemob.tinchat.lib.refreashtabview.extras.PullLoadMoreRecyclerView.PullLoadMoreListener;
import com.sharemob.tinchat.modules.dao.cache.CacheManager;

/**
 * 
 * <一句话功能简述>
 *
 * @author lihangjie
 * version [版本号,2016-4-15 下午11:51:56]
 * @see    [相关类/方法]
 * @since  [产品/模块版本]
 *
 */
public class NearbyUserFragment extends LazyFragment  {
	private NearbyUserAdapter userAdapter;
	private PullLoadMoreRecyclerView mPullRefreshRecyclerView;
	private View view;
	
	private void loadingUser(){
		mPullRefreshRecyclerView = (PullLoadMoreRecyclerView)view.findViewById(R.id.recyclerview_nearbyuser);
		mPullRefreshRecyclerView.setGridLayout(1);
		userAdapter=new NearbyUserAdapter(view.getContext());
		mPullRefreshRecyclerView.setAdapter(userAdapter);
		mPullRefreshRecyclerView.setOnPullLoadMoreListener(new PullLoadMoreListener() {
			@Override
			public void onRefresh() {
				new GetDataTask().execute();
			}
			
			@Override
			public void onLoadMore() {
				new GetDataTask().execute();
			}
		});
	}
	
	public void loadData(){
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("uid", CacheManager.getInstance().getUserInfoUid());
		map.put("currentPage",userAdapter.currentPage);
		
		LocalUtils.requestHttp(10055,map,new RequestCallback() {
					public void requestDidFinished(String body) {
						System.out.println(body);
						try {
							JSONObject object=new JSONObject(body);
							if(object.has("body")){
								JSONArray array=object.getJSONArray("body");
								String json=array.toString();
								userAdapter.addArray(json);
								userAdapter.notifyDataSetChanged();
								userAdapter.currentPage++;
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
	            String str="Added after refresh...I add";  
	            return str;  
		}  
		protected void onPostExecute(String result) {  
			mPullRefreshRecyclerView.setPullLoadMoreCompleted();
            super.onPostExecute(result);  
        }  
	}

	@Override
	public void initData() {
		loadData();
	}

	@Override
	protected View initView(LayoutInflater inflater, ViewGroup container) {
		view = inflater.inflate(R.layout.fragment_nearby_user,container, false);
		loadingUser();	
		loadData();
		return view;
	}
}
