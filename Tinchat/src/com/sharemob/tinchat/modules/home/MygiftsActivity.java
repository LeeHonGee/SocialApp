/**
 *  文件名:MessageActivity.java
 *  修改人:lihangjie
 *  创建时间:2016-3-12 下午8:41:41
 */
package com.sharemob.tinchat.modules.home;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.sharemob.tinchat.R;
import com.sharemob.tinchat.lib.LocalUtils;
import com.sharemob.tinchat.lib.MyApplication;
import com.sharemob.tinchat.lib.viewpager.FragmentsAdapter;
import com.sharemob.tinchat.lib.viewpager.PagerScrollerActivity;
import com.sharemob.tinchat.lib.viewpager.TabInfo;
import com.sharemob.tinchat.modules.message.MytipFragment;
import com.sharemob.tinchat.modules.message.QuestionFragment;

/**
 * 
 * <一句话功能简述>
 *
 * @author lihangjie
 * version [版本号,2016-3-12 下午8:41:41]
 * @see    [相关类/方法]
 * @since  [产品/模块版本]
 *
 */
public class MygiftsActivity extends PagerScrollerActivity {
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.fragment_pager);
	  	initView();
		findViewById(R.id.fragment_title_back).setOnClickListener(
				new OnClickListener() {
					public void onClick(View v) {
						finish();
					}
				});
	  
		MyApplication.getInstance().addActivity(this);
		LocalUtils.applyLocalFont(getWindow().getDecorView());
	}
	
	@Override
	protected void setTabsAndAdapter() {
        tabs.add(new TabInfo(0, "赠送",  new MySendGiftsFragment()));
        tabs.add(new TabInfo(1, "收到",  new MyRevGiftsFragment()));
        this.adapter = new FragmentsAdapter(this, getSupportFragmentManager(), tabs);
	}

	@Override
	 public void finish() {
		 super.finish();
		 overridePendingTransition(R.anim.animi_remain,R.anim.out_to_right);
	 }
}
