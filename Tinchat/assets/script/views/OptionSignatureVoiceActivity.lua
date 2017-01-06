require "init"
LuaTimer=luajava.bindClass("com.androlua.LuaTimer")
MediaRecorder=luajava.bindClass("android.media.MediaRecorder")
MediaPlayer=luajava.bindClass("android.media.MediaPlayer")
CircleTimerView=luajava.bindClass("com.sharemob.tinchat.lib.CircleTimerView")
File=luajava.bindClass("java.io.File")

local circleTimerView
local ib_signatrueVoice
local tv_signature_void
local mStartPlaying
local handler
local luaTimer
local start=true
local mStartPlaying=true;
local ticket=0
local TargetTicket=100
local signatureVoiceFile
local SignatureVoicePath=LocalUtils.SignatureVoiceFilePath
local mplayer
main={}

function finish()
	this.finish() 
	this.overridePendingTransition(R.anim.in_from_left,R.anim.out_to_right);
end

function onKeyDown(keyCode,event)
	if(keyCode == KeyEvent.KEYCODE_BACK) then
		finish()
	end
end

local function controller()
	btn_recorde=main.btn_recorde
	tv_signature_void=main.tv_signatrue_voice_tip
	circleTimerView=main.ctv_signatrue_voice
	ib_signatrueVoice=main.ibtn_signatrueVoice
	
	signatureVoiceFile=File(SignatureVoicePath)
	 if(signatureVoiceFile.exists() and signatureVoiceFile.length()>0) then
        	ib_signatrueVoice.setBackgroundResource(R.drawable.btn_signatrue_voice_play);
        	circleTimerView.circleTime(100);
        	start=false
      else
        	signatureVoiceFile.createNewFile();
        	ib_signatrueVoice.setBackgroundResource(R.drawable.btn_signatrue_voice_recorde);
      end
	
	function onPlay(start)
		if (start) then
			playerListener=luajava.createProxy("com.sharemob.tinchat.lib.LocalUtils$LocalMediaPlayerListener",{
				onCompletion=function()
					mplayer.stop()
					btn_recorde.setEnabled(true);
					tv_signature_void.setText("")
					ib_signatrueVoice.setBackgroundResource(R.drawable.btn_signatrue_voice_play)
				end
			});
			mplayer=LocalUtils.CreateMediaPlayer(signatureVoiceFile.getAbsolutePath(),playerListener)
			tv_signature_void.setText("在播放中...");
			btn_recorde.setEnabled(false);
			ib_signatrueVoice.setBackgroundResource(R.drawable.btn_signatrue_voice_stop);
			mStartPlaying=false;
		else
			mplayer.stop();
			ib_signatrueVoice.setBackgroundResource(R.drawable.btn_signatrue_voice_play);
			mStartPlaying=true;
		end
	end
	
	 function onRecord(start)
        if (start) then
        	tv_signature_void.setText("在录音中...");
        	ib_signatrueVoice.setEnabled(false);
        	btn_recorde.setEnabled(false);
            mRecorder=LocalUtils.CreateMediaRecording(signatureVoiceFile.getAbsolutePath());
        else
           mRecorder.stop();
        end
    end
	
	local handler =luajava.newInstance("android.os.Handler",{
	handleMessage=function(msg)
		if(ticket<TargetTicket)then
			ticket=ticket+1;
		elseif(ticket==TargetTicket)then
			luaTimer.cancel();
			luaTimer=nil;
			ticket=0;
			ib_signatrueVoice.setEnabled(true);
			btn_recorde.setEnabled(true);
            ib_signatrueVoice.setBackgroundResource(R.drawable.btn_signatrue_voice_play);
            onRecord(false);
            tv_signature_void.setText("");
		end
        circleTimerView.circleTime(ticket);
	end})
	
	
	attachOnClickListener(ib_signatrueVoice,function()
		if(signatureVoiceFile.exists() and signatureVoiceFile.length()>0)then
            onPlay(mStartPlaying);
        else
            luaTimer=LocalUtils.getTimerTask(handler,100);
            ib_signatrueVoice.setBackgroundResource(R.drawable.btn_signatrue_voice_stop);
            if(timer~=nil) then
                onRecord(true);
            end
         end
	
	end)
	
	setColorButton(btn_recorde,'#f6f6f6','#e4e4e4',function()
			if(signatureVoiceFile.exists())then
					signatureVoiceFile.delete();
					circleTimerView.circleTime(0);
					tv_signature_void.setText("");
					ib_signatrueVoice.setBackgroundResource(R.drawable.btn_signatrue_voice_recorde);
			end
	 end,0)
	
	local btn_title_left=main.btn_title_left
	setColorButton(btn_title_left,'#e8e8e8','#00000000',
	function()
		println("取消")
		finish()
	end,0)
	
	local btn_title_right_submit=main.btn_title_right_submit
	setColorButton(btn_title_right_submit,'#e8e8e8','#00000000',
	function()
		println("保存语音签名")
		LocalUtils.UploadVoiceFile(this,string.format("%s",SignatureVoicePath),UserInfo.uid)
	end,0)
end

local function layout()
layout_header={
		FrameLayout,
		id="activity_header",
		layout_width="match_parent",
		layout_height=dimens.dx_45,
		layout_marginBottom="15dp",
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
			text="录制语音签名"
		},
		layout_title_text("right|center_vertical","btn_title_right_submit","保存")
	}
	
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
			{
				CircleTimerView,id="ctv_signatrue_voice",layout_gravity="center_horizontal",layout_marginTop="50dp",
				layout_width="250dp",layout_height="250dp",layout_centerInParent="true"
			},
			{
				TextView,id="tv_signatrue_voice_tip",layout_width="wrap_content",layout_height="wrap_content",
				layout_centerInParent="true",layout_marginTop="20dp",layout_gravity="center_vertical|center_horizontal",
				text="试试语音签名",textColor=Color.Back,textSize="13sp",textStyle="normal"
			},
			{
				ImageView,id="ibtn_signatrueVoice",layout_width="120dp",layout_height="120dp",
				background="drawable/btn_record_normal.png",
				layout_marginTop="20dp",layout_gravity="center_horizontal"
			}
		},
		{
			ColorButton,
			id="btn_recorde",
			layout_width="fill",
			layout_height="60dp",
			layout_centerHorizontal="true",
			layout_alignParentBottom="true",
			text="重新录制",
			textColor=Color.Back
		}
	}
		
	this.setContentView(loadlayout(layout,main))
end

function onCreate(savedInstanceState)
	layout()
	controller()
end