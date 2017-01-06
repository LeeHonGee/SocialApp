package com.sharemob.tinchat.modules.home;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.sharemob.tinchat.R;
import com.sharemob.tinchat.lib.MyApplication;
import com.sharemob.tinchat.lib.viewpagerindicator.TabPageIndicator;
import com.sharemob.tinchat.modules.dao.cache.CacheManager;
import com.sharemob.tinchat.modules.linkman.FragmentAttention;
import com.sharemob.tinchat.modules.linkman.FragmentFans;

public class FragmentLinkman extends Fragment {
	
	private static final String[] TITLE = new String[] {"关注", "粉丝"};
	private List<Fragment> fragments = new ArrayList<Fragment>();

	public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
		Context context = new ContextThemeWrapper(getActivity(),R.style.StyledIndicators);
		LayoutInflater localInflater = inflater.cloneInContext(context);
		fragments.add(new FragmentFans());
		fragments.add(new FragmentAttention());
		return localInflater.inflate(R.layout.fragment_linkman, container,false);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		FragmentPagerAdapter adapter = new TabPageIndicatorAdapter(getActivity().getSupportFragmentManager());
		ViewPager pager = (ViewPager) getView().findViewById(R.id.linkman_pager);
		pager.setAdapter(adapter);

		// 实例化TabPageIndicator然后设置ViewPager与之关联
		TabPageIndicator indicator = (TabPageIndicator) getView().findViewById(R.id.indicator);
		indicator.setViewPager(pager);
		
		// 如果我们要对ViewPager设置监听，用indicator设置就行了
		indicator.setOnPageChangeListener(new OnPageChangeListener() {
			public void onPageSelected(int index) {}
			public void onPageScrolled(int arg0, float arg1, int arg2) {}
			public void onPageScrollStateChanged(int arg0) {}
		});
		MyApplication.getInstance().addActivity(this.getActivity());
	}

	/**
	 * ViewPager适配器
	 * 
	 * @author len
	 * 
	 */
	class TabPageIndicatorAdapter extends FragmentPagerAdapter {
		public TabPageIndicatorAdapter(FragmentManager fm) {
			super(fm);
		}

		public int getItemPosition(Object object) {
			return super.getItemPosition(object);
		}

		@Override
		public Fragment getItem(int position) {
			return fragments.get(position);
		}

		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			System.out.println("position Destory" + position);
			super.destroyItem(container, position, object);
		}

		@Override
		public CharSequence getPageTitle(int position) {
			return TITLE[position % TITLE.length];
		}

		@Override
		public Object instantiateItem(ViewGroup container, int position) {
			// 得到缓存的fragment
			Fragment fragment = (Fragment) super.instantiateItem(container,position);
			return fragment;
		}

		@Override
		public int getCount() {
			return TITLE.length;
		}
	}
}
