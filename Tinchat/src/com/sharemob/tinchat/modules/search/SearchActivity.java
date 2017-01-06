///**
// *  文件名:SearchActivity.java
// *  修改人:lihangjie
// *  创建时间:2016-4-14 上午12:12:34
// */
//package com.sharemob.tinchat.modules.search;
//
//import java.util.ArrayList;
//
//import android.app.Activity;
//import android.os.Bundle;
//import android.os.Handler;
//import android.view.View;
//import android.widget.AdapterView;
//import android.widget.AdapterView.OnItemClickListener;
//import android.widget.ListView;
//
//import com.sharemob.tinchat.R;
//import com.sharemob.tinchat.base.Item;
//import com.sharemob.tinchat.base.ListAdapter;
//import com.sharemob.tinchat.base.OptionHandler;
//import com.sharemob.tinchat.lib.LocalOptionUtils;
//import com.sharemob.tinchat.lib.LocalUtils;
//import com.sharemob.tinchat.lib.common.SMGlobal;
//
///**
// * 
// * <一句话功能简述>
// *
// * @author lihangjie
// * version [版本号,2016-4-14 上午12:12:34]
// * @see    [相关类/方法]
// * @since  [产品/模块版本]
// *
// */
//public class SearchActivity extends Activity {
//	private ArrayList<Item> list = new ArrayList<Item>();
//	private ListAdapter listAdapter = new ListAdapter();
//	private ListView listView = null;
//	private Handler handler = null;
//	
//	private String[] TagNames = new String[] {"基本资料", "年龄:","身高:","体型:", "学历:", "月收入:","婚姻状态:"};
//	private String[] TagKeys = new String[] { "title_info","spouse_age", "spouse_stature", "spouse_habitus", "spouse_education",
//			"spouse_income", "spouse_marriage" };
//	protected void onCreate(Bundle savedInstanceState) {
//		super.onCreate(savedInstanceState);
//		setContentView(R.layout.activity_search);
//		listView = (ListView) findViewById(R.id.layout_search_listview);
//		handler=new OptionHandler(this.getActivity(), list, listAdapter);
//		SMGlobal.getInstance().handler = this.handler;
//		listViewLoadData();
//		LocalUtils.exitView(this,R.id.title_back, R.anim.in_from_left,R.anim.out_to_right);
//	}
//	
//	protected void listViewLoadData() {
//		for (int i = 0; i < TagNames.length; i++) {
//			Item item = new Item();
//			item.setId(TagKeys[i]);
//			if ("title_info".equals(TagKeys[i])) {
//				item.setSubgroupTitle(true);
//				item.setSubgrouptTitleName(TagNames[i]);
//			}else {
//				item.setSubgroupTitle(false);
//				item.setIcon(false);
//				if ("spouse_location".equals(TagKeys[i]) ||"account".equals(TagKeys[i])) {
//					item.setArrow_icon(false);
//				} else {
//					item.setArrow_icon(true);
//				}
//
//				item.setDrawLeft(0);
//				item.setRightName("未填写", 0);
//				item.setName(TagNames[i], 0);
//			}
//			list.add(item);
//		}
//		listAdapter.setAdapter(list);
//		listView.setAdapter(listAdapter);
//		listView.setOnItemClickListener(new OnItemClickListener() {
//			public void onItemClick(AdapterView<?> parent, View view,
//					int position, long id) {
//				keyEventOfListView(position, parent.getId());
//			}
//		});
//	}
//
//	public Activity getActivity(){
//		return this;
//	}
//	private void SpouseMateKeyEvent(int position, int id) {
//		final Item item = list.get(position);
//		String tagKey = item.getId();
//		System.out.println("--------" + tagKey);
//		if ("spouse_income".equals(tagKey)) {
//			LocalOptionUtils.OptionIncome(getActivity(),item, SMGlobal.KeySpouseIncome,handler);
//		} else if ("spouse_education".equals(tagKey)) {
//			LocalOptionUtils.OptionEducation(getActivity(),item, SMGlobal.KeySpouseEducation,handler);
//		} else if ("spouse_stature".equals(tagKey)) {
//			LocalOptionUtils.OptionStature(getActivity(),item, SMGlobal.KeySpouseStature,handler);
//		} else if ("spouse_age".equals(tagKey)) {
//			LocalOptionUtils.OptionAge(getActivity(),item, SMGlobal.KeySpouseAge,handler);
//		} else if ("spouse_location".equals(tagKey)) {
//
//		} else if ("spouse_marriage".equals(tagKey)) {
//			LocalOptionUtils.OptionMarriage(getActivity(),item,SMGlobal.KeySpouseMarrige,handler);
//		}else if("spouse_habitus".equals(tagKey)){
//			LocalOptionUtils.OptionHabitus(getActivity(),item,SMGlobal.KeySpouseHabitus,handler);
//		}else if("native_place".equals(tagKey)){
//			LocalOptionUtils.OptionLocation(getActivity(),item,listAdapter);
//		}
//	}
//	
//	protected void keyEventOfListView(int position, int id) {
//		SpouseMateKeyEvent(position, id);
//	}
//}
