///**
// *  文件名:BaseActivity.java
// *  修改人:lihangjie
// *  创建时间:2016-5-18 上午9:07:50
// */
//package com.sharemob.tinchat.component;
//
//import java.util.Iterator;
//import java.util.concurrent.ConcurrentHashMap;
//
//import android.graphics.Bitmap;
//import android.os.Bundle;
//import android.widget.ImageView;
//import android.widget.ImageView.ScaleType;
//
//import com.androlua.LuaActivity;
//import com.androlua.LuaBitmap;
//import com.nostra13.universalimageloader.core.ImageLoader;
//import com.sharemob.tinchat.lib.LocalUtils;
//import com.sharemob.tinchat.lib.common.ReleaseImageLoadingListener;
//
///**
// * 
// * <一句话功能简述>
// *
// * @author lihangjie
// * version [版本号,2016-5-18 上午9:07:50]
// * @see    [相关类/方法]
// * @since  [产品/模块版本]
// *
// */
//public class BaseLuaActivity extends LuaActivity {
//
//	@Override
//	public void onCreate(Bundle savedInstanceState) {
//		super.onCreate(savedInstanceState);
//		 LocalUtils.applyLocalFont(getWindow().getDecorView());
//	}
//	
//	
////	
////	public ReleaseImageLoadingListener loadingListener=new ReleaseImageLoadingListener(){
////		 public void onLoadingComplete(String imageUri, android.view.View view, android.graphics.Bitmap loadedImage) {
////			 ImageView imageView=(ImageView)view;
////			 imageView.setScaleType(ScaleType.CENTER_CROP);
////			 imageView.setImageBitmap(loadedImage);   
////           };
////	};
//	
//}
