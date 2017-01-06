require "init"
local File=luajava.bindClass("java.io.File")
local LuaTimer=luajava.bindClass("com.androlua.LuaTimer")
local SurfaceHolder=luajava.bindClass("android.view.SurfaceHolder")
local MediaRecorder=luajava.bindClass("android.media.MediaRecorder")
local Camera=luajava.bindClass("android.hardware.Camera")
local CamcorderProfile=luajava.bindClass("android.media.CamcorderProfile")
local CameraUtils=luajava.bindClass("com.sharemob.tinchat.lib.CameraUtils")
local RelativeLayout=luajava.bindClass("android.widget.RelativeLayout")
local ImageFormat=luajava.bindClass("android.graphics.ImageFormat")
local LocalVideoView=luajava.bindClass("com.sharemob.tinchat.base.LocalVideoView")
local main={}
local surfaceview
local surfaceHolder
local recorder
local btn_video_start
local camera
local cameraFacingPosition=Camera.CameraInfo.CAMERA_FACING_FRONT
local ticket=0
local TargetTicket=10
local progress_video_time
local SignatureVideoFilePath=LocalUtils.SignatureVideoFilePath

function finish()
	this.finish() 
	this.overridePendingTransition(R.anim.in_from_left,R.anim.out_to_right);
end

local layout_header={
		FrameLayout,
		id="activity_header",
		layout_width="match_parent",
		layout_height=dimens.dx_45,
		layout_gravity="top",
		background=Color.TitleBgColor,
		layout_title_text("left|center_vertical","btn_title_left","取消"),
		{
			TextView,
			id="tv_title_content",
			layout_width="wrap_content",
			layout_height="wrap_content",
			layout_gravity="center|center_vertical",
			textStyle="bold",
			textColor=Color.TitleTextColor,
			textSize="17sp",
			text="个人视频介绍"
		},
		layout_title_text("right|center_vertical","btn_title_right_submit","保存")
}

local layout_bottom={
	LinearLayout,
	layout_alignParentBottom=true,
	layout_width="match_parent",
	layout_height="wrap",
	orientation="vertical",
	background=Color.TitleBgColor,
	{
		ProgressBar,
		id="progress_video_time",
		style="?android:attr/progressBarStyleHorizontal",
		layout_width="fill_parent",
		layout_height="2dp",
		max="100",
		progress="0",
		secondaryProgress="0",
	},
	{
		FrameLayout,
		layout_width="fill_parent",
		layout_height="120dp",
		{
			TextView,
			id="tv_video_countdown",
			layout_width="wrap_content",
			layout_height="wrap_content",
			layout_gravity="center_vertical|right",
			textColor=Color.White,
			textSize="20sp",
			layout_marginRight="20dp",
			text="10秒"
		},
		{
			ImageView,
			id="btn_camera_change",
			layout_width="35dp",
			layout_height="35dp",
			layout_marginLeft="20dp",
			visibility=LocalView.GONE,
			layout_gravity="center_vertical|left",
			background="drawable/ic_video_play"
		},
		{
			Button,
			id="btn_video_start",
			layout_width="100dp",
			layout_height="100dp",
			textColor=Color.White,
			textSize="15sp",
			text="开拍",
			layout_gravity="center_vertical|center_horizontal"
		}
	
	}
}

function startVideo()


end

function loadLayout()
	layout={
		RelativeLayout,
		layout_width="match_parent",
		layout_height="match_parent",
		{
			LinearLayout,
			layout_width="match_parent",
			layout_height="match_parent",
			background=Color.WhiteSmoke,
			orientation="vertical",
			layout_header,
		},
		{
			SurfaceView,
			id="surfaceview_video",
			layout_marginTop="45dp",
			layout_marginBottom="120dp",
			layout_width="match_parent",
			layout_height="match_parent"
		},
		{
			LocalVideoView,
			id="preview_video",
			visibility=LocalView.GONE,
			layout_marginTop="45dp",
			layout_marginBottom="120dp",
			layout_width="match_parent",
			layout_height="match_parent"
		},
		layout_bottom
	}
	this.setContentView(loadlayout(layout,main))
end

local Width
local Height
local preview_video
function PreviewVideo()
	preview_video=main["preview_video"]
	preview_video.setVideoPath(SignatureVideoFilePath)

	local file=File(SignatureVideoFilePath)
	if(file.length()==0 or UserInfo.signaturevideo=="")then
		main["btn_video_start"].setText("开拍")
	else
		main["btn_video_start"].setText("重试")
		preview_video.setVisibility(LocalView.VISIBLE)
		
		main["surfaceview_video"].setVisibility(LocalView.GONE)
	end
	
	CameraUtils.previewVideo(preview_video,SignatureVideoFilePath)
--	preview_video.setOnPreparedListener(MediaRecorder.OnPreparedListener,{
--		onPrepared=function(mp)
--			mp.start()
--		end
--	})
--	
--	preview_video.setOnCompletionListener(luajava.createProxy("android.media.MediaRecorder$MediaRecorder.OnCompletionListener",{
--		onCompletion=function(mp)
--			mp.reset()
--		end
--	}))
end

