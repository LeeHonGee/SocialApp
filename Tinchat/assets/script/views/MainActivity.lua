require "init"

import "android.support.v4.app.Fragment"
import "android.support.v4.app.FragmentActivity"
import "android.support.v4.app.FragmentManager"
import "android.support.v4.app.FragmentTransaction"
--FragmentFriend=luajava.bindClass("com.sharemob.tinchat.modules.home.FragmentFriend")
FragmentMessage=luajava.bindClass("com.sharemob.tinchat.modules.home.FragmentMessage")
ArrayList=luajava.bindClass("java.util.ArrayList")
android_R=luajava.bindClass("android.R")
LuaFragment=luajava.bindClass("com.sharemob.tinchat.component.LuaFragment")
--BaseActivityGroup=luajava.bindClass("com.sharemob.tinchat.base.BaseActivityGroup")


FragmentCenter=luajava.new(LuaFragment,{

onCreate=function(savedInstanceState)
	pritnln("构造fragmenCenter")
end;
onCreateView=function(inflater,container,savedInstanceState) 
		layout_header=function(title)
			layout=	{
				FrameLayout,
				id="activity_header",
				layout_width="match_parent",
				layout_height=dimens.dx_45,
				layout_gravity="top",
				background="#fb5552",
				{
					TextView,
					id="tv_title_content",
					layout_width="wrap_content",
					layout_height="wrap_content",
					layout_gravity="center",
					text=title,
					textColor="#ffffff",
					textSize="16sp"
				}
			}
			return layout
		end

	layout_fragmentCenter=function()
		layout={
			LinearLayout,
			layout_width="match_parent",
			layout_height="match_parent",
			background=Color.WhiteSmoke,
			layout_header("个人中心")
		}
	end
	
	local view=loadlayout(layout_fragmentCenter)
	pritnln("构造fragmenCenter")
	return view
end;
onActivityCreated=function(savedInstanceState) 
	pritnln("构造fragmenCenter")

end;
onSaveInstanceState=function(outState) 
pritnln("构造fragmenCenter")
end;
getId=function()
	return 9999
end

})


function finish()

	this.finish()
end

function onKeyDown(keyCode,event)
	if(keyCode == KeyEvent.KEYCODE_BACK) then
		finish()
	end
end


function onlayout()

	layout={
		LinearLayout,
		id="simple_fragment",
		layout_width="match_parent",
		layout_height="match_parent",
		background=Color.WhiteSmoke,
		orientation="vertical"
	}

	main={}
	this.setContentView(loadlayout(layout,main))
end

--fragmentCenter={}

function onCreate(bundle)
	onlayout()
	local fragmentCenter=FragmentMessage()
	
	local id=fragmentCenter.getId()
	println("id:"..id)

end

--onlayout()