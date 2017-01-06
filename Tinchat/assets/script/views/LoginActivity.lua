require "init"

local packageName="com.sharemob.tinchat.modules.account."
local main={}

function onKeyDown(keyCode,event)
	if(keyCode == KeyEvent.KEYCODE_BACK) then
		this.finish()
	end
end

local function loadLayout()

	local layout_title_header=require "layout_title_header".new()
	layout_title_header:create({title="登录",leftListenerFunc=function() this.finish()   end,rightListenerFunc=nil})
	
	layout={
		LinearLayout,
		layout_width="match_parent",
		layout_height="match_parent",
		background=Color.WhiteSmoke,
		orientation="vertical",
		layout_title_header.layout,
		{	
			LinearLayout,
			layout_marginTop="10dp",
			layout_width="fill_parent",
			layout_height="fill_parent",
			gravity="fill_vertical",
			orientation="vertical",
			line,
			{
				ClearEditText,
				id="et_account_phone",
				layout_width="fill_parent",
				layout_height="50dp",
				textSize="16sp",
				background=Color.White,
				text="",
				textColorHint=Color.textgrey,
				textColor=Color.Back,
				paddingRight="10dp",
				paddingLeft="15dp",
				inputType="phone",
				hint="请输入你的手机号",
				gravity="center_vertical",
				digits="1234567890",
				singleLine=true,
			},
			line,
			{
				ClearEditText,
				id='et_account_pwd',
				layout_width="fill_parent",
				layout_height="50dp",
				textSize="16sp",
				background=Color.White,
				textColorHint=Color.textgrey,
				textColor=Color.Back,
				paddingRight="10dp",
				paddingLeft="15dp",
				hint="请输入密码",
				password=true,
				text="",
				gravity="center_vertical",
				inputType="textPassword",
				singleLine=true
			},
			line,
			{
				TextView,
				id='tv_forget_password',
				layout_width="wrap_content",
				layout_height="wrap_content",
				layout_gravity="right",
				layout_margin="10dp",
				textStyle="bold",
				text="忘记密码?",
				textColor="#ef5308",
				textSize="14sp"
			},
			{
				ColorButton,
				id="btn_login",
				layout_width="fill",
				layout_height="40dp",
				layout_gravity="center|bottom",
				layout_marginLeft="50dp",
				layout_marginRight="50dp",
				layout_marginTop="50dp",
				gravity="center",
				soundEffectsEnabled="true",
				text="登录",
				textColor=Color.Back,
				textSize="15sp"
			}
			
		}
	}

	this.setContentView(loadlayout(layout,main))
	
	layout_title_header:initComponent(main.iBtn_title_back,main.iBtn_title_right)
	
	local et_account_phone=main.et_account_phone
	et_account_phone.setInputFilter(12)
	
	local et_account_pwd=main.et_account_pwd
	et_account_pwd.setInputFilter(15)
	
	setColorButton(main.btn_login,"#f2f1f1","#ffffffff",function()
--		local account_phone=tostring(et_account_phone.getText())
--		local account_pwd=tostring(et_account_pwd.getText())
		--测试账号
		account_phone="13138866890"
		account_pwd="leehongee"
		
		if(account_phone=='') then
			ToastText("手机号码不能为空")
			return
		elseif(string.len(account_phone)<11) then
			ToastText("请输入手机的号码长度")
			return
		elseif (account_pwd=='') then
			ToastText("密码不能为空")
			return
		else
			local url=URL_User(10004,cjson.encode({phone=account_phone,password=account_pwd}))
--			CacheManager.getInstance().requestHttp(this,url)
			AsyncHttp(url,function(body)
				local tbody=cjson.decode(body)
				if(tbody.result==0) then
					enterMainView(cjson.encode(tbody.info))
				elseif(tbody.result==1) then
					ToastText(user.desc)
				end
			end)
		end
	end,15)
	--界面跳转
	local function gotoView_MainActivity()
		local MainActivity='com.sharemob.tinchat.modules.launch.MainActivity'
		LocalUtils.gotoViewByAnim(this,MainActivity,anim.in_from_right,anim.out_to_left,false)
		this.finish()
	end
--	--登录
--	local LoginOnClick=luajava.createProxy("android.view.View$OnClickListener",{
--	onClick=function(v)
----		LocalUtils.gotoViewByAnim(activity,"com.sharemob.tinchat.modules.launch.MainActivity",anim.in_from_right,anim.out_to_left,false)
--		phone_flag=true
--		password_flag=true
--		println(string.format("onClick:%d",string.len(account_phone)))
--		--验证账号和密码
--	end})
--	btn_login.setOnClickListener(LoginOnClick)
	
	--忘记密码
	local forget_password=main.tv_forget_password
	attachOnClickListener(forget_password,
	function() 
		enterActivity("ForgetPasswordActivity",anim.in_from_right,anim.out_to_left,false)
	end)
end


function onCreate(bundle)
	loadLayout()
end

--function onReceive(context,intent)
--	local action = intent.getAction()
--	if (action==SMGlobal.HTTP_RESPONSE_TEXT_ACTION) then
--		local bundle = intent.getExtras()
--		local responseText=bundle.getString("text")
--		local user=cjson.decode(responseText)
--		println(responseText)
----		if(user.result==0) then
----			enterMainView(cjson.encode(user.info))
----		elseif(user.result==1) then
----			ToastText(user.desc)
----		end
--	end
--end
--local function register_http_response_text()
--	local filter =IntentFilter()
--	filter.addAction(SMGlobal.HTTP_RESPONSE_TEXT_ACTION)
--	this.registerReceiver(filter)
--end
