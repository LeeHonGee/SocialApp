package com.sharemob.tinchat.modules.home;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import com.sharemob.tinchat.R;
import com.sharemob.tinchat.lib.LocalUtils;
import com.sharemob.tinchat.lib.viewpager.FragmentsAdapter;
import com.sharemob.tinchat.lib.viewpager.PagerScrollerFragment;
import com.sharemob.tinchat.lib.viewpager.TabInfo;
import com.sharemob.tinchat.modules.dynamic.EmotionsFragment;
import com.sharemob.tinchat.modules.dynamic.NearbyDynamicFragment;
import com.sharemob.tinchat.modules.dynamic.NearbyUserFragment;

public class FragmentNearby extends PagerScrollerFragment {
	private Button btn_add_dynamic, btn_search;
	private ImageView iv_line;
	private Activity activity;


	public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
		view=LayoutInflater.from(getContext()).inflate(R.layout.fragment_nearby_main, container,true);
		viewPager = (ViewPager) view.findViewById(R.id.vPager);
		
		initView();
		btn_add_dynamic = (Button) view.findViewById(R.id.btn_add_dynamic);
		btn_add_dynamic.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				LocalUtils.enterAppActivity(activity, "ReleaseDynamicItem",R.anim.in_from_right,R.anim.out_to_left, false);
			}
		});
		
		btn_search = (Button) view.findViewById(R.id.btn_search);
		btn_search.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
//				LocalUtils.gotoViewByAnim(getActivity(), SearchActivity.class.getName(),R.anim.in_from_right, R.anim.out_to_left, false);
			}
		});
		
		iv_line = (ImageView) view.findViewById(R.id.iv_line);
		
		// 如果我们要对ViewPager设置监听，用indicator设置就行了
		viewPager.addOnPageChangeListener(new OnPageChangeListener() {
			@Override
			public void onPageSelected(final int position) {
				if (position == 0) {
					btn_add_dynamic.setVisibility(View.VISIBLE);
					btn_search.setVisibility(View.GONE);
					iv_line.setVisibility(View.VISIBLE);
				} else if (position == 1) {
					btn_add_dynamic.setVisibility(View.GONE);
					btn_search.setVisibility(View.VISIBLE);
					iv_line.setVisibility(View.VISIBLE);
				} else {
					btn_add_dynamic.setVisibility(View.GONE);
					btn_search.setVisibility(View.GONE);
					iv_line.setVisibility(View.GONE);
				}
				 title.setCurrentTab(position);
			}
			@Override
			public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
				   title.onScroll((viewPager.getWidth() + viewPager.getPageMargin()) * position + positionOffsetPixels);
			}

			@Override
			public void onPageScrollStateChanged(final int state) {

			}
		});
		btn_search.setVisibility(View.GONE);
		btn_add_dynamic.setVisibility(View.VISIBLE);
		return view;
	}
	
    public void setTabsAndAdapter() {
        tabs.add(new TabInfo(0x0,getString(R.string.nearby_item_tabinfo_news), new NearbyDynamicFragment()));
        tabs.add(new TabInfo(0x1,getString(R.string.nearby_item_tabinfo_users), new NearbyUserFragment()));
        tabs.add(new TabInfo(0x2,getString(R.string.nearby_item_tabinfo_magazine), new EmotionsFragment()));
        adapter=new FragmentsAdapter(view.getContext(),getFragmentManager(), tabs);
    }
}
