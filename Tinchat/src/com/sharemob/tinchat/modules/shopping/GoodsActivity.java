package com.sharemob.tinchat.modules.shopping;

import android.os.Bundle;
import android.view.View;

import com.sharemob.tinchat.R;
import com.sharemob.tinchat.lib.LocalUtils;
import com.sharemob.tinchat.lib.MyApplication;
import com.sharemob.tinchat.lib.viewpager.FragmentsAdapter;
import com.sharemob.tinchat.lib.viewpager.PagerScrollerActivity;
import com.sharemob.tinchat.lib.viewpager.TabInfo;

public class GoodsActivity extends PagerScrollerActivity {
    
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_goods_item);
        initView();
        findViewById(R.id.fragment_title_back).setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                finish();
                overridePendingTransition(R.anim.in_from_right, R.anim.out_to_left);
            }
        });
        MyApplication.getInstance().addActivity(this);
        LocalUtils.applyLocalFont(getWindow().getDecorView());
    }
    
    protected void setTabsAndAdapter() {
        tabs.add(new TabInfo(0x0,getString(R.string.goods_item_tabinfo_goods), new FragmentGoods()));
        tabs.add(new TabInfo(0x1,getString(R.string.goods_item_tabinfo_details), new FragmentGoodsDetails()));
        tabs.add(new TabInfo(0x2,getString(R.string.goods_item_tabinfo_comments), new FragmentComments()));
        adapter = new FragmentsAdapter(this, getSupportFragmentManager(), tabs);
    }
}