require "init"

local gridAdapter
local PhotobookItemAdapter=luajava.bindClass("com.sharemob.tinchat.lib.adapter.PhotobookItemAdapter")
local DynamicImageItemAdapter=luajava.bindClass("com.sharemob.tinchat.lib.adapter.DynamicImageItemAdapter")
local main={}

local onAvatarHeaderLayout={
		FrameLayout,
		id="layout_header",
		layout_width="match_parent",
		layout_height="210dp",
		background=Color.WhiteSmoke,
		orientation="vertical",
		{
			ImageView,
			id="layout_header_bg",
			layout_width="match_parent",
			layout_height="match_parent",
			scaleType="centerCrop",
			src="drawable/ic_like_match_bg"
		},
		{
			TextView,
			id="btn_latestlogin",
			layout_width="wrap_content",
			layout_height="wrap_content",
			layout_gravity="top|right",
			layout_margin="20dp",
			textSize="10sp",
			paddingTop="6dp",
			paddingBottom="6dp",
			Radius="50",
			paddingLeft="15dp",
			paddingRight="15dp",
			background=Color.transparent,
			text="查看最近登录时间 >",
			visibility=LocalView.GONE,
			textColor=Color.White
		}
		,{
			LinearLayout,
			id="layout_change_avatar",
			layout_width="match_parent",
			layout_height="90dp",
			layout_gravity="bottom",
			gravity="bottom",
			orientation="horizontal",
			layout_margin="15dp"
				,{
					RelativeLayout,
					layout_width="90dp",
					layout_height="match_parent",
					{
						CircleImageView,
						id="user_avatar",
						layout_width="match_parent",
						layout_height="match_parent",
						BorderWidth="10dp",
						scaleType="centerCrop",
						src="drawable/public_default_head.png"
					},
					{
						ImageView,
						layout_width="30dp",
						layout_height="30dp",
						layout_alignParentBottom="true",
						layout_alignRight="user_header_icon",
						layout_marginBottom="5dp",
						layout_marginRight="5dp",
						scaleType="centerCrop",
						visibility=LocalView.GONE,
						src="drawable/cashier_camera.png"
					}
				},{
					LinearLayout,
					layout_width="fill",
					layout_height="match_parent",
					layout_marginRight="40dp",
					orientation="vertical",
					gravity="center_vertical",
					padding="8dp",
					{
						FrameLayout,
						layout_width="fill",
						layout_height="wrap",
						{
							TextView,
							id="user_nickname",
							layout_width="wrap_content",
							layout_height="wrap_content",
							layout_gravity="left|center_vertical",
							layout_marginBottom="3dp",
							text="",
							textColor=Color.White,
							textSize="20sp",
							textStyle="bold"
						},
						{
							TextView,
							id="user_attention",
							layout_gravity="right|center_vertical",
							layout_width="wrap_content",
							layout_height="fill",
							paddingTop="6dp",
							paddingBottom="6dp",
							paddingLeft="15dp",
							paddingRight="15dp",
							text="+关注",
							textColor=Color.White,
							textSize="12sp",
							visibility=LocalView.GONE
--							textStyle="bold"
						}
					},
					{
						LinearLayout,
						layout_width="wrap_content",
						layout_height="30dp",
						{
							TextView,
							id="tv_myspace_sex_age",
							layout_width="35dp",
							layout_height="15dp",
							paddingRight="8dp",
							layout_marginRight="5dp",
							background="drawable/gender_boy.png",
							gravity="right|center_vertical",
							textColor=Color.White,
							textSize="9sp",
							textStyle="normal"
						},
						{
							TextView,
							id="tv_myspace_constellation",
							layout_width="wrap_content",
							layout_height="wrap_content",
--							background="#ffc336",
							gravity="center|center_vertical",
							textColor=Color.White,
							paddingLeft="8dp",
							paddingRight="8dp",
							textSize="9sp",
							textStyle="normal" 
						}
					},
					{
						LinearLayout,
						layout_width="wrap_content",
						layout_height="wrap_content",
						orientation="horizontal",
						{
							ImageView,
							layout_width="15dp",
							layout_height="15dp",
							layout_marginRight="5dp",
							scaleType="centerCrop",
							gravity="center_vertical",
							background="drawable/ic_discover_normal.png"
						},
						{
							TextView,
							id="user_location",
							layout_width="wrap_content",
							layout_height="wrap_content",
							gravity="center_vertical",
							layout_marginTop="1dp",
							text="",
							textColor=Color.White,
							textSize="12sp",
							textStyle="normal" 
						}
				}
				}
			}
	}

