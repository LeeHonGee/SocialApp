require "import"

require "functions"

import "android.app.*"
import "android.widget.ListView"
import "android.widget.SimpleAdapter"

import "java.util.*"
import "android.graphics.drawable.*"
import "android.widget.*"
import "android.view.*"
import "android.view.View_*"
import "android.view.View_OnClickListener"
import "android.os.*"
import "http"
import "com.androlua.LuaThread"


proxy=luajava.createProxy
bind=luajava.bindClass

cjson = require ("cjson")
--this=activity

URLDecoder=luajava.bindClass("java.net.URLDecoder")
URLEncoder=luajava.bindClass("java.net.URLEncoder")
GlobalParams=luajava.bindClass("com.sharemob.tinchat.lib.common.GlobalParams")
ImageLoader=luajava.bindClass("com.nostra13.universalimageloader.core.ImageLoader")
--LocalHoveringScrollView=luajava.bindClass("com.sharemob.tinchat.base.LocalHoveringScrollView")
CacheManager=luajava.bindClass("com.sharemob.tinchat.modules.dao.cache.CacheManager")
CircleImageView=luajava.bindClass("com.sharemob.tinchat.component.CircleImageView")
RoundImageView=luajava.bindClass("com.sharemob.tinchat.component.RoundImageView")
ClearEditText=luajava.bindClass("com.sharemob.tinchat.component.ClearEditText")
SlideSwitch=luajava.bindClass("com.sharemob.tinchat.component.SlideSwitch")
ColorButton=luajava.bindClass("com.sharemob.tinchat.component.LocalButton")
Matrix=luajava.bindClass("com.sharemob.tinchat.lib.common.Matrix")
Html=luajava.bindClass("android.text.Html")
SMGlobal=luajava.bindClass("com.sharemob.tinchat.lib.common.SMGlobal")
ClipDrawable=luajava.bindClass("android.graphics.drawable.ClipDrawable")
Activity=luajava.bindClass("android.app.Activity")

ImageUtils=luajava.bindClass("com.sharemob.tinchat.lib.ImageUtils")
LocalUtils = luajava.bindClass("com.sharemob.tinchat.lib.LocalUtils")
LuaUtils = luajava.bindClass("com.androlua.LuaUtil")
R=luajava.bindClass("com.sharemob.tinchat.R")
anim= luajava.bindClass("com.sharemob.tinchat.R$anim")
layout=luajava.bindClass("com.sharemob.tinchat.R$layout")
id= luajava.bindClass("com.sharemob.tinchat.R$id")
drawable= luajava.bindClass("com.sharemob.tinchat.R$drawable")
ViewPager=luajava.bindClass("android.support.v4.view.ViewPager")
ListView=luajava.bindClass("android.widget.ListView")
Intent = luajava.bindClass("android.content.Intent")
IntentFilter = luajava.bindClass("android.content.IntentFilter")
Bundle=luajava.bindClass("android.os.Bundle")

LuaThread=luajava.bindClass("com.androlua.LuaThread")
WXConstant=luajava.bindClass("com.sharemob.tinchat.thirdparty.weixin.WXConstant")
WheelView =luajava.bindClass("com.sharemob.tinchat.component.wheeldialog.WheelView")
--ImageItemAdapter=luajava.bindClass("com.sharemob.tinchat.lib.adapter.ImageItemAdapter")
AsyncHttpRequest=luajava.bindClass("com.sharemob.tinchat.lib.common.AsyncHttpRequest")
AsyncHttpsRequest=luajava.bindClass("com.sharemob.tinchat.lib.common.AsyncHttpsRequest")
Uri=luajava.bindClass("android.net.Uri")

LinearLayoutManager=luajava.bindClass("android.support.v7.widget.LinearLayoutManager")
RecyclerView=luajava.bindClass("android.support.v7.widget.RecyclerView")
DefaultItemAnimator=luajava.bindClass("android.support.v7.widget.DefaultItemAnimator")
StaggeredGridLayoutManager=luajava.bindClass("android.support.v7.widget.StaggeredGridLayoutManager")
GridLayoutManager=luajava.bindClass("android.support.v7.widget.GridLayoutManager")

