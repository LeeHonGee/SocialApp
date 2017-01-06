package com.sharemob.tinchat.lib.photoview;

import java.util.ArrayList;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.sharemob.tinchat.R;
import com.sharemob.tinchat.lib.LocalUtils;
import com.sharemob.tinchat.modules.dao.cache.CacheManager;

public class ImagePagerActivity extends FragmentActivity {
	private static final String SM_ACTION_DELETE_PHOTO = "sm.action.delete.photo";
	private static final String STATE_POSITION = "STATE_POSITION";
	private static final String EXTRA_CALLAUTH_INDEX = "call_auth";
	public static final String EXTRA_IMAGE_INDEX = "image_index";
	public static final String EXTRA_IMAGE_URLS = "image_urls";

	private HackyViewPager mPager;
	private int current_index=0;
	private int CallState=CacheManager.CallAuth.Mine.ordinal();
	private ArrayList<CharSequence> urls=new ArrayList<CharSequence>();
	private int pagerPosition;
	private TextView indicator;
	private ImagePagerAdapter mAdapter;
	private Button btn_del_photo;

	 // 在程序onCreate时就可以调用

	
	public void deleteCurrentPhoto(){
		LocalUtils.localAlertDialog(this,new String[]{null,"您确定要删除当前照片?","确定","取消"},
				new OnClickListener() {
					public void onClick(View v) {
							Intent intent = new Intent(SM_ACTION_DELETE_PHOTO); 
							intent.putExtra("Key", current_index);
							sendBroadcast(intent);
							urls.remove(current_index);
							mAdapter.setArrayList(urls);
							mAdapter.notifyDataSetChanged();
							if(mAdapter.getCount()>0&&current_index>0){
								current_index=current_index-1;
								mPager.setCurrentItem(current_index);
							}
						initIndicator();
						if(mAdapter.getCount()==0){
							finish();
						}
						
					}
				},
				new OnClickListener() {
					public void onClick(View v) {
						System.out.println("----------取消");
					}
				});
	}
	
	public void initIndicator(){
		CharSequence text = getString(R.string.viewpager_indicator, 1, mPager.getAdapter().getCount());
		indicator.setText(text);
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		System.out.println("释放内存");
		urls.clear();
		mAdapter=null;
//		ImageLoader.getInstance().clearMemoryCache();
//		ImageLoader.getInstance().getMemoryCache().clear();
//		ImageLoader.getInstance().clearDiscCache();
	}
	
	@Override
	public void finish() {
		super.finish();
		overridePendingTransition(0, R.anim.out_to_right);
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.image_detail_pager);

		pagerPosition = getIntent().getIntExtra(EXTRA_IMAGE_INDEX, 0);
		current_index=pagerPosition;
		
		CallState= getIntent().getIntExtra(EXTRA_CALLAUTH_INDEX,CacheManager.CallAuth.Other.ordinal());
		
		urls=getIntent().getCharSequenceArrayListExtra(EXTRA_IMAGE_URLS);

		mPager = (HackyViewPager) findViewById(R.id.pager);
		mAdapter = new ImagePagerAdapter(getSupportFragmentManager(), urls);
		
		mPager.setAdapter(mAdapter);
		indicator = (TextView) findViewById(R.id.indicator);
		btn_del_photo=(Button)findViewById(R.id.btn_del_photo);
		if(CallState==CacheManager.CallAuth.Mine.ordinal()){
			btn_del_photo.setVisibility(View.VISIBLE);
			btn_del_photo.setOnClickListener(new OnClickListener() {
				public void onClick(View v) {
					deleteCurrentPhoto();
				}
			});
		}else if(CallState==CacheManager.CallAuth.Other.ordinal()){
			btn_del_photo.setVisibility(View.GONE);
		}
		
		
		//初始化
		initIndicator();
		// 更新下标
		mPager.setOnPageChangeListener(new OnPageChangeListener() {
			@Override
			public void onPageScrollStateChanged(int arg0) {}
			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {}
			@Override
			public void onPageSelected(int index) {
				CharSequence text = getString(R.string.viewpager_indicator,index + 1, mPager.getAdapter().getCount());
				indicator.setText(text);
				current_index=index;
			}

		});
		if (savedInstanceState != null) {
			pagerPosition = savedInstanceState.getInt(STATE_POSITION);
		}

		mPager.setCurrentItem(pagerPosition);
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		outState.putInt(STATE_POSITION, mPager.getCurrentItem());
	}

	private class ImagePagerAdapter extends FragmentStatePagerAdapter {

		private ArrayList<CharSequence> fileLists=new ArrayList<CharSequence>();

		public ImagePagerAdapter(FragmentManager fm, ArrayList<CharSequence> fileLists) {
			super(fm);
			this.fileLists = fileLists;
		}

		public void setArrayList(ArrayList<CharSequence> fileLists){
			this.fileLists = fileLists;
		}
		
		public void removeItem(int index){
			fileLists.remove(index);
			notifyDataSetChanged();
		}
		
		@Override
		public int getCount() {
			return fileLists.size();
		}

		@Override
		public Fragment getItem(int position) {
			String url = fileLists.get(position).toString();
			return ImageDetailFragment.newInstance(url);
		}

	}
}