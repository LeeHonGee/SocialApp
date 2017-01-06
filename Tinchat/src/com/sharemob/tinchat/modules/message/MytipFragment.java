package com.sharemob.tinchat.modules.message;

import java.util.ArrayList;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.sharemob.tinchat.R;
import com.sharemob.tinchat.lib.LocalUtils;
import com.sharemob.tinchat.lib.refreashtabview.refreash.ILoadingLayout;
import com.sharemob.tinchat.lib.refreashtabview.refreash.PullToRefreshBase;
import com.sharemob.tinchat.lib.refreashtabview.refreash.PullToRefreshListView;

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
public class MytipFragment  extends Fragment {
	private ArrayList<MytipEntity> mytips = new  ArrayList< MytipEntity>();
	private PullToRefreshListView mPullRefreshListView;
	private ListView listView;
	private MytipAdapter adapter;
	public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_message_recyclerview,container, false);
		return view;
	}
	private void loadingMytipMessageData(){
		mPullRefreshListView = (PullToRefreshListView)getActivity().findViewById(R.id.listview_mytip_message);
		
		mPullRefreshListView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
			@Override
			public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
				new GetDataTask().execute(); 
//				 adapter.notifyDataSetChanged();
			}

			@Override
			public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
				new GetDataTask().execute(); 
//				 adapter.notifyDataSetChanged();
			}
		});
		
		String LastUpdatedLabel=LocalUtils.getDateTime();
		ILoadingLayout startLabels =mPullRefreshListView.getLoadingLayoutProxy(true, false);
		startLabels.setLastUpdatedLabel(String.format("最后刷新时间:%s",LastUpdatedLabel));
		startLabels.setPullLabel(null);// 刚下拉时，显示的提示    
        startLabels.setRefreshingLabel("正在努力加载");// 刷新时    
        startLabels.setReleaseLabel("松开刷新");// 下来达到一定距离时，显示的提示
		
		listView =mPullRefreshListView.getRefreshableView();
		if(listView==null){
			return;
		}
		System.out.println("-------------loadingMytipMessageData");
		adapter=new MytipAdapter();
		
		MytipEntity entity=new MytipEntity();
		entity.setPhoto("http://img1.mm131.com/pic/2325/22.jpg");
		entity.setBottomText("点击查看详情");
		entity.setTitle("钱多活少离家近,明天就有信不信?");
		entity.setDesc("钱多活少离家近,明天就有信不信?");
		entity.setTime("03-11 18:29");
		entity.setUrl("http://img1.mm131.com/pic/2325/22.jpg");
		mytips.add(entity);
		
		adapter.setAdapter(mytips);
		listView.setAdapter(adapter);
		
		listView.setOnItemClickListener(new OnItemClickListener(){
            public void onItemClick(AdapterView<?> parent, View view, int position,long id)
            {
            	keyEventOfListView(position,parent.getId());
            }
     });
	}
	protected void keyEventOfListView(int position, int id) {
		MytipEntity entity=mytips.get(position);
		System.out.println("-------------"+entity.getUrl());
	}
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		
		loadingMytipMessageData();
		
	}
	
	private class GetDataTask extends AsyncTask<Void, Void, String> {

		@Override
		protected String doInBackground(Void... params) {
			 try {  
	                Thread.sleep(1000);  
	            } catch (InterruptedException e) {  
	            }  
	            String str="Added after refresh...I add";  
	            return str;  
		}  
		protected void onPostExecute(String result) {  
            //在头部增加新添内容  
              
            //通知程序数据集已经改变，如果不做通知，那么将不会刷新mListItems的集合  
            adapter.notifyDataSetChanged();  
            // Call onRefreshComplete when the list has been refreshed.  
            mPullRefreshListView.onRefreshComplete(); 
            System.out.println("------------result:"+result);
            super.onPostExecute(result);  
        }  
	}
	
}
