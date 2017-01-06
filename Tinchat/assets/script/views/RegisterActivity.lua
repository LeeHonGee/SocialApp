require "init"
import "android.content.*"

local SMSSDK=luajava.bindClass("cn.smssdk.SMSSDK")
local HashMap=luajava.bindClass("java.util.HashMap")
local Bundle=luajava.bindClass("android.os.Bundle")
local Message=luajava.bindClass("android.os.Message")
local ticket=0
local TargetTicket=30
local account={channel=RegChannel.Phone,uid=UUID()}
local luaTimer
local btn_verification_code
local et_account_phone
local handler
local eventHandler
local EVENT={
	EVENT_SEND_VERIFICATION_CODE=1,
	EVENT_SUBMIT_VERIFICATION_CODE=2, 
	EVENT_GET_VERIFICATION_CODE=3,
	EVENT_GET_SUPPORTED_COUNTRIES=4
}
--初始化短信
LocalUtils.SMSSDK_initSDK(this,ShareMob_AppKey,ShareMob_AppSecret)

local verification_state=false
function finish()
	SMSSDK.unregisterEventHandler(eventHandler);
	enterActivity("IndexActivity",anim.in_from_left,anim.out_to_right,true)
	this.finish()
end

local main={}

function onKeyDown(keyCode,event)
	if(keyCode == KeyEvent.KEYCODE_BACK) then
		finish()
	end
end

local function EventVerificationCode(msg)
		if(ticket<TargetTicket)then
			ticket=ticket+1;
			btn_verification_code.setText(string.format(" %d秒后重试",(TargetTicket-ticket)))
			elseif(ticket==TargetTicket)then
			luaTimer.cancel();
			luaTimer=nil;
			ticket=0;
			btn_verification_code.setEnabled(true)
			btn_verification_code.setText("获取验证码")
			btn_verification_code.setBackgroundResource(R.drawable.style_btn_send_code);
		end	
end

local function EventSubmitVerificationCode(msg)
println("EventSubmitVerificationCode")

end


local function EventGetVerificationCode(msg)
	local bundle=msg.getData()
	local country=bundle.getString("country")
	local phone=bundle.getString("phone")
 	println("EventGetVerificationCode")
end


handler =luajava.newInstance("android.os.Handler",{
	handleMessage=function(msg)
		if(msg.what==EVENT.EVENT_SEND_VERIFICATION_CODE) then
			EventVerificationCode(msg)
		elseif(msg.what==EVENT.EVENT_SUBMIT_VERIFICATION_CODE) then
			EventSubmitVerificationCode(msg)
		elseif(msg.what==EVENT.EVENT_GET_VERIFICATION_CODE)then
			EventGetVerificationCode(msg)
		elseif(msg.what==EVENT.EVENT_GET_SUPPORTED_COUNTRIES)then
		
		end
	end
})

--EventHandler=luajava.bindClass("cn.smssdk.EventHandler")
--local eh=EventHandler{
--	afterEvent=function(event, result, data)
--		if (result == SMSSDK.RESULT_COMPLETE) then
--			ToastText("RESULT_COMPLETE")
--			if (event == SMSSDK.EVENT_SUBMIT_VERIFICATION_CODE) then
--				ToastText("EVENT_SUBMIT_VERIFICATION_CODE")
--				
--			elseif (event == SMSSDK.EVENT_GET_VERIFICATION_CODE)then
--				local phoneMap=data;
--				local code =phoneMap.get("phone");
--				ToastText("EVENT_GET_VERIFICATION_CODE:");
--				verification_state=true
--			end
--		end
--	end
--
--}
eventHandler=LocalUtils.SMSSDK_EventHandler(handler)
SMSSDK.registerEventHandler(eventHandler);

layout_header={
	FrameLayout,
	id="activity_header",
	layout_width="match_parent",
	layout_height=dimens.dx_45,
	layout_marginBottom="0dp",
	layout_gravity="top",
	background=Color.TitleBgColor,
--	{
--		ImageButton,
--		id="btn_cancle",
--		layout_width=dimens.dx_45,
--		layout_height=dimens.dx_45,
--		layout_gravity="left|center_vertical",
--		textColor="#ffffff",
--		background="#00000000",
--		textSize="17sp",
--		layout_marginLeft="10dp",
--		layout_marginTop="2dp",
--		background="drawable/btn_public_back_normal.png",
--		onClick=luajava.createProxy("android.view.View$OnClickListener",{
--			onClick=function(v)
--				finish()
--			end
--		})
--	},
	layout_title_image("left|center_vertical","btn_cancle","drawable/btn_public_back_normal.png"),
	{
		TextView,
		layout_width="wrap_content",
		layout_height="wrap_content",
		layout_gravity="center|center_vertical",
		textColor="#ffffff",
		textSize="17sp",
		text="用户注册"
	},
--	layout_title_text("right|center_vertical","btn_title_right_next","下一步")
--	,{
--		TextView,
--		id="btn_regnext",
--		layout_width="wrap_content",
--		layout_height="wrap_content",
--		layout_gravity="right|center_vertical",
--		textColor="#ffffff",
--		textSize="17sp",
--		text="下一步",
--		layout_marginRight="10dp",
--		layout_marginTop="2dp"
--	}
}

