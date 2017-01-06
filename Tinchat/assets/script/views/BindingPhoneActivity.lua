require "init"

local ticket=0
local TargetTicket=30
local luaTimer
local btn_verification_code
local et_account_phone
local handler
local eventHandler

local main={}

function finish()
	this.finish() 
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

function onKeyDown(keyCode,event)
	if(keyCode == KeyEvent.KEYCODE_BACK) then
		finish()
	end
end

function layout()
	
	layout_header={
		FrameLayout,
		id="activity_header",
		layout_width="match_parent",
		layout_height=dimens.dx_45,
		layout_marginBottom="15dp",
		layout_gravity="top",
		background=Color.TitleBgColor,
		{
			Button,
			id="btn_cancle",
			layout_width="wrap_content",
			layout_height="wrap_content",
			layout_gravity="left|center_vertical",
			textColor="#ffffff",
			background="#00000000",
			textSize="17sp",
			layout_marginLeft="10dp",
			layout_marginTop="2dp",
			text='取消',
			onClick=luajava.createProxy("android.view.View$OnClickListener",{
				onClick=function(v)
					finish()
				end
			})
		},
		{
			TextView,
			layout_width="wrap_content",
			layout_height="wrap_content",
			layout_gravity="center|center_vertical",
			textColor="#ffffff",
			textSize="17sp",
			text="手机认证绑定"
		}
--		,{
--			Button,
--			id="btn_next",
--			layout_width="wrap_content",
--			layout_height="wrap_content",
--			layout_gravity="right|center_vertical",
--			textColor="#ffffff",
--			textSize="17sp",
--			text="下一步",
--			background="#00000000",
--			layout_marginRight="10dp",
--			layout_marginTop="2dp"
--		}
	}
	
	layout={
		LinearLayout,
		layout_width="match_parent",
		layout_height="match_parent",
		background=Color.WhiteSmoke,
		orientation="vertical",
		layout_header,
		{	
			LinearLayout,
			layout_width="fill_parent",
			layout_height="fill_parent",
			gravity="fill_vertical",
			orientation="vertical",
--			line,
--			{
--				TextView,
--				layout_width="fill_parent",
--				layout_height="50dp",
--				textSize="16sp",
--				background=Color.White,
--				text="中国    +86",
--				textColorHint=Color.textgrey,
--				textColor=Color.Back,
--				paddingRight="10dp",
--				paddingLeft="15dp",
--				gravity="center_vertical"
--			},
			line,
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
					ColorButton,id="btn_send_verification_code",
					layout_width="120dp",gravity="center_vertical|center_horizontal",layout_gravity="right|center_vertical",
					layout_height="50dp",textColor=Color.White,textStyle="bold",textSize="14sp",text="获取验证码",background="#5fac45"
				}
			},
			line,
			{
				ColorButton,
				id="btn_bing_phone",
				layout_width="260dp",
				layout_height="40dp",
				layout_gravity="center|bottom",
				layout_marginLeft="50dp",
				layout_marginRight="50dp",
				layout_marginTop="80dp",
				layout_marginBottom="20dp",
				gravity="center",
				soundEffectsEnabled=true,
				text="绑定",
				textColor=Color.White,
				textSize="15sp"
			}
			
			
		}
	}
	main={}
	this.setContentView(loadlayout(layout,main))

	local account_phone=main["et_account_phone"]
	account_phone.antoRequestFocus()
	account_phone.setInputFilter(11)
	
	local et_verification_code=main.et_verification_code
	et_verification_code.setInputFilter(4)
	
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
--			LocalUtils.SMSSDK_sendMSM(account_phone)
		end
	end,0)
--	attachOnClickListener(main.btn_next,
--	function()  
--		println("next")
--		
--	end)
	
	--
	setColorButton(main["btn_bing_phone"],'#ed8d83','#ec7063',function()
		local phone=et_account_phone.getText().toString();
		local verification_code=et_verification_code.getText().toString();
		
		if(string.len(phone)==0) then
			ToastText("请输入手机号码!")
			return
		elseif(string.len(verification_code)<4) then
			ToastText("验证不正确")
		else
			UserInfo.phone=phone
			local url=URL_User(10003,cjson.encode({uid=UserInfo.uid,phone=phone}))
			AsyncHttps(url,function(body)
				finish()
			end)
		end
	end,50)
	
end

function onCreate(savedInstanceState)
	layout()

end