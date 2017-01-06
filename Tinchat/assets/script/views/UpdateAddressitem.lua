require "init"

local AddressItemAdapter=luajava.bindClass("com.sharemob.tinchat.lib.adapter.address.AddressItemAdapter")
local adapter
local recycler_addresses
local addressItem={}

local main={}

function finish()
	this.finish() 
	this.overridePendingTransition(R.anim.in_from_left,R.anim.out_to_right);
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
		textStyle="bold",
		textColor=Color.TitleTextColor,
		textSize="17sp",
		text="收货地址"
	},
	layout_title_text("right|center_vertical","btn_title_right_next","更新")
}

function onLoadlayout()
	layout={
		FrameLayout,
		layout_width="match_parent",
		layout_height="match_parent",
		background=Color.WhiteSmoke,
		orientation="vertical",
		layout_header,
		{
			LinearLayout,
			layout_width="fill_parent",
			layout_height="wrap",
			gravity="fill_vertical",
			orientation="vertical",
			layout_marginTop="55dp",
			background=Color.White,
			cellitem_editText_layout("收 货 人:","","receiver","textPersonName"),line,
			cellitem_editText_layout("手机号码:","","telephone","phone"),line,
			cellitem_editText_layout("省 市 区:","请选择","address","textPostalAddress"),line,
			cellitem_editText_layout("详细地址:","","detailedness","textPostalAddress"),line
		}
	}	
	this.setContentView(loadlayout(layout,main))		
end

local function registerOptionUpateUserInfo()
	local filter =IntentFilter()
	filter.addAction(SMGlobal.ACTIONO_OPTION_UPDATE_USERINFO)
	this.registerReceiver(filter)
end

function onController()
	--返回按钮
	local btn_title_cancle=main.btn_cancle
	setColorButton(btn_title_cancle,'#e8e8e8','#00000000',
	function()
		finish()
	end,0)
	
	local btn_title_right_next=main.btn_title_right_next
	setColorButton(btn_title_right_next,'#e8e8e8','#00000000',
	function()
		local receiver_val=main["receiver"].getText().toString()
		local telephone_val=main["telephone"].getText().toString()
		local address_val=main["address"].getText().toString()
		local detailedness_val=main["detailedness"].getText().toString()
		if(receiver_val=="")then
			ToastText("为填写姓名")
		elseif(telephone_val=="")then
			ToastText("为填写手机号码")
		elseif(address_val=="")then
			ToastText("为填写省市区")
		elseif(detailedness_val=="")then
			ToastText("为填写详细地址")
		else
			local data={
--				uid=UserInfo.uid,
				receiver=receiver_val,
				telephone=telephone_val,
				address=address_val,
				address=address_val,
				id=addressItem.id,
				detailedness=detailedness_val
			}
			local url=URL_User(10039,cjson.encode(data))
			println(url)
			AsyncHttp(url,function(body)
					finish()
			end)
		end
	end,0)
	
	attachOnTouchListener(main["address"],function()
		OptionFellowTownsman()
	end)
	--获取单条数据
	local url=URL_User(10040,cjson.encode({id=json_param.id}))
	println(url)
	AsyncHttp(url,function(body)
		local t_info=cjson.decode(body)
		main["receiver"].setText(t_info.receiver)
		main["telephone"].setText(t_info.telephone)
		main["address"].setText(t_info.address)
		main["detailedness"].setText(t_info.detailedness)
		addressItem["id"]=t_info.id
	end)
end


function onReceive(context,intent)
	local action = intent.getAction()
	println(action)
	if (action==SMGlobal.ACTIONO_OPTION_UPDATE_USERINFO) then
		local id=intent.getStringExtra(BUNDLE_ITEM_KEY)
		local item_id=id.."Ofitem_right_name"
		local item_value=intent.getStringExtra(BUNDLE_ITEM_VALUE)
		local value= string.gsub(item_value,"-","")
		main["address"].setText(tostring(value))
	end
end

function onCreate(savedInstanceState)
	onLoadlayout()
	onController()
	registerOptionUpateUserInfo()
end