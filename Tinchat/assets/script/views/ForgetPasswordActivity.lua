require "init"

function finish()
	this.finish() 
end

function onKeyDown(keyCode,event)
	if(keyCode == KeyEvent.KEYCODE_BACK) then
		finish()
	end
end

local function layout()
	local layout_title_header=require "layout_title_header".new()
	layout_title_header:create({title="忘记密码",leftListenerFunc=function() finish()   end,rightListenerFunc=nil})
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
			line,
			{
				TextView,
				layout_width="fill_parent",
				layout_height="50dp",
				textSize="16sp",
				background=Color.White,
				text="中国    +86",
				textColorHint=Color.textgrey,
				textColor=Color.Back,
				paddingRight="10dp",
				paddingLeft="15dp",
				gravity="center_vertical"
			},
			line,
			{
				ClearEditText,
				id='et_account_phone',
				layout_width="fill_parent",
				layout_height="50dp",
				textSize="16sp",
				background=Color.White,
				textColorHint=Color.textgrey,
				textColor=Color.Back,
				paddingRight="10dp",
				paddingLeft="15dp",
				text="",
				inputType="phone",
				hint="请输入你的手机号",
				gravity="center_vertical",
				digits="1234567890",
				singleLine=true,
				focusable=true
			},
			line,
			{
				ColorButton,
				id="btn_next",
				layout_width="240dp",
				layout_height="40dp",
				layout_gravity="center|bottom",
				layout_marginLeft="50dp",
				layout_marginRight="50dp",
				layout_marginTop="50dp",
				gravity="center",
				soundEffectsEnabled="true",
				text="下一步",
				textColor="#ffffffff",
				textSize="17sp"
			}
			
		}
	}
	main={}
	this.setContentView(loadlayout(layout,main))
	layout_title_header:initComponent(main.iBtn_title_back,main.iBtn_title_right)


	local account_phone=main.et_account_phone
	account_phone.antoRequestFocus()
	account_phone.setInputFilter(11)
	
	local btn_next=main.btn_next
	btn_next.setFillet(true)
	btn_next.setRadius(50)
	btn_next.setBackColor("#95cd73")
	btn_next.setBackColorSelected("#a9cd94")
	
	attachOnClickListener(btn_next,
	function()   
		println("asdasd")
	end)
end

function onCreate(savedInstanceState)
	layout()

end