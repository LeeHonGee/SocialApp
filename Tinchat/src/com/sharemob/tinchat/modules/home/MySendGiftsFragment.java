package com.sharemob.tinchat.modules.home;

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
import android.widget.TextView;

import com.sharemob.tinchat.R;
import com.sharemob.tinchat.lib.LocalUtils;
import com.sharemob.tinchat.lib.adapter.ReceiveGiftItemAdapter;
import com.sharemob.tinchat.lib.common.AsyncHttpRequest.RequestCallback;
import com.sharemob.tinchat.lib.refreashtabview.extras.PullLoadMoreRecyclerView;
import com.sharemob.tinchat.lib.refreashtabview.extras.PullLoadMoreRecyclerView.PullLoadMoreListener;
import com.sharemob.tinchat.modules.dao.cache.CacheManager;

/**
 * 
 * 
 * <一句话功能简述>
 *
 * @author lihangjie
 * version [版本号,2016-3-13 下午10:55:24]
 * @see    [相关类/方法]
 * @since  [产品/模块版本]
 *
 */
public class MySendGiftsFragment  extends Fragment {
	private static final int CMD_SEND_GIFTS=10059;
	protected PullLoadMoreRecyclerView pullToRefresh;
	protected ReceiveGiftItemAdapter adapter;
	protected TextView gift_tv_tip;
	
	public void loadData(){
		Map<String,Object> map=new HashMap<String,Object>();
		map.put("uid",CacheManager.getInstance().userInfo.uid);
		map.put("currentPage",adapter.currentPage);
		
		
		LocalUtils.requestHttp(CMD_SEND_GIFTS, map,new RequestCallback() {
			@Override
			public void requestDidFinished(String body) {
				try {
					JSONObject object = new JSONObject(body);
					if(object.has("body")){
						JSONArray array=object.getJSONArray("body");
						if(array.length()!=0){
							pullToRefresh.setVisibility(View.VISIBLE);
							adapter.addArray(array.toString());
							adapter.notifyDataSetChanged();
							adapter.currentPage++;
						}else{
							if(adapter.currentPage==0){
								pullToRefresh.setVisibility(View.GONE);
								gift_tv_tip.setText(object.getString("desc"));
								gift_tv_tip.setVisibility(View.VISIBLE);
							}
						}
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
			@Override
			public void requestDidFailed() {
				
			}
		});
	}
	
	public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_sendgift_recyclerview,container, false);
		gift_tv_tip=(TextView)view.findViewById(R.id.sendgift_tv_tip);
		return view;
	}

	private void loadingMytipMessageData(){
		pullToRefresh = (PullLoadMoreRecyclerView)getActivity().findViewById(R.id.sendgifts_recyclerview);
		pullToRefresh.setGridLayout(1);
		adapter=new ReceiveGiftItemAdapter(getActivity());
		adapter.setState(ReceiveGiftItemAdapter.Send);
		pullToRefresh.setAdapter(adapter);
		pullToRefresh.setOnPullLoadMoreListener(new PullLoadMoreListener() {
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
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		loadingMytipMessageData();
		loadData();
	}
	
	private class GetDataTask extends AsyncTask<Void, Void, String> {
		@Override
		protected String doInBackground(Void... params) {
			 try {  
	                Thread.sleep(500);  
	                loadData();
	            } catch (InterruptedException e) {  
	            }  
	            String str="Added after refresh...I add";  
	            return str;  
		}  
		protected void onPostExecute(String result) {  
			pullToRefresh.setPullLoadMoreCompleted();
            super.onPostExecute(result);  
        }  
	}
	
}
