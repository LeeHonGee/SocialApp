require "init"
import "android.content.*"

main={}

function finish()
	this.finish()
end

function onKeyDown(keyCode,event)
	if(keyCode == KeyEvent.KEYCODE_BACK) then
		finish()
	end
end

function loadLayout()
	local layout_title_header=require "layout_title_header".new()
	layout_title_header:create({title="请选择举报原因",leftListenerFunc=function() finish()   end,rightListenerFunc=nil})
	
	cell_layout=function(title,cell_id)
	cell={
		LinearLayout,
		layout_width="fill",
		layout_height="wrap",
		orientation="vertical",
		background=Color.White,
		{
			FrameLayout,
			id=cell_id.."_main",
			layout_width="match_parent",
			layout_height="match_parent",
			{
				TextView,
				id=cell_id.."_title",
				layout_width="wrap_content",
				layout_height="wrap_content",
				layout_gravity="left|center_vertical",
				text=title,
				layout_marginLeft="15dp",
				textColor=Color.Back,
				textSize="17sp"
			},
			{
				CheckBox,
				id=cell_id,
				layout_width="35dp",
				layout_height="35dp",
				layout_gravity="center_vertical|right",
				layout_marginRight="15dp",
				layout_marginTop="7dp",
				layout_marginBottom="7dp",
				textColor=Color.slategrey,
				background=Color.transparent,
				button="@null",
				checked="false",
				textSize="16sp"
			}
		},line
	}
			return cell
	end
	
	layout={
		LinearLayout,
		layout_width="fill",
		layout_height="fill",
		background=Color.WhiteSmoke,
		orientation="vertical",
		layout_title_header.layout,
		cell_layout("色情低俗","SeQingDiSu"),
		cell_layout("政治敏感","ZhengZhiMinGan"),
		cell_layout("违法暴力","WeiFanBaoli"),
		cell_layout("伪造个人资料","WeiZaoZiliao"),
		cell_layout("广告骚扰","GuangGaoShaorao"),
		cell_layout("发布虚假兼职信息骗钱","QiZhaXinXi"),
		cell_layout("其他","Other"),
		{
			ColorButton,
			id="btn_submit",
			layout_width="240dp",
			layout_height="40dp",
			layout_gravity="center|bottom",
			layout_marginTop="15dp",
			gravity="center",
			background="#ec7063",
			soundEffectsEnabled=true,
			text="确定",
			textColor="#ffffffff",
			textSize="15sp"
		}
	}
	
	
	this.setContentView(loadlayout(layout,main))
	layout_title_header:initComponent(main.iBtn_title_back,main.iBtn_title_right)
	
	local ckeck_btn={}
	local reportItems={"SeQingDiSu","ZhengZhiMinGan","WeiFanBaoli","WeiZaoZiliao","GuangGaoShaorao","QiZhaXinXi","Other"}
	
	local function setCheckedItems(k)
		for i=1,#reportItems,1 do
			local chkBtn=main[reportItems[i]]
			if(i~=k)then
				chkBtn.setChecked(false)
			end
		end
	end
	
	for i=1,#reportItems,1 do
		local chkBtn=main[reportItems[i]]
		chkBtn.setButtonDrawable(R.drawable.style_btn_check)
		attachOnClickListener(chkBtn,function()
			setCheckedItems(i)
		end)
	end

	setColorButton(main.btn_submit,'#ec7063','#fc5652',
	function() 
		for i=1,#reportItems,1 do
			local chkBtn=main[reportItems[i]]
			if(chkBtn.isChecked()) then
				title=tostring(main[reportItems[i].."_title"].getText())
				local url=URL_User(10053,cjson.encode({uid=json_param.uid,reason=reportItems[i]}))
				println(url)
				AsyncHttp(url,function(body)
					local json=cjson.decode(body)
					if(json.desc~=nil)then
						ToastText(json.desc)
						finish()
					end
				end)
			end
		end
	end,50)
	
end

function onCreate(savedInstanceState)
	loadLayout()

end