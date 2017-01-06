require "init"

local main={}
local text_tip="<h1>诚信中心</h1>作为严肃的婚恋网站,诚信级别越高,对方就会对您更多一份信任,更容易对您敞开心扉,提高交友成功率,同时:"..
			   "<br/><br/>通过认证的会员将获得推荐,获得更高的关注度!<br/><br/>星级越高的用户,将在搜索结果中排名靠前,相同条件下优先得到匹配."..
			   "<br/><br/>您当前的诚信星级:<b><font color='red'> %s</font></b>"
			   
local item_photobook_desc="上传3张照片\n可以获得1颗星,已获%d颗星"
local item_identity_card_desc="身份证认证\n可以获得1颗星,已获%d颗星"
local item_video_desc="视频认证\n可以获得1颗星,已获%d颗星"
local item_telephone_desc="手机号码认证\n可以获得1颗星,已获%d颗星"
local item_info_desc="资料完善度90%s\n可以获得1颗星,已获%d颗星"

function finish()
	this.finish()
end

function onKeyDown(keyCode,event)
	if(keyCode == KeyEvent.KEYCODE_BACK) then
		finish()
	end
end

local 	layout_header={
	FrameLayout,
	id="activity_header",
	layout_width="match_parent",
	layout_height=dimens.dx_45,
	layout_gravity="top",
	background=Color.TitleBgColor,
	layout_title_image("left|center_vertical","btn_cancle","drawable/btn_public_back_normal.png"),
	{
		TextView,
		id="tv_title_content",
		layout_width="wrap_content",
		layout_height="wrap_content",
		layout_gravity="center|center_vertical",
		textStyle="bold",
		textColor=Color.TitleTextColor,
		textSize="17sp",
		text="诚信认证"
	}
}

local layout_title=function(cell_id,txt)
	local layout={
		LinearLayout,
		layout_width="match_parent",
		layout_height="wrap",
		background=Color.White,
		layout_margin="10dp",
		orientation="vertical",
		{
			TextView,
			id=cell_id,
			padding="15dp",
			layout_width="wrap_content",
			layout_height="wrap_content",
			layout_gravity="left|center_vertical",
			textColor=Color.Back,
			textSize="14sp",
			text=txt
		}
	}
	return layout
end

local layout_item=function(cell_id,txt,btn_text)
	local layout={
		LinearLayout,
		layout_width="match_parent",
		layout_height="wrap",
		background=Color.White,
		layout_margin="10dp",
		orientation="vertical",
		{
			TextView,
			id="tv_"..cell_id,
			padding="15dp",
			layout_width="wrap_content",
			layout_height="wrap_content",
			layout_gravity="left|center_vertical",
			textColor=Color.Back,
			textSize="14sp",
			text=txt
		},
		{
			ColorButton,
			id=cell_id,
			layout_width="240dp",
			layout_height="40dp",
			layout_gravity="center|bottom",
			layout_marginLeft="50dp",
			layout_marginRight="50dp",
			layout_marginTop="10dp",
			layout_marginBottom="20dp",
			gravity="center",
			soundEffectsEnabled=true,
			text=btn_text,
			textColor=Color.White,
			textSize="15sp"
		}
		
		}

	return layout
end

function onLayout()
	layout={
		LinearLayout,
		layout_width="match_parent",
		layout_height="match_parent",
		background=Color.WhiteSmoke,
		orientation="vertical",
		layout_header,
		{
			LocalHoveringScrollView,
			layout_width="match_parent",
			layout_height="match_parent",
			{
				LinearLayout,
				layout_width="match_parent",
				layout_height="match_parent",
				orientation="vertical",
--				<b></b>
				{
						ImageView,
						id="iBtn_login_wechat",
						layout_width="match_parent",
						scaleType="fitCenter",
						layout_height="120dp",
						background="drawable/credit_verify_banner"
				},
				layout_title("layout_title",""),
				layout_item("item_info","","去补充资料"),
				layout_item("item_photobook","","马上上传"),
				layout_item("item_telephone","","现在去认证"),
				layout_item("item_video","","现在去认证"),
				layout_item("item_identity_card","","立即去认证")
			 }
		 }
	}

	this.setContentView(loadlayout(layout,main))
end

function onResume()
	local tip_star=""
 	if(UserInfo.photobooks.size()>=3)then
		main["tv_item_photobook"].setText(string.format(item_photobook_desc,1))
		tip_star=tip_star.."⭐"
	else
		main["tv_item_photobook"].setText(string.format(item_photobook_desc,0))
	end
	
	if(UserInfo.phone~=nil)then
		main["tv_item_telephone"].setText(string.format(item_telephone_desc,1))
		tip_star=tip_star.."⭐"
		main["item_telephone"].setText("编辑手机号码认证")
	else
		main["tv_item_telephone"].setText(string.format(item_telephone_desc,0))
		main["item_telephone"].setText("现在去认证")
	end
	
	if(UserInfo.video==nil)then
		main["tv_item_video"].setText(string.format(item_video_desc,0))
		main["item_video"].setText("现在去认证")
	else
		main["tv_item_video"].setText(string.format(item_video_desc,1))
		tip_star=tip_star.."⭐"
		main["item_video"].setText("编辑视频认证")
	end
	
	if(UserInfo.IDCard==nil)then
		main["tv_item_identity_card"].setText(string.format(item_identity_card_desc,0))
		main["item_identity_card"].setText("立即去认证")
	else
		main["tv_item_identity_card"].setText(string.format(item_identity_card_desc,1))
		tip_star=tip_star.."⭐"
		main["item_identity_card"].setText("编辑身份认证")
	end
	
	main["tv_item_info"].setText(string.format(item_info_desc,"%",1))
	tip_star=tip_star.."⭐"
	
		
	local text=Html.fromHtml(string.format(text_tip,tip_star))
	main["layout_title"].setText(text)
end

function onController()
	--返回按钮
	local btn_title_cancle=main.btn_cancle
	setColorButton(btn_title_cancle,'#e8e8e8','#00000000',
	function()
		finish()
	end,0)
	
	setColorButton(main["item_info"],'#a9cd94','#95cd73',
	function()
		enterActivity("AccountInfoActivity",anim.in_from_right,anim.out_to_left,false)
	end,0)
	
	setColorButton(main["item_photobook"],'#a9cd94','#95cd73',
	function()
		enterActivity("PhotobookActivity",anim.in_from_right,anim.out_to_left,false)
	end,0)
	
		setColorButton(main["item_telephone"],'#a9cd94','#95cd73',
	function()
		if(UserInfo.realname~="")then
			enterActivity("BindingPhoneResultActivity",anim.in_from_right,anim.out_to_left,false)
		else
			enterActivity("BindingPhoneActivity",anim.in_from_right,anim.out_to_left,false)
		end
	end,0)
	
	setColorButton(main["item_video"],'#a9cd94','#95cd73',
	function()
		enterActivity("OptionSignatureVideoActivity",anim.in_from_right,anim.out_to_left,false)
	end,0)
	
	setColorButton(main["item_identity_card"],'#a9cd94','#95cd73',
	function()
		if(UserInfo.realname~="")then
			enterActivity("IDCardResultActivity",anim.in_from_right,anim.out_to_left,false)
		else
			enterActivity("IdentityCardActivity",anim.in_from_right,anim.out_to_left,false)
		end
	end,0)
end

function onCreate(savedInstanceState)
	onLayout()
	onController()
end