local	layout_space_mydynamic={
		LinearLayout,
		id="layout_space_mydynamic",
		layout_width="match_parent",
		layout_height="wrap_content",
		orientation="vertical",
	{
		RelativeLayout,
		layout_width="match_parent",
		layout_height="60dp",
		background=Color.White,
		paddingLeft="10dp",
		{
			LinearLayout,
			layout_width="wrap_content",
			layout_height="wrap_content",
			layout_marginRight="20dp",
			layout_centerVertical="true",
			gravity="center_vertical",
			orientation="vertical",
			{
				TextView,layout_width="wrap_content",layout_height="wrap_content",layout_gravity="center_vertical|left",
				gravity="center_vertical",text="个人动态",textColor=Color.textgrey,textSize="16sp",textStyle="normal"
			},
			{
				TextView,id="tv_dynamic_count",layout_width="wrap_content",layout_height="wrap_content",layout_gravity="center_vertical|left",
				gravity="center_vertical",text="0",textColor=Color.Back,textSize="25sp",textStyle="bold",layout_marginLeft="10dp"
			}
		},
		{
			RecyclerView,
			id="recyclerview_dynamic_photos",
			layout_width="match_parent",
			layout_height="match_parent",
			scrollbars="vertical",
			paddingRight="30dp",
			layout_marginLeft="80dp",
			gravity="center_vertical"
		},
		{
			ImageView,
			layout_width="10dp",
			layout_height="10dp",
			layout_marginRight="10dp",
			background="drawable/icon_public_arrow.png",
			layout_alignParentRight="true",
			layout_centerVertical="true",
			layout_gravity="center_vertical"
		}
	},line
}

voice_item=function(cell_id,title,voice)
	layout={
		LinearLayout,
		id="signaturevoice_layout",
		layout_width="match_parent",
		layout_height="match_parent",
		background=Color.White,
		gravity="center_vertical",
		orientation="vertical",
		{
			LinearLayout,
			layout_width="match_parent",
			layout_height="match_parent",
			gravity="center_vertical",
			orientation="horizontal",
			padding="15dp",
			{
				TextView,
				id=cell_id,
				layout_width="90dp",
				layout_height="match_parent",
				layout_gravity="left",
				gravity="center_vertical",
				text=title,
				textColor=Color.textgrey,
				textSize="16sp",
				textStyle="normal"
			},
			{
				FrameLayout,
				layout_width="130dp",
				layout_height="27dp",
				gravity="center_vertical",
				background="drawable/ic_chatig_voice_normal",
--				paddingTop="2dp",
--				paddingBottom="2dp",
--				background="drawable/ic_chatig_voice_pressed",
--				src="drawable/ic_chatig_voice_pressed",
				{
					ImageView,
					id="play_voice_layout",
					layout_width="130dp",
					layout_height="35dp",
					layout_gravity="center_vertical"
--					src="drawable/ic_chatig_voice_normal"
				}
				,{
					ImageView,
					id=cell_id,
					layout_marginLeft="20dp",
					background="drawable/receive_voice_play_icon04",
					layout_width="wrap",
					layout_height="wrap",
					layout_gravity="center_vertical"
				},
				{
					TextView,
					layout_marginRight="15dp",
					layout_width="wrap",
					layout_height="match_parent",
					layout_gravity="right",
					gravity="center_vertical",
--					text=info.voiceTime,
					text="10\"",
					textColor=Color.White,
					textSize="15sp",
					textStyle="normal"
				}
			}
		}
		,line
	}
	return layout
