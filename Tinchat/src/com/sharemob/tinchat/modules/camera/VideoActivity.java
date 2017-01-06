/**
 *  文件名:VideoActivity.java
 *  修改人:lihangjie
 *  创建时间:2015-10-2 下午11:10:38
 */
package com.sharemob.tinchat.modules.camera;

import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.hardware.Camera;
import android.media.CamcorderProfile;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.sharemob.tinchat.R;
import com.sharemob.tinchat.base.Callback;
import com.sharemob.tinchat.lib.LocalUtils;
import com.sharemob.tinchat.lib.common.SMGlobal;

/**
 * 
 * <一句话功能简述>
 *
 * @author lihangjie
 * version [版本号,2015-10-2 下午11:10:38]
 * @see    [相关类/方法]
 * @since  [产品/模块版本]
 *
 */
public class VideoActivity extends BaseCameraActivity implements SurfaceHolder.Callback{
	private MediaRecorder recorder;// 录制视频的类  
	private ProgressBar progressBar;
	private Timer timer;
	private Handler handler;
	private boolean VideoMark=false;
	private int cameraFacingPosition=1;
	private int ticket=0;
	private int TargetTicket=5*10;
	private TextView tv_video_countdown;
	private ImageButton iBtn_camera_change;
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		// 设置竖屏显示 
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		// 选择支持半透明模式,在有surfaceview的activity中使用
//		getWindow().setFormat(PixelFormat.TRANSLUCENT);  
		setContentView(R.layout.activity_video);
		//状态栏返回按钮
//		 LocalUtils.exitView(this,R.id.title_back,  R.anim.out_to_right,0);
		
		//显示倒计时
		tv_video_countdown=(TextView)findViewById(R.id.tv_video_countdown);
		tv_video_countdown.setText(String.valueOf(TargetTicket/10)+"秒");
		//显示录制视频
		surfaceview = (SurfaceView) this.findViewById(R.id.surfaceview_video);
		surfaceHolder = surfaceview.getHolder();// 取得holder  
//		surfaceview.getHolder().setFixedSize(320, 280);
		surfaceHolder.addCallback(this); // holder加入回调接口
		surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
	
		//进度条
		UpdateProgressBar();
		BtnEventVideoStart();
		BtnEventChangeCamera();
	}
	
	private void UpdateProgressBar(){
		progressBar=(ProgressBar)findViewById(R.id.progress_video_time);
		progressBar.setMax(TargetTicket);
		
	 	handler = new Handler() {  
            public void handleMessage(Message msg) {  
                super.handleMessage(msg);
                progressBar.incrementProgressBy(1);
                if(ticket%10==0){
                	tv_video_countdown.setText(String.valueOf(TargetTicket/10-ticket/10)+"秒");
                }
                if(ticket==TargetTicket){
                	//必须先关闭录制再关闭摄像头预览,否则出错
                	recorder.stop();
                	recorder.release();
                	camera.stopPreview();
                	camera.release();
//                	surfaceHolder=null;
                	forwardReleaseVideo_View();
                }
            }
    	};
	}
	
	private void BtnEventVideoStart(){
		LocalUtils.BtnEvent(this,R.id.btn_video_start, new Callback() {
			public void doDown() {
				if(!VideoMark){
					iBtn_camera_change.setVisibility(View.GONE);
					countDown();
					VideoMark=true;
					startVideo();
				}else{
					recorder.stop();    
//	                recorder.reset();    
	                recorder.release();    
					VideoMark=false;
				}
			}
		});
		
	}
	
	private void BtnEventChangeCamera(){
		iBtn_camera_change=(ImageButton)findViewById(R.id.iBtn_camera_change);
		iBtn_camera_change.setOnClickListener(new OnClickListener()
        {
            public void onClick(View v)
            {
            	ChangeCameraFacing();
            }
        });
	}
	
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
	}
	
	private void countDown(){
		timer=new Timer();
		timer.schedule(new TimerTask() {
			public void run() {
				if(ticket<TargetTicket){
					ticket++;
					handler.obtainMessage().sendToTarget();
				}else{
					timer.cancel();
				}
				
			}
		}, 1000, 100);
	}
	
	// 开始录制
	private void startVideo(){
				camera.unlock();
				recorder = new MediaRecorder();
		        recorder.reset(); 
			
				recorder.setCamera(camera);
//				camera.setPreviewTexture(surfaceview);
//				try {
//					camera.setPreviewDisplay(surfaceHolder);
//				} catch (IOException e1) {
//					e1.printStackTrace();
//				}
	             
	            recorder.setVideoSource(MediaRecorder.VideoSource.CAMERA);//视频源    
	            recorder.setAudioSource(MediaRecorder.AudioSource.MIC); //录音源为麦克风    
	            recorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);//输出格式为3gp  
	            CamcorderProfile mProfile = CamcorderProfile.get(CamcorderProfile.QUALITY_TIME_LAPSE_CIF);
	            recorder.setVideoSize(640, 480);
	            recorder.setAudioEncodingBitRate(1024);
	            recorder.setVideoEncodingBitRate(mProfile.videoBitRate);
	            recorder.setVideoFrameRate(mProfile.videoFrameRate);//视频帧频率 
	            recorder.setMaxDuration(10000);// 设置最大的录制时间  
	            recorder.setPreviewDisplay(surfaceHolder.getSurface());//预览
	            recorder.setOrientationHint(180);
	            recorder.setVideoEncoder(MediaRecorder.VideoEncoder.H264);//视频编码    
	            recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);//音频编码
	            recorder.setOutputFile(LocalUtils.SignatureVideoFilePath);//保存路径
	        try {    
	            recorder.prepare(); 
	            recorder.start(); 
	        } catch (IOException e) {    
	            e.printStackTrace();    
	        }  
	}

	 // 停止录制
	public void stopVideo(){
		if (recorder != null) {  
			recorder.stop();  
        }  
	}
	public void surfaceCreated(SurfaceHolder holder) {
		 surfaceHolder = holder;
		 
	}
	
	
	public void surfaceChanged(SurfaceHolder holder, int format, int width,int height) {
		   	surfaceview.setLayoutParams(new RelativeLayout.LayoutParams(width,height));
	        surfaceHolder = holder;
	        this.InitCameraParameter(width, height);
	}
	 // 释放资源 
	public void surfaceDestroyed(SurfaceHolder holder) {
//		  camera.setPreviewCallback(null) ;
		  camera.stopPreview();
		  camera.release();
		  camera = null;
	}
	protected void keyEventOfListView(int position, int id) {}
	
	public void finish() {
		super.finish();
		if(recorder!=null){
			recorder.stop(); 
	        recorder.release(); 
		}
		 overridePendingTransition(0, R.anim.out_to_right);
	}
	private void forwardReleaseVideo_View(){
//          gotoViewByAnim(VideoActivity.this, ReleaseVideoActivity.class, R.anim.in_from_bottom, true);
//          Intent intent = new Intent(); 
//          intent.setClass(SMGlobal.getInstance().context,ReleaseVideoActivity.class);
//          startActivity(intent);
//          overridePendingTransition(R.anim.in_from_bottom,0);
	}
	
}
