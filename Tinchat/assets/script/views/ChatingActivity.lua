require "init"

ChatingItemAdapter=luajava.bindClass("com.sharemob.tinchat.lib.adapter.ChatingItemAdapter")
GridLayoutManager=luajava.bindClass("android.support.v7.widget.GridLayoutManager")
EmojiAdapter=luajava.bindClass("com.sharemob.tinchat.lib.adapter.EmojiAdapter")
LocalGridLayoutManager=luajava.bindClass("com.sharemob.tinchat.component.LocalGridLayoutManager")
MyLinearLayoutManager=luajava.bindClass("com.sharemob.tinchat.component.MyLinearLayoutManager")
LocalScrollview=luajava.bindClass("com.sharemob.tinchat.component.LocalScrollview")
LinkedList=luajava.bindClass("java.util.LinkedList")
local main={}
local startAnimation=true
local revuid
--记录语音
local luaTimer=nil
local TargetTicket=10
local ticket=0
local startVoiceRecord=true
local voicePath=LocalUtils.getVoiceChatingItemPath()
local mRecorder=nil
--聊天记录
local list = LinkedList()
local recyclerView=nil
--聊天记录适配器
local chatingItemAdapter=nil
function finish()
	this.finish()
end

function onDestroy()
	chatingItemAdapter.onDestroy()
end

function setStackFromEnd()
	recyclerView.scrollToBottom()
end

function onKeyDown(keyCode,event)
	if(keyCode == KeyEvent.KEYCODE_BACK) then
		finish()
	end
end

local function registerReceiver()
	local filter =IntentFilter()
	filter.addAction(SMGlobal.CHATING_EMOJI_ACTION)
	filter.addAction(SMGlobal.CHATING_VOICE_ACTION)
	filter.addAction(SMGlobal.CHATING_IMAGE_ACTION)
	filter.addAction(SMGlobal.CHATING_LOCATION_ACTION)
	filter.addAction(SMGlobal.CHATING_WS_ACTION)
	this.registerReceiver(filter)
end

local layout_titlebar={
	FrameLayout,
	layout_width="match_parent",
	layout_height=dimens.dx_45,
	layout_gravity="top",
	background=Color.TitleBgColor,
	{
		TextView,
		id="tv_title_content",
		layout_width="wrap_content",
		layout_height="wrap_content",
		layout_gravity="left|center_vertical",
		layout_marginLeft="65dp",
		text="",
		textColor=Color.TitleTextColor,
		textSize="16sp"
	},
	{
		FrameLayout,
		layout_width="50dp",
		layout_height="fill",
		layout_gravity="left|center_vertical",
		{
			ImageView,
			layout_width="30dp",
			layout_height="30dp",
			layout_gravity="center_vertical|center_horizontal",
			background="drawable/btn_public_back_normal.png"
		},
		{
			ColorButton,
			alpha="0.3",
			id="iBtn_title_back",
			layout_width="match_parent",
			layout_height="match_parent",
			gravity="vertical"
		}
	},
	layout_title_image_size("right|center_vertical","iBtn_title_right","drawable/btn_title_more.png",60,20)
--	{
--		FrameLayout,
--		layout_width="70dp",
--		layout_height="fill",
--		layout_gravity="right|center_vertical",
--		{
--			ImageView,
--			layout_width="20dp",
--			layout_height="10dp",
--			layout_gravity="center_vertical|center_horizontal",
--			background="drawable/btn_title_more.png"
--		},
--		{
--			ColorButton,
--			alpha="0.3",
--			id="iBtn_title_right",
--			layout_width="match_parent",
--			layout_height="match_parent"
--		}
--	}
}

local SizeWH="26dp"
--表情面板布局
local layout_emoji_grid={
	RecyclerView,
	visibility=LocalView.GONE,
	id="emoji_grid",
	scrollbars="vertical",
	layout_weight="1",
	background=Color.White,
	layout_width="match_parent",
	layout_height="190dp"
}
--声音布局
local voice_layout=	{
	LinearLayout,
	id="voice_layout",
	layout_width="match_parent",
	layout_height="190dp",
	background=Color.White,
	visibility=LocalView.GONE,
	layout_gravity="center_horizontal|center_vertical",
	orientation="vertical",
	{
		TextView,
		id="tv_ticket",
		layout_width="wrap_content",
		layout_height="wrap_content",
		layout_gravity="center_horizontal|center_vertical",
		text="0\"",
		layout_marginLeft="5dp",
		layout_marginTop="15dp",
		layout_marginBottom="15dp",
		textColor=Color.slategrey,
		textSize="16sp"
	},
	{
		FrameLayout,layout_width="100dp",layout_height="100dp",layout_gravity="center_horizontal|center_vertical",
		{
			ImageView,
			id="bg_audio_record",
			layout_width="match_parent",
			layout_height="match_parent",
			src="drawable/bg_audio_record_normal"
		},
		{
			ImageView,
			layout_width="60dp",
			layout_height="31dp",
			layout_gravity="center_horizontal|center_vertical",
			src="drawable/ic_chat_audio_record_blue"
		}
	}
}

