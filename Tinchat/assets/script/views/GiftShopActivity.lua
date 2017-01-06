require "init"

local GiftShopItemAdapter=luajava.bindClass("com.sharemob.tinchat.lib.adapter.GiftShopItemAdapter")

local main={}
function finish()
	this.finish() 
end

function onKeyDown(keyCode,event)
	if(keyCode == KeyEvent.KEYCODE_BACK) then
		finish()
	end
end

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
		textColor=Color.TitleTextColor,
		textSize="17sp",
		text="礼物商店"
	}
}

function onLayout()
	layout={
		LinearLayout,
		layout_width="match_parent",
		layout_height="match_parent",
		background=Color.WhiteSmoke,
		orientation="vertical",
		layout_header,
		line,
		{
			RecyclerView,
			id="recycler_gift_shopping",
			layout_width="match_parent",
			layout_height="wrap"
		}
	}

	this.setContentView(loadlayout(layout,main))
	
--	local btn_submit=main.btn_title_submit
--	attachOnClickListener(btn_submit,
--	function()   
--		local content=tostring(ev_feedback.getText())
--		if(content=='') then
--			ToastText("提交内容不能为空")
--			return
--		end
--		
--		local url=URL_APP(2003,cjson.encode({content=content,userid=URLEncoder.encode(UserInfo.uid, "UTF-8")}))
--		println(url)
--		AsyncHttps(url,function(body)
--			println(body)
--			local status=cjson.decode(body)
--			if(status.result==0) then
--				ToastText(status.desc)
--				finish()
--			end
--		end)
--	end)
--	
end

function onController()
	local btn_title_cancle=main.btn_cancle
	setColorButton(btn_title_cancle,'#e8e8e8','#00000000',
	function()
		finish()
	end,0)
	
	local adapter=GiftShopItemAdapter(this)
	adapter.addParam("loveruid",json_param.uid)
	local recycler_shopping=main["recycler_gift_shopping"]
	recycler_shopping.setHasFixedSize(true);
	local layoutManager =StaggeredGridLayoutManager(1,StaggeredGridLayoutManager.VERTICAL);
	recycler_shopping.setItemAnimator(DefaultItemAnimator()); 
	recycler_shopping.setLayoutManager(layoutManager);
	recycler_shopping.setAdapter(adapter)
	
	local url=URL_User(10044,cjson.encode({uid=json_param.uid,myuid=UserInfo.uid}))
	println(url)
	AsyncHttp(url,function(body)
		adapter.addArray(body)
	end)
end
function onCreate(savedInstanceState)
	onLayout()
	onController()
end