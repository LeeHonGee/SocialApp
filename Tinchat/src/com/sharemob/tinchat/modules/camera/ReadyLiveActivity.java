///**
// *  文件名:ReadyLiveActivity.java
// *  修改人:lihangjie
// *  创建时间:2015-10-7 下午4:52:20
// */
//package com.sharemob.tinchat.modules.camera;
//
//import java.io.File;
//
//import android.annotation.SuppressLint;
//import android.content.Intent;
//import android.graphics.Bitmap;
//import android.graphics.Rect;
//import android.graphics.drawable.Drawable;
//import android.os.Bundle;
//import android.widget.TextView;
//
//import com.sharemob.tinchat.R;
//import com.sharemob.tinchat.base.BaseActivity;
//import com.sharemob.tinchat.base.Callback;
//import com.sharemob.tinchat.component.SlideSwitch;
//import com.sharemob.tinchat.lib.ImageUtils;
//import com.sharemob.tinchat.lib.LocalUtils;
//
///**
// * 
// * <一句话功能简述>
// *
// * @author lihangjie
// * version [版本号,2015-10-7 下午4:52:20]
// * @see    [相关类/方法]
// * @since  [产品/模块版本]
// *
// */
//public class ReadyLiveActivity extends BaseActivity {
//
//	private TextView tv_live_location_address;
//	private String live_location_address;
//	private CropView crop_livecover;
//	@SuppressLint("NewApi")
//	protected void onCreate(Bundle savedInstanceState) {
//		super.onCreate(savedInstanceState);
//		setContentView(R.layout.activity_readylive);
//		Bundle bundle=getIntent().getExtras();
//		final Rect liveCover=bundle.getParcelable(LiveCoverActivity.LIVE_COVER);
//		
//		//获取临时直播封面照片
//		File file=new File(LocalUtils.getLiveCoverFilePath().getPath());
//		Bitmap livecover_bitmap = ImageUtils.compressImageFromFile(file.getAbsolutePath());
//		Drawable drawable_crop_livecover=ImageUtils.BitmapToDrawable(ImageUtils.rotateBitmapByDegree(livecover_bitmap, 90));
//		
//		crop_livecover=(CropView)findViewById(R.id.iv_crop_livecover);
//		
//		
//		crop_livecover.setDrawable(drawable_crop_livecover,liveCover);
//		
////		iv_crop_livecover.setBackground(drawable_crop_livecover);
//		
//		
//		
////		RelativeLayout layout_readylive=(RelativeLayout)findViewById(R.id.layout_readylive);
////		File file=new File(Utils.getLiveCoverFilePath().getPath());
////		Bitmap bitmap = Utils.compressImageFromFile(file.getAbsolutePath());
////		layout_readylive.setBackground(Utils.BitmapToDrawable(Utils.rotateBitmapByDegree(bitmap, 90)));
//		
//		tv_live_location_address=(TextView)findViewById(R.id.tv_live_location_address);
//		live_location_address=new String("广东省深圳市宝安区勤诚达");
//		SlideSwitch btn_change_location=(SlideSwitch)findViewById(R.id.btn_change_location);
//		btn_change_location.setSlideListener(new SlideSwitch.SlideListener(){
//			public void open() {
//				tv_live_location_address.setText(live_location_address);
//			}
//			public void close() {
//				tv_live_location_address.setText("宇宙星系火星麒麟岛屿");
//			}
//		});
//		
//		 LocalUtils.exitView(this,R.id.title_back,  R.anim.out_to_right,0);
//		
//		 LocalUtils.BtnEvent(this,R.id.iBtn_change_camera_facing, new Callback() {
//			public void doDown() {
//			   	Intent intent = new Intent();
//	            intent.setClass(ReadyLiveActivity.this,LiveCoverActivity.class);
//	            startActivity(intent);
//	        	overridePendingTransition(R.anim.in_from_zoom,0);
//			}
//		});
//		
//		 LocalUtils.BtnEvent(this,R.id.btn_start_live, new Callback() {
//			public void doDown() {
//				
//				byte[] data=ImageUtils.Bitmap2Bytes(crop_livecover.getCropImage());
//				ImageUtils.ImageByteSaveFile(data, LocalUtils.getLiveCoverTempFilePath());
//				
//			   	Intent intent = new Intent();
//	            intent.setClass(ReadyLiveActivity.this,LiveActivity.class);
//	            startActivity(intent);
//	        	overridePendingTransition(R.anim.in_from_bottom,0);
//				
//
//				
//			}
//		});
//	}
//	protected void keyEventOfListView(int position, int id) {
//
//	}
//
//	public void finish() {
//		super.finish();
//		overridePendingTransition(0, R.anim.out_to_right);
//	}
//}
