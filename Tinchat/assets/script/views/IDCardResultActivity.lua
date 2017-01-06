require "init"

local main={}

local 	layout_header={
	FrameLayout,
	id="activity_header",
	layout_width="match_parent",
	layout_height=dimens.dx_45,
	layout_gravity="top",
	background=Color.TitleBgColor,
	layout_title_image("left|center_vertical","btn_cancle","drawable/btn_public_back_normal.png"),
	{
		TextView,
		id="tv_title_content",
		layout_width="wrap_content",
		layout_height="wrap_content",
		layout_gravity="center|center_vertical",
		textColor="#ffffff",
		textSize="17sp",
		text="真人实名认证"
	}
}

function loadLayout()
		local layout={
			LinearLayout,
			layout_width="match_parent",
			layout_height="match_parent",
			background=Color.WhiteSmoke,
			orientation="vertical",
			layout_header,
			{	
				LinearLayout,
				layout_width="fill_parent",
				layout_height="wrap",
				orientation="vertical",
				line,
				{
					TextView,
					id="et_real_name",
					layout_width="fill_parent",
					layout_height="50dp",
					textSize="17sp",
					background=Color.White,
					textColor="#606060",
					paddingRight="10dp",
					paddingLeft="15dp",
					text=UserInfo.realname,
					gravity="left|center_vertical"
				},
				line,
				{
					TextView,
					id='et_id_card',
					layout_width="fill_parent",
					layout_height="50dp",
					textSize="17sp",
					background=Color.White,
					textColor="#606060",
					paddingRight="10dp",
					paddingLeft="15dp",
					text=UserInfo.IDCard,
					gravity="left|center_vertical"
				},
				line
			},
			{
				ColorButton,
				id="btn_submit",
				layout_width="240dp",
				layout_height="40dp",
				layout_gravity="center|bottom",
				layout_marginLeft="50dp",
				layout_marginRight="50dp",
				layout_marginTop="50dp",
				gravity="center",
				soundEffectsEnabled="true",
				text="重新提交",
				textColor="#ffffffff",
				textSize="15sp"
			},
			{
				TextView,
				id="tv_title_content",
				layout_width="wrap_content",
				layout_height="wrap_content",
				layout_margin="10dp",
				layout_marginTop="30dp",
				layout_gravity="left|center_vertical",
				textColor=Color.Back,
				textSize="14sp",
				text="郑重声明:\n\n1、此功能仅用在网络核实您的身份,为网络征友提高安全保障,其他用户无权查看\n\n2、你提交的身份证信息已使用加密存储,请放心提交"
			}
		}
		this.setContentView(loadlayout(layout,main))
end

function finish()
	this.finish()
end

function onKeyDown(keyCode,event)
	if(keyCode == KeyEvent.KEYCODE_BACK) then
		finish()
	end
end

function onController()
	setColorButton(main.btn_cancle,'#e8e8e8','#00000000',
	function()
		finish()
	end,0)
	
			
	setColorButton(main["btn_submit"],'#ed8d83','#ec7063',
	function()
		enterActivity("IdentityCardActivity",anim.in_from_right,anim.out_to_left,false)
--		local real_name=tostring(main.et_real_name.getText())
--		local id_card=tostring(main.et_id_card.getText())
--		if (real_name=='') then
--			ToastText("真实姓名不能为空")
--			return
--		elseif(id_card=='') then
--			ToastText("身份证号码不能为空")
--			return
--		elseif(string.len(id_card)<18) then
--			ToastText("请输入身份证号码长度不正确")
--			return
--		else
--			UserInfo.IDCard=id_card
--			UserInfo.realname=real_name
--			local url=URL_User(10003,cjson.encode({uid=UserInfo.uid,IDCard=id_card,realname= URLEncoder.encode(real_name, "UTF-8")}))
--			http.get(url)
--			local url=URL_User(10004,cjson.encode({phone=account_phone,password=account_pwd}))
--			println(url)
--			AsyncHttps(url,function(body)
--				local user=cjson.decode(body)
--				if(user.result==0) then
--					enterMainView(cjson.encode(user.info))
--				elseif(user.result==1) then
--					ToastText(user.desc)
--				end
--			end)

	end,50)
end

function onCreate(bundle)
	loadLayout()
	onController()
end