end

local listview_myInfo={
		LinearLayout,
		id="listview_myInfo",
		layout_width="fill_parent",
		layout_height="wrap",
		orientation="vertical",
		line,
		voice_item("voice","语音介绍",""),
		layout_space_item("signature","交友宣言",""),
		layout_space_mydynamic,
		layout_space_item("location","居住地",""),
		layout_space_item("regTime","注册日期",""),
		space_item_title_layout("drawable/profile_basics_info.png","征婚资料","myinfo"),line,
		{
			LinearLayout,
			id="marriage_seeking_layout",
			layout_width="match_parent",
			layout_height="wrap_content",
			background=Color.WhiteSmoke,
			orientation="vertical",
			layout_space_item("nickname","交友昵称",""),
			layout_space_item("sex","性别",""),
			layout_space_item("age","年龄",""),
			layout_space_item("feeling","情感状态",""),
			layout_space_item("bodily","体型",""),
			layout_space_item("education","学历",""),
			layout_space_item("profession","职业",""),
			layout_space_item("income","月收入",""),
			layout_space_item("hasHouse","购房情况",""),
			layout_space_item("hasCar","购车情况",""),
			layout_space_item("fellow_townsman","老乡","")
			}
}

local layout_bottom_btn={
		LinearLayout,
		id="layout_bottom_btn",
		visibility=LocalView.GONE,
		layout_gravity="center|bottom",
		layout_width="fill_parent",
		layout_height="45dp",
		background="#333546",
		orientation="horizontal",
		padding="0dp",
		{
			ColorButton,
			id="btn_sayhi",
			layout_weight="1",
			paddingLeft="35dp",
			layout_width="wrap_content",
			layout_height="match_parent",
			layout_gravity="center|bottom",
			gravity="left|center_vertical",
			soundEffectsEnabled=true,
			text="打招呼",
			textColor="#ffffffff",
			textSize="13sp",
			background=Color.transparent
		},
		 {
			ColorButton,
			id="btn_change_chatting",
			layout_weight="1",
			paddingLeft="35dp",
			layout_width="wrap_content",
			layout_height="match_parent",
			layout_gravity="center|bottom",
			gravity="left|center_vertical",
			soundEffectsEnabled=true,
			text="对话",
			textColor="#ffffffff",
			textSize="13sp",
			background=Color.transparent
		},
		{
			ColorButton,
			id="btn_send_gift",
			layout_weight="1",
			paddingLeft="35dp",
			layout_width="wrap_content",
			layout_height="match_parent",
			layout_gravity="center|bottom",
			gravity="left|center_vertical",
			soundEffectsEnabled=true,
			text="送礼物",
			textColor="#ffffffff",
			textSize="13sp",
			background="#adbac5"
		}
}

local layout_bottom_editor={
		LinearLayout,
		id="layout_bottom_editor",
		visibility=LocalView.GONE,
		layout_gravity="center|bottom",
		layout_width="fill_parent",
		layout_height="50dp",
		background=Color.textgrey,
		orientation="horizontal",
		padding="8dp",
		{
			ColorButton,
			id="btn_editor_myinfo",
			layout_weight="1",
			layout_marginLeft="15dp",
			layout_width="wrap_content",
			layout_height="match_parent",
			layout_gravity="center|bottom",
			gravity="center",
			soundEffectsEnabled=true,
			text="编辑交友资料",
			textColor="#ffffffff",
			textSize="13sp",
			background=Color.transparent
		}
}
local layout_header={
	FrameLayout,
	id="activity_header",
	layout_width="match_parent",
	layout_height=dimens.dx_45,
	layout_marginBottom="0dp",
	layout_gravity="top",
	background=Color.TitleBgColor,
	layout_title_image("left|center_vertical","btn_cancle","drawable/btn_public_back_normal.png"),
	{
		TextView,
		id="title_name",
		layout_width="wrap_content",
		layout_height="wrap_content",
		layout_gravity="center|center_vertical",
		textStyle="bold",
		textColor=Color.TitleTextColor,
		textSize="17sp",
		text="昵称"
	},
	layout_title_image_size("right|center_vertical","btn_title_right_next","drawable/btn_title_more.png",60,20),
--	layout_title_text("right|center_vertical","btn_title_right_next","举报")
}