LocalScrollview=luajava.bindClass("com.sharemob.tinchat.component.LocalScrollview")
LocalHoveringScrollView=luajava.bindClass("com.sharemob.tinchat.base.LocalHoveringScrollView")
PullToRefreshListView=luajava.bindClass("com.sharemob.tinchat.lib.refreashtabview.refreash.PullToRefreshListView")
PullLoadMoreRecyclerView=luajava.bindClass("com.sharemob.tinchat.lib.refreashtabview.extras.PullLoadMoreRecyclerView")

UserInfo=CacheManager.getInstance().userInfo

require "lib"

line={
		View,
		layout_width="fill_parent",
		layout_height=1,
		background="#c8c8c8"
}

CacheManager.getInstance().TitleBgColor=0x2f2b2f

function println(val)
	if(val~=nil) then
		local print=PrintStream.println
		print(val)
	end
end

function enterMainView(json)
	CacheManager.getInstance().initUserInfo(json)
	LocalUtils.gotoViewByAnim(this,
	"com.sharemob.tinchat.modules.launch.MainActivity",
	anim.in_from_right,
	anim.out_to_left,
	true)
end

function ToastText(val)
	Toast.makeText(this,val, Toast.LENGTH_SHORT ).show();
end

function enterActivity(sence,enterAnim,exitAnim,flag)
	LocalUtils.enterAppActivity(activity,sence,enterAnim,exitAnim,flag)
end

function enterActivity(sence)
	LocalUtils.enterAppActivity(activity,sence,anim.in_from_right,anim.out_to_left,false)
end

function getFragment(fragmentName)
	local fragment=LocalUtils.enterAppFragment(fragmentName)
	return fragment
end

function enterFragmentActivity(sence,enterAnim,exitAnim,flag)
	LocalUtils.enterFragmentActivity(activity,sence,enterAnim,exitAnim,flag)
end

function enterLocalActivity(sence,enterAnim,outAnim,flag)
	LocalUtils.gotoViewByAnim(activity,sence,enterAnim,outAnim,flag)
end

function sendEmail()
	local data=Intent(Intent.ACTION_SENDTO); 
	data.setData(Uri.parse("mailto:leehongee@126.com")); 
	data.putExtra(Intent.EXTRA_SUBJECT, "这是标题"); 
	data.putExtra(Intent.EXTRA_TEXT, "这是内容"); 
	this.startActivity(data); 

end

function LocalSheetDialog(callback,array)
	OnSheetItemClickListener=luajava.createProxy("com.sharemob.tinchat.base.LocalSheetDialog$OnSheetItemClickListener",{
		onClick=function(which)
			callback(which)
		end
	})
	local select=ArrayList()
	for i=1,#array,1 do
		select.add(array[i])
	end
	
	LocalUtils.selectedInfo(this,nil,select,OnSheetItemClickListener)
end

function attachOnClickListener(view,callback)
	onClick=luajava.createProxy("android.view.View$OnClickListener",{
		onClick=function(v)
			if(callback~=nil) then
				callback()
			end
		end})
	view.setOnClickListener(onClick)
end

function attachOnTouchListener(view,callback)
	onTouch=luajava.createProxy("android.view.View$OnTouchListener",{
		onTouch=function(v)
			if(callback~=nil) then
				callback()
			end
		end})
	view.setOnTouchListener(onTouch)
end

localAlertDialogListener=function(confirm_callback,cance_callbackl)
	listener=luajava.createProxy("com.sharemob.tinchat.lib.LocalUtils$LocalAlertDialogListener",{
		confirm=function()
			if(confirm_callback~=nil)then
				confirm_callback()
			end
		end;
		cancel=function()
			if(cance_callbackl~=nil)then
				cance_callbackl()
			end
		end;
	})
	return listener
end

function attachOnTouchListener(view,down_callback,up_callback)
	onTouch=luajava.createProxy("android.view.View$OnTouchListener",{
		onTouch=function(view,event)
			if(MotionEvent.ACTION_DOWN==event.getAction())then
				if(down_callback~=nil)then
					 down_callback()
				 end
			elseif(MotionEvent.ACTION_UP==event.getAction()) then
				if(up_callback~=nil)then
					up_callback()
				end
			end
			return false;
		end})
	view.setOnTouchListener(onTouch)
end

