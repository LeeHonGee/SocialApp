require "init"

local main={}

layout_header=function(title)
	layout=	{
		FrameLayout,
		id="activity_header",
		layout_width="match_parent",
		layout_height=dimens.dx_45,
		layout_gravity="top",
		background=Color.TitleBgColor,
		layout_title_image_size("left|center_vertical","btn_message","drawable/ic_beauty_review_count_n","25dp","25dp"),
		{
			TextView,
			id="tv_title_content",
			layout_width="wrap_content",
			layout_height="wrap_content",
			layout_gravity="center",
			text=title,
			textStyle="bold",
			textColor=Color.TitleTextColor,
			textSize="16sp"
		}
	}
	return layout
end

local function 	gender()
		if(UserInfo.sex==Gender.MALE) then
			return "gender_boy"
		elseif(UserInfo.sex==Gender.FEMALE) then
			return "gender_girl"
		end
end

local function getAge()
	return tostring(LocalUtils.calculateDatePoor(CacheManager.getInstance().userInfo.birthday))
end

local function getConstellation()
	return tostring(LocalUtils.constellation(CacheManager.getInstance().userInfo.birthday))
end

cell_account_header=function(cell_id)

	cell={
		FrameLayout,
		id=cell_id,
		background=Color.WhiteSmoke,
		layout_width="match_parent",
		layout_height="wrap_content",
		{
		LinearLayout,padding="12dp",orientation="horizontal",layout_width="fill",layout_height="wrap",
		{
			RoundImageView,
			id="account_avatar",
			layout_width="65dp",
			layout_height="65dp",
			layout_marginRight="10dp",
--			border_color=Color.White,
			layout_gravity="center_vertical",
			src="drawable/public_default_head"
		},
		{
			LinearLayout,orientation="vertical",layout_width="fill",layout_height="wrap",
			{
				TextView,
				id="tv_center_nickname",
				layout_width="wrap_content",
				layout_height="wrap_content",
				textColor=Color.Back,
				text="",
				textSize="16sp",
				textStyle="bold",
				layout_gravity="left|center_vertical"
			},
			{
				TextView,
				id="tv_center_ID",
				layout_width="wrap_content",
				layout_height="wrap_content",
				layout_marginTop="5dp",
				textColor="#535b5e",
				text="00000",
				textSize="16sp",
				textStyle="bold",
				layout_gravity="left|center_vertical"
			},
			{
				LinearLayout,orientation="horizontal",layout_width="fill",layout_height="wrap",	layout_marginTop="5dp",
				{
				TextView,
				id="tv_center_sex_age",
				layout_width="30dp",
				layout_height="14dp",
				paddingRight="5dp",
				background="drawable/gender_boy.png",
				gravity="right|center_vertical",
				text="0",
				textColor=Color.White,
				textSize="9sp",
				textStyle="normal"
			},
			{
				TextView,
				id="tv_constellation",
				layout_width="wrap_content",
				layout_height="wrap_content",
				paddingLeft="10dp",
				paddingRight="10dp",
				paddingTop="3dp",
				paddingBottom="3dp",
				layout_gravity="left|center_vertical",
				text="",
				textColor="#535b5e",
				textSize="11sp",
				textStyle="bold"
			}
			}
		}
		},
		{
			ImageView,
			id="item_arrow_icon",
			layout_width="12dp",
			layout_height="12dp",
			layout_marginRight="20dp",
			gravity="center_vertical",
			layout_gravity="right|center_vertical",
			background="drawable/icon_public_arrow"
		}
	}
	return cell
end

cell_visitor_item=function(cell_id,title,drawableurl)
	cell={
		LinearLayout,id=cell_id,orientation="vertical",layout_width="fill",layout_height="wrap",background=Color.White,
		{
			LinearLayout,
			padding="12dp",
			layout_width="fill",
			layout_height="match_parent",
			orientation="horizontal",
			{
				ImageView,
				layout_marginRight="10dp",
				layout_width="30dp",
				layout_height="30dp",
				layout_gravity="center_vertical",
				background=drawableurl
			},
			{
				TextView,
				id="visitor_title",
				layout_width="wrap_content",
				layout_height="wrap_content",
				layout_marginRight="20dp",
				textColor="#535b5e",
				text=title,
				textSize="16sp",
				textStyle="normal",
				layout_gravity="left|center_vertical"
			},
			{
				ImageView,
				id="iv_visitor",
--				layout_marginLeft="10dp",
				layout_width="10dp",
				layout_height="10dp",
				layout_gravity="center_vertical"
			}
	
		},
		line
}
	return cell
end

function onAttach(activity)
	println("onAttach")
end

