require "init"
--UserInfo=cjson.decode(CacheManager.getInstance().UserInfo)

function finish()
	this.finish() 
end

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
			text="意见反馈"
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
			text="提交",
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
			id='ev_feedback',
			layout_width="fill_parent",
			layout_height="150dp",
			textSize="13sp",
			background=Color.White,
			textColorHint=Color.textgrey,
			textColor=Color.Back,
			paddingRight="10dp",
			paddingLeft="10dp",
			paddingTop="5dp",
			hint='有什么不爽的,马上吐槽吧～',
			text="",
			gravity="fill_horizontal",
			singleLine=false
		},
		line
	}

	main={}
	this.setContentView(loadlayout(layout,main))
	
	local ev_feedback=main.ev_feedback
	ev_feedback.setInputFilter(100)
	
	
	local btn_submit=main.btn_title_submit
	attachOnClickListener(btn_submit,
	function()   
		local content=tostring(ev_feedback.getText())
		if(content=='') then
			ToastText("提交内容不能为空")
			return
		end
		
		local url=URL_APP(2003,cjson.encode({content=content,userid=URLEncoder.encode(UserInfo.uid, "UTF-8")}))
		println(url)
		AsyncHttps(url,function(body)
			println(body)
			local status=cjson.decode(body)
			if(status.result==0) then
				ToastText(status.desc)
				finish()
			end
		end)
		
		
	end)
	
end

function onCreate(savedInstanceState)
	layout()

end