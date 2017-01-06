/**
 *  文件名:ActivityFriend.java
 *  修改人:lihangjie
 *  创建时间:2015-8-28 上午2:38:24
 */
package com.sharemob.tinchat.modules.linkman;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.sharemob.tinchat.R;
import com.sharemob.tinchat.component.LazyFragment;
import com.sharemob.tinchat.lib.LocalUtils;
import com.sharemob.tinchat.lib.adapter.Gender;
import com.sharemob.tinchat.lib.adapter.LinkmanItemAdapter;
import com.sharemob.tinchat.lib.common.AsyncHttpRequest.RequestCallback;
import com.sharemob.tinchat.lib.photoview.HackyViewPager;
import com.sharemob.tinchat.lib.refreashtabview.extras.PullLoadMoreRecyclerView;
import com.sharemob.tinchat.lib.refreashtabview.extras.PullLoadMoreRecyclerView.PullLoadMoreListener;
import com.sharemob.tinchat.modules.dao.cache.CacheManager;

/**
 * 
 * <一句话功能简述>
 *
 * @author lihangjie
 * version [版本号,2015-8-28 上午2:38:24]
 * @see    [相关类/方法]
 * @since  [产品/模块版本]
 *
 */
public class FragmentAttention extends LazyFragment {
//	private static final String URL_Linkman_list=String.format("http://%s/api/user?cmd=10008",CacheManager.IP) ;
	private PullLoadMoreRecyclerView recyclerView;
	private LinkmanItemAdapter adapter;
	private TextView linkman_tv_desc;
	private View view;

	
	
	public void loadData(){
		
		Map<String,Object> map=new HashMap<String,Object>();
		map.put("uid", CacheManager.getInstance().userInfo.uid);
		
		LocalUtils.requestHttp(10008, map, new RequestCallback() {
			@Override
			public void requestDidFinished(String body) {
				try {
					JSONObject object=new JSONObject(body);
					if(object.has("body")){
						adapter.setArray(object.getJSONArray("body").toString());
						adapter.notifyDataSetChanged();
						linkman_tv_desc.setVisibility(View.GONE);
						 return;
					}else{
						adapter.clear();
						linkman_tv_desc.setVisibility(View.VISIBLE);
					}
				}catch(Exception e){
					
				}
			}
			@Override
			public void requestDidFailed() {
				
			}
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
			recyclerView.setPullLoadMoreCompleted();
			super.onPostExecute(result);
		}
	}
	@Override
	public void initData() {
		loadData();
	}

	@Override
	protected View initView(LayoutInflater inflater, ViewGroup container) {
		view=inflater.inflate(R.layout.fragment_attention_tab, container,false);
		adapter=new LinkmanItemAdapter(this.getActivity());
		recyclerView=(PullLoadMoreRecyclerView)view.findViewById(R.id.recyclerview_linkman); 
		linkman_tv_desc=(TextView)LocalUtils.getFindViewById(view,R.id.linkman_tv_desc);
		if(CacheManager.getInstance().userInfo.sex==Gender.MALE.ordinal()){
			linkman_tv_desc.setText(String.format(linkman_tv_desc.getText().toString(), "帅哥"));
		}else{
			linkman_tv_desc.setText(String.format(linkman_tv_desc.getText().toString(), "妹纸"));
		}
		
		recyclerView.setGridLayout(1);
		recyclerView.setAdapter(adapter);
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
		loadData();
		return view;
	}
}
