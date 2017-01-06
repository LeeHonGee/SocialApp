///**
// *  文件名:LiveActivity.java
// *  修改人:lihangjie
// *  创建时间:2015-10-2 下午11:09:31
// */
//package com.sharemob.tinchat.modules.camera;
//
//import java.util.Timer;
//import java.util.TimerTask;
//
//import android.hardware.Camera;
//import android.hardware.Camera.PreviewCallback;
//import android.os.Bundle;
//import android.os.Handler;
//import android.os.Message;
//import android.view.SurfaceHolder;
//import android.view.SurfaceView;
//import android.widget.FrameLayout;
//import android.widget.ProgressBar;
//import android.widget.RelativeLayout;
//import android.widget.TextView;
//
//import com.sharemob.tinchat.R;
//import com.sharemob.tinchat.base.BaseActivity;
//
///**
// * 
// * <一句话功能简述>
// *
// * @author lihangjie
// * version [版本号,2015-10-2 下午11:09:31]
// * @see    [相关类/方法]
// * @since  [产品/模块版本]
// *
// */
//public class LiveActivity extends BaseCameraActivity implements SurfaceHolder.Callback{
//	private ProgressBar progressBar;
//	private Timer timer;
//	private Handler handler;
//	private int ticket=0;
//	private int TargetTicket=3*1000;
//	
//	private TextView tv_live_time,tv_live_total,tv_live_online_total;
//	private TextView tv_progress_live_love_total;
//	
//	private int live_online_total=0;
//	private int live_total=0;
//	protected void onCreate(Bundle savedInstanceState) {
//		super.onCreate(savedInstanceState);
//		setContentView(R.layout.activity_live);
//		
//		tv_live_online_total=(TextView)findViewById(R.id.tv_live_online_total);
//		tv_live_online_total.setText("在线 "+live_online_total);
//		
//		tv_live_total=(TextView)findViewById(R.id.tv_live_total);
//		tv_live_total.setText("累计 "+live_total);
//		
//		tv_progress_live_love_total=(TextView)findViewById(R.id.tv_progress_live_love_total);
//		tv_progress_live_love_total.setText("0 ℃");
//		
//		
//		surfaceview = (SurfaceView) this.findViewById(R.id.surfaceview_live_cover);
//		surfaceHolder = surfaceview.getHolder();// 取得holder  
//		surfaceHolder.addCallback(this); // holder加入回调接口
//		surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
//		
//		tv_live_time=(TextView)findViewById(R.id.tv_live_time);
//		UpdateProgressBar();
//		countDown();
//	}
//	protected void keyEventOfListView(int position, int id) {}
//	public void surfaceCreated(SurfaceHolder holder) {
//		 surfaceHolder = holder;
//		 
//	}
//	
//	private void UpdateProgressBar(){
//		progressBar=(ProgressBar)findViewById(R.id.progress_live_love);
//		progressBar.setMax(500);
//		
//	 	handler = new Handler() {
//            public void handleMessage(Message msg) {  
//                super.handleMessage(msg);
//                progressBar.incrementProgressBy(1);
//    			ticket++;
//    			tv_progress_live_love_total.setText(ticket+" ℃");
//    			int hour=0;
//				if(ticket/3600>0){
//					hour=(ticket/3600)%24;
//				}else{
//					hour=ticket/3600;
//				}
//				int minute=0;
//				if(ticket/60>60){
//					minute=(ticket/60)%60;
//				}else{
//					minute=ticket/60;
//				}
//				int second=ticket%60;
//				
//                tv_live_time.setText("已直播  "+(hour>=10?hour:"0"+hour)+":"+(minute>=10?minute:"0"+minute)+":"+(second>=10?second:"0"+second));
//                if(ticket==TargetTicket){
//                	
//                }
//            }
//    	};
//	}
//	
//	private void countDown(){
//		timer=new Timer();
//		timer.schedule(new TimerTask() {
//			public void run() {
////				if(ticket<TargetTicket){
////					ticket++;
//					handler.obtainMessage().sendToTarget();
////				}else{
////					timer.cancel();
////				}
//				
//			}
//		}, 1000, 1000);
//	}
//	
//	public void surfaceChanged(SurfaceHolder holder, int format, int width,int height) {
//		   	surfaceview.setLayoutParams(new FrameLayout.LayoutParams(width,height));
//	        surfaceHolder = holder;
//	        this.InitCameraParameter(width, height);
//	        camera.setPreviewCallback(new PreviewCallback() {
//				public void onPreviewFrame(byte[] data, Camera camera) {
//					System.out.println(data.length);
//				}
//			});
//	}
//	 // 释放资源 
//	public void surfaceDestroyed(SurfaceHolder holder) {
//		  camera.setPreviewCallback(null) ;
//		  camera.stopPreview();
//		  camera.release();
//		  camera = null;
//	}
//
//}
