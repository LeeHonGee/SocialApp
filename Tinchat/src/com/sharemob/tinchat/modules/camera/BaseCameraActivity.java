/**
 *  文件名:BaseCameraActivity.java
 *  修改人:lihangjie
 *  创建时间:2015-10-7 上午10:52:31
 */
package com.sharemob.tinchat.modules.camera;

import java.io.IOException;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.ImageFormat;
import android.graphics.PixelFormat;
import android.hardware.Camera;
import android.hardware.Camera.CameraInfo;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.view.KeyEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.sharemob.tinchat.R;
import com.sharemob.tinchat.base.BaseActivity;

/**
 * 
 * <一句话功能简述>
 * 
 * @author lihangjie version [版本号,2015-10-7 上午10:52:31]
 * @see [相关类/方法]
 * @since [产品/模块版本]
 * 
 */
public abstract class BaseCameraActivity extends BaseActivity {

	protected Camera camera;
	protected SurfaceView surfaceview;
	protected SurfaceHolder surfaceHolder;
	private int cameraFacingPosition=1;
	
	public Camera.Size getBestPreviewSize(int surfaceViewWidth,int surfaceViewHeight, Camera.Parameters parameters) {
		Camera.Size bestSize = null;
		// 不同机器 尺寸大小排序方式不一样 有的从小到大有的从大到小
		for (Camera.Size size : parameters.getSupportedPreviewSizes()) {
			if (size.width <= surfaceViewWidth
					&& size.height <= surfaceViewHeight) {
				if (bestSize == null) // 初始化一个值
					bestSize = size;
				else {
					int tempArea = bestSize.width * bestSize.height;
					int newArea = size.width * size.height;

					if (newArea > tempArea) // 取满足条件里面最大的
						bestSize = size;
				}
			}
		}

		return bestSize;
	}

	public void finish() {
		super.finish();
//		if(camera!=null){
//			camera.setPreviewCallback(null) ;
//			camera.stopPreview();
//			camera.release();
//			camera = null;
//		}
	
//		surfaceview = null;  
//		surfaceHolder = null; 
		overridePendingTransition(0, R.anim.out_to_right);
	}
	protected void InitCameraParameter(int width,int height){
		   try {
			   		camera=null;
	        	if(camera==null){
	        		camera = Camera.open(CameraInfo.CAMERA_FACING_FRONT); // 获取Camera实例
			        
		        	Camera.Parameters parameters = camera.getParameters();
		        	Camera.Size bestSize=getBestPreviewSize(width,height,parameters);
		        	parameters.setPreviewSize(bestSize.width, bestSize.height);
		        	parameters.setPictureFormat(ImageFormat.JPEG); 
		        	parameters.setJpegThumbnailQuality(100);// 设置质量
		        	parameters.set("jpeg-quality", 80);
		        	camera.setParameters(parameters);
		            camera.setPreviewDisplay(surfaceHolder);
		            camera.setDisplayOrientation(90);// 设置预览视频时时竖屏
			        // 启动预览功能
			        camera.startPreview();
		            cameraFacingPosition=CameraInfo.CAMERA_FACING_FRONT;
	        	}
	        } catch (Exception e) {
	            // 如果出现异常，则释放Camera对象
	        	if(camera!=null){
	        		  camera.release();
	        	}
	          
	        }
	}
	@SuppressLint("NewApi")
	protected void ChangeCameraFacing(){
		//切换前后摄像头
        int cameraCount = 0;
        CameraInfo cameraInfo = new CameraInfo();
        cameraCount = Camera.getNumberOfCameras();//得到摄像头的个数

        for(int i = 0; i < cameraCount; i++) {
            Camera.getCameraInfo(i, cameraInfo);//得到每一个摄像头的信息
            if(cameraFacingPosition == 1) {
                //现在是后置，变更为前置
                if(cameraInfo.facing  == Camera.CameraInfo.CAMERA_FACING_FRONT) {//代表摄像头的方位，CAMERA_FACING_FRONT前置      CAMERA_FACING_BACK后置  
                    camera.stopPreview();//停掉原来摄像头的预览
                    camera.release();//释放资源
                    camera = null;//取消原来摄像头
                    camera = Camera.open(i);//打开当前选中的摄像头
                    try {
//                    	camera.startFaceDetection();
                        camera.setPreviewDisplay(surfaceHolder);//通过surfaceview显示取景画面
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    
                    
                    camera.setDisplayOrientation(90);// 设置预览视频时时竖屏
                    camera.startPreview();//开始预览
                    cameraFacingPosition = Camera.CameraInfo.CAMERA_FACING_BACK;
                    break;
                }
            } else {
                //现在是前置， 变更为后置
                if(cameraInfo.facing  == Camera.CameraInfo.CAMERA_FACING_BACK) {//代表摄像头的方位，CAMERA_FACING_FRONT前置      CAMERA_FACING_BACK后置  
                    camera.stopPreview();//停掉原来摄像头的预览
                    camera.release();//释放资源
                    camera = null;//取消原来摄像头
                    camera = Camera.open(i);//打开当前选中的摄像头
                    
                    try {
                        camera.setPreviewDisplay(surfaceHolder);//通过surfaceview显示取景画面
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    
                    camera.setDisplayOrientation(90);// 设置预览视频时时竖屏
                    camera.startPreview();//开始预览
                    cameraFacingPosition = Camera.CameraInfo.CAMERA_FACING_FRONT;
                    break;
                }
            }

        }
	}
	
	public int getCameraFacingPosition() {
		return cameraFacingPosition;
	}
	

    /** 
     *   播放系统拍照声音 
     */  
	private MediaPlayer shootMP=null;
    public void shootSound()  
    {  
        AudioManager meng = (AudioManager) getSystemService(Context.AUDIO_SERVICE);  
        int volume = meng.getStreamVolume( AudioManager.STREAM_NOTIFICATION);  
      
        if (volume != 0)  
        {  
            if (shootMP == null) {
                shootMP = MediaPlayer.create(this, Uri.parse("file:///system/media/audio/ui/camera_click.ogg"));  
            }
            if (shootMP != null){  
                shootMP.start(); 
            }
        }  
    }  
    
    public void clearSound(){
//    	shootMP=null;
    }
	
}
