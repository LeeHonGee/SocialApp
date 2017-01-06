
require "init"
Paint=luajava.bindClass("android.graphics.Paint")
BannerSlideView=luajava.bindClass("com.sharemob.tinchat.component.BannerSlideView")
Message=luajava.bindClass("android.os.Message")

local main={}

function finish()
	this.finish() 
end
aa=0
function onKeyDown(keyCode,event)
	if(keyCode == KeyEvent.KEYCODE_BACK) then
		finish()
	end
end

local cell_layout=function(cell_id,item)
		
		local title_price_visible=function()
			if item.price=="" then
				return LocalView.GONE
			else
				return LocalView.VISIBLE
			end
		end
	
		cell={
			FrameLayout,
			layout_width="match_parent",
			layout_height="55dp",
			background=Color.White,
			orientation="vertical",
			{
				RelativeLayout,
				layout_width="match_parent",
				layout_height="match_parent",
				layout_margin="15dp",
				layout_gravity="center_vertical|fill_horizontal",
				gravity="center_vertical",
				{
					TextView,
					id="tv_buy_member_item_name",
					layout_width="wrap_content",
					layout_height="wrap_content",
					layout_centerVertical="true",
					layout_gravity="center_vertical",
					gravity="right",
					text=item.month,
					textColor=Color.slategrey,
					textSize="16sp"
				},
				{
					ImageView,
					layout_width="1dp",
					layout_height="15dp",
					layout_centerVertical="true",
					layout_gravity="center_vertical|left",
					layout_marginLeft="8dp",
					visibility=title_price_visible(),
					layout_toRightOf="tv_buy_member_item_name",
					background=Color.slategray 
				},
				{
					TextView,
					id="item_price_"..cell_id,
					layout_width="wrap_content",
					layout_height="wrap_content",
					layout_centerVertical="true",
					layout_gravity="center_vertical|right",
					layout_marginLeft="25dp",
					layout_toRightOf="tv_buy_member_item_name",
					text=item.price,
					textColor=Color.textgrey,
					textSize="16sp",
					textStyle="bold"
				},
				{
					TextView,
					id=cell_id.."_item_favorable",
					layout_width="wrap_content",
					layout_height="wrap_content",
					layout_centerVertical="true",
					layout_gravity="center_vertical",
					layout_marginLeft="5dp",
					layout_toRightOf="item_price_"..cell_id,
					background="#ff9287",
					gravity="center_vertical",
					visibility=title_price_visible(),
					text=item.youhui,
					textSize="10sp",
					padding="2dp",
					textColor=Color.White
				}
			},
			{
			LinearLayout,
			layout_width="wrap_content",
			layout_height="wrap_content",
			layout_marginRight="10dp",
			layout_gravity="right|center_vertical",
			orientation="horizontal",
			{
				TextView,
				id="tv_buy_member_item_real_price",
				layout_width="wrap_content",
				layout_height="wrap_content",
				layout_gravity="center_vertical|right",
				layout_marginRight="15dp",
				text=item.real_price,
				textSize="17sp",
				textColor="#01becb",
				textColor="#01becb",
				textStyle="bold"
			},
			{
				ColorButton,
				id="buy_member_"..cell_id,
				layout_width="60dp",
				layout_height="30dp",
				background="#95cd73",
				text="开通",
				paddingTop="7dp",
				paddingBottom="7dp",
				paddingLeft="15dp",
				paddingRight="15dp",
				textSize="13sp",
				checked="false" ,
				textColor=Color.White
			}
			}
		}
		return cell
end

function layout()
	local layout_title_header=require "layout_title_header".new()
	layout_title_header:create({title="升级会员",leftListenerFunc=function() finish()   end,rightListenerFunc=nil})
	
	layout={
		LinearLayout,
		layout_width="fill",
		layout_height="fill",
		orientation="vertical",
		background=Color.WhiteSmoke,
		layout_title_header.layout,
		{
			BannerSlideView,id="member_banner_ad",layout_width="fill_parent",layout_height="120dp"
		},
		{
			LinearLayout,
			id="ll_list_buyitem",
			layout_width="match_parent",
			layout_height="wrap_content",
			background=Color.WhiteSmoke,
			orientation="vertical"
		},
		{
			LinearLayout,layout_width="match_parent",layout_height="wrap_content",orientation="vertical",layout_margin="20dp",
			{
				LinearLayout,
				layout_width="match_parent",
				layout_height="wrap_content",
				orientation="horizontal",
				{
					ImageView,
					layout_width="15dp",
					layout_height="15dp",
					layout_centerVertical="true",
					layout_gravity="center_vertical|left",
					layout_marginRight="5dp",
					background="drawable/safety_icon.png"
				},
				{
					TextView,id="safety_icon",
					layout_width="match_parent",
					layout_height="wrap_content",
					layout_gravity="center_vertical|left",
					textColor=Color.slategray,
					textSize="13sp",
					textStyle="bold",
					text="温馨安全提示:"
				}
		},
		{
			TextView,
			layout_width="match_parent",
			layout_height="wrap_content",
			layout_marginTop="10dp",
			layout_gravity="center_vertical|left",
			text="在以下情况下请不要轻易透露您的联系方式(如：电话、手机号码、邮箱、即时通讯、通讯地址等)\n\n1.在未充分了解对方前,请不要透露您的隐私. \n\n2.当对方无相片或资料填写不完整时,请不要亲密的透露个人信息.\n\n3.私下相亲过程中,如遇到对方提及借款、投资或索要礼物等行为，请务必提高警惕,谨防受骗,并及时举报.",
            textColor=Color.slategray,
            textSize="13sp",
            textStyle="bold"
		}
		}
	}
	this.setContentView(loadlayout(layout,main))
	layout_title_header:initComponent(main.iBtn_title_back,main.iBtn_title_right)
end


function controller()

	AsyncHttp(URL_Member_BannerAD(),function(body)
				local response=cjson.decode(body)  
				member_banner_ad=main.member_banner_ad
				member_banner_ad.loadBannerSlide(this.loadingListener,cjson.encode(response.banners))
				
				local shopping=response.shopping
				local ll_list_buyitem=main.ll_list_buyitem
				for i=1,#shopping,1 do
					local item_main={}
					local entity=shopping[i]
					local cell_item=cell_layout(entity.id,{
						month=entity.month,
						price=entity.price,
						youhui=entity.youhui,
						real_price=entity.real_price
					})
					ll_list_buyitem.addView(loadlayout(cell_item,item_main))
					ll_list_buyitem.addView(loadlayout(line,item_main))
					ID="buy_member_"..entity.id
					tv_price="item_price_"..entity.id
					item_main[tv_price].getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG)
					setColorButton(item_main[ID],'#a8d28f','#6eb046',
					function() 
						println("购买") 
					end,50)
				end
	end)
end


function onCreate(savedInstanceState)
	layout()
	controller()
end