function loadImage(uri,callback)
	simpleImageLoadingListener=luajava.createProxy("com.nostra13.universalimageloader.core.assist.ImageLoadingListener",{
		onLoadingComplete=function(imageUri,v,loadedImage)
			callback(loadedImage)
		end
	})
	ImageLoader.getInstance().loadImage(uri,simpleImageLoadingListener)
end

CircleOptions=LocalUtils.getCircleOptions()
function displayCircleImage(uri,view)
	ImageLoader.getInstance().displayImage(uri,view)
--	view.setBorderWidth(3)
end

function displayImage(uri,view)
	ImageLoader.getInstance().displayImage(uri,view,this.loadingListener)
end

function SetButtonColor(view,downcolor,upcolor,callback)
	local bg=BColor(downcolor,upcolor)
	view.setBackgroundDrawable(bg)
	--按键监听事件
	attachOnClickListener(view,callback)
end

function OnClickListener(t)
	local OnClick=luajava.createProxy("android.view.View$OnClickListener",{
		onClick=function(v)
			if(t.id==nil)then
				t.callback()
			else
				t.callback(t.id)
			end
		end})
	t.view.setOnClickListener(OnClick)
end

function SetBackgoundColor(t,downcolor,upcolor)
	local bg=BColor(downcolor,upcolor)
	t.view.setBackgroundDrawable(bg)
	OnClickListener(t)
end

function setColorButton(view,downColor,upColor,callback,radius)
	view.setFillet(true)
	view.setRadius(radius)
	view.setBackColorSelected(downColor)
	view.setBackColor(upColor)
	attachOnClickListener(view,callback)
end

function setImageBitmap(view,resource)
	if(this.setImageBitmap~=nil) then
		local drawable=string.format("%s/drawable/%s.png",luajava.luadir,resource)
		this.setImageBitmap(drawable,view)
	end
end

MotionEvent=bind("android.view.MotionEvent")
function setDrawableButton(view,pressedDrawable,normalDrawable,callback)
	view.setVisibility(view.VISIBLE);
	local pressedPath=string.format("%s/drawable/%s.png",luajava.luadir,pressedDrawable)
	local normalPath=string.format("%s/drawable/%s.png",luajava.luadir,normalDrawable)
	this.setImageBitmap(normalPath,view)
	local onTouchListener=luajava.createProxy("android.view.View$OnTouchListener",{
	onTouch=function(view,event)
		if(MotionEvent.ACTION_DOWN==event.getAction())then
			this.setImageBitmap(pressedPath,view)
		elseif(MotionEvent.ACTION_UP==event.getAction()) then
			this.setImageBitmap(normalPath,view)
		end
		return false;
	end
	})
	view.setOnTouchListener(onTouchListener)
	if(callback~=nil) then
		attachOnClickListener(view,callback)
	end
end

function BColor(downcolor,upcolor)
	local state=StateListDrawable();
	local down=ColorDrawable(downcolor);
	local up=ColorDrawable(upcolor);
	state.addState({android.R.attr.state_pressed},down);
	state.addState({-android.R.attr.state_pressed},up);
	return state
end

function getHttp(url,callback)
	local body,cookie,code,headers=http.get(url)
	callback(body)
end

function pullLoadmoreListener(mPullRefresh,onRefresh_callback,onLoadMore_callback)
	mPullRefresh.setGridLayout(1);
	mPullRefresh.setPullRefreshEnable(false)
	local className="com.sharemob.tinchat.lib.refreashtabview.extras.PullLoadMoreRecyclerView$PullLoadMoreListener"
	local pullloadmorelistener=luajava.createProxy(className,{
	onRefresh=function()
		onRefresh_callback()
	end;
	onLoadMore=function()
		onLoadMore_callback()
	end
	})
	mPullRefresh.setOnPullLoadMoreListener(pullloadmorelistener)
end

function AsyncHttp(url,didFinished)
	local asyncHttpRequest=AsyncHttpRequest()
	local className="com.sharemob.tinchat.lib.common.AsyncHttpRequest$RequestCallback"
	responseCallback=luajava.createProxy(className,{
	requestDidFinished=function(body)
		didFinished(body)
	end,
	requestDidFailed=function()
	
	end
	})
	local url_parameters=split(url,"?")
	asyncHttpRequest.doRequest(url_parameters[1],url_parameters[2],false,responseCallback)
