///**
// *  文件名:LiveCoverActivity.java
// *  修改人:lihangjie
// *  创建时间:2015-10-6 下午9:26:11
// */
//package com.sharemob.tinchat.modules.camera;
//
//import android.content.Intent;
//import android.graphics.Rect;
//import android.hardware.Camera;
//import android.hardware.Camera.CameraInfo;
//import android.hardware.Camera.PictureCallback;
//import android.hardware.Camera.ShutterCallback;
//import android.os.Bundle;
//import android.view.SurfaceHolder;
//import android.view.SurfaceView;
//import android.view.ViewTreeObserver;
//import android.widget.FrameLayout;
//
//import com.sharemob.tinchat.R;
//import com.sharemob.tinchat.base.Callback;
//import com.sharemob.tinchat.base.CropFloatDrawable;
//import com.sharemob.tinchat.lib.ImageUtils;
//import com.sharemob.tinchat.lib.LocalUtils;
//
///**
// * 
// * <一句话功能简述>
// *
// * @author lihangjie
// * version [版本号,2015-10-6 下午9:26:11]
// * @see    [相关类/方法]
// * @since  [产品/模块版本]
// *
// */
//public class LiveCoverActivity extends BaseCameraActivity implements SurfaceHolder.Callback{
//
//	public static final String LIVE_COVER = "LiveCover";
//	
//	private CropFloatDrawable mFloatDrawable;
//	private FrameLayout layout_crop_livecover;
//	private Rect rect=new Rect();
//	@Override
//	protected void onCreate(Bundle savedInstanceState) {
//		super.onCreate(savedInstanceState);
//		setContentView(R.layout.activity_livecover);
//		
//		 LocalUtils.exitView(this,R.id.title_back,  R.anim.out_to_right,0);
//		
//		surfaceview = (SurfaceView) this.findViewById(R.id.surfaceview_livecover);
//		mFloatDrawable=new CropFloatDrawable(this);
//		
//		layout_crop_livecover=(FrameLayout)findViewById(R.id.layout_crop_livecover);
//		ViewTreeObserver vto =layout_crop_livecover.getViewTreeObserver();
//		vto.addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener()
//		{
//			private boolean hasMeasured = false;
//			public boolean onPreDraw()
//			{
//				if (hasMeasured == false)
//				{
//					int height = layout_crop_livecover.getMeasuredHeight();
//					int width = layout_crop_livecover.getMeasuredWidth();
//					
//					int left=(int)layout_crop_livecover.getX();
//					int top=(int)layout_crop_livecover.getY();
//					
//					int right=(int)(left+width);
//					int bottom=(int)(top+height);
////					Utils.showToast("-height->"+height+"-width->"+width+"-left->"+left+"-top->"+top);
//					
//					rect.set(left,top,right,bottom);
//					mFloatDrawable.setBounds(rect);
//					layout_crop_livecover.setBackgroundDrawable(mFloatDrawable);
//					// 获取到宽度和高度后，可用于计算
//					hasMeasured = true;
//				}
//				return true;
//			}
//		});
//		surfaceHolder = surfaceview.getHolder();// 取得holder  
//		surfaceHolder.addCallback(this); // holder加入回调接口
//		surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
//		
//		LocalUtils.BtnEvent(this,R.id.iBtn_cover_change_camera, new Callback() {
//			public void doDown() {
//				ChangeCameraFacing();
//			}
//		});
//		
//		LocalUtils.BtnEvent(this,R.id.btn_live_cover,new Callback() {
//			public void doDown() {
//				camera.setDisplayOrientation(90);
//				camera.takePicture(new ShutterCallback(){
//					public void onShutter() {
//						
//					}
//					
//				}, new  PictureCallback() {
//					public void onPictureTaken(byte[] raw, Camera camera) {
//						shootSound();
//					}
//				}, new PictureCallback(){
//					public void onPictureTaken(byte[] data, Camera camera) {
////						Bitmap bitmap=	Utils.rotaingImageView(data);
////						Utils.ImageByteSaveFile(Utils.Bitmap2Bytes(bitmap),Utils.getLiveCoverFilePath().getPath());
////						Bitmap bitmap = Utils.compressImageFromFile(file.getAbsolutePath());
//						
//						ImageUtils.ImageByteSaveFile(data,LocalUtils.getLiveCoverFilePath().getPath());
//						Intent intent = new Intent();
//						Bundle bundle=new Bundle();
//						bundle.putParcelable(LIVE_COVER, rect);
//						intent.putExtras(bundle);
//						intent.setClass(LiveCoverActivity.this,ReadyLiveActivity.class);
//						startActivity(intent);
//						finish();
//						overridePendingTransition(R.anim.in_from_zoom,R.anim.out_from_top);
//					}
//					
//				});
//			}
//		});
//		
//	}
//	protected void keyEventOfListView(int position, int id) {
//
//	}
//	
//	public void surfaceCreated(SurfaceHolder holder) {
//			surfaceHolder = holder;
//	}
//	public void surfaceChanged(SurfaceHolder holder, int format, int width,int height) {
//		surfaceview.setLayoutParams(new FrameLayout.LayoutParams(width,height));  
//		surfaceHolder = holder;
//		InitCameraParameter(width, height);
//	}
//	
//	public void surfaceDestroyed(SurfaceHolder holder) {
//		  camera.setPreviewCallback(null) ;
//		  camera.stopPreview();
//		  camera.release();
//		  camera = null;
//	}
//	
//
//	public void finish() {
//		super.finish();
//		clearSound();
//		overridePendingTransition(0, R.anim.out_to_right);
//	}
//
//}
