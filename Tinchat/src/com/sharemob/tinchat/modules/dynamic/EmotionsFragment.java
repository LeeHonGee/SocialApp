package com.sharemob.tinchat.modules.dynamic;

import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.sharemob.tinchat.R;
import com.sharemob.tinchat.component.LazyFragment;
import com.sharemob.tinchat.lib.LocalUtils;
import com.sharemob.tinchat.lib.adapter.EmotionArticleAdapter;
import com.sharemob.tinchat.lib.common.AsyncHttpRequest.RequestCallback;
import com.sharemob.tinchat.lib.refreashtabview.extras.PullLoadMoreRecyclerView;

public class EmotionsFragment extends LazyFragment
{
  private Activity activity;
  private EmotionArticleAdapter emotionArticleAdapter;
  private PullLoadMoreRecyclerView mPullRefreshRecyclerView;

  private void initPullLoadMoreRecyclerView(View view)
  {
    this.mPullRefreshRecyclerView = ((PullLoadMoreRecyclerView)view.findViewById(R.id.recyclerview));
    this.mPullRefreshRecyclerView.setGridLayout(1);
    this.emotionArticleAdapter = new EmotionArticleAdapter(view.getContext());
    this.mPullRefreshRecyclerView.setAdapter(this.emotionArticleAdapter);
    this.mPullRefreshRecyclerView.setOnPullLoadMoreListener(new PullLoadMoreRecyclerView.PullLoadMoreListener()
    {
      public void onLoadMore()
      {
        new GetDataTask().execute();
      }

      public void onRefresh()
      {
        new GetDataTask().execute();
      }
    });
  }

  public void loadData()
  {
        LocalUtils.requestHttp(10042, new HashMap<String,Object>(), new RequestCallback() {
			public void requestDidFinished(String body) {
				 System.out.println(body);
                 try {
                     JSONObject object = new JSONObject(body);
                     if(object.has("body")) {
                         JSONArray array = object.getJSONArray("body");
                         String json = array.toString();
                         emotionArticleAdapter.addArray(json);
                         emotionArticleAdapter.notifyDataSetChanged();
                         return;
                     }
                 } catch(JSONException e) {
                     e.printStackTrace();
                 }
			}
			public void requestDidFailed() {}
		});
  }

//  public void onActivityCreated(Bundle paramBundle)
//  {
//    super.onActivityCreated(paramBundle);
//    initPullLoadMoreRecyclerView();
//  }

//  public View onCreateView(LayoutInflater paramLayoutInflater, ViewGroup paramViewGroup, Bundle paramBundle)
//  {
//    View localView = paramLayoutInflater.inflate(R.layout.fragment_discovery_emotions, paramViewGroup, false);
//    this.activity = getActivity();
//    return localView;
//  }

  private class GetDataTask extends AsyncTask<Void, Void, String>
  {
    protected String doInBackground(Void... params)
    {
      try
      {
        Thread.sleep(1000L);
        EmotionsFragment.this.loadData();
      }
      catch (InterruptedException localInterruptedException)
      {
      }
      return "Added after refresh...I add";
    }

    protected void onPostExecute(String paramString)
    {
      EmotionsFragment.this.mPullRefreshRecyclerView.setPullLoadMoreCompleted();
      super.onPostExecute(paramString);
    }
  }

	@Override
	public void initData() {
		
		loadData();
	}
	
	@Override
	protected void onInvisible() {
		
	}

	@Override
	protected View initView(LayoutInflater inflater, ViewGroup container) {
		View view = inflater.inflate(R.layout.fragment_discovery_emotions, container, false);
		initPullLoadMoreRecyclerView(view);
		return view;
	}
}