function onlayout()
layout={
		LinearLayout,
		background=Color.WhiteSmoke,
		layout_width="match_parent",
		layout_height="match_parent",
		orientation="vertical",
		layout_header("个人中心"),
		{
		LocalHoveringScrollView,
		layout_width="match_parent",
		layout_height="match_parent",
		layout_marginBottom="50dp",
		background=Color.WhiteSmoke,
		{
			LinearLayout,
			layout_width="match_parent",
			layout_height="match_parent",
			orientation="vertical",
			{
				LinearLayout,
				layout_width="fill",
				layout_height="wrap",
				orientation="vertical",line,
				{
					LinearLayout,
					layout_marginBottom="20dp",
					background=Color.WhiteSmoke,
					layout_width="match_parent",
					layout_height="match_parent",
					orientation="vertical",
					cell_account_header("account_header"),
					line
				},
				line,
				cell_item("item_space","我的动态","drawable/icon_account_room.png"),
				cell_visitor_item("item_visitor","最近访客","drawable/personal_recent_visitors_icon"),
				cell_item("item_gift","我的礼物","drawable/icon_gift"),
				cell_item("item_member","会员中心","drawable/icon_account_vip"),
				cell_item("item_honesty","诚信认证","drawable/icon_account_verification")
			}
			,{
				LinearLayout,
				layout_width="fill",
				layout_height="wrap",
				layout_marginTop="20dp",
				orientation="vertical",
				line,
				cell_item("item_feedback","客服中心","drawable/icon_account_feedback.png"),
				cell_item("item_privacy","全部订单","drawable/icon_account_notification.png"),
				cell_item("item_more","关注","drawable/icon_account_more.png"),
				cell_item("item_attention&fans","关注/粉丝","drawable/icon_account_share.png"),
			}
			,{
				LinearLayout,
				layout_width="fill",
				layout_height="wrap",
				layout_marginTop="20dp",
				orientation="vertical",
				line,
				cell_item("item_feedback","客服中心","drawable/icon_account_feedback.png"),
				cell_item("item_privacy","全部订单","drawable/icon_account_notification.png"),
				cell_item("item_addressmanager","地址管理","drawable/icon_account_share.png"),
				cell_item("item_more","更多","drawable/icon_account_more.png")
			}
			
		}
		}
	}
		



	this.setContentView(loadlayout(cell_item("item_feedback","客服中心","drawable/icon_account_feedback.png"),main))
	--	"item_feedback","item_privacy","item_shared"
--	local center_cell_ids={"account_header","item_space","item_visitor","item_member","item_honesty","item_attention&fans","item_more","item_addressmanager","item_gift"}
--	
--	local activitys={}
--	activitys["account_header"]="AccountInfoActivity"
--	activitys["item_visitor"]="VisitorActivity"
--	activitys["item_member"]="MemberActivity"
--	activitys["item_honesty"]="HonestyActivity"
--	activitys["item_space"]="MyDynamicArticleActivity"
--	activitys["item_addressmanager"]="AddressManagerActivity"
----	activitys["item_attention&fans"]="PrivacyActivity"
--	activitys["item_more"]="MoreActivity"
--	activitys["item_gift"]="MygiftsFragmentActivity"
	
--	local function eventCallback(id)
--		println(id)
--		if(id=="item_space") then
--			OptionUpdateActivity(activitys[id],cjson.encode({uid=UserInfo.uid}))
--		elseif(id=="item_gift")then
--			LocalUtils.enterActivity(this,"com.sharemob.tinchat.modules.home.MygiftsActivity")
--		elseif(id=="item_attention&fans")then
--			LocalUtils.enterActivity(this,"com.sharemob.tinchat.modules.home.LinkmanActivity")
--		else
--			if(id=="item_visitor")then
--				main["iv_visitor"].setVisibility(LocalView.GONE)
--			end
--			enterActivity(activitys[id],anim.in_from_right,anim.out_to_left,false)
--		end
--	end
--	
--	for i=1,#center_cell_ids,1 do
--		local id=center_cell_ids[i]
--		local view=main[id]
--	
--		SetBackgoundColor({view=view,callback=eventCallback,id=id},0xec7063,0xffffffff)
--	end
end

local account_avatar
function onResume()
	
--	if(UserInfo.nickname~=nil and URLDecoder~=nil) then
----		ImageUtils.loaderImageForCache(UserInfo.server_addr..UserInfo.avatar,main["account_avatar"],false)
----		println(avatar_url)
--		local url=string.format("%s%s",UserInfo.server_addr,UserInfo.avatar)
--		ImageLoader.getInstance().displayImage(url,account_avatar)
----		,GlobalParams.getInstance().round_options)
--
--		println("onResume"..CacheManager.getInstance().userInfo.birthday)
--		main["tv_center_sex_age"].setText(getAge())
--		main["tv_constellation"].setText(getConstellation())
--		main["tv_center_nickname"].setText(URLDecoder.decode(UserInfo.nickname,"UTF-8"))
--		LocalUtils.setTextViewBackgroundDrawable(this,main["tv_center_sex_age"],string.format(string.format("drawable/%s.png",gender())))
--	
--	end
	
end

function onController()

	main["layout_btn_message"].setVisibility(LocalView.VISIBLE)

	account_avatar=main["account_avatar"]
	account_avatar.setType(RoundImageView.TYPE_ROUND);
	account_avatar.setBorderRadius(5);
	
	if(UserInfo.nickname~=nil and URLDecoder~=nil)then
		main["tv_center_nickname"].setText(URLDecoder.decode(UserInfo.nickname,"UTF-8"))
		main["tv_center_ID"].setText(string.format("ID:%05d",UserInfo.ID))
		main["tv_center_sex_age"].setText(getAge())
		
		LocalUtils.setTextViewBackgroundDrawable(this,main["tv_center_sex_age"],string.format(string.format("drawable/%s.png",gender())))
		
		if(tointeger(UserInfo.visitor_count)>0) then
			main["iv_visitor"].setBackgroundResource(R.drawable.red_oval);
			main["visitor_title"].setText("最近访客 +"..UserInfo.visitor_count)
		elseif(tointeger(UserInfo.visitor_count)==0) then
			main["iv_visitor"].setVisibility(LocalView.GONE)
		end
	end
	

--谁看过我
--		attachOnClickListener(main.btn_visitor,function()
--			enterLocalActivity("com.sharemob.tinchat.modules.space.VisitorActivity",
--			R.anim.in_from_right,
--			R.anim.out_to_left,
--			false);
--		end)
	--谁看过我
--	local recyclerView=main.recycler_visitors
--	setRecyclerView(recyclerView)
--	loadRecyclerAdapter(recyclerView,visitors,0)
end

function onCreate(savedInstanceState)
	onlayout()
--	onController()
end