function controller()
	
	main["user_attention"].setBackgroundResource(R.drawable.style_attention_border)
	main["user_attention"].setBackgroundResource(R.drawable.style_btn_latest_login)
	--打招呼
	local btn_editor_myinfo=main.btn_editor_myinfo
	setColorButton(btn_editor_myinfo,"#8da6bc","#7090ab",function()
			println("编辑个人资料")
	end,50)
	
--	Html=luajava.bindClass("android.text.Html")
--	local weixin=Html.fromHtml(info.weixin)
--	main.weixin_space_item_content.setText(weixin)
--	main.telephone_space_item_content.setText(weixin)
	
	local myspace=main.myspace
	LocalUtils.applyLocalFont(myspace)
	main.iBtn_loving.setBackgroundResource(R.drawable.red_oval)
end
layout_horizontal_photobook={
		LinearLayout,
		id="layout_horizontal_photobook",
		layout_width="match_parent",
		layout_height="100dp",
		{
			RecyclerView,
			id="recycler_photobook",
			gravity="center_vertical",
			layout_width="match_parent",
			layout_height="match_parent"
		}
}
function onLoadLayout()
	layout={
		FrameLayout,
		id="myspace",
		layout_width="match_parent",
		layout_height="match_parent",
		background=Color.WhiteSmoke,
		orientation="vertical",
		{
			LinearLayout,
			layout_gravity="center|top",
			layout_width="match_parent",
			layout_height="match_parent",
			orientation="vertical",
			layout_header,
			{
				LocalHoveringScrollView,
				id="view_hover",
				layout_width="match_parent",
				layout_height="match_parent",
				background=Color.WhiteSmoke,
				layout_marginBottom="45dp",
				scrollbars="none",
				{
					LinearLayout,
					id="id_myspace_root",
					layout_width="match_parent",
					layout_height="fill_parent",
					background=Color.WhiteSmoke,
					orientation="vertical",
					onAvatarHeaderLayout,
					layout_horizontal_photobook,
					listview_myInfo
				}
			}
		},
		layout_bottom_editor,
		layout_bottom_btn
	}
	this.setContentView(loadlayout(layout,main))
	
end