function initCamera()
	camera = Camera.open(Camera.CameraInfo.CAMERA_FACING_FRONT);
	local parameters = camera.getParameters();
	local bestSize=CameraUtils.getBestPreviewSize(Width,Height,parameters);
	parameters.setPreviewSize(bestSize.width, bestSize.height);
	parameters.setPictureFormat(ImageFormat.JPEG); 
	parameters.setJpegThumbnailQuality(100);
	parameters.set("jpeg-quality", 80);
	parameters.set("orientation", "portrait");
	camera.setParameters(parameters);
	camera.setPreviewDisplay(surfaceHolder);
	camera.setDisplayOrientation(90);
	camera.startPreview();
end

function preview()
	surfaceview=main["surfaceview_video"]
	local callback=SurfaceHolder_Callback{
    surfaceChanged=function(holder,format,width,height)
    	println("surfaceChanged")
		surfaceHolder = holder;
		Width=width
		Height=height
		initCamera()
    end,
    surfaceCreated=function(holder)
    	println("surfaceCreated")
    	surfaceHolder = holder;
		
    end,
    surfaceDestroyed=function(holder)
    	println("surfaceDestroyed")	
		if(camera~=nil)then
			surfaceHolder.removeCallback(callback);
			camera.release();
--    		camera.stopPreview();
    	end
    end
    }
	surfaceHolder=surfaceview.getHolder()
	surfaceHolder.addCallback(callback)
end

function onStop()
--	camera.release();
--	camera.stopPreview();
--	camera = nil
end
local handler =luajava.newInstance("android.os.Handler",{
	handleMessage=function(msg)
		if(ticket<TargetTicket)then
			ticket=ticket+1;
		elseif(ticket==TargetTicket)then
			recorder.stop()
			recorder.release()
			camera.stopPreview();
--            camera.release()
			luaTimer.cancel();
			luaTimer=nil;
			ticket=0;
			btn_video_start.setEnabled(true);
            main["tv_video_countdown"].setText("10秒");
            main["btn_video_start"].setText("重试")
            main["btn_camera_change"].setVisibility(LocalView.VISIBLE)
		end
		 progress_video_time.incrementProgressBy(1);
		 main["tv_video_countdown"].setText((TargetTicket-ticket).."秒");
end})

function start()
		local file=File(LocalUtils.SignatureVideoFilePath)
		if(file.exists())  then
			file.delete();
		end
	
		camera.unlock()
		recorder = MediaRecorder();
		recorder.reset(); 
		recorder.setCamera(camera);
		recorder.setVideoSource(MediaRecorder.VideoSource.CAMERA);--视频源    
        recorder.setAudioSource(MediaRecorder.AudioSource.MIC); --录音源为麦克风    
        recorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4) --输出格式为3gp
        local mProfile = CamcorderProfile.get(CamcorderProfile.QUALITY_TIME_LAPSE_CIF)
        recorder.setVideoSize(640, 480);
        recorder.setAudioEncodingBitRate(1024);
        recorder.setVideoEncodingBitRate(mProfile.videoBitRate)
        recorder.setVideoFrameRate(mProfile.videoFrameRate)
        recorder.setMaxDuration(10000)
        recorder.setPreviewDisplay(surfaceHolder.getSurface())
       	recorder.setOrientationHint(270)
        recorder.setVideoEncoder(MediaRecorder.VideoEncoder.H264)
        recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB)
        recorder.setOutputFile(LocalUtils.SignatureVideoFilePath)
        recorder.prepare()
        recorder.start()
        
        luaTimer=LocalUtils.getTimerTask(handler,1000);
        btn_video_start.setEnabled(false);
end

function onKeyDown(keyCode,event)
	if(keyCode == KeyEvent.KEYCODE_BACK or keyCode == KeyEvent.KEYCODE_HOME) then
		println("KEYCODE_HOME")
		this.finish()
	end
end

function onController()
	--取消
	setColorButton(main.btn_title_left,'#e8e8e8','#00000000',
	function()
		println("取消")
		finish()
	end,0)
	
	--保存视频
	setColorButton(main.btn_title_right_submit,'#e8e8e8','#00000000',
	function()
		println("保存视频")
		LocalUtils.UploadVoiceFile(this,string.format("%s",SignatureVideoFilePath),UserInfo.uid)
	end,0)
	
	--切换前置摄像头
	local btn_camera_change=main["btn_camera_change"]
	setDrawableButton(btn_camera_change,"ic_video_play","ic_video_play",function()
--		println("切换前置摄像头")
--		CameraUtils.ChangeCameraFacing(camera,surfaceHolder,Camera.CameraInfo.CAMERA_FACING_FRONT);
--		CameraUtils.previewVideo(this,SignatureVideoFilePath)
		preview_video.start()
	end)

	--进度条
	progress_video_time=main["progress_video_time"]
	progress_video_time.setBackgroundResource(R.drawable.progressbar)
	progress_video_time.setMax(TargetTicket)
	
	--摄像头预览
	preview()
	
	--开始录频
	btn_video_start=main["btn_video_start"]
	btn_video_start.setBackgroundResource(R.drawable.background_bg_special_disease_circle)
	attachOnClickListener(btn_video_start,function()
		start()
	end)
	
	PreviewVideo()
end

function onResume()

end

function onCreate(savedInstanceState)
	loadLayout()
	onController()
end