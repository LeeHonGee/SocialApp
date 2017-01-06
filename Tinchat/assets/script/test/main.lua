
--require "import"
--
--import "android.graphics.drawable.*"
--import "android.widget.*"
--import "android.view.*"
require "init"
import "http"

local println=PrintStream.println

function finish()
	println("finishss")
--	activity.finish()
end

function onResume()
	println("返回程序")
end
function onDestroy()
	println("程序已退出")
end


function onKeyDown(code,event)
    println("onKeyDown")
end

local layout_title_header=require "layout_title_header".new()
layout_title_header:create({title="asdasd",leftListenerFunc=function()  finish()  end,rightListenerFunc=nil})


--local layout_title_header=require "layout_title_header".new()
--layout_title_header:create({title="asdasd",leftListenerFunc=function()  finish()  end,rightListenerFunc=nil})

local function http_request()
	local body,cookie,code,headers=http.get("http://119.29.99.152/igame?cmd=1003")
	println(body)
end

local function json()
	local cjson = require ("cjson")
	println(cjson.encode({val='leehongee'}))
end

local function socket()
	local socket = require("socket")
	println(socket._VERSION)
end


local function jni_java()

	--	localUtils = luajava.bindClass("com.sharemob.tinchat.lib.LocalUtils")

	--	local anim= luajava.bindClass("com.sharemob.tinchat.R$anim")
	--	LocalUtils:gotoViewByAnim(activity,"com.sharemob.tinchat.modules.account.LoginActivity",anim.in_from_right,anim.out_to_left,false)

	--    c = luajava.newInstance("com.sharemob.tinchat.modules.launch.SplashActivity")
	--    intent:setFlags(intent.FLAG_ACTIVITY_NEW_TASK);

	--    intent:setComponent(c)
	--	intent:setClass(activity,"com.sharemob.tinchat.modules.launch.SplashActivity")
	--	activity:finish()
	--    activity:startActivity(intent)
	
	--	onTouch=luajava.createProxy("android.view.View$OnTouchListener",{onTouch=function(v,e)
--		println(MotionEvent.ACTION_DOWN)
--		if(e.getAction()==MotionEvent.ACTION_DOWN) then
--			btn_login.setBackColorSelected("#ed8d83")
--		elseif(e.getAction()==MotionEvent.ACTION_UP) then
--			btn_login.setBackColor("#ec7063")
--			btn_login.setBackColorSelected("#ed8d83")
--		end
--	end})
--	btn_login.setOnTouchListener(onTouch)
end

local function click()

println("asd")
end

import "android.widget.FrameLayout"
local function layout()

-- local layoutParams =FrameLayout.LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT, FrameLayout.LayoutParams.WRAP_CONTENT)
-- --设置广告条的悬浮位置，这里示例为右下角
-- layoutParams.gravity = Gravity.BOTTOM | Gravity.RIGHT;
-- 
-- 
----实列化广告条
--local ad=AdView(activity,AdSize.FIT_SCREEN)
--
--
----广告条切换
--local onSwitchedAd=function(ad)
--  print("广告条切换")
--  end
--local onReceivedAd=function(ad)
--  print("请求广告条成功")
--end
--local onFailedToReceivedAd=function(ad)
--  print("请求广告条失败")
--end
--
--
--activity.addContentView(ad,layoutParams)
end

local function main()
--	http_request()



end




main()