local function loadLayout()
	layout={
		LinearLayout,
		layout_width="fill",
		layout_height="fill",
		background=Color.WhiteSmoke,
		orientation="vertical",
		layout_header,line,
		{
			LinearLayout,
			background=Color.White,
			layout_width="fill",
			layout_height="wrap",
			visibility=LocalView.GONE,
			orientation="horizontal",
			{
				ImageView,
				id="btn_chat_loc",
				layout_width="20dp",
				layout_height="20dp",
				layout_marginLeft="15dp",
				layout_marginRight="5dp",
				layout_gravity="center_vertical",
				src="drawable/ic_chat_loc_normal"
			},{
				TextView,
				id="location_city",
				layout_width="match_parent",
				layout_height="50dp",
				background=Color.White,
				gravity="center_vertical",
				textStyle="bold",
				paddingRight="5dp",
				text="定位 ...",
				textColor="#595959",
				textSize="13sp"
			}
		},
		{
			LinearLayout,
			layout_width="fill",
			layout_height="wrap",
			orientation="horizontal",
			{
				TextView,
				layout_width="wrap",
				layout_height="50dp",
				background=Color.White,
				gravity="center_vertical",
				textStyle="bold",
				maxEms="10",
				digits="0123456789",
				maxLength="10",
				paddingLeft="15dp",
				paddingRight="5dp",
				singleLine=true,
				text="+86 |",
				textColor="#595959",
				textSize="18sp"
			},
				cell_editText_layout("请输入你的手机号","et_account_phone"),
		},
		line,
		{
			FrameLayout,layout_width="fill_parent",layout_height="50dp",
			cell_editText_layout("请输入4位验证码","et_verification_code"),
			{
				ColorButton,id="btn_send_verification_code",layout_margin="7dp",
				layout_width="100dp",gravity="center_vertical|center_horizontal",layout_gravity="right|center_vertical",
				layout_height="match_parent",textColor=Color.White,textStyle="bold",textSize="14sp",text="获取验证码",background="#5fac45"
			}
		},
		line,
		{
			ClearEditText,
			id="et_account_pwd",
			layout_width="fill_parent",
			layout_height="50dp",
			textSize="13sp",
			background=Color.White,
			text="",
			textColorHint=Color.textgrey,
			textColor=Color.Back,
			paddingRight="10dp",
			paddingLeft="15dp",
			maxEms="10",
			maxLength="20",
			password="true",
			hint="请设置6-15位数字或字母的密码",
			gravity="center_vertical",
			singleLine=true
		},line,
		{
			TextView,
			id="agree_agreement",
			layout_width="wrap_content",
			layout_height="wrap_content",
			layout_gravity="left",
			layout_marginTop="20dp",
			layout_marginLeft="15dp",
			text="同意",
			textColor="#595959",
			textSize="12sp"
		},
		{
			ColorButton,
			id="btn_next",
			layout_width="fill",
			layout_height="50dp",
			layout_gravity="center|bottom",
			layout_marginLeft="40dp",
			layout_marginRight="40dp",
			layout_marginTop="80dp",
			layout_marginBottom="20dp",
			gravity="center",
			soundEffectsEnabled=true,
			text="下一步",
			textColor=Color.White,
			textSize="15sp"
		}
		
	}
	this.setContentView(loadlayout(layout,main))
end

function onController()

	--限制输入框长度
	et_account_phone=main.et_account_phone
	et_account_phone.setInputFilter(11)
	
	local et_verification_code=main.et_verification_code
	et_verification_code.setInputFilter(4)
	
	local btn_title_cancle=main.btn_cancle
	setColorButton(btn_title_cancle,'#e8e8e8','#00000000',
	function()
		finish()
	end,0)
	
	
	local et_account_pwd=main.et_account_pwd
	et_account_pwd.setInputFilter(15)
		--ip定位
			AsyncHttps("http://restapi.amap.com/v3/ip?key=ee95e52bf08006f63fd29bcfbcf21df0",function(body)
				println(body)
				local t_status=cjson.decode(body)
					if(tointeger(t_status.status)==1) then
						account.province=t_status.province
						account.city=t_status.city
						account.adcode=t_status.adcode
						main.location_city.setText(account.city)
					else
						main.location_city.setText("定位 获取地理位置失败")
					end
			end)
	--浏览协议
	local agree_agreement=main.agree_agreement
	Html=luajava.bindClass("android.text.Html")
	local agreement_content=Html.fromHtml("<span color='#595959'>点击下一步表示同意<font color='#00a8ff'>TinChat软件许可及服务协议</font></span>")
	agree_agreement.setText(agreement_content)
	attachOnClickListener(agree_agreement,function() 
		enterActivity("UserProtocolActivity",anim.in_from_right,anim.out_to_left,false)
	end)
	
	
