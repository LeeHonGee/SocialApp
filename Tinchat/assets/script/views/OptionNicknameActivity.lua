require "init"
local main={}
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
			text="昵称"
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
			id='ev_nickname',
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

	this.setContentView(loadlayout(layout,main))
	
	local ev_nickname=main.ev_nickname
	ev_nickname.setInputFilter(10)
	if(UserInfo.nickname~=nil) then
			ev_nickname.setText(URLDecoder.decode(UserInfo.nickname,"UTF-8"))
	end
	
	local btn_submit=main.btn_title_submit
	OnClickListener({view=btn_submit,callback=
	function()   
		local nickname=tostring(ev_nickname.getText())
		if(nickname=='') then
			ToastText("提交内容不能为空")
			return
		end
		sendBroadcastOption("item_nickname",nickname)
		
		if(UserInfo.nickname~=nil) then
			--更新缓存
			UserInfo.nickname=nickname
			local url=URL_User(10003,cjson.encode({uid=UserInfo.uid,nickname= URLEncoder.encode(nickname, "UTF-8")}))
			http.get(url)
		end
		finish()
	end})
end


function onCreate(savedInstanceState)
	layout()

end