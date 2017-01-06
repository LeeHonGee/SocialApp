require "init"

function finish()
	this.finish()
end

function onKeyDown(keyCode,event)
	if(keyCode == KeyEvent.KEYCODE_BACK) then
		finish()
	end
end
function onLayout()	

	local layout_title_header=require "layout_title_header".new()
	layout_title_header:create({title="关于",leftListenerFunc=function() finish()   end,rightListenerFunc=nil})
		cell_layout=function(title,content,cell_id)
		cell={
			FrameLayout,
			id=cell_id,
			layout_width="match_parent",
			layout_height="50dp",
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
	
	cell_binding=cell_layout("检测更新软件","V1.23","btn_updater")
	cell_evaluate=cell_layout("用户协议与声明","","btn_userprotocol")
	cell_aboutus=cell_layout("企业邮箱","leehongee@126.com","btn_email")
	
	local html ="<span color='#595959'>深圳市盛享时代网络科技有线公司<br/>" ..
        		"<font color='#595959'>CopyRight @ 2015 ShareMob.<br/></font>" ..
        		"<font color='#595959'>All Rights Reserved.</font></span>"
	
	layout_content={
		LinearLayout,
		layout_gravity="center|top",
		layout_width="match_parent",
		layout_height="match_parent",
		background=Color.WhiteSmoke,
		orientation="vertical",
		layout_title_header.layout,
		{
			ImageView,
			layout_width="match_parent",
			layout_height="100dp",
			layout_marginBottom="10dp",
			layout_marginTop="20dp",
			gravity="center_vertical|center",
			src="drawable/ic_launcher"
		},
		{
			TextView,
			id="tv_copyright",
			layout_width="fill_parent",
			layout_height="wrap_content",
			text="在这里,我们相遇相爱了❤️",
			layout_gravity="center|bottom",
			gravity="center",
			textColor=Color.slategrey,
			layout_marginTop="10dp",
			layout_marginBotton="20dp",
			textSize="14sp",
			background="@null"
		},
		{
			LinearLayout,
			layout_width="match_parent",
			layout_height="wrap_content",
			background=Color.WhiteSmoke,
			layout_marginTop="20dp",
			layout_marginBottom="10dp",
			orientation="vertical",
			cell_binding,
			line
		},
		cell_evaluate,
		cell_aboutus,
		line
	}
	
	layout={
		FrameLayout,
		layout_width="match_parent",
		layout_height="match_parent",
		layout_content,
		{
			TextView,
			id="tv_copyright",
			layout_width="fill_parent",
			layout_height="wrap_content",
			text=html,
			layout_gravity="center|bottom",
			gravity="center_horizontal",
			textColor=Color.Back,
			layout_marginBottom="15dp",
			textSize="12sp",
			background="@null"
		}
	}
	
	
	main={}
	this.setContentView(loadlayout(layout,main))
	layout_title_header:initComponent(main.iBtn_title_back,main.iBtn_title_right)
	
	Html=luajava.bindClass("android.text.Html")
	
	local content=Html.fromHtml(html)
	
	local tv_copyright=main.tv_copyright
	tv_copyright.setText(content)
	
	SetButtonColor(main.btn_updater,0xec7063,0xffffffff,
	function()
		local url=URL_APP(2002,cjson.encode({version="1.02"}))
		local body,cookie,code,headers=http.get(url)
			println(body)
			local json=cjson.decode(body)
			if(json.desc~=nil)then
				ToastText(json.desc)
			end
--		local updater=cjson.decode(body)
--		if(updater.result==0) then
--		
--		elseif(updater.result==1) then
--			ToastText("当前已经是最新版本!")
--		end
	end)
	
	SetButtonColor(main.btn_userprotocol,0xec7063,0xffffffff,
	function()  	
		println("协议")
		enterActivity("UserProtocolActivity",anim.in_from_right,anim.out_to_left,false)
	end)
	
	SetButtonColor(main.btn_email,0xec7063,0xffffffff,
	function()  
		println("企业邮箱")
		sendEmail()
	end)
	
end

function onCreate(savedInstanceState)
	onLayout()
end