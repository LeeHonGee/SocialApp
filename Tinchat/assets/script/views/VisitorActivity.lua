require "init"
local VisitorItemAdapter=luajava.bindClass("com.sharemob.tinchat.lib.adapter.VisitorItemAdapter")

local main={}
local adapter
local recycler_visitor
layout_header={
	FrameLayout,
	id="activity_header",
	layout_width="match_parent",
	layout_height=dimens.dx_45,
	layout_marginBottom="45dp",
	layout_gravity="top",
	background=Color.TitleBgColor,
	layout_title_image("left|center_vertical","btn_cancle","drawable/btn_public_back_normal.png"),
	{
		TextView,
		layout_width="wrap_content",
		layout_height="wrap_content",
		layout_gravity="center|center_vertical",
		textStyle="bold",
		textColor=Color.TitleTextColor,
		textSize="17sp",
		text="谁看过我"
	}
}

function onLoadLayout()
	layout={
			FrameLayout,
			layout_gravity="center|top",
			layout_width="match_parent",
			layout_height="match_parent",
			background=Color.WhiteSmoke,
			orientation="vertical",
			layout_header,
			{
				PullLoadMoreRecyclerView,
--				background=Color.WhiteSmoke,
				layout_marginTop="45dp",
				id="recycler_visitor",
				gravity="center_vertical",
				layout_width="match_parent",
				layout_height="match_parent"
			}
			,{
				TextView,
				id="visitor_tv_tip",
				layout_width="wrap_content",
				layout_height="wrap_content",
				layout_gravity="center_vertical|center_horizontal",
				textColor="#8a8989",
				textSize="17sp",
				text="暂时没有访问我资料"
			}
	}
	this.setContentView(loadlayout(layout,main))
end

local function loadNetData()
			local function update(n)
				os.execute("sleep 0.2")
			end
			--回调函数
			local function callback()
				local url=URL_User(10023,cjson.encode({uid=UserInfo.uid}))
				println(url)
				AsyncHttp(url,function(body)
					local reponse=cjson.decode(body)
					println(body)
					if(reponse.result==1) then
						main["visitor_tv_tip"].setVisibility(LocalView.VISIBLE)
						recycler_visitor.setVisibility(LocalView.GONE)
					elseif(reponse.result==0) then
						main["visitor_tv_tip"].setVisibility(LocalView.GONE)
						recycler_visitor.setVisibility(LocalView.VISIBLE)
						adapter.setArray(cjson.encode(reponse.body))
						adapter.notifyDataSetChanged()
						recycler_visitor.setPullLoadMoreCompleted();
					end
				end)
			end
			task(update,2000,callback)
end

function onController()
	--返回按钮
	local btn_title_cancle=main.btn_cancle
	setColorButton(btn_title_cancle,'#e8e8e8','#00000000',
	function()
		finish()
	end,0)
	
	local pullLoadMoreListener=luajava.createProxy("com.sharemob.tinchat.lib.refreashtabview.extras.PullLoadMoreRecyclerView$PullLoadMoreListener",{
		onRefresh=function()
			loadNetData()
		end,
		onLoadMore=function() 
			loadNetData()
		end
	})
	
	recycler_visitor=main["recycler_visitor"]
	recycler_visitor.setGridLayout(1)
	
	adapter=VisitorItemAdapter(this)
	recycler_visitor.setAdapter(adapter)
	recycler_visitor.setOnPullLoadMoreListener(pullLoadMoreListener)
	
	loadNetData()
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