--按钮面板
local layout_btn_panel={
	FrameLayout,
	layout_width="match_parent",
	layout_height="40dp",
	background=Color.WhiteSmoke,
	{
		FrameLayout,
		id="layout_emoji_del",
		layout_width="65dp",
		layout_height="match_parent",
		layout_gravity="right|center_vertical",
		{
			ColorButton,
			id="colorBtn_del",
			background="#d4d8d8",
			layout_width="match_parent",
			layout_height="match_parent"
		}
		,{
			ImageView,
			layout_width="60dp",
			layout_height="31dp",
			layout_gravity="center_horizontal|center_vertical",
			src="drawable/ic_topbar_back_normal"
		}

	},
	{
		LinearLayout,
		id="layout_btn_panel",
		layout_width="wrap_content",
		layout_height="wrap_content",
		layout_gravity="left|center_vertical",
		{
			ImageView,
			layout_width=SizeWH,
			layout_height=SizeWH,
			id="btn_emote",
			layout_marginLeft="15dp",
			layout_marginRight="15dp",
			src="drawable/ic_chat_emote_normal"
		},
		{
			ImageView,
			layout_width=SizeWH,
			layout_height=SizeWH,
			id="btn_audio",
			layout_marginLeft="15dp",
			layout_marginRight="15dp",
			src="drawable/ic_chat_audio_normal"
		},
		{
			ImageView,
			id="btn_select_pic",
			layout_width=SizeWH,
			layout_height=SizeWH,
			layout_marginLeft="15dp",
			layout_marginRight="15dp",
			src="drawable/ic_chat_select_pic_normal"
		},
		{	ImageView,
			id="btn_chat_loc",
			layout_width=SizeWH,
			layout_height=SizeWH,
			layout_marginLeft="15dp",
			layout_marginRight="15dp",
			src="drawable/ic_chat_loc_normal"
		}
	}

}