end


function AsyncHttps(url,requestDidFinished)
	local className="com.sharemob.tinchat.lib.common.AsyncHttpsRequest$RequestCallback"
	asyncHttpsRequest=AsyncHttpsRequest()
	responseCallback=luajava.createProxy(className,{
	requestDidFinished=function(body)
		requestDidFinished(body)
	end;
	requestDidFailed=function()
	
	end
	})
	local url_parameters=split(url,"?")
	asyncHttpsRequest.doRequest(url_parameters[1],url_parameters[2],false,responseCallback)
end

function sendBroadcastOption(item_key,item_value)
	intent =Intent()
	intent.setAction(ACTIONO_OPTION_UPDATE_USERINFO);
	intent.putExtra(BUNDLE_ITEM_KEY,item_key);
	intent.putExtra(BUNDLE_ITEM_VALUE, item_value);
	this.sendBroadcast(intent);
end

function EnterAppFragmentActivity(activityName,parameter)
	local intent = Intent();
	intent.putExtra("AppActivity",activityName);
	intent.putExtra("parameters", parameter);
	intent.setClassName(this,"com.sharemob.tinchat.component.AppFragmentActivity")
	this.startActivity(intent);
end

function OptionUpdateActivity(activityName,parameter)
	local intent = Intent();
	intent.putExtra("AppActivity",activityName);
	intent.putExtra("parameters", parameter);
	intent.setClassName(this,"com.sharemob.tinchat.component.AppActivity")
	this.startActivity(intent);
end
startActivity=OptionUpdateActivity

function setRecyclerView(recyclerView)
--StaggeredGridLayoutManager.HORIZONTAL 0
--StaggeredGridLayoutManager.VERTICAL 1
	recyclerView.setHasFixedSize(true);
	local layoutManager =StaggeredGridLayoutManager(1,StaggeredGridLayoutManager.HORIZONTAL);
	recyclerView.setLayoutManager(layoutManager);
end

function setRecyclerViewHORIZONTAL(recyclerView)
--StaggeredGridLayoutManager.HORIZONTAL 0
--StaggeredGridLayoutManager.VERTICAL 1
	recyclerView.setHasFixedSize(true);
	local layoutManager =StaggeredGridLayoutManager(1,StaggeredGridLayoutManager.HORIZONTAL);
	recyclerView.setLayoutManager(layoutManager);
end

function setRecyclerViewOrientation(_recyclerView,orientation)
	_recyclerView.setHasFixedSize(true);
	local layoutManager=LinearLayoutManager(this)
--	layoutManager.setOrientation(LocalGridLayoutManager.VERTICAL);
	layoutManager.setStackFromEnd(true);
	_recyclerView.setItemAnimator(DefaultItemAnimator()); 
	_recyclerView.setLayoutManager(layoutManager);
end


function loadRecyclerAdapter(recyclerView,urls,type)
	local adapter=ImageItemAdapter(this,type,this.loadingListener)
	adapter.setList(urls);
	recyclerView.setAdapter(adapter);
	adapter.notifyDataSetChanged();
end

function getDrawableUri(name)
	local drawable=string.format("%s/drawable/%s.png",luajava.luadir,name)
	return drawable
end

--function loadMyData()
--	local TINCHAT_LAUNCH_ACTIVITY = "TINCHAT_LAUNCH_ACTIVITY";
--	local url=URL_Load_UserInfo({accountID=""})
--	
--	AsyncHttp(url,function(body)
--		CacheManager.UserInfo=body
--		UserInfo=cjson.decode(CacheManager.UserInfo)
--		local activityName=Matrix.getMetaObjectValue(this.getApplicationContext(),TINCHAT_LAUNCH_ACTIVITY)
--		local LoginActivity="com.sharemob.tinchat.modules.launch.MainActivity"
--		LocalUtils.gotoViewByAnim(activity,LoginActivity,anim.in_from_right,anim.out_to_left,true)
--	end)
--end

function getItemTextID(cell_id)
	return string.format("%sOf%s" ,cell_id,"item_right_name")
end
require "layout"
require "OptionUtils"

local text=this.getParameters()
json_param=nil
if(text~=nil)then
	json_param=cjson.decode(text)
end
