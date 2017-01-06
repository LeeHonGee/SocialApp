package com.sharemob.tinchat.modules.home;

import android.os.Bundle;
import android.view.View;

import com.sharemob.tinchat.R;
import com.sharemob.tinchat.lib.LocalUtils;
import com.sharemob.tinchat.lib.MyApplication;
import com.sharemob.tinchat.lib.viewpager.FragmentsAdapter;
import com.sharemob.tinchat.lib.viewpager.PagerScrollerActivity;
import com.sharemob.tinchat.lib.viewpager.TabInfo;
import com.sharemob.tinchat.modules.linkman.FragmentAttention;
import com.sharemob.tinchat.modules.linkman.FragmentFans;

public class LinkmanActivity extends PagerScrollerActivity {
	protected void onCreate(Bundle paramBundle) {
		super.onCreate(paramBundle);
		setContentView(R.layout.fragment_pager);
		initView();
		findViewById(R.id.fragment_title_back).setOnClickListener(
				new View.OnClickListener() {
					public void onClick(View v) {
						finish();
					}
				});
		MyApplication.getInstance().addActivity(this);
		LocalUtils.applyLocalFont(getWindow().getDecorView());
	}

	protected void setTabsAndAdapter() {
		this.tabs.add(new TabInfo(0, "关注", new FragmentAttention()));
		this.tabs.add(new TabInfo(1, "粉丝", new FragmentFans()));
		this.adapter = new FragmentsAdapter(this, getSupportFragmentManager(),this.tabs);
	}

	@Override
	public void finish() {
		super.finish();
		overridePendingTransition(R.anim.animi_remain, R.anim.out_to_right);
	}
}