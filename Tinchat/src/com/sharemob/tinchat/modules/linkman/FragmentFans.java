/**
 *  文件名:ActivityFriend.java
 *  修改人:lihangjie
 *  创建时间:2015-8-28 上午2:38:24
 */
package com.sharemob.tinchat.modules.linkman;

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
import com.sharemob.tinchat.lib.LocalUtils;
import com.sharemob.tinchat.lib.adapter.Gender;
import com.sharemob.tinchat.lib.adapter.LinkmanItemAdapter;
import com.sharemob.tinchat.lib.common.AsyncHttpRequest.RequestCallback;
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
public class FragmentFans extends Fragment {
	private static final String URL_Lover_list="http://"+CacheManager.IP+"/api/user?cmd=10007";
//    private PullToRefreshListView pullToRefresh=null;
//    private ListAdapterForFriend listAdapter=null;
    
	private PullLoadMoreRecyclerView recyclerView;
	private LinkmanItemAdapter adapter;
	private TextView linkman_tv_desc;
	public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
		
		return inflater.inflate(R.layout.fragment_fans_tab, container,false);
	}

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}
	
	
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
	   
		adapter=new LinkmanItemAdapter(this.getActivity());
		recyclerView=(PullLoadMoreRecyclerView)getView().findViewById(R.id.recyclerview_linkman); 
		linkman_tv_desc=(TextView)LocalUtils.getFindViewById(getView(),R.id.linkman_tv_desc);
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
//		LocalUtils.initILoadingLayout(pullToRefresh);
	}
	
	@Override
	public void onResume() {
		super.onResume();
		loadData();
	}
	
	public void loadData(){
		JSONObject json=new JSONObject();
		try {
			json.put("uid", CacheManager.getInstance().userInfo.uid);
		} catch (JSONException e) {
			e.printStackTrace();
		}
	    String url=String.format("%s&data=%s", URL_Lover_list,json.toString());
	    System.out.println("url:"+url);
	   CacheManager.requestHttp(url,new RequestCallback() {
				public void requestDidFinished(String body) {
					try {
						JSONObject object=new JSONObject(body);
						if(object.has("body")){
							adapter.setArray(object.getJSONArray("body").toString());
							adapter.notifyDataSetChanged();
							linkman_tv_desc.setVisibility(View.GONE);
						}else{
							linkman_tv_desc.setVisibility(View.VISIBLE);
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
			String str = "Added after refresh...I add";
			return str;
		}

		protected void onPostExecute(String result) {
			recyclerView.setPullLoadMoreCompleted();
			super.onPostExecute(result);
		}
	}
}
