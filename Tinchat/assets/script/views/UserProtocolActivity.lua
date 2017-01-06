require "init"
local WebView=luajava.bindClass("android.webkit.WebView")
local help_url="file:///android_asset/user_protocol.html"

function finish()
	this.finish() 
	this.overridePendingTransition(R.anim.in_from_left,R.anim.out_to_right)
end

function onKeyDown(keyCode,event)
	if(keyCode == KeyEvent.KEYCODE_BACK) then
		finish()
	end
end


local function loadLayout()

	layout_header={
			FrameLayout,
			id="activity_header",
			layout_width="match_parent",
			layout_height=dimens.dx_45,
			layout_gravity="top",
			background=Color.TitleBgColor,
			{
				TextView,
				id="tv_title_content",
				layout_width="wrap_content",
				layout_height="wrap_content",
				layout_gravity="center",
				text="协议",
				textColor=Color.TitleTextColor,
				textSize="16sp"
			},
			{
				ImageButton,
				id="iBtn_title_back",
				layout_width=dimens.dx_45,
				layout_height=dimens.dx_45,
				layout_gravity="left|center_vertical",
				layout_marginLeft="5dp",
				background="drawable/btn_public_back_normal.png",
				onClick=luajava.createProxy("android.view.View$OnClickListener",{
				onClick=function(v)
					finish()
				end})
			}
		}
	
	layout={
		LinearLayout,
		layout_width="fill",
		layout_height="fill",
		background=Color.WhiteSmoke,
		orientation="vertical",
		layout_header,
		{
			WebView,
			id="webview_protocol",
			layout_width="match_parent",
			layout_height="match_parent",
			isScrollContainer=false,
			persistentDrawingCache="none",
			layout_weight=1
		}
	}

	main={}
	this.setContentView(loadlayout(layout,main))

	local webView=main.webview_protocol
	webView.setVerticalScrollBarEnabled(false)
	webView.loadUrl(help_url)
end

function onCreate(savedInstanceState)
	loadLayout()

end