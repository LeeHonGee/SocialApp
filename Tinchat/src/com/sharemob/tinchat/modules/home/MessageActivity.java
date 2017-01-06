/**
 * 
 */
package com.sharemob.tinchat.modules.home;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONObject;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;

import com.sharemob.tinchat.R;
import com.sharemob.tinchat.lib.MyApplication;
import com.sharemob.tinchat.lib.adapter.MessageItemAdapter;
import com.sharemob.tinchat.lib.common.AsyncHttpRequest.RequestCallback;
import com.sharemob.tinchat.lib.common.Matrix;
import com.sharemob.tinchat.lib.refreashtabview.extras.PullLoadMoreRecyclerView;
import com.sharemob.tinchat.lib.refreashtabview.extras.PullLoadMoreRecyclerView.PullLoadMoreListener;
import com.sharemob.tinchat.modules.dao.cache.CacheManager;

/**
 * @author lihangjie
 *
 */
public class MessageActivity extends Activity {
	private static final String URL_MESSAGE_LIST="http://ip:port/api/user?cmd=10009".replace("ip:port", CacheManager.IP);;
	private PullLoadMoreRecyclerView recyclerView;
	private MessageItemAdapter adapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.fragment_message_tab);
		MyApplication.getInstance().addActivity(this);
		
	    findViewById(R.id.fragment_title_back).setOnClickListener(new View.OnClickListener() {
	        public void onClick(View v) {
	            finish();
//	        	overridePendingTransition(R.anim.in_from_left,R.anim.out_to_right);
	        }
	    });
		
		
		adapter=new MessageItemAdapter(this);
		recyclerView=(PullLoadMoreRecyclerView)findViewById(R.id.recyclerview_notice); 
		recyclerView.setGridLayout(1);
		recyclerView.setAdapter(adapter);
		recyclerView.scrollToTop();
//		recyclerView.setPushRefreshEnable(false)
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
	}
	
	@Override
	public void finish() {
		super.finish();
		overridePendingTransition(R.anim.animi_remain,R.anim.out_to_right);
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		MyApplication.getInstance().removeActivity(this);
	}
	
	public void loadData(){
		Map<String,Object> map=new HashMap<String, Object>();
		map.put("rid", Integer.valueOf(Matrix.getIntegerForKey(this,"USER_ID", 0)));
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
