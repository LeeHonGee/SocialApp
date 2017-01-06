require "init"
import "android.content.*"

function finish()
	this.finish()
	
end

function onKeyDown(keyCode,event)
	if(keyCode == KeyEvent.KEYCODE_BACK) then
--		finish()

	end
end

function onReceive(context,intent)
	local action = intent.getAction()
	if (action==WXConstant.ACTION_WXAUTH_LOGIN) then
		local bundle = intent.getExtras()
		local responseText=bundle.getString("WXUserInfo")
		local t_response=cjson.decode(responseText)
		
		if(t_response.errcode==40001) then
--			enterActivity("LoginActivity",anim.in_from_right,anim.out_to_left,true)
			println("---back:"..responseText)
		else
			println(responseText)
			local WXUserInfo=cjson.decode(responseText)
			println(WXUserInfo.nickname)
			enterLocalActivity("com.sharemob.tinchat.modules.launch.MainActivity",R.anim.in_from_right,R.anim.out_to_left,true)
		end
		
	end
end

--local wxauthreceiver=luajava.createProxy("com.androlua.LuaBroadcastReceiver$OnReceiveListerer",{
--		onReceive=function(context,intent)
--		local action = intent.getAction()
--		if (action==WXConstant.ACTION_WXAUTH_LOGIN) then
--			local bundle = intent.getExtras()
--			local responseText=bundle.getString("WXUserInfo")
--			println("---user:"..responseText)
--			local WXUserInfo=cjson.decode(responseText)
--			
--			println(WXUserInfo.nickname)
--			enterLocalActivity("com.sharemob.tinchat.modules.launch.MainActivity",anim.in_from_right,anim.out_to_left,true)
--		end
--	end})
	
local function registerWinXinAuth()
	local filter =IntentFilter()
	filter.addAction(WXConstant.ACTION_WXAUTH_LOGIN)
	this.registerReceiver(filter)
end


function onDestroy()
--	println("程序已退出")
end

local function loadLayout()

	layout_button={
		LinearLayout,
		layout_gravity="bottom",
		layout_width="match_parent",
		layout_height="wrap_content",
		padding="10dp",
		layout_marginBottom="50dp",
		orientation="vertical",
		{
			ColorButton,
			id="btn_register",
			layout_width="fill",
			layout_height="40dp",
			layout_gravity="center|bottom",
			layout_marginLeft="50dp",
			layout_marginRight="50dp",
			layout_marginTop="50dp",
			gravity="center",
			soundEffectsEnabled=true,
			text="注册",
			textColor=Color.White,
			textSize="15sp"
		},
		{
			ColorButton,
			id="btn_login",
			layout_width="fill",
			layout_height="40dp",
			layout_gravity="center|bottom",
			layout_marginLeft="50dp",
			layout_marginRight="50dp",
			layout_marginTop="10dp",
			layout_marginBottom="20dp",
			gravity="center",
			soundEffectsEnabled=true,
			text="登录",
			textColor=Color.White,
			textSize="15sp"
		},
		{
			LinearLayout,
			layout_width="wrap_content",
			layout_height="wrap_content",
			layout_gravity="center_horizontal",
			gravity="fill_horizontal|center_vertical",
			orientation="horizontal",
			padding="10dp",
			spite_line,
			{
				TextView,
				layout_width="wrap_content",
				layout_height="wrap_content",
				textSize="14sp",
				text="第三方账户登录",
				textColor="#cacaca"
			},
			spite_line
		},
		{
			ImageView,
			id="iBtn_login_wechat",
			layout_width="60dp",
			layout_height="60dp",
			src="drawable/btn_wechat_friend_normal",
			layout_gravity="center|bottom",
			gravity="center"
		}
	}

	layout={
		FrameLayout,
		layout_width="match_parent",
		layout_height="match_parent",
		background="drawable/bg_get_start.jpg",
		orientation="vertical",
		layout_button
		
	}

	main={}
	this.setContentView(loadlayout(layout,main))
	
	--注册微信授权
	WXConstant.WeiXinRegisterApp()
	registerWinXinAuth()
	--注册
	setColorButton(main.btn_register,'#ed8d83','#ec7063',
	function() 
		enterActivity("RegisterActivity",anim.in_from_right,anim.out_to_left,true)
--		enterLocalActivity("com.sharemob.tinchat.modules.account.RegisterActivity",anim.in_from_right,anim.out_to_left,false)
	end,15)
	--登录按钮
	setColorButton(main.btn_login,'#a9cd94','#95cd73',
	function()
		enterActivity("LoginActivity",anim.in_from_right,anim.out_to_left,true)
	end,15)
	--分享登录
	setDrawableButton(main.iBtn_login_wechat,"btn_wechat_friend_pressed","btn_wechat_friend_normal",
	function()
		println("onClickFunc")
		WXConstant.WXSendAuthLogin()
	end)
	
end


function onCreate(savedInstanceState)
	loadLayout()

end