package com.sharemob.tinchat.lib.photoview;

import java.util.ArrayList;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;

import com.sharemob.tinchat.R;

public class MyListAdapter extends BaseAdapter{

	private ArrayList<MyPersonal> mList;
	private LayoutInflater mInflater;
	private Context mContext;
	
	public MyListAdapter(Context context,ArrayList<MyPersonal> list) {
		this.mInflater = LayoutInflater.from(context);
		this.mContext=context;
		this.mList=list;
	}

	@Override
	public int getCount() {
		return mList==null?0:mList.size();
	}

	@Override
	public MyPersonal getItem(int position) {
		return mList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder;
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = mInflater.inflate(R.layout.list_item, null);
			holder.gridView=(NoScrollGridView)convertView.findViewById(R.id.gridView);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		final MyPersonal bean = getItem(position);
		
		if(bean.urls!=null&&bean.urls.length>0){
			holder.gridView.setVisibility(View.VISIBLE);
//			holder.gridView.setAdapter(new MyGridAdapter(bean.urls, mContext));
			holder.gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
					imageBrower(position,bean.urls);
				}
			});
		}else{
			holder.gridView.setVisibility(View.GONE);
		}
		return convertView;
	}

	private void imageBrower(int position, String[] urls) {
		// 图片url,为了演示这里使用常量，一般从数据库中或网络中获取
		Intent intent = new Intent(mContext, ImagePagerActivity.class);
		intent.putExtra(ImagePagerActivity.EXTRA_IMAGE_URLS, urls);
		intent.putExtra(ImagePagerActivity.EXTRA_IMAGE_INDEX, position);
		mContext.startActivity(intent);
	}
	private static class ViewHolder {
		NoScrollGridView gridView;
	}
}
