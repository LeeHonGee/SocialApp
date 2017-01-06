require "init"
import "android.content.*"
ImagePickerAdapter=luajava.bindClass("com.sharemob.tinchat.lib.adapter.ImagePickerAdapter")
ImagePickerAdapter.Limit=1


local main={}
local account=nil
function finish()
	LocalUtils.LocalAlertDialog(this,"温馨提示","您是否放弃本次注册!")
end

function onKeyDown(keyCode,event)
	if(keyCode == KeyEvent.KEYCODE_BACK) then
		finish()
	end
end

layout_header={
	FrameLayout,
	id="activity_header",
	layout_width="match_parent",
	layout_height=dimens.dx_45,
	layout_marginBottom="0dp",
	layout_gravity="top",
	background=Color.TitleBgColor,
--	{
--		ImageButton,
--		id="btn_cancle",
--		layout_width=dimens.dx_45,
--		layout_height=dimens.dx_45,
--		layout_gravity="left|center_vertical",
--		textColor="#ffffff",
--		background="#00000000",
--		textSize="17sp",
--		layout_marginLeft="10dp",
--		layout_marginTop="2dp",
--		background="drawable/btn_public_back_normal.png",
--		onClick=luajava.createProxy("android.view.View$OnClickListener",{
--			onClick=function(v)
--				finish()
--			end
--		})
--	},
	layout_title_image("left|center_vertical","btn_cancle","drawable/btn_public_back_normal.png"),
	{
		TextView,
		layout_width="wrap_content",
		layout_height="wrap_content",
		layout_gravity="center|center_vertical",
		textColor="#ffffff",
		textSize="17sp",
		text="完善资料"
	},
	layout_title_text("right|center_vertical","btn_title_right_submit","提交")
--	{
--		TextView,
--		id="btn_submit",
--		layout_width="wrap_content",
--		layout_height="wrap_content",
--		layout_gravity="right|center_vertical",
--		textColor="#ffffff",
--		textSize="17sp",
--		text="提交",
--		layout_marginRight="10dp",
--		layout_marginTop="2dp"
--	}
}

function loadLayout()
	layout={
			LinearLayout,
			layout_width="fill",
			layout_height="fill",
			background=Color.WhiteSmoke,
			orientation="vertical",
			layout_header,
			{
				RelativeLayout,
				id="upload_avatar",
				layout_width="150dp",
				layout_height="150dp",
				layout_marginTop="50dp",
				layout_marginBottom="50dp",
				layout_gravity="center_vertical|center_horizontal",
				{
					CircleImageView,
					id="account_avatar",
					layout_width="match_parent",
					layout_height="match_parent",
					scaleType="centerCrop",
					layout_gravity="center_vertical|center_horizontal",
					src="drawable/public_default_head"
				},
				{
					ImageView,
					layout_width="30dp",
					layout_height="30dp",
					layout_alignParentBottom=true,
					layout_alignParentRight=true,
					layout_marginBottom="5dp",
					layout_marginRight="10dp",
					scaleType="centerCrop",
					src="drawable/cashier_camera.png"
				}
			},
			cell_layout("item_nickname",{left_name="昵称",right_name="请输入昵称"}),
			cell_layout("item_sex",{left_name="性别",right_name=""}),
			cell_layout("item_birthday",{left_name="出生日期",right_name="1990-01-01"})
		}
	this.setContentView(loadlayout(layout,main))
end

function onController()
	local  accountTxt=this.getIntent().getStringExtra("parameters"); 
	account=cjson.decode(accountTxt)
	println(accountTxt)
	
	--上传头像
	attachOnClickListener(main.upload_avatar,function()
--		enterActivity("ImagePickerActivity",anim.in_from_right,anim.out_to_left,false)
		local parameter=cjson.encode({action=SMGlobal.UPLOAD_IMAGE_AVATAR_ACTION,upload_type=SMGlobal.UPLOAD_IMAGE_Avatar})
				OptionUpdateActivity("ImagePickerActivity",parameter)
	end)
	--返回
	local btn_title_cancle=main.btn_cancle
	setColorButton(btn_title_cancle,'#e8e8e8','#00000000',
	function()
		finish()
	end,0)
	
	
	local function submitRegisterUserInfo()
		
		local url=URL_Register(cjson.encode(account))
		AsyncHttps(url,function(body)
			println(body)
			local user=cjson.decode(body)
			if(user.result==0) then
				ToastText(user.desc)
				enterMainView(cjson.encode(user.info))
			else
				ToastText(user.desc)
			end
		end)
	end
	
	--提交按钮
	local btn_title_right_submit=main.btn_title_right_submit
	setColorButton(btn_title_right_submit,'#e8e8e8','#00000000',
	function()
		local nicknameText=main[getItemTextID("item_nickname")].getText()
		local sexText=main[getItemTextID("item_sex")].getText()
		if(nicknameText=="请输入昵称") then
			ToastText("请给自己输入个昵称")
			return
		elseif(sexText=="") then
			ToastText("请选择你的性别")
			return
		end
		
		account.nickname=nicknameText
		account.sex=Gender[sexText]
		account.birthday=main[getItemTextID("item_birthday")].getText()
		
		println(cjson.encode(account))
		submitRegisterUserInfo()
	end,0)
	--
	local activitys={}
	activitys["item_nickname"]="OptionNicknameActivity"
	activitys["item_sex"]="OptionSexActivity"
	activitys["item_birthday"]="OptionBirthdayActivity"
	
	local array_ids={
		"item_nickname",
		"item_sex",
		"item_birthday"
	}
	
	local function eventCallback(id)
	if(id=="item_sex") then
		local key=main["item_sexOfitem_right_name"].getText()
		if(value=="") then
			OptionUpdateActivity(activitys[id],cjson.encode({sex=2}))
		else
			OptionUpdateActivity(activitys[id],cjson.encode({sex=Gender[key]}))
		end
	else
		OptionUpdateActivity(activitys[id])
	end
			
	end
	
	
	for i=1,#array_ids,1 do
		local id=array_ids[i]
		local view=main[array_ids[i]]
		SetBackgoundColor({view=view,callback=eventCallback,id=id},0xec7063,0xffffffff)
	end
end

function onReceive( context,  intent)
	local action = intent.getAction()
	if(action==SMGlobal.UPLOAD_IMAGE_AVATAR_ACTION)then
		local bundle = intent.getExtras()
		local code=bundle.getInt("code")
		local path=bundle.getString("filename")
		local name=bundle.getString("name")
		println(name)
		CacheManager.getInstance().userInfo.avatar=name
		account.avatar=name
		println(name)
		if(code==200) then
			displayCircleImage("file://"..path,main["account_avatar"])
		end
	elseif (action==SMGlobal.ACTIONO_OPTION_UPDATE_USERINFO) then
		local item_id=intent.getStringExtra(BUNDLE_ITEM_KEY).."Ofitem_right_name"
		local item_value=intent.getStringExtra(BUNDLE_ITEM_VALUE)
		println(item_id)
		
		if(main[item_id]~=nil)then
			main[item_id].setText(tostring(item_value))
		end
	end
end

function registerReceiver()
	local filter =IntentFilter()
	filter.addAction(SMGlobal.UPLOAD_IMAGE_AVATAR_ACTION)
	filter.addAction(SMGlobal.ACTIONO_OPTION_UPDATE_USERINFO)
	this.registerReceiver(filter)
end

function onCreate(savedInstanceState)
	loadLayout()
	onController()
	registerReceiver()
end