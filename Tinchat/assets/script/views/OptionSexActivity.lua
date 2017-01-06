require "init"

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

	local layout_title_header=require "layout_title_header".new()
	layout_title_header:create({title="性别",leftListenerFunc=function() finish()   end,rightListenerFunc=nil})
	
	cell_layout=function(cell_id,item)
		
		local sex_visible=function(cell_id)
			if cell_id=="item_man" then
				if(tonumber(item.sex)==Gender.MALE) then
					return LocalView.VISIBLE
				else
					return LocalView.GONE
				end
			else
				if(tonumber(item.sex)==Gender.FEMALE) then
					return LocalView.VISIBLE
				else
					return LocalView.GONE
				end
			end
		end
	
		cell={
			LinearLayout,
			layout_width="fill",
			layout_height="fill",
			orientation="vertical",
			{
				FrameLayout,
				id=cell_id,
				layout_width="match_parent",
				layout_height="wrap_content",
				padding="14dp",
				layout_gravity="top",
				background="#ffffff",
				{
					TextView,
					id=string.format("%sOf%s" ,cell_id,"item_left_name"),
					layout_width="wrap_content",
					layout_height="wrap_content",
					layout_marginRight="20dp",
					textColor="#535b5e",
					text=item.left_name,
					textSize="16sp",
					textStyle="normal",
					layout_gravity="left|center_vertical"
				},
				{
					ImageView,
					id="item_arrow_icon",
					layout_width="20dp",
					layout_height="20dp",
					visibility=sex_visible(cell_id),
					gravity="center_vertical",
					layout_gravity="right|center_vertical",
					background="drawable/ic_done_light"
				}
			},
			line
		}
		return cell
	end
	local sex
	if(UserInfo.sex==nil)then
		sex=2
	else
		sex=UserInfo.sex
	end
--	local parameters=this.getIntent().getStringExtra("parameters"); 
--	local sex=cjson.decode(parameters).sex
	cell_item_man=cell_layout('item_man',{left_name="男",sex=sex})
	cell_item_female=cell_layout('item_female',{left_name="女",sex=sex})
	
	layout={
		LinearLayout,
		layout_width="fill",
		layout_height="fill",
		orientation="vertical",
		background=Color.WhiteSmoke,
		layout_title_header.layout,
		{
			LinearLayout,
			layout_width="match_parent",
			layout_height="wrap_content",
			background=Color.WhiteSmoke,
			orientation="vertical",
			cell_item_man,
			cell_item_female
		}
		
	}

	main={}
	this.setContentView(loadlayout(layout,main))
	layout_title_header:initComponent(main.iBtn_title_back,main.iBtn_title_right)
	
	local function eventCallback(id)
		
		local val=tostring(main[id].getText())
		println(val)
--		local sex={["男"]=1,["女"]=0}
--		local userInfo={
--			account=UserInfo.id,sex=sex[val]
--		}
		sendBroadcastOption("item_sex",val)
		
		UserInfo.sex=Gender[val]
--		CacheManager.getInstance().UserInfo=cjson.encode(UserInfo)
		
		if(UserInfo.sex~=nil) then
			UserInfo.sex=Gender[val]
			local url=URL_User(10003,cjson.encode({uid=UserInfo.uid,sex=Gender[val]}))
			http.get(url)
		end
--		local url=URL_Update_UserInfo(cjson.encode(userInfo))
--		println(url)
--		local body,cookie,code,headers=http.get(url)
--		println(body)
		finish()
	end

	local function getId(id)
		return string.format("%sOfitem_left_name" ,id)
	end
	
	SetBackgoundColor({view=main["item_man"],callback=eventCallback,id=getId("item_man")},0xec7063,0xffffffff)
	SetBackgoundColor({view=main["item_female"],callback=eventCallback,id=getId("item_female")},0xec7063,0xffffffff)
		
--	local btn_submit=main.btn_title_submit
--	OnClickListener({view=btn_submit,function()   
--		local nickname=tostring(ev_nickname.getText())
--		if(nickname=='') then
--			ToastText("提交内容不能为空")
--			return
--		end
--		local userInfo={
--			account=account.id,nickname=nickname
--		}
--		sendBroadcastOption("item_nickname",userInfo.nickname)
--		local url=URL_Update_UserInfo(cjson.encode(userInfo))
--		println(url)
--		local body,cookie,code,headers=http.get(url)
--		println(body)
--		
--		finish()
--	end})
end


function onCreate(savedInstanceState)
	layout()

end