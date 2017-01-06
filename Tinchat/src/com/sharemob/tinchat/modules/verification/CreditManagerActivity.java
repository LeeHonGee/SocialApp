//package com.sharemob.tinchat.modules.verification;
//
//import java.util.ArrayList;
//
//import com.sharemob.tinchat.R;
//import com.sharemob.tinchat.base.Item;
//import com.sharemob.tinchat.base.ListAdapter;
//import com.sharemob.tinchat.base.NoScrollListview;
//import com.sharemob.tinchat.lib.LocalUtils;
//
//import android.app.Activity;
//import android.graphics.Color;
//import android.graphics.drawable.ColorDrawable;
//import android.os.Bundle;
//import android.view.View;
//import android.view.View.OnClickListener;
//import android.widget.FrameLayout;
//import android.widget.ImageButton;
//import android.widget.LinearLayout;
//import android.widget.LinearLayout.LayoutParams;
//import android.widget.ListView;
//import android.widget.TextView;
//
//public class CreditManagerActivity extends Activity {
//
//	@Override
//	protected void onCreate(Bundle savedInstanceState) {
//		super.onCreate(savedInstanceState);
//
//		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.MATCH_PARENT);
//		final LinearLayout layout_main=new LinearLayout(this); 
//		layout_main.setBackgroundColor(0xFFEBEBEB);
//		layout_main.setOrientation(LinearLayout.VERTICAL);
//		
//		//状态栏返回按钮
//		LinearLayout layout_title=(LinearLayout)View.inflate(this,R.layout.activity_title_header, null);
//		ImageButton btn_title_back=(ImageButton)layout_title.findViewById(R.id.title_back);
//		btn_title_back.setVisibility(View.VISIBLE);
//		btn_title_back.setOnClickListener(new OnClickListener() {
//			public void onClick(View v) {
//				finish();
//			}
//		});
//		TextView tv_title_content=(TextView)layout_title.findViewById(R.id.tv_title_content);
//		tv_title_content.setText("信用管理");
//		
//		layout_main.addView(layout_title);
//		
//		
//		
////		  <com.sharemob.tinchat.base.NoScrollListview
////          android:layout_width="fill_parent"
////          android:layout_height="wrap_content"
////          android:background="@drawable/style_panel_bg"
////          android:divider="#cbcbcb"
////          android:dividerHeight="0.8px"
////          android:isScrollContainer="true"
////          android:listSelector="@drawable/selector_list_item"/>
//		NoScrollListview listView=new NoScrollListview(this);
//		ListView.LayoutParams listView_params = new ListView.LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.WRAP_CONTENT);
//		listView.setLayoutParams(listView_params);
//		
//		listView.setBackgroundDrawable(getResources().getDrawable(R.drawable.style_panel_bg));
//		listView.setScrollContainer(true);
//		listView.setSelector(R.drawable.selector_list_item);
//		listView.setDivider(new ColorDrawable(0xFFcbcbcb));
//		listView.setDividerHeight(1);
//		
//		ArrayList<Item> items=new ArrayList<Item>();
//		ListAdapter listAdapter=new ListAdapter();
//		
//		 Item autonym=new Item();
//		 autonym.setId("autonym");
//		 autonym.setIcon(true);
//		 autonym.setResid(R.drawable.realname_large_gray);
//		 autonym.setArrow_icon(true);
//		 autonym.setDrawLeft(0);
//		 autonym.setRightName("尚未认证");
//		 autonym.setName("实名认证"); 
//	     items.add(autonym);
//	     
//	     Item phone=new Item();
//	     phone.setId("phone");
//	     phone.setIcon(true);
//	     phone.setResid(R.drawable.phone_large_gray);
//		 phone.setArrow_icon(true);
//		 phone.setDrawLeft(0);
//		 phone.setRightName("尚未认证");
//		 phone.setName("手机认证"); 
//	     items.add(phone);
//	     
//	     Item workplace=new Item();
//	     workplace.setId("workplace");
//	     workplace.setIcon(true);
//	     workplace.setResid(R.drawable.phone_large_gray);
//	     workplace.setArrow_icon(true);
//	     workplace.setDrawLeft(0);
//	     workplace.setRightName("尚未认证");
//	     workplace.setName("就职公司"); 
//	     items.add(workplace);
//		
//	     Item education=new Item();
//	     education.setId("education");
//	     education.setIcon(true);
//	     education.setResid(R.drawable.education_large_gray);
//	     education.setArrow_icon(true);
//	     education.setDrawLeft(0);
//	     education.setRightName("尚未认证");
//	     education.setName("学历学籍"); 
//	     items.add(education);
//		listAdapter.setAdapter(items);
//		listView.setAdapter(listAdapter);
//		
//		
//		
//		
//		
//		layout_main.addView(listView);
//		this.addContentView(layout_main, params);
//	}
//	
//}
