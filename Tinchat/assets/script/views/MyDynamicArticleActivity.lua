require "init"

import "android.app.*"
import "android.widget.ListView"
import "android.widget.SimpleAdapter"

--local LatestLoginAdapter=luajava.bindClass("com.sharemob.tinchat.lib.adapter.LatestLoginAdapter")
local DynamicArticleItemAdapter=luajava.bindClass("com.sharemob.tinchat.lib.adapter.DynamicArticleItemAdapter")
local recyclerView
local pullToRefresh
local adapter
local list=ArrayList()
local main={}
local function onBackEvent()
	finish()
end

function finish()
	this.finish()
end

function onKeyDown(keyCode,event)
	if(keyCode == KeyEvent.KEYCODE_BACK) then
		finish()
	end
end

local white_line={
	View,
	layout_width="100dp",
	layout_height="1dp",
	background="#c8c8c8"
}

local layout_header={
	FrameLayout,
	id="activity_header",
	layout_width="match_parent",
	layout_height=dimens.dx_45,
	layout_marginBottom="0dp",
	layout_gravity="top",
	background=Color.TitleBgColor,
	layout_title_image_size("left|center_vertical","btn_cancle","drawable/btn_public_back_normal","25dp","25dp"),
	{
		TextView,
		id="title_content",
		layout_width="wrap_content",
		layout_height="wrap_content",
		layout_gravity="center|center_vertical",
		textStyle="bold",
		textColor=Color.TitleTextColor,
		textSize="17sp",
		text="分享我的生活"
	},
	layout_title_image_size("right|center_vertical","btn_title_right","drawable/ic_topbar_edit_white","20dp","20dp")
}


local layout_footerView={
	LinearLayout,
	layout_width="wrap_content",
	layout_height="wrap_content",
	orientation="horizontal",
	gravity="center_vertical",
	padding="10dp",
	white_line,
	{
		TextView,
		layout_width="wrap_content",
		layout_height="wrap_content",
		gravity="center_horizontal|center_vertical",
		layout_gravity="center_horizontal|center_vertical",
		textSize="14sp",
		layout_marginLeft="5dp",
		layout_marginRight="5dp",
		text="怎么不写下你身边的故事呢?",
		textColor=Color.Back
	},
	white_line
}
	
local layout_dynamic_empty_tip={
			LinearLayout,
			id="dynamic_empty_tip",
			visibility=LocalView.GONE,
			layout_width="wrap_content",
			layout_height="match_parent",
			layout_gravity="center_horizontal|center_vertical",
			gravity="fill_horizontal|center_vertical",
			orientation="horizontal",
			padding="10dp",
			spite_line,
			{
				TextView,
				layout_width="wrap_content",
				layout_height="wrap_content",
				textSize="15sp",
				text="哎哟,什么也没有啊",
				textColor=Color.textgrey
			},
			spite_line
}

local layout_dynamic_header={
		FrameLayout,
		id="layout_header",
		layout_width="match_parent",
		layout_height="150dp",
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
			LinearLayout,
			id="layout_change_avatar",
			layout_width="match_parent",
			layout_height="80dp",
			layout_gravity="bottom",
			gravity="bottom",
			orientation="horizontal",
			layout_margin="15dp",
			{
				CircleImageView,
				id="user_avatar",
				layout_width="match_parent",
				layout_height="match_parent",
				layout_marginRight="30dp",
				BorderWidth="10dp",
				scaleType="centerCrop",
				src="drawable/public_default_head.png"
			},
			{
					LinearLayout,layout_width="wrap",layout_height="wrap",orientation="vertical",layout_marginRight="40dp",
					{
						TextView,id="user_nickname",layout_width="wrap_content",
						layout_height="wrap_content",layout_gravity="left|center_vertical",
						layout_marginBottom="3dp",text="25",textColor=Color.White,textSize="20sp",textStyle="bold"
					},
					{
						TextView,layout_width="wrap_content",layout_height="wrap_content",layout_gravity="left|center_vertical",
						layout_marginBottom="3dp",text="动态",textColor=Color.White,textSize="11sp",textStyle="bold"
					}
			},
			{
					LinearLayout,layout_width="wrap",layout_height="wrap",orientation="vertical",
					{
						TextView,id="user_nickname",layout_width="wrap_content",layout_height="wrap_content",layout_gravity="left|center_vertical",
						layout_marginBottom="3dp",text="1846",textColor=Color.White,textSize="20sp",textStyle="bold"
					},
					{
						TextView,layout_width="wrap_content",layout_height="wrap_content",layout_gravity="left|center_vertical",
						layout_marginBottom="3dp",text="浏览次数",textColor=Color.White,textSize="11sp",textStyle="bold"
					}
			}
		}
}