function onController()
	main["marriage_seeking_switch"].setVisibility(LocalView.GONE)
	main["btn_latestlogin"].setBackgroundResource(R.drawable.style_btn_latest_login)
	attachOnClickListener(main["btn_latestlogin"],function()
		enterActivity("LatestLoginActivity",R.anim.in_from_right,R.anim.out_to_left, false)
	end,50)
	--返回按钮
	local btn_title_cancle=main.btn_cancle
	setColorButton(btn_title_cancle,'#e8e8e8','#00000000',
	function()
		finish()
	end,0)
	--个人写真相册
	local recycler_photobook=main["recycler_photobook"]
	setRecyclerView(recycler_photobook)
	
	local recyclerview_dynamic_photos=main["recyclerview_dynamic_photos"]
	setRecyclerView(recyclerview_dynamic_photos)
	local dynamic_adapter=DynamicImageItemAdapter(this)
	recyclerview_dynamic_photos.setAdapter(dynamic_adapter)
	
	attachOnClickListener(main["layout_space_mydynamic"],function(body)
		dynamic_adapter.enterUserSpace()
	end)
	
	local url=URL_User(10012,cjson.encode({uid=json_param.uid,myuid=UserInfo.uid}))
	println(url)
	AsyncHttp(url,function(body)
		local t_info=cjson.decode(body)
		
		dynamic_adapter.addParam("uid",t_info.uid)
		main["title_name"].setText(t_info.nickname)
		main["nickname"].setText(t_info.nickname)
		main["user_nickname"].setText(t_info.nickname)
		main["sex"].setText(Gender[t_info.sex])
		if(t_info.sex==1)then
			main["tv_myspace_sex_age"].setBackgroundResource(R.drawable.gender_boy)
		else
			main["tv_myspace_sex_age"].setBackgroundResource(R.drawable.gender_girl)
		end
		if t_info.signature~=nil then
			main["signature"].setText(t_info.signature)
		end

		local regTime=LocalUtils.simpleDateFormat("yyyy-MM-dd",t_info.regTime)
		main["regTime"].setText(regTime)
		local age=LocalUtils.calculateDatePoor(t_info.birthday)
		main["tv_myspace_sex_age"].setText(age)
		
		local constellation=LocalUtils.constellation(t_info.birthday)
		main["age"].setText(age.."岁 "..constellation)		
		main["tv_myspace_constellation"].setText(constellation)
		
		if t_info.location~=nil then
			main["location"].setText(t_info.location)
			main["user_location"].setText(t_info.location)
		end
		
		main["tv_dynamic_count"].setText(tostring(t_info.article_count))
		--征婚资料
		if(t_info.marriage==0)then
			main["marriage_seeking_layout"].setVisibility(LocalView.GONE)
			main["myinfotitle"].setText("该用户暂未开启征婚资料")
		elseif(t_info.marriage==1)then
			main["marriage_seeking_layout"].setVisibility(LocalView.VISIBLE)
		end
		--个人写真
		dynamic_adapter.setArrayString(cjson.encode(t_info.images))
--		--语音介绍
		if(t_info.voice==nil or t_info.voice=="")then
			main["signaturevoice_layout"].setVisibility(LocalView.GONE)
		else
			main["signaturevoice_layout"].setVisibility(LocalView.VISIBLE)
			local voice=main["voice"]
			voice.setBackgroundResource(R.anim.play_voice_animation)
			
			local play_voice_layout=main["play_voice_layout"]
			play_voice_layout.setImageResource(R.drawable.play_voice_clip)
			attachOnClickListener(play_voice_layout,function()
				voice.callOnClick();
				local voiceUrl=string.gsub(UserInfo.server_addr,"img","voice")
				LocalUtils.AsyncProgressVoice(this,play_voice_layout,voice,string.format("%s%s",voiceUrl,t_info.voice),10)
			end)
		end
		
		
		
		if(t_info.seeking~=nil)then
			main["profession"].setText(t_info.seeking.profession)
			main["education"].setText(t_info.seeking.education)
			main["feeling"].setText(t_info.seeking.marital_status)
			main["bodily"].setText(t_info.seeking.bodily)
			main["income"].setText(t_info.seeking.income)
			main["hasHouse"].setText(t_info.seeking.hasHouse)
			main["hasCar"].setText(t_info.seeking.hasCar)
			main["fellow_townsman"].setText(t_info.seeking.fellow_townsman)
		else
			local default_text=Html.fromHtml("<b><font color='red'> 未填写</font></b>")
			main["profession"].setText(default_text)
			main["education"].setText(default_text)
			main["feeling"].setText(default_text)
			main["bodily"].setText(default_text)
			main["income"].setText(default_text)
			main["hasHouse"].setText(default_text)
			main["hasCar"].setText(default_text)
			main["fellow_townsman"].setText(default_text)
		end

		local avatar_url=UserInfo.server_addr..t_info.avatar
		local user_avatar=main["user_avatar"]
		user_avatar.setBorderWidth(10)
		displayCircleImage(avatar_url,user_avatar)
--		displayImage(avatar_url,user_avatar)
		--对话
		local btn_change_chatting=main.btn_change_chatting
		LocalUtils.setTextViewDrawableLeft(this,btn_change_chatting,"drawable/user_icon_chatbtn.png")
		setColorButton(btn_change_chatting,"#41485a","#333546",function()
			local prameterText=cjson.encode({rid=UserInfo.ID,uid=t_info.uid,avatar=t_info.avatar,nickname=t_info.nickname,sid=t_info.ID})
			LocalUtils.enterAppActivity(this,"ChatingActivity",prameterText)
		end,0)
		--举报
		local btn_title_right_next=main["btn_title_right_next"]
		setColorButton(btn_title_right_next,'#00000000','#00000000',function()
			OptionReport(t_info.uid)
		end,0)
		
		local layout_bottom_btn=main["layout_bottom_btn"]
		if(t_info.uid==UserInfo.uid)then
			local btn_editor_myinfo=main["btn_editor_myinfo"]
			setColorButton(btn_editor_myinfo,"#8da6bc","#7090ab",function()
				enterActivity("AccountInfoActivity",R.anim.in_from_right,R.anim.out_to_left, false)
			end,50)
			main["user_attention"].setVisibility(LocalView.GONE)
			main["layout_btn_title_right_next"].setVisibility(LocalView.GONE)
			layout_bottom_btn.setVisibility(LocalView.GONE)
			btn_title_right_next.setVisibility(LocalView.GONE)
			main["layout_bottom_editor"].setVisibility(LocalView.VISIBLE)
			main["layout_btn_title_right_next"].setVisibility(LocalView.GONE)
			main["btn_latestlogin"].setVisibility(LocalView.GONE)
		else
			layout_bottom_btn.setVisibility(LocalView.VISIBLE)
			main["user_attention"].setVisibility(LocalView.VISIBLE)
			main["layout_btn_title_right_next"].setVisibility(LocalView.VISIBLE)
--			main["layout_bottom_btn"].setVisibility(LocalView.VISIBLE)
			main["layout_bottom_editor"].setVisibility(LocalView.GONE)
			main["btn_latestlogin"].setVisibility(LocalView.VISIBLE)
		end
		
		--打招呼
		local btn_sayhi=main.btn_sayhi
		LocalUtils.setTextViewDrawableLeft(this,btn_sayhi,"drawable/user_icon_hibtn.png")
		setColorButton(btn_sayhi,"#41485a","#333546",function()
			LocalUtils.sendSayHi(this, t_info.ID);
		end,0)
		--送礼物
		local btn_send_gift=main.btn_send_gift
		LocalUtils.setTextViewDrawableLeft(this,btn_send_gift,"drawable/user_icon_hibtn.png")
		setColorButton(btn_send_gift,"#41485a","#333546",function()
			startActivity("GiftShopActivity",cjson.encode({uid=t_info.uid}))
		end,0)
		--心动
		local btn_attention=main.user_attention
		btn_attention.setBackgroundResource(R.drawable.style_attention_border)
		if(t_info.linkman~=nil)then
			btn_attention.setText("已关注")
		else
			btn_attention.setText("＋关注")
		end
--		
		--关注
		local function doAttention()
			local url=URL_User(10006,cjson.encode({uid=UserInfo.uid,linkmanid=t_info.uid}))
			AsyncHttp(url,function(body)  
				println(body) 
				local json=cjson.decode(body)
				if(json.result==0)then
					btn_attention.setText("＋关注")
				elseif(json.result==1)then
					btn_attention.setText("已关注")
				end
				ToastText(json.desc)
			end)
		end
		attachOnClickListener(btn_attention,function()
			listener=localAlertDialogListener(
		 	function()
		 		doAttention()
		 	end,
		 	function()
		 	end)
		 	LocalUtils.LocalAlertDialog(this,"","确定不再关注TA吗？",listener)
		end,0)
--		
		if (t_info.photobook~=nil) then
			main["layout_horizontal_photobook"].setVisibility(LocalView.VISIBLE)
			local photobookItemAdapter=PhotobookItemAdapter(this)
			photobookItemAdapter.setArray(cjson.encode(t_info.photobook))
			recycler_photobook.setAdapter(photobookItemAdapter)
			photobookItemAdapter.notifyDataSetChanged()
		else
			main["layout_horizontal_photobook"].setVisibility(LocalView.GONE)
		end
	end)
end

function onCreate(bundle)
	onLoadLayout()
 	onController()
 	
end

function finish()
	this.finish()
end

function onKeyDown(keyCode,event)
	if(keyCode == KeyEvent.KEYCODE_BACK) then
		finish()
	end
end