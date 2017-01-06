require "init"

local dynamicItemID
local DynamicArticleItemAdapter=luajava.bindClass("com.sharemob.tinchat.lib.adapter.DynamicArticleItemAdapter")
--local DynamicCommentAdapter=luajava.bindClass("com.sharemob.tinchat.lib.adapter.DynamicCommentAdapter")
local recyclerView
local dynamicItemAdapter
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
	layout_title_image_size("left|center_vertical","btn_cancle","drawable/btn_public_back_normal","40dp","40dp"),
	{
		TextView,
		id="title_content",
		layout_width="wrap_content",
		layout_height="wrap_content",
		layout_gravity="center|center_vertical",
		textColor="#ffffff",
		textSize="17sp",
		text="动态详情"
	}
}


local layout_dynamicItem_comment={
		LinearLayout,
		layout_marginTop="45dp",
		layout_marginBottom="45dp",
		layout_width="match_parent",
		layout_height="match_parent",
		background=Color.WhiteSmoke,
		orientation="vertical",
		{
			RecyclerView,
			id="recyclerview_dynamicItem",
			scrollbars="vertical",
			background=Color.WhiteSmoke,
			layout_width="match_parent",
			layout_height="wrap"
		}
}

local layout_chating_input={
		LinearLayout,
		layout_width="match_parent",
		layout_height="wrap",
		orientation="vertical",
		background=Color.White,
		layout_gravity="center_vertical|fill_horizontal|bottom",
		line,
		{
			FrameLayout,
			layout_width="match_parent",
			layout_height="45dp",
			background=Color.White,
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
				text="评论",
				textColor=Color.White,
				textSize="14sp"
			}
		}
}
local function loadLayout()
	local layout={
		FrameLayout,
		orientation="vertical",
		layout_width="match_parent",
		layout_height="match_parent",
		background=Color.ChatingBG,
		layout_header,
		layout_dynamicItem_comment,
		layout_chating_input
	}

	this.setContentView(loadlayout(layout,main))
	
	--返回按钮
	local layout_btn_cancle=main["layout_btn_cancle"]
	layout_btn_cancle.setVisibility(LocalView.VISIBLE)
	
	setColorButton(main["btn_cancle"],'#e8e8e8',Color.transparent,
	function()
		println("onClickFunc")
		finish()
	end,0)
	
	local btn_comment_send=main.btn_comment_send
	setColorButton(btn_comment_send,'#159aff','#159aff',
	function()
		println("onClickFunc")
		local text_value=main.et_comment_text.getText().toString()
		local data={
			ID=dynamicItemID,
			type=1,
			uid=UserInfo.uid,
			nickname=UserInfo.nickname,
			content=text_value,
			birthday=UserInfo.birthday,
			sex=UserInfo.sex,
			createTime=CurrentDateTime(),
			server_addr=UserInfo.server_addr,
			avatar=UserInfo.avatar,
			dcid=UUID()
		}
		local array={}
		table.insert(array,data)
		local requestText=cjson.encode(array)
		
		dynamicItemAdapter.addArrayComment(requestText)
		
		data.content=URLEncoder.encode(text_value, "UTF-8")
		data.type=nil
		data.birthday=nil
		data.sex=nil
		data.createTime=nil
		data.server_addr=nil
		data.avatar=nil
		requestText=cjson.encode(data)
		
		local url=URL_User(10051,requestText)
		println(url)
		AsyncHttp(url,function(body) 
		
		end)
		main.et_comment_text.setText("")
		this.hideFaceToolAndInputFromWindow();
	end,10)
	LocalUtils.setEditChangedListener(main.et_comment_text,btn_comment_send)
end


function recyclerViewOrientation(_recyclerView,orientation)
	_recyclerView.setHasFixedSize(true);
	local layoutManager=LinearLayoutManager(this)
	layoutManager.setOrientation(orientation);
	layoutManager.setStackFromEnd(false);
	_recyclerView.setItemAnimator(DefaultItemAnimator()); 
	_recyclerView.setLayoutManager(layoutManager);
end

function onController()
		local parameters=this.getIntent().getStringExtra("parameters")
		local dynamicItem=cjson.decode(parameters)
		local dynamicItemID=dynamicItem.dynamicItemID
		local uid=dynamicItem.uid
		
		dynamicItemAdapter=DynamicArticleItemAdapter(this)
		dynamicItemAdapter.onClickItem=false
		recyclerview_dynamicItem=main["recyclerview_dynamicItem"]
		recyclerview_dynamicItem.setAdapter(dynamicItemAdapter)
		recyclerViewOrientation(recyclerview_dynamicItem,StaggeredGridLayoutManager.VERTICAL)
		
		local url=URL_User(10025,cjson.encode({ID=dynamicItemID,visitor_uid=UserInfo.uid,myuid=uid}))
		AsyncHttp(url,function(body)
			local reponse=cjson.decode(body)
			if(reponse.result==0) then
				dynamicItemAdapter.addArray(cjson.encode(reponse.body))
				if(reponse.comment~=nil) then
					dynamicItemAdapter.addArrayComment(cjson.encode(reponse.comment))
				end
				dynamicItemAdapter.notifyDataSetChanged()
			end
		end)
end

function onCreate(savedInstanceState)
	loadLayout()
	onController()
end
