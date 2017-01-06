require "init"

function finish()
	this.finish()
end

function onKeyDown(keyCode,event)
	if(keyCode == KeyEvent.KEYCODE_BACK) then
		finish()
	end
end

local function setConfig(id,event)
	println(id.."--"..event)
	if("btn_voice"==id) then
		
	elseif("btn_vibrate"==id) then
	
	elseif("btn_no_trouble"==id) then
	
	elseif("btn_stranger"==id) then
	
	end
end

local function onLayout()
	local layout_title_header=require "layout_title_header".new()
	layout_title_header:create({title="隐私设置",leftListenerFunc=function() finish()   end,rightListenerFunc=nil})


	cell_layout=function(title,cell_id)
		cell={
			FrameLayout,
			id="activity_header",
			layout_width="match_parent",
			layout_height=dimens.dx_45,
			layout_gravity="top",
			background="#ffffff",
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
				SlideSwitch,
				id=cell_id,
				layout_width="50dp",
				layout_height="30dp",
				isOpen=true,
				shape="circle",
				themeColor="#ff8733",
				layout_gravity="right|center_vertical",
				layout_marginRight="15dp",
				background="drawable/btn_public_back_normal.png",
				SlideListener=luajava.createProxy("com.sharemob.tinchat.component.SlideSwitch$SlideListener",{
					open=function()
						setConfig(cell_id,"open")
					end;
					close=function()
						setConfig(cell_id,"close")
					end
				})
			}
		}
		return cell		
	end

	cell_voice=cell_layout("声音","btn_voice")
	cell_vibrate=cell_layout("震动","btn_vibrate")
	cell_no_trouble=cell_layout("免打扰时段[00:00-5:00]","btn_no_trouble")
	cell_stranger=cell_layout("允许陌生人后台来电","btn_stranger")
	
	layout={
		LinearLayout,
		layout_width="match_parent",
		layout_height="match_parent",
		background=Color.WhiteSmoke,
		orientation="vertical",
		layout_title_header.layout,
		{
			LinearLayout,
			layout_width="fill_parent",
			layout_height="fill_parent",
			gravity="fill_vertical",
			orientation="vertical",
			cell_voice,
			cell_vibrate,
			line,
			{
				TextView,
				layout_width="wrap_content",
				layout_height="wrap_content",
				text="在免打扰时段内接收的新消息不会进行声音和震动提醒",
				textColor=Color.textgrey,
				layout_margin="15dp",
				layout_gravity="left",
				textSize="13sp"
			},
			cell_no_trouble,
			line,
			{
				TextView,
				layout_width="wrap_content",
				layout_height="wrap_content",
				text="开启后,陌生人可以在关闭软件时呼叫你",
				textColor=Color.textgrey,
				layout_margin="15dp",
				layout_gravity="left",
				textSize="13sp"
			},
			cell_stranger,
			line
		}

	}

	main={}
	this.setContentView(loadlayout(layout,main))
	layout_title_header:initComponent(main.iBtn_title_back,main.iBtn_title_right)
end

function onCreate(savedInstanceState)
	onLayout()
end