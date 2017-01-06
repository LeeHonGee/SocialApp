package com.sharemob.tinchat.modules.verification;

import com.sharemob.tinchat.R;
import com.sharemob.tinchat.lib.LocalUtils;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.LinearLayout.LayoutParams;

public class PromoteCreditActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.MATCH_PARENT);
		final LinearLayout layout_main=new LinearLayout(this); 
		layout_main.setBackgroundColor(0xFFEBEBEB);
		layout_main.setOrientation(LinearLayout.VERTICAL);
		
		//状态栏返回按钮
		LinearLayout layout_title=(LinearLayout)View.inflate(this,R.layout.activity_title_header, null);
		ImageButton btn_title_back=(ImageButton)layout_title.findViewById(R.id.title_back);
		btn_title_back.setVisibility(View.VISIBLE);
		btn_title_back.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				finish();
			}
		});
		TextView tv_title_content=(TextView)layout_title.findViewById(R.id.tv_title_content);
		tv_title_content.setText("信用提升");
		
		layout_main.addView(layout_title);
		
		this.addContentView(layout_main, params);
		
	}
}
