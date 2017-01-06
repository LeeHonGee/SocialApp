/**
 *  文件名:MessageActivity.java
 *  修改人:lihangjie
 *  创建时间:2016-3-12 下午8:41:41
 */
package com.sharemob.tinchat.modules.message;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;

import com.sharemob.tinchat.R;
import com.sharemob.tinchat.lib.viewpager.FragmentsAdapter;
import com.sharemob.tinchat.lib.viewpager.PagerScrollerActivity;
import com.sharemob.tinchat.lib.viewpager.TabInfo;

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
public class MessageActivity extends PagerScrollerActivity {

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		findViewById(R.id.fragment_title_back).setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				finish();
				overridePendingTransition(R.anim.in_from_left,R.anim.out_to_right);
			}
		});
	}

	@Override
	protected void setTabsAndAdapter() {
        tabs.add(new TabInfo(0, "系统消息",  new MytipFragment()));
        tabs.add(new TabInfo(1, "常见问题",  new QuestionFragment()));
        this.adapter = new FragmentsAdapter(this, getSupportFragmentManager(), tabs);
	}

}
