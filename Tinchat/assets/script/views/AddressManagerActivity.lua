require "init"

local AddressItemAdapter=luajava.bindClass("com.sharemob.tinchat.lib.adapter.address.AddressItemAdapter")
local adapter
local recycler_addresses
local main={}

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
		text="收货地址管理"
	},
--	layout_title_image_size("right|center_vertical","btn_title_right_next","drawable/btn_title_more.png",60,20),
	layout_title_text("right|center_vertical","btn_title_right_next","新增")
}

local function onLoadlayout()
	layout={
		FrameLayout,
		id="myspace",
		layout_width="match_parent",
		layout_height="match_parent",
		background=Color.WhiteSmoke,
		orientation="vertical",
		layout_header,
		{
			PullLoadMoreRecyclerView,
			id="recycler_addresses",
--			background=Color.WhiteSmoke,
			layout_marginTop="45dp",
			gravity="center_vertical",
			layout_width="match_parent",
			layout_height="match_parent"
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
				local url=URL_User(10045,cjson.encode({uid=UserInfo.uid}))
				println(url)
				AsyncHttp(url,function(body)
					local reponse=cjson.decode(body)
					println(body)
					if(reponse.result==1) then
--						main["visitor_tv_tip"].setVisibility(LocalView.VISIBLE)
--						recycler_visitor.setVisibility(LocalView.GONE)
					elseif(reponse.result==0) then
--						main["visitor_tv_tip"].setVisibility(LocalView.GONE)
--						recycler_visitor.setVisibility(LocalView.VISIBLE)
						adapter.addArray(cjson.encode(reponse.body))
						adapter.notifyDataSetChanged()
						recycler_addresses.setPullLoadMoreCompleted();
					end
				end)
			end
			task(update,2000,callback)
end

function onResume()
	loadNetData()
end

function onController()
	--返回按钮
	local btn_title_cancle=main.btn_cancle
	setColorButton(btn_title_cancle,'#e8e8e8','#00000000',
	function()
		this.finish()
	end,0)
	
	local btn_title_right_next=main.btn_title_right_next
	setColorButton(btn_title_right_next,'#e8e8e8','#00000000',
	function()
		enterActivity("AddAddressItem")
	end,0)
	
	recycler_addresses=main["recycler_addresses"]
	recycler_addresses.setGridLayout(1)
	adapter=AddressItemAdapter(this)
	recycler_addresses.setAdapter(adapter)
	
	local pullLoadMoreListener=luajava.createProxy("com.sharemob.tinchat.lib.refreashtabview.extras.PullLoadMoreRecyclerView$PullLoadMoreListener",{
		onRefresh=function()
			loadNetData()
		end,
		onLoadMore=function() 
			loadNetData()
		end
	})
	
	recycler_addresses.setOnPullLoadMoreListener(pullLoadMoreListener)
end

function onCreate(savedInstanceState)
	onLoadlayout()
	onController()
end