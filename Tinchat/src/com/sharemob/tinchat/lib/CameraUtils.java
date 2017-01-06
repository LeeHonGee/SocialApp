package com.sharemob.tinchat.lib;

import java.io.IOException;

import com.sharemob.tinchat.base.LocalVideoView;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.hardware.Camera;
import android.hardware.Camera.CameraInfo;
import android.media.MediaPlayer;
import android.net.Uri;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.RelativeLayout;

public class CameraUtils {

	public static void setLayoutParams(SurfaceView surfaceview,int width,int height){
		surfaceview.setLayoutParams(new RelativeLayout.LayoutParams(width,height));
	}
	
	public static void previewVideo(LocalVideoView videoView,String url){
		videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {  
            public void onPrepared(MediaPlayer mp) {
//            	mp.setVolume(0, 0);
//              mp.setLooping(true); 
              mp.start();
            }  
        });  
	}

	public  static Camera.Size getBestPreviewSize(int surfaceViewWidth,int surfaceViewHeight, Camera.Parameters parameters) {
		Camera.Size bestSize = null;
		// 不同机器 尺寸大小排序方式不一样 有的从小到大有的从大到小
		for (Camera.Size size : parameters.getSupportedPreviewSizes()) {
			if (size.width <= surfaceViewWidth&& size.height <= surfaceViewHeight) {
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
	
	@SuppressLint("NewApi")
	public static void ChangeCameraFacing(Camera camera,SurfaceHolder surfaceHolder,int cameraFacingPosition){
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
}