local layout_chating_input={
--		LinearLayout,
--		id="chating_layout_bottom",
--		layout_alignParentBottom=true,
--		layout_below="recycler_chating_msg",
--		layout_width="match_parent",
--		layout_height="wrap",
--		orientation="vertical",
--		layout_gravity="bottom",
--		{
			FrameLayout,
			layout_width="match_parent",
			layout_height="45dp",
			orientation="horizontal",
			background=Color.White,
			layout_gravity="center_vertical|fill_horizontal",
			{
				EditText,
				id="et_comment_text",
				layout_width="match_parent",
				layout_height="match_parent",
				layout_marginRight="65dp",
				paddingBottom="0dp",
				background=Color.White,
				gravity="center_vertical|fill_horizontal",
				hint="发消息",
				maxLength="100",
				paddingLeft="10dp",
				textColor=Color.Back,
				textColorHint=Color.textgrey,
				textSize="14sp"
			},
			{
				ColorButton,
				visibility=LocalView.GONE,
				id="btn_comment_send",
				layout_width="60dp",
				layout_height="match_parent",
				layout_gravity="right",
				layout_margin="5dp",
				background="#159aff",
				text="发送",
				textColor=Color.White,
				textSize="14sp"
			}
	}

ChatingListAndInputPanel={
		RelativeLayout,
		orientation="vertical",
		id="chating_layout",
		layout_width="match_parent",
		layout_height="match_parent",
		background=Color.ChatingBG,
		{
			PullLoadMoreRecyclerView,
			id="recycler_chating_msg",
			visibility=LocalView.GONE,
			scrollbars="vertical",
--			stackFromBottom=true,
			layout_alignParentTop=true,
			layout_width="match_parent",
			layout_height="match_parent"
		},
		{
			LinearLayout,
			id="chating_layout_bottom",
			visibility=LocalView.GONE,
			layout_alignParentBottom=true,
			layout_width="match_parent",
			layout_height="wrap",
			orientation="vertical",
			layout_gravity="bottom",
			layout_chating_input,
			layout_btn_panel,
			line,
			layout_emoji_grid,
			voice_layout
		}
}

local rootView
function onLayout(title)
	layout={
		LinearLayout,
		id="main_layout",
		layout_width="match_parent",
		layout_height="match_parent",
		orientation="vertical",
		layout_titlebar,
		ChatingListAndInputPanel
		
	}
	rootView=loadlayout(layout,main)
	this.setContentView(rootView)
	
	LocalUtils.setLayoutParams(main.chating_layout,main.recycler_chating_msg,main.chating_layout_bottom)
end

local WHOES={
	ME=0,Other=1
}
local temp_ticket_time="0\""
local function cancelTimer()
	if(luaTimer~=nil)then
		luaTimer.cancel();
		luaTimer=nil;
		ticket=0;
		setImageBitmap(main.bg_audio_record,"bg_audio_record_normal")
		if(mRecorder~=nil) then
			mRecorder.stop()
			temp_ticket_time=tostring(main.tv_ticket.getText())
			LocalUtils.SendVoiceChatingItem(this,temp_ticket_time,voicePath)
		end
		startAnimation=true
		main.tv_ticket.setText("0\"")
	end
end
local handler =luajava.newInstance("android.os.Handler",{
	handleMessage=function(msg)
		if(ticket<TargetTicket)then
			ticket=ticket+1;
		else --if(ticket==TargetTicket)then
			cancelTimer()
			main.bg_audio_record.clearAnimation()
		end
		main.tv_ticket.setText(ticket.."\"")
	end
})
function onController()
	local text=this.getIntent().getStringExtra("parameters")
	println("------"..text)
	local json=cjson.decode(text)
	chatingItemAdapter=ChatingItemAdapter(this)
	chatingItemAdapter.addRevObjectInfo("avatar",json.avatar)
--	chatingItemAdapter.addRevObjectInfo("uid",json.uid)
	chatingItemAdapter.addRevObjectInfo("id",json.sid)
	chatingItemAdapter.openWSConnection()
	--注册监听广播
	registerReceiver()
	--状态栏
	local tv_title_content=main.tv_title_content
	tv_title_content.setText(json.nickname)
	local iBtn_title_back=main.iBtn_title_back
	--返回按钮
	setColorButton(iBtn_title_back,'#e8e8e8','#00000000',
	function()
		this.finish()
	end,0)
	--视频
	local iBtn_title_right=main.iBtn_title_right
	main["layout_iBtn_title_right"].setVisibility(LocalView.VISIBLE)
	setColorButton(iBtn_title_right,'#e8e8e8','#00000000',
	function()
--		println("onClickFunc")
		OptionWheelDailogForChatting(json.uid,json.sid)
	end,0)
	
--	local btn_title_right_next=main["btn_title_right_next"]
--	setColorButton(btn_title_right_next,"#7090ab",'#333233',function()
--		OptionWheelDailogForChatting(json.uid)
--	end,0)

	--显示表情包
	local emoji_grid=main.emoji_grid
	emoji_grid.setHasFixedSize(true);
	--	local layoutManager =StaggeredGridLayoutManager(6,StaggeredGridLayoutManager.VERTICAL);
	local layoutManager =GridLayoutManager(this,7)
	layoutManager.setOrientation(GridLayoutManager.VERTICAL);
	layoutManager.setSmoothScrollbarEnabled(true);
	emoji_grid.setLayoutManager(layoutManager);
	--表情适配器
	local emojiAdapter=EmojiAdapter(this)
	emoji_grid.setAdapter(emojiAdapter)
	emojiAdapter.notifyDataSetChanged()
	--加载表情资源
	emojiAdapter.AsyncLoadResourceToAdapterList()
	--加载聊天
	recyclerView=main.recycler_chating_msg
	recyclerView.setGridLayout(1);
	recyclerView.setPushRefreshEnable(false)
	recyclerView.setAdapter(chatingItemAdapter);
	local firstLoad=true
	local function loadData()
			local url=URL_User(10047,cjson.encode({sid=json_param.sid,rid=json_param.rid}))
			println(url)
			AsyncHttp(url,function(body)
				local object=cjson.decode(body)
				if(object.body~=nil)then
					chatingItemAdapter.setArray(cjson.encode(object.body))
					chatingItemAdapter.notifyDataSetChanged();
					setStackFromEnd()
					recyclerView.setPullLoadMoreCompleted();
				end
			end)
	end
	
	
	local className="com.sharemob.tinchat.lib.refreashtabview.extras.PullLoadMoreRecyclerView$PullLoadMoreListener"
	local pullloadmorelistener=luajava.createProxy(className,{
	onRefresh=function()
		recyclerView.setPullLoadMoreCompleted();
	end;
	onLoadMore=function()
--		loadData()
		chatingItemAdapter.notifyDataSetChanged();
		recyclerView.setPullLoadMoreCompleted();
	end
	})
	recyclerView.setOnPullLoadMoreListener(pullloadmorelistener)
	
	loadData()

	--发送
	local btn_comment_send=main.btn_comment_send
	setColorButton(btn_comment_send,'#159aff','#159aff',
	function()
		local text_value=main.et_comment_text.getText().toString()
--		chatingItemAdapter.sendText(URLEncoder.encode(text_value, "UTF-8"))
		chatingItemAdapter.sendText(text_value)
		main.et_comment_text.setText("")
		setStackFromEnd()
	end,10)

	--监听输入框
--	ChatingItemAdapter.setStackFromEnd(recyclerView,true)
	LocalUtils.setEditChangedListener(main.et_comment_text,btn_comment_send)
	local colorBtn_del=main.colorBtn_del
	setColorButton(colorBtn_del,'#cecece','#d4d8d8',
	function()
		LocalUtils.EditorTextDel(main.et_comment_text)
	end,1)
	colorBtn_del.setRadius(1)

	--表情
	local btn_emote=main.btn_emote
	local function EmojiVisibilityEvent()
		if(emoji_grid.getVisibility()==LocalView.VISIBLE) then
			emoji_grid.setVisibility(LocalView.GONE)
			this.setImageBitmap(getDrawableUri("ic_chat_emote_normal"),main.btn_emote)
		elseif (emoji_grid.getVisibility()==LocalView.GONE) then
			emoji_grid.setVisibility(LocalView.VISIBLE)
			this.setSoftInputMode()
			this.setImageBitmap(getDrawableUri("ic_chat_emote_press"),main.btn_emote)
			this.hideFaceToolAndInputFromWindow();
		end
	end

	--声音按钮按钮状态
	local function VoiceBtnVisibilityEvent()
		if(main.voice_layout.getVisibility()==LocalView.VISIBLE) then
			this.setImageBitmap(getDrawableUri("ic_chat_audio_press"),main.btn_audio)
		elseif (main.voice_layout.getVisibility()==LocalView.GONE) then
			this.setImageBitmap(getDrawableUri("ic_chat_audio_normal"),main.btn_audio)
		end
	end

	--表情
	attachOnClickListener(btn_emote,function()
		EmojiVisibilityEvent()
		main.voice_layout.setVisibility(LocalView.GONE)
		VoiceBtnVisibilityEvent()
	end)
	--添加面板监听事件，隐藏表情面板
	attachOnTouchListener(main.recycler_chating_msg,function()
		
--		if(chatingItemAdapter.getItemCount()>5) then
--			ChatingItemAdapter.setStackFromEnd(recyclerView,true)
--		elseif(chatingItemAdapter.getItemCount()<5) then
--			ChatingItemAdapter.setStackFromEnd(recyclerView,false)
--		end
	
		
		this.hideFaceToolAndInputFromWindow()
		
		if(emoji_grid.getVisibility()==LocalView.VISIBLE) then
			this.setImageBitmap(getDrawableUri("ic_chat_emote_normal"),btn_emote)
			emoji_grid.setVisibility(LocalView.GONE)
		end
		
		if(main.voice_layout.getVisibility()==LocalView.VISIBLE) then
			this.setImageBitmap(getDrawableUri("ic_chat_audio_normal"),main.btn_audio)
			main.voice_layout.setVisibility(LocalView.GONE)
		end
	end)

	attachOnTouchListener(main.et_comment_text,function()
		
--		if(chatingItemAdapter.getItemCount()>2) then
--			ChatingItemAdapter.setStackFromEnd(recyclerView,true)
--		else
--			ChatingItemAdapter.setStackFromEnd(recyclerView,false)
--		end
		
		emoji_grid.setVisibility(LocalView.GONE)
		this.setImageBitmap(getDrawableUri("ic_chat_emote_press"),btn_emote)
		main.voice_layout.setVisibility(LocalView.GONE)
		VoiceBtnVisibilityEvent()
	end)
	--语音
	local btn_audio=main.btn_audio
	setDrawableButton(btn_audio,"ic_chat_audio_press","ic_chat_audio_normal",function()
		println("语音")
		emoji_grid.setVisibility(LocalView.GONE)
		this.setImageBitmap(getDrawableUri("ic_chat_emote_normal"),main.btn_emote)
		this.hideFaceToolAndInputFromWindow();
		main.voice_layout.setVisibility(LocalView.VISIBLE)
		VoiceBtnVisibilityEvent()
	end)


	attachOnTouchListener(main.bg_audio_record,
	function()
		if(startAnimation)then
			startAnimation=false
			local operatingAnim=LocalUtils.rotateImageAnimation(this)
			main.bg_audio_record.startAnimation(operatingAnim);
			setImageBitmap(main.bg_audio_record,"bg_audio_record_selected")
			luaTimer=LocalUtils.getTimerTask(handler,1000)
			voicePath=LocalUtils.getVoiceChatingItemPath()
			mRecorder=LocalUtils.CreateMediaRecording(voicePath)
			println("down")
		end
	end,
	function()
		main.bg_audio_record.clearAnimation()
		setImageBitmap(main.bg_audio_record,"bg_audio_record_normal")
		cancelTimer()
		println("up")
	end)
	attachOnClickListener(main.bg_audio_record,nil)
	--图片
	local btn_select_pic=main.btn_select_pic
	setDrawableButton(btn_select_pic,"ic_chat_select_pic_press","ic_chat_select_pic_normal",function()
		println("图片")
--		enterActivity("ImagePickerActivity",anim.in_from_right,anim.out_to_left,false)
		local map=HashMap()
		map.put("action",SMGlobal.CHATING_IMAGE_ACTION)
		map.put("upload_type",SMGlobal.UPLOAD_IMAGE_MyPhoto)
		LocalUtils.enterAppActivity(activity, map, "ImagePickerActivity");
		main.voice_layout.setVisibility(LocalView.GONE)
	end)
	--定位
	local btn_chat_loc=main.btn_chat_loc
	setDrawableButton(btn_chat_loc,"ic_chat_loc_press","ic_chat_loc_normal",function()
		println("定位")
		enterActivity("LocationActivity",anim.in_from_right,anim.out_to_left,false)
		main.voice_layout.setVisibility(LocalView.GONE)
	end)
	--输入框
	local et_comment_text=main.et_comment_text
	
	
	--
	chatingItemAdapter.addView(main.voice_layout)
	chatingItemAdapter.addView(main.emoji_grid)
end

function onCreate(savedInstanceState)
	onLayout()
	onController()
end


function onReceive( context,  intent)
	local action = intent.getAction()
	if (action==SMGlobal.CHATING_EMOJI_ACTION) then
		local bundle = intent.getExtras()
		local emoji_name=bundle.getString(SMGlobal.EMIJI_KEY)
		--		local emoji=bundle.getParcelable(EmojiAdapter.EMIJI)
		local emoji_path=string.format("assets://emojis/emoji_%s.png",emoji_name)
		println(emoji_path)
		LocalUtils.EmojiSpane(this,emoji_path,main.et_comment_text,emoji_name)
	elseif(action==SMGlobal.CHATING_VOICE_ACTION)then
		local bundle = intent.getExtras()
--		local json=bundle.getString(SMGlobal.VOICE_KEY)
--		local localFilename=bundle.getString("filename")
		local uploadFilename=bundle.getString("name")
		chatingItemAdapter.sendVoice(cjson.encode({voice=uploadFilename,time=temp_ticket_time,type=SMGlobal.MsgType.Voice}))
--		chatingItemAdapter.sendVoice(json)
		setStackFromEnd()
	elseif(action==SMGlobal.CHATING_IMAGE_ACTION)then
		local bundle = intent.getExtras()
--		local array_str=bundle.getString(SMGlobal.IMAGE_KEY)
--		local array=cjson.decode(array_str)
--		chatingItemAdapter.addItem(cjson.encode({}))
--		for i=1,#array,1 do
--			println(array[i])
--			chatingItemAdapter.addItem(cjson.encode(array[i]))
--		end
		local localFilename=bundle.getString("filename")
		local uploadFilename=bundle.getString("name")
		chatingItemAdapter.sendImage(localFilename,uploadFilename)
		setStackFromEnd()
	elseif(action==SMGlobal.CHATING_LOCATION_ACTION)then
		local bundle = intent.getExtras()
		local json=bundle.getString(SMGlobal.LOCATION_KEY)
		println(json)
		chatingItemAdapter.sendLocation(json)
		setStackFromEnd()
	elseif(action==SMGlobal.CHATING_WS_ACTION) then
		local bundle = intent.getExtras()
		local json=bundle.getString("text")
		println(json)
		chatingItemAdapter.addItem(json)
		setStackFromEnd()
	end
end
