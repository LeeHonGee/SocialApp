//package com.sharemob.tinchat.modules.message;
//
//import com.sharemob.tinchat.R;
//import com.sharemob.tinchat.lib.adapter.ReceiveGiftItemAdapter;
//import com.sharemob.tinchat.lib.refreashtabview.extras.PullLoadMoreRecyclerView;
//import com.sharemob.tinchat.lib.refreashtabview.extras.PullLoadMoreRecyclerView.PullLoadMoreListener;
//
//import android.os.AsyncTask;
//import android.os.Bundle;
//import android.support.annotation.Nullable;
//import android.support.v4.app.Fragment;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//
//public abstract class RecyclerviewFragment extends Fragment {
//	protected PullLoadMoreRecyclerView pullToRefresh;
//	protected ReceiveGiftItemAdapter adapter;
//	
//	public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
//		View view = inflater.inflate(R.layout.fragment_sendgift_recyclerview,container, false);
//		return view;
//	}
//
//	private void loadingMytipMessageData(){
//		pullToRefresh = (PullLoadMoreRecyclerView)getActivity().findViewById(R.id.pullloadmorerecyclerview);
//		pullToRefresh.setGridLayout(1);
//		adapter=new ReceiveGiftItemAdapter(getActivity());
//		pullToRefresh.setAdapter(adapter);
//		pullToRefresh.setOnPullLoadMoreListener(new PullLoadMoreListener() {
//			@Override
//			public void onRefresh() {
//				new GetDataTask().execute();
//			}
//			
//			@Override
//			public void onLoadMore() {
//				new GetDataTask().execute();
//			}
//		});
//	}
//	@Override
//	public void onCreate(@Nullable Bundle savedInstanceState) {
//		super.onCreate(savedInstanceState);
//		
//	}
//	protected abstract void loadData();
//	@Override
//	public void onActivityCreated(Bundle savedInstanceState) {
//		super.onActivityCreated(savedInstanceState);
//		loadingMytipMessageData();
//		loadData();
//	}
//	
//	private class GetDataTask extends AsyncTask<Void, Void, String> {
//		@Override
//		protected String doInBackground(Void... params) {
//			 try {  
//	                Thread.sleep(500);  
//	                loadData();
//	            } catch (InterruptedException e) {  
//	            }  
//	            String str="Added after refresh...I add";  
//	            return str;  
//		}  
//		protected void onPostExecute(String result) {  
//			adapter.notifyDataSetChanged();
//			pullToRefresh.setPullLoadMoreCompleted();
//            super.onPostExecute(result);  
//        }  
//	}
//	
//}
