package com.sharemob.tinchat.component;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.json.JSONArray;
import org.json.JSONException;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.sharemob.tinchat.R;
import com.sharemob.tinchat.lib.common.GlobalParams;
import com.sharemob.tinchat.lib.common.ReleaseImageLoadingListener;

public class BannerSlideView extends LinearLayout {

	private ViewPager adViewPager;
	private List<ImageView> imageViews;// 滑动的图片集合
	private List<View> dots; // 图片标题正文的那些点
	private List<View> dotList;
	private int currentItem = 0; // 当前图片的索引号
	private final int[] dotIDs = new int[] { R.id.v_dot0, R.id.v_dot1,
			R.id.v_dot2, R.id.v_dot3, R.id.v_dot4, R.id.v_dot5, R.id.v_dot6 };
	private ScheduledExecutorService scheduledExecutorService;
	private JSONArray arrayBanner;
	private View view;
	private DisplayImageOptions options = new DisplayImageOptions
			.Builder()
			.showImageForEmptyUri(R.drawable.shadow_article)
			.showImageOnFail(R.drawable.shadow_article)
			.cacheOnDisc(true)
			.bitmapConfig(Bitmap.Config.RGB_565)
			.imageScaleType(ImageScaleType.IN_SAMPLE_POWER_OF_2)
			.build();
	
	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			adViewPager.setCurrentItem(currentItem);
		};
	};

	public BannerSlideView(Context context) {
		super(context);
		initContext(context);
	}

	public BannerSlideView(Context context, AttributeSet attrs) {
		super(context, attrs);
		initContext(context);
	}

	public BannerSlideView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, R.style.dot_style);
		initContext(context);
	}

	public void loadBannerSlide(ReleaseImageLoadingListener loadingListener,
			String json) {
		initAdData(loadingListener, json);
		startAd();
	}

	private void initContext(Context context) {
		view = LayoutInflater.from(context).inflate(R.layout.layout_ab_banner,this, true);
	}

	private void initAdData(ReleaseImageLoadingListener loadingListener,String json) {
		if (json.length() == 0) {
			return;
		}
		try {
			arrayBanner = new JSONArray(json);
			imageViews = new ArrayList<ImageView>(arrayBanner.length());
			dots = new ArrayList<View>();
			dotList = new ArrayList<View>();
			for (int i = 0; i < arrayBanner.length(); i++) {
				View dot = view.findViewById(dotIDs[i]);
				dots.add(dot);
			}
			addDynamicView(loadingListener);
		} catch (Exception e) {
			e.printStackTrace();
		}

		adViewPager = (ViewPager) view.findViewById(R.id.vp);
		// 设置填充ViewPager页面的适配器
		BannerAdapter bannerAdapter = new BannerAdapter(arrayBanner, imageViews);
		adViewPager.setAdapter(bannerAdapter);
		bannerAdapter.notifyDataSetChanged();
		// 设置一个监听器，当ViewPager中的页面改变时调用
		adViewPager.setOnPageChangeListener(new MyPageChangeListener());
	}

	private void addDynamicView(ReleaseImageLoadingListener loadingListener)
			throws JSONException {
		for (int i = 0; i < arrayBanner.length(); i++) {
			ImageView imageView = new ImageView(view.getContext());
			imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);

			String url = arrayBanner.getJSONObject(i).getString("url");
			ImageLoader.getInstance().displayImage(url, imageView, GlobalParams.getInstance().defaultOptions);

			imageViews.add(imageView);
			dots.get(i).setVisibility(View.VISIBLE);
			dotList.add(dots.get(i));
		}
	}

	private void startAd() {
		scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();
		scheduledExecutorService.scheduleAtFixedRate(new ScrollTask(), 1, 5,
				TimeUnit.SECONDS);
	}

	private class ScrollTask implements Runnable {
		public void run() {
			synchronized (adViewPager) {
				currentItem = (currentItem + 1) % imageViews.size();
				handler.obtainMessage().sendToTarget();
			}
		}
	}

	protected void stopPlay() {
		scheduledExecutorService.shutdown();
	}

	private class MyPageChangeListener implements OnPageChangeListener {
		private MyPageChangeListener() {
		}

		private int oldPosition = 0;
		private boolean isAutoPlay = false;

		@Override
		public void onPageScrollStateChanged(int position) {
			switch (position) {
			case 1:// 手势滑动，空闲中
				isAutoPlay = false;
				break;
			case 2:// 界面切换中
				isAutoPlay = true;
				break;
			case 0:// 滑动结束，即切换完毕或者加载完毕
					// 当前为最后一张，此时从右向左滑，则切换到第一张
				if (adViewPager.getCurrentItem() == adViewPager.getAdapter()
						.getCount() - 1 && !isAutoPlay) {
					adViewPager.setCurrentItem(0);
				}
				// 当前为第一张，此时从左向右滑，则切换到最后一张
				else if (adViewPager.getCurrentItem() == 0 && !isAutoPlay) {
					adViewPager.setCurrentItem(adViewPager.getAdapter()
							.getCount() - 1);
				}
				break;
			}
		}

		@Override
		public void onPageScrolled(int arg0, float arg1, int arg2) {
		}

		@Override
		public void onPageSelected(int position) {
			dots.get(oldPosition).setBackgroundResource(
					R.drawable.banner_dot_normal);
			dots.get(position).setBackgroundResource(
					R.drawable.banner_dot_focused);
			oldPosition = position;
		}
	}

	public class BannerAdapter extends PagerAdapter {

		// 轮播banner的数据
		// private List<AdBannerItem> list;
		private JSONArray list;
		private List<ImageView> imageViews;// 滑动的图片集合

		public BannerAdapter(JSONArray adList, List<ImageView> imageViews) {
			this.list = adList;
			this.imageViews = imageViews;
		}

		@Override
		public int getCount() {
			return list.length();
		}

		@Override
		public Object instantiateItem(ViewGroup container, final int position) {
			if (imageViews.size() > 0) {
				ImageView iv = imageViews.get(position);
				((ViewPager) container).addView(iv);
				// 在这个方法里面设置图片的点击事件
				iv.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						try {
							String url = list.getJSONObject(position).getString("url");
						} catch (JSONException e) {
							e.printStackTrace();
						}

					}
				});
				return iv;
			}
			return null;
		}

		@Override
		public void destroyItem(View arg0, int arg1, Object arg2) {
			((ViewPager) arg0).removeView((View) arg2);
		}

		@Override
		public boolean isViewFromObject(View arg0, Object arg1) {
			return arg0 == arg1;
		}

		@Override
		public void restoreState(Parcelable arg0, ClassLoader arg1) {

		}

		@Override
		public Parcelable saveState() {
			return null;
		}

		@Override
		public void startUpdate(View arg0) {

		}

		@Override
		public void finishUpdate(View arg0) {

		}

	}
}
