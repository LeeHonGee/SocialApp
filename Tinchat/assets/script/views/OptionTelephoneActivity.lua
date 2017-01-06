require "init"

function finish()
	this.finish() 
	this.overridePendingTransition(R.anim.in_from_left,R.anim.out_to_right);
end

function onKeyDown(keyCode,event)
	if(keyCode == KeyEvent.KEYCODE_BACK) then
		finish()
	end
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
		{
			Button,
			id="tv_title_content",
			layout_width="wrap_content",
			layout_height="wrap_content",
			layout_gravity="left|center_vertical",
			textStyle="bold",
			textColor=Color.TitleTextColor,
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
			id="tv_title_content",
			layout_width="wrap_content",
			layout_height="wrap_content",
			layout_gravity="center|center_vertical",
			textStyle="bold",
			textColor=Color.TitleTextColor,
			textSize="17sp",
			text="手机号码"
		},
		{
			Button,
			id="btn_title_submit",
			layout_width="wrap_content",
			layout_height="wrap_content",
			layout_gravity="right|center_vertical",
			textStyle="bold",
			textColor=Color.TitleTextColor,
			textSize="17sp",
			text="保存",
			background="#00000000",
			layout_marginRight="10dp",
			layout_marginTop="2dp"
		}
	}

	layout={
		LinearLayout,
		layout_width="match_parent",
		layout_height="match_parent",
		background=Color.WhiteSmoke,
		orientation="vertical",
		layout_header,
		line,
		{
			ClearEditText,
			id='ev_telephone',
			layout_width="fill_parent",
			layout_height="50dp",
			textSize="13sp",
			gravity="center_vertical",
			background=Color.White,
			textColorHint=Color.textgrey,
			textColor=Color.Back,
			paddingRight="10dp",
			paddingLeft="10dp",
			paddingTop="5dp",
			hint='请输入昵称',
			text="",
			singleLine=true
		},
		line
	}

	main={}
	this.setContentView(loadlayout(layout,main))
	
	local ev_telephone=main.ev_telephone
	ev_telephone.setInputFilter(100)
	
	local btn_submit=main.btn_title_submit
	OnClickListener({view=btn_submit,callback=
	function()   
		local telephone=tostring(ev_telephone.getText())
		if(telephone=='') then
			ToastText("提交内容不能为空")
			return
		end
		local userInfo={
			account=UserInfo.id,telephone=telephone
		}
		sendBroadcastOption("item_telephone",userInfo.telephone)
		local url=URL_Update_UserInfo(cjson.encode(userInfo))
		println(url)
		local body,cookie,code,headers=http.get(url)
		println(body)
		
		finish()
	end})
end


local function onCreate(savedInstanceState)
	layout()

end