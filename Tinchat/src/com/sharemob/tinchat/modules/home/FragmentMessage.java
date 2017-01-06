/**
 *  文件名:ActivityFeedback.java
 *  修改人:lihangjie
 *  创建时间:2015-8-28 下午12:09:05
 */
package com.sharemob.tinchat.modules.home;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONObject;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.sharemob.tinchat.R;
import com.sharemob.tinchat.base.BaseFragment;
import com.sharemob.tinchat.lib.MyApplication;
import com.sharemob.tinchat.lib.adapter.MessageItemAdapter;
import com.sharemob.tinchat.lib.common.AsyncHttpRequest.RequestCallback;
import com.sharemob.tinchat.lib.common.Matrix;
import com.sharemob.tinchat.lib.refreashtabview.extras.PullLoadMoreRecyclerView;
import com.sharemob.tinchat.lib.refreashtabview.extras.PullLoadMoreRecyclerView.PullLoadMoreListener;
import com.sharemob.tinchat.modules.dao.cache.CacheManager;

/**
 * 
 * <一句话功能简述>
 * 
 * @author lihangjie version [版本号,2015-8-28 下午12:09:05]
 * @see [相关类/方法]
 * @since [产品/模块版本]
 * 
 */
public class FragmentMessage extends BaseFragment {
	private static final String URL_MESSAGE_LIST="http://ip:port/api/user?cmd=10009".replace("ip:port", CacheManager.IP);;
	private PullLoadMoreRecyclerView recyclerView;
	private MessageItemAdapter adapter;
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		MyApplication.getInstance().addActivity(this.getActivity());
	}
	
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view=inflater.inflate(R.layout.fragment_message_tab, container, false);
		return view;
	}

	public void loadData(){
			Map<String,Object> map=new HashMap<String, Object>();
			map.put("rid", Integer.valueOf(Matrix.getIntegerForKey(this.getActivity(),"USER_ID", 0)));
			JSONObject json=new JSONObject(map);
		    String url=String.format("%s&data=%s", URL_MESSAGE_LIST,json.toString());
		    System.out.println("url:"+url);
		   CacheManager.requestHttp(url,new RequestCallback() {
					public void requestDidFinished(String body) {
						System.out.println(body);
						adapter.setArray(body);
						adapter.notifyDataSetChanged();
					}
					public void requestDidFailed() {}
				});
	}
	
	@Override
	public void onResume() {
		super.onResume();
		 loadData();
	}
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		
			adapter=new MessageItemAdapter(this.getActivity());
			recyclerView=(PullLoadMoreRecyclerView)getView().findViewById(R.id.recyclerview_notice); 
			recyclerView.setGridLayout(1);
			recyclerView.setAdapter(adapter);
			recyclerView.scrollToTop();
//			recyclerView.setPushRefreshEnable(false)
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