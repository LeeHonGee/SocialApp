///**
// *  文件名:MainTestActivity.java
// *  修改人:lihangjie
// *  创建时间:2015-11-11 下午8:04:13
// */
//package com.sharemob.tinchat.modules.launch;
//
//import java.util.List;
//import android.annotation.SuppressLint;
//import android.os.Bundle;
//import android.view.View;
//import com.sharemob.tinchat.R;
//import com.sharemob.tinchat.base.BaseActivity;
//import com.sharemob.tinchat.base.NoScrollListview;
//import com.sharemob.tinchat.lib.ImageUtils;
//import com.sharemob.tinchat.modules.danmu.CacheMemory;
//import com.sharemob.tinchat.modules.danmu.DanmuItem;
//import com.sharemob.tinchat.modules.danmu.ListAdapterForDanmu;
//import com.sharemob.tinchat.modules.danmu.RemoveTaskMgr;
//import com.sharemob.tinchat.modules.danmu.SendTaskMgr;
//
///**
// * 
// * <一句话功能简述>
// *
// * @author lihangjie
// * version [版本号,2015-11-11 下午8:04:13]
// * @see    [相关类/方法]
// * @since  [产品/模块版本]
// *
// */
//@SuppressLint("NewApi")
//public class MainTestActivity extends BaseActivity {
//
//	private List<DanmuItem> list_danmu=CacheMemory.list_danmu;
//	private ListAdapterForDanmu listAdapter=null;
//	private static final int ADD_VIEW=1;
//	private static final int REMOVE_VIEW=2;
//	private static String[] messages=new String[]{"那他怎么能把灿烂的星星偷来放在你双眸中呢?","在吗","淡淡的","来了","吧"};
//	private DanmuItem addView(){
//		DanmuItem danmuItem=new DanmuItem();
//		danmuItem.setHeaderIcon(ImageUtils.getBitMapFromRescourceID(this,R.drawable.avatar_normal));
//		danmuItem.setNickname("@leehongee"+(list_danmu.size()));
//		danmuItem.setMessage(messages[list_danmu.size()%4]);
////		list_danmu.add(danmuItem);
//		return danmuItem;
//	}
//	
//	private void removeItemView(){
//		if(list_danmu.size()>0&&listView!=null&&listAdapter!=null){
//			final int index=listView.getFirstVisiblePosition();
//			final View viewChild=listView.getChildAt(index);
//			if(viewChild!=null)
//			listAdapter.remove(viewChild, index); 
//		}
//	}
//	
//	private NoScrollListview listView=null;
//	protected void onCreate(Bundle savedInstanceState) {
//		super.onCreate(savedInstanceState);
//		setContentView(R.layout.activity_main_test);
//		
//		listView=(NoScrollListview)findViewById(R.id.listview_danmu);
//		listAdapter=new ListAdapterForDanmu(this);
//		listAdapter.setAdapter(list_danmu);
//		listView.setAdapter(listAdapter);
//		listAdapter.notifyDataSetChanged();
//
//        
//       final SendTaskMgr sendTaskMgr=new SendTaskMgr(new SendTaskMgr.Callback(){
//			public void sendMessageItem() {
//	        	listAdapter.notifyDataSetChanged();
//			}
//        });
//       for(int i=0;i<7;i++){
//    	   sendTaskMgr.sendMessage(addView());
//       }
//       
//        
//        RemoveTaskMgr removeTaskMgr=new RemoveTaskMgr(new RemoveTaskMgr.Callback() {
//			public void removeItem() {
//				removeItemView();
//			}
//		});
//        removeTaskMgr.start();
//
////		BtnEvent(R.id.test_btn_fade_out,new Callback() {
////			public void doDown() {
////				handler.sendEmptyMessage(ADD_VIEW);
////			}
////		});
//		
//	}
//	
//	protected  void keyEventOfListView(int position, int id){}
//
//}
