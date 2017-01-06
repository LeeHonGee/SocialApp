require "init"
local main={}
local TINCHAT_LAUNCH_ACTIVITY = "TINCHAT_LAUNCH_ACTIVITY";
local url="http://pic.qiantucdn.com/58pic/17/81/35/78C58PICdQ2_1024.jpg"
local DelayTime=10

local delay =luajava.newInstance("android.os.Handler",{
	handleMessage=function(msg)
		if(CacheManager.getInstance().userInfo.uid~=nil)then
			LocalUtils.gotoViewByAnim(this,"com.sharemob.tinchat.modules.launch.MainActivity",R.anim.in_from_right,R.anim.out_to_left,true)
		else
			LocalUtils.enterAppActivity(this,"IndexActivity",R.anim.in_from_right,R.anim.out_to_left,true)
		end	
	end
})

function onKeyDown(keyCode,event)
	if(keyCode == KeyEvent.KEYCODE_BACK) then
		println("onKeyDown")
		delay.removeMessages(1);
	end
end

local function layout()
layout={
		LinearLayout,
		layout_width="fill",
		layout_height="fill",
		orientation="vertical",
		background="drawable/logo.png",
		id="splash_view"
}

	this.setContentView(loadlayout(layout,main))

	LocalUtils.loadSplashImage(this,main.splash_view,url)
end


local function loadMyData()
	local url=URL_Load_UserInfo({accountID=""})
	
	AsyncHttp(url,function(body)
		CacheManager.UserInfo=body
		UserInfo=cjson.decode(CacheManager.UserInfo)
		local activityName=Matrix.getMetaObjectValue(this.getApplicationContext(),TINCHAT_LAUNCH_ACTIVITY)

		local LoginActivity="com.sharemob.tinchat.modules.launch.MainActivity"
		LocalUtils.gotoViewByAnim(activity,LoginActivity,anim.in_from_right,anim.out_to_left,true)
	end)
end


function onCreate(savedInstanceState)
	layout()
	delay.sendEmptyMessageDelayed(1, 1000);
end