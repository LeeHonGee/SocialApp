package com.sharemob.tinchat.lib.photoview;

import java.util.ArrayList;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap.Config;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.sharemob.tinchat.R;
import com.sharemob.tinchat.lib.ImageUtils;
import com.sharemob.tinchat.lib.LocalUtils;
import com.sharemob.tinchat.lib.common.GlobalParams;
import com.sharemob.tinchat.lib.common.ReleaseImageLoadingListener;

/**
 * 首页ListView的数据适配器
 * 
 * @author Administrator
 * 
 */
public class ListItemAdapter extends BaseAdapter {

	private Context mContext;
	private ArrayList<ItemEntity> items;
	private ReleaseImageLoadingListener loadingListener;
	public ListItemAdapter(Context ctx,ReleaseImageLoadingListener loadingListener, ArrayList<ItemEntity> items) {
		this.mContext = ctx;
		this.items = items;
		this.loadingListener=loadingListener;
	}

	@Override
	public int getCount() {
		return items == null ? 0 : items.size();
	}

	@Override
	public Object getItem(int position) {
		return items.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}
	private TextView getFindViewById(View convertView,int id){
		TextView tv=(TextView) convertView.findViewById(id);
		tv.setTypeface(GlobalParams.getInstance().localFontTypeface);
		return tv;
	}
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder;
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = View.inflate(mContext, R.layout.layout_item_space_say, null);
//			holder.iv_avatar = (ImageView) convertView.findViewById(R.id.iv_avatar);
			holder.tv_title =getFindViewById(convertView,R.id.tv_title);
			holder.tv_content = getFindViewById(convertView,R.id.tv_content);
			holder.gridview = (NoScrollGridView) convertView.findViewById(R.id.gridview);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		ItemEntity itemEntity = items.get(position);
		holder.tv_title.setText(itemEntity.getTitle());
		holder.tv_content.setText(itemEntity.getContent());
		// 使用ImageLoader加载网络图片
//		DisplayImageOptions options = new DisplayImageOptions.Builder()//
//				.showImageOnLoading(R.drawable.empty_photo) // 加载中显示的默认图片
//				.showImageOnFail(R.drawable.empty_photo) // 设置加载失败的默认图片
////				.cacheInMemory(true) // 内存缓存
//				.cacheOnDisc(true) // sdcard缓存
//				.bitmapConfig(Config.RGB_565)// 设置最低配置
//				.build();
//		ImageLoader.getInstance().displayImage(itemEntity.getAvatar(), holder., options);
//		final String[] imageUrlsForArgs=itemEntity.getImageUrlsForArgs();
		
		final ArrayList<CharSequence> imageUrls = itemEntity.getImageUrls();
		if (imageUrls == null || imageUrls.size() == 0) { // 没有图片资源就隐藏GridView
			holder.gridview.setVisibility(View.GONE);
		} else {
			holder.gridview.setAdapter(new NoScrollGridAdapter(mContext,this.loadingListener).setList(imageUrls));
			
		}
		// 点击回帖九宫格，查看大图
		holder.gridview.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
			ImageUtils.imageBrower(mContext, position,imageUrls);
			}
		});
		return convertView;
	}

//	protected void imageBrower(int position, String[] urls2) {
//		Intent intent = new Intent(mContext, ImagePagerActivity.class);
//		// 图片url,为了演示这里使用常量，一般从数据库中或网络中获取
//		intent.putExtra(ImagePagerActivity.EXTRA_IMAGE_URLS, urls2);
//		intent.putExtra(ImagePagerActivity.EXTRA_IMAGE_INDEX, position);
//		mContext.startActivity(intent);
//	}

	/**
	 * listview组件复用，防止“卡顿”
	 * 
	 * @author Administrator
	 * 
	 */
	class ViewHolder {
//		private ImageView iv_avatar;
		private TextView tv_title;
		private TextView tv_content;
		private NoScrollGridView gridview;
	}
}
