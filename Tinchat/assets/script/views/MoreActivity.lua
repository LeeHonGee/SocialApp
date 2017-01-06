require "init"

local array_ids={"btn_binding","btn_privacy","btn_aboutus","btn_feedback"}
local main={}

function finish()
	this.finish()
end

function onKeyDown(keyCode,event)
	if(keyCode == KeyEvent.KEYCODE_BACK) then
		finish()
	end
end
function onLayout()
	local btn_binding_content="已绑定"
	if(UserInfo.phone~=nil)then
		btn_binding_content="已绑定"
	else
		btn_binding_content="未绑定"
	end

	local layout_title_header=require "layout_title_header".new()
	layout_title_header:create({title="更多",leftListenerFunc=function() finish()   end,rightListenerFunc=nil})
	
		cell_layout=function(title,content,cell_id)
		cell={
			FrameLayout,
			id=cell_id,
			layout_width="match_parent",
			layout_height="50dp",
			layout_gravity="top",
			background=Color.White,
			line,
			{
				TextView,
				layout_width="wrap_content",
				layout_height="wrap_content",
				layout_gravity="left|center_vertical",
				text=title,
				layout_marginLeft="15dp",
				textColor=Color.slategrey,
				textSize="16sp"
			},
			{
				TextView,
				layout_width="wrap_content",
				layout_height="wrap_content",
				layout_gravity="right|center_vertical",
				text=content,
				layout_marginLeft="15dp",
				textColor=Color.textgrey,
				layout_marginRight="15dp",
				textSize="14sp"
			}
		}
		return cell		
	end
	
	cell_binding=cell_layout("手机号绑定",btn_binding_content,"btn_binding")
	cell_feedback=cell_layout("用户反馈","","btn_feedback")
	cell_aboutus=cell_layout("关于我们","","btn_aboutus")
	cell_item_privacy=cell_layout("隐私","","btn_privacy")
--	cell_shared=cell_layout("推荐好友","","btn_shared")
	layout={
		LinearLayout,
		layout_width="fill",
		layout_height="fill",
		background=Color.WhiteSmoke,
		orientation="vertical",
		layout_title_header.layout,
		{
			LinearLayout,
			layout_width="match_parent",
			layout_height="wrap_content",
			background=Color.WhiteSmoke,
			layout_marginBottom="10dp",
			orientation="vertical",
			cell_binding,
			line
		},
		cell_item_privacy,
		cell_aboutus,line,
		{
			LinearLayout,
			layout_width="match_parent",
			layout_height="wrap_content",
			background=Color.WhiteSmoke,
			layout_marginTop="10dp",
			layout_marginBottom="50dp",
			orientation="vertical",
			cell_feedback,
			line
		},line
		,{
			ColorButton,
			id="btn_logout",
			layout_width="fill_parent",
			layout_height="45dp",
			text="退出登录",
			layout_gravity="center|bottom",
			gravity="center",
			textColor="#e74c3c",
			textSize="17sp",
			background="#f2f1f1"
		},line
	}
	this.setContentView(loadlayout(layout,main))
	layout_title_header:initComponent(main.iBtn_title_back,main.iBtn_title_right)
	
	local btn_logout=main.btn_logout
	btn_logout.setRadius(0)
	btn_logout.setBackColor("#ffffff")
	btn_logout.setBackColorSelected("#e5e5e5")
	
	local function eventLogout()
	 	println("退出应用")
	 	listener=localAlertDialogListener(
	 	function()
	 		LocalUtils.exit(this)
	 	end,
	 	function()
	 	
	 	end)
	 	LocalUtils.LocalAlertDialog(this,"退出提示","您确定关闭当前账号",listener)
	end 
	attachOnClickListener(btn_logout,eventLogout)
	
end

function onController()

	local function eventCallback(id)
		if id=="btn_aboutus" then
			enterActivity("AboutusActivity")
		elseif id=="btn_feedback" then
			enterActivity("FeedbackActivity")
		elseif id=="btn_binding" and UserInfo.phone==nil then
			enterActivity("BindingPhoneActivity")
		elseif id=="btn_privacy"then
			enterActivity("PrivacyActivity")
--		elseif id=="btn_logout" then
--			local message="能对你温柔入睡,也能让你三观尽毁,上佳缘恋情.";
--			local text="能对你温柔入睡,也能让你三观尽毁,上佳缘恋情.";
--			local url="http://www.baidu.com";
--			LocalUtils.sharedWXFriend(this,message,text,url)
		end
	end
	for i=1,#array_ids,1 do
		local id=array_ids[i]
		local view=main[array_ids[i]]
		SetBackgoundColor({view=view,callback=eventCallback,id=id},0xec7063,0xffffffff)
	end
end

function onCreate(savedInstanceState)
	onLayout()
	onController()
end