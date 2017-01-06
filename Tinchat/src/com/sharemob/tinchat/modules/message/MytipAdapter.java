/**
 *  文件名:MytipAdapter.java
 *  修改人:lihangjie
 *  创建时间:2016-3-12 下午9:13:47
 */
package com.sharemob.tinchat.modules.message;

import java.util.ArrayList;

import android.graphics.Bitmap.Config;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.sharemob.tinchat.R;
import com.sharemob.tinchat.lib.LocalUtils;

/**
 * 
 * <一句话功能简述>
 *
 * @author lihangjie
 * version [版本号,2016-3-12 下午9:13:47]
 * @see    [相关类/方法]
 * @since  [产品/模块版本]
 *
 */
public class MytipAdapter extends BaseAdapter {

	private ArrayList<MytipEntity> list = new ArrayList<MytipEntity>();

	@Override
	public int getCount()
	{
		return list.size();
	}

	@Override
	public Object getItem(int position)
	{
		return list.get(position);
	}

	@Override
	public long getItemId(int position)
	{
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent)
	{
		ViewHolder viewHolder = null;
		if (convertView == null)
		{
			viewHolder = new ViewHolder();

			convertView = View.inflate(parent.getContext(),R.layout.layout_item_mytip_message, null);
			findViews(viewHolder, convertView);
			
			convertView.setTag(viewHolder);
		}
		else
		{
			
			viewHolder = (ViewHolder) convertView.getTag();
		}
		setView(viewHolder, position);
		return convertView;
	}

	private void setView(final ViewHolder viewHolder, int position)
	{
		MytipEntity item = list.get(position);
		DisplayImageOptions options = new DisplayImageOptions.Builder()//
		.showImageOnLoading(R.drawable.empty_photo) // 加载中显示的默认图片
		.showImageOnFail(R.drawable.empty_photo) // 设置加载失败的默认图片
		.cacheOnDisc(true) // sdcard缓存
		.bitmapConfig(Config.RGB_565)// 设置最低配置
		.build();
		ImageLoader.getInstance().displayImage(item.getPhoto(), viewHolder.iv_photo, options);
		
		viewHolder.tv_title.setText(item.getTitle());
		viewHolder.tv_desc.setText(item.getDesc());
		viewHolder.tv_bottom_text.setText(item.getBottomText());	
		viewHolder.tv_time.setText(item.getTime());
		
	}

	public void setAdapter(ArrayList<MytipEntity> list)
	{
		this.list = list;
	}
	
	private void findViews(ViewHolder viewHolder, View convertView)
	{
		viewHolder.iv_photo = (ImageView) convertView.findViewById(R.id.tv_mytip_photo);
		viewHolder.tv_title = LocalUtils.getFindViewById( convertView,R.id.tv_mytip_title);
		viewHolder.tv_time=LocalUtils.getFindViewById( convertView,R.id.tv_mytip_time);
		viewHolder.tv_desc=LocalUtils.getFindViewById( convertView,R.id.tv_mytip_desc);
		viewHolder.tv_bottom_text=LocalUtils.getFindViewById( convertView,R.id.tv_mytip_bottom_text);
	}

	protected final class ViewHolder
	{
		ImageView iv_photo;
		TextView tv_title;
		TextView tv_desc;
		TextView tv_time;
		TextView tv_bottom_text;
	}

}