local function loadLayout()
	local layout={
		LinearLayout,
		layout_width="match_parent",
		layout_height="match_parent",
		background=Color.WhiteSmoke,
		orientation="vertical",
		layout_header,
--		layout_dynamic_header,
		{
			PullLoadMoreRecyclerView,
			id="recycler_myDynamicArticle",
			gravity="center_vertical",
			layout_width="match_parent",
			layout_height="match_parent"
		},
		layout_dynamic_empty_tip
	}
	
	this.setContentView(loadlayout(layout,main))
	--设置标题
	main["title_content"].setText(UserInfo.nickname.." 的动态")
	
	--头像
--	local avatar_url=UserInfo.server_addr..UserInfo.avatar
--	local user_avatar=main["user_avatar"]
--	user_avatar.setBorderWidth(10)
--	displayCircleImage(avatar_url,user_avatar)
	--返回按钮
	local layout_btn_cancle=main["layout_btn_cancle"]
	layout_btn_cancle.setVisibility(LocalView.VISIBLE)
	setColorButton(main.btn_cancle,'#e8e8e8',Color.transparent,
	function()
		println("onClickFunc")
		this.finish()
	end,0)
	--发布新鲜事
	local release=main.btn_title_right
	if(json_param.uid==UserInfo.uid) then
		release.setVisibility(LocalView.VISIBLE)
	else
		release.setVisibility(LocalView.GONE)
	end
	setColorButton(release,'#e8e8e8',Color.transparent,
	function()
		enterActivity("ReleaseDynamicItem",anim.in_from_right,anim.out_to_left,false)
	end,0)
end

----添加ListView页脚
--local function emptyDataForFooterView()
--	local footerView=loadlayout(layout_footerView)
--	footerView.setLayoutParams(LocalUtils.getLayoutParams())
--	pullToRefresh.getRefreshableView().addFooterView(footerView)
--end
--
--local function addHeaderView()
--	local headerView=loadlayout(layout_dynamic_header)
--	headerView.setLayoutParams(LocalUtils.getLayoutParams())
--	pullToRefresh.getRefreshableView().addHeaderView(headerView)
--end

local function loadNetData()
		
			local function update(n)
				os.execute("sleep 0.2")
			end
			
			--回调函数
			local function callback()
				local request_uids=string.format("'%s'",json_param.uid)
				local url=URL_User(10024,cjson.encode({uid=request_uids,myuid=UserInfo.uid,maxID=adapter.getItemMaxID()}))
				println(url)
				AsyncHttp(url,function(body)
					local reponse=cjson.decode(body)
					println(body)
					if(reponse.result==1 and adapter.getItemCount()==0) then
						main["dynamic_empty_tip"].setVisibility(LocalView.VISIBLE)
						main["recycler_myDynamicArticle"].setVisibility(LocalView.GONE)
					elseif(reponse.result==0) then
						main["dynamic_empty_tip"].setVisibility(LocalView.GONE)
						main["recycler_myDynamicArticle"].setVisibility(LocalView.VISIBLE)
						adapter.addArray(cjson.encode(reponse.body))
						adapter.notifyDataSetChanged()
						recyclerView.setPullLoadMoreCompleted();
					else
						recyclerView.setPullLoadMoreCompleted();
					end
					adapter.addHeader(cjson.encode(reponse.info))
				end)
			end
			task(update,2000,callback)
end

function onResume()
	loadNetData()
end

function onController()

	recyclerView=main["recycler_myDynamicArticle"]
	recyclerView.setGridLayout(1)
	
--	local headerView=loadlayout(layout_dynamic_header)
--	headerView.setLayoutParams(LocalUtils.getLayoutParams())
--	recyclerView.addHeaderView(headerView)
	
	local pullLoadMoreListener=luajava.createProxy("com.sharemob.tinchat.lib.refreashtabview.extras.PullLoadMoreRecyclerView$PullLoadMoreListener",{
		onRefresh=function()
			loadNetData()
		end,
		onLoadMore=function() 
			loadNetData()
		end
	})

	
	adapter=DynamicArticleItemAdapter(this)
	recyclerView.setAdapter(adapter)
	recyclerView.setOnPullLoadMoreListener(pullLoadMoreListener)
--	addHeaderView()
	loadNetData()
end
function onCreate(savedInstanceState)
	loadLayout()
	onController()
end