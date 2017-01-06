package com.sharemob.tinchat.lib.photoview;

import java.util.List;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.sharemob.tinchat.R;
import com.sharemob.tinchat.base.PetItem;

public class MyGridAdapter extends BaseAdapter {
//	private String[] files;
	private List<MyPhoto> list;
	private LayoutInflater mLayoutInflater;

//	public MyGridAdapter(List<MyPhoto> list, Context context) {
////		this.files = files;
//		this.list=list;
//		mLayoutInflater = LayoutInflater.from(context);
//	}

	public void setAdapter(List<MyPhoto> list)
	{
		this.list = list;
	}
	
	@Override
	public int getCount() {
//		return files == null ? 0 : files.length;
		return list.size();
	}

	@Override
	public MyPhoto getItem(int position) {
//		return files[position];
		return list.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	private void setView(final ViewHolder viewHolder, int position)
	{
		MyPhoto item = list.get(position);
		if(item.getUrl()==null){
			viewHolder.imageView.setVisibility(View.GONE);
			viewHolder.uploadPhoto.setVisibility(View.VISIBLE);
		}else{
			viewHolder.imageView.setVisibility(View.VISIBLE);
			viewHolder.uploadPhoto.setVisibility(View.GONE);
			ImageLoader.getInstance().displayImage(item.getUrl(), viewHolder.imageView);
		}
		
	}
	
	private void findViews(ViewHolder viewHolder, View convertView)
	{
		viewHolder.imageView = (ImageView) convertView.findViewById(R.id.album_image);
		viewHolder.uploadPhoto=(TextView)convertView.findViewById(R.id.upload_photo);
		
		
		
	}
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder viewHolder;
		if (convertView == null) {
			viewHolder = new ViewHolder();
			convertView = View.inflate(parent.getContext(),R.layout.gridview_item, null);
			findViews(viewHolder, convertView);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		setView(viewHolder, position);
		return convertView;
	}

	private static class ViewHolder {
		TextView uploadPhoto;
		ImageView imageView;
	}
}
