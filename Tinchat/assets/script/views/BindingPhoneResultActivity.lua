require "init"
this=activity

function finish()
	this.finish() 
end

function onKeyDown(keyCode,event)
	if(keyCode == KeyEvent.KEYCODE_BACK) then
		finish()
	end
end

function layout()
	
		layout_header={
		FrameLayout,
		id="activity_header",
		layout_width="match_parent",
		layout_height=dimens.dx_45,
		layout_marginBottom="15dp",
		layout_gravity="top",
		background=Color.TitleBgColor,
		{
			Button,
			id="btn_cancle",
			layout_width="wrap_content",
			layout_height="wrap_content",
			layout_gravity="left|center_vertical",
			textColor="#ffffff",
			background="#00000000",
			textSize="17sp",
			layout_marginLeft="10dp",
			layout_marginTop="2dp",
			text='取消',
			onClick=luajava.createProxy("android.view.View$OnClickListener",{
				onClick=function(v)
					finish()
				end
			})
		},
		{
			TextView,
			layout_width="wrap_content",
			layout_height="wrap_content",
			layout_gravity="center|center_vertical",
			textColor="#ffffff",
			textSize="17sp",
			text="手机认证绑定"
		},
		{
			Button,
			id="btn_next",
			layout_width="wrap_content",
			layout_height="wrap_content",
			layout_gravity="right|center_vertical",
			textColor="#ffffff",
			textSize="17sp",
			text="编辑",
			background="#00000000",
			layout_marginRight="10dp",
			layout_marginTop="2dp"
		}
	}
	
	layout={
		LinearLayout,
		layout_width="match_parent",
		layout_height="match_parent",
		background=Color.WhiteSmoke,
		orientation="vertical",
		layout_header,
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
				textSize="17sp",
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
				TextView,
				id='et_account_phone',
				layout_width="fill_parent",
				layout_height="50dp",
				textSize="17sp",
				background=Color.White,
				textColor="#606060",
				paddingRight="10dp",
				paddingLeft="17dp",
				text=UserInfo.phone,
				gravity="center_vertical"
			},
			line
			
		}
	}
	main={}
	this.setContentView(loadlayout(layout,main))

	attachOnClickListener(main.btn_next,
	function()  
		println("next")
		enterActivity("BindingPhoneActivity",anim.in_from_right,anim.out_to_left,false)
	end)
	
end

function onCreate(savedInstanceState)
	layout()

end