package com.sharemob.tinchat.lib.photoview;

import java.util.ArrayList;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.sharemob.tinchat.R;
import com.sharemob.tinchat.lib.common.GlobalParams;
import com.sharemob.tinchat.lib.common.ReleaseImageLoadingListener;

public class NoScrollGridAdapter extends BaseAdapter {

	/** 上下文 */
	private Context ctx;
	/** 图片Url集合 */
	private ArrayList<CharSequence> imageUrls=new ArrayList<CharSequence>();
	private ReleaseImageLoadingListener loadingListener;
	public NoScrollGridAdapter(Context ctx,ReleaseImageLoadingListener loadingListener) {
		this.ctx = ctx;
		this.loadingListener=loadingListener;
	}
	
	public NoScrollGridAdapter setList(ArrayList<CharSequence> urls){
		this.imageUrls = urls;
		return this;
	}
	
	public void removeItem(int index){
		imageUrls.remove(index);
		notifyDataSetChanged();
	}
	
	@Override
	public int getCount() {
		return imageUrls.size();
	}

	@Override
	public Object getItem(int position) {
		return imageUrls.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}
	
	public void addItem(String object){
		imageUrls.add(object);
		notifyDataSetChanged();
	}
	private void findViews(ViewHolder viewHolder, View convertView)
	{
		viewHolder.iv_image = (ImageView) convertView.findViewById(R.id.iv_image);
	}
	
//	public void releaseImages(){
//		loadingListener.cleanBitmapList();
//	}
	
//	private ReleaseImageLoadingListener loadingListener=new ReleaseImageLoadingListener(){
//		 public void onLoadingComplete(String imageUri, android.view.View view, android.graphics.Bitmap loadedImage) {
//			 ImageView imageView=(ImageView)view;
//			 imageView.setScaleType(ScaleType.CENTER_CROP);
//			 imageView.setImageBitmap(loadedImage);   
//            };
//	};
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder viewHolder = null;
		if (convertView == null)
		{
			viewHolder = new ViewHolder();
			convertView = View.inflate(parent.getContext(),R.layout.layout_item_gridview, null);
			findViews(viewHolder, convertView);
			convertView.setTag(viewHolder);
		}else
		{
			viewHolder = (ViewHolder) convertView.getTag();
		}
		
		String uri=imageUrls.get(position).toString();
		
		if(loadingListener!=null){
			ImageLoader.getInstance().displayImage (
					uri,
					viewHolder.iv_image ,
					GlobalParams.getInstance().round_options,
					loadingListener);
		}else{
			ImageLoader.getInstance().displayImage (
					uri,
					viewHolder.iv_image ,
					GlobalParams.getInstance().round_options,
					loadingListener);
		}
		return convertView;
	}

	protected final class ViewHolder
	{
		ImageView iv_image;
	}
}