--	attachOnClickListener(main.btn_regnext,function()
--		account.phone=main.et_account_phone.getText().toString();
--		account.password=main.et_account_pwd.getText().toString();
--		local parameter=cjson.encode(account)
--	
--		local intent = Intent();
--		intent.putExtra("AppActivity","RegisterNextActivity");
--		intent.putExtra("parameters", parameter);
--		intent.setClassName(this,"com.sharemob.tinchat.component.AppActivity")
--		this.startActivity(intent);
--		this.finish()
--		enterActivity("RegisterNextActivity",anim.in_from_right,anim.out_to_left,false)
--	end)
--	
	--发送验证码
	btn_verification_code=main.btn_send_verification_code
	setColorButton(btn_verification_code,"#a9cd94","#82b266",function()
	
		local account_phone=et_account_phone.getText().toString();
		if(string.len(account_phone)==0) then
			ToastText("请输入手机号码!")
			return
		elseif(string.len(account_phone)==11) then
			if(btn_verification_code.isEnabled()) then
				luaTimer=LocalUtils.getTimerTask(handler,1000,EVENT.EVENT_SEND_VERIFICATION_CODE);
				btn_verification_code.setEnabled(false);
				btn_verification_code.setBackgroundResource(R.drawable.style_btn_send_code_disable);
			end
			LocalUtils.SMSSDK_sendMSM(account_phone)
		end
	end,0)
	
	local function goRegNextView()
		account.phone=main.et_account_phone.getText().toString();
		account.password=main.et_account_pwd.getText().toString();
		local parameter=cjson.encode(account)
	
		local intent = Intent();
		intent.putExtra("AppActivity","RegisterNextActivity");
		intent.putExtra("parameters", parameter);
		intent.setClassName(this,"com.sharemob.tinchat.component.AppActivity")
		this.startActivity(intent);
		this.finish()
	end
	
--	local function loadMyData(phone,password)
--		local data={
--			phone=phone,
--			adcode=adcode,
--			password=password,
--			channel=RegChannel.Phone
--		}
--		local url=URL_Register(cjson.encode(data))
----			println(url)
--		AsyncHttps(url,function(body)
--			println(body)
--			local status=cjson.decode(body)
----			println(status["result"])
--			if(status.result==0) then
--				ToastText(status.desc)
--			else
--				ToastText(status.desc)
----				UpdateUserInfo()
----				LocalUtils.gotoViewByAnim(activity,"com.sharemob.tinchat.modules.launch.MainActivity",anim.in_from_right,anim.out_to_left,true)
--			end
--		end)
--	end

	local function verifyTelephoneExist()
		local data={
			phone=account.phone,
			channel=account.channel
		}
		
		local url=URL_verifyTelephoneExist(cjson.encode(data))
		AsyncHttps(url,function(body)
			local status=cjson.decode(body)
			if(status.result==1) then
				ToastText(status.desc)
			else
--				ToastText(status.desc)
				goRegNextView()
			end
		end)
	end
	
	--下一步
	local function enterApp()

		local phone=et_account_phone.getText().toString();
		local account_pwd=et_account_pwd.getText().toString();
		local verification_code=et_verification_code.getText().toString();
		
		account.phone=phone
		account.password=account_pwd
		if(string.len(phone)==0) then
			ToastText("请输入手机号码!")
			return
		elseif(string.len(account_pwd)<6) then
			ToastText("密码不能小于6位")
		else
			local parameters=HashMap{
				appkey=ShareMob_AppKey,
				phone=phone,
				zone=86,
				code=verification_code
			}
			--验证码
			local url=string.format("https://api.sms.mob.com/sms/verify?appkey=%s&phone=%s&zone=86&code=%s",ShareMob_AppKey,phone,verification_code)
			AsyncHttps(url,function(body)
				println(body)
				local t_status=cjson.decode(body)
--					if(t_status.status==524 or t_status.status==520) then
						verifyTelephoneExist()
--					end
			end) 
		end
	end
	
	--点击下一步
	local btn_next=main["btn_next"]
	setColorButton(btn_next,"#a9cd94","#82b266",
	function()
		enterApp()
		println("点击下一步")
	
	end,5)
end

function onCreate(savedInstanceState)
	loadLayout()
	onController()
end