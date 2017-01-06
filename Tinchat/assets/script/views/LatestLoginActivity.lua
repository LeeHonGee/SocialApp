require "init"

import "android.app.*"
import "android.widget.ListView"
import "android.widget.SimpleAdapter"

local LatestLoginAdapter=luajava.bindClass("com.sharemob.tinchat.lib.adapter.LatestLoginAdapter")
--local ArrayList=luajava.bindClass("java.util.ArrayList")
--local LuaAsyncTask=luajava.bindClass("com.androlua.LuaAsyncTask")
--local int=luajava.bindClass("int")

local list=ArrayList()
	
local function onBackEvent()
	finish()
end

function finish()
	this.finish()
end

function onKeyDown(keyCode,event)
	if(keyCode == KeyEvent.KEYCODE_BACK) then
		finish()
	end
end

local function loadLayout()
	local layout_header=require "layout_title_header".new()
	layout_header:create({title="最近登录时间",leftListenerFunc=function() onBackEvent()   end,rightListenerFunc=nil})
	
	local white_line={
		View,
		layout_width="100dp",
		layout_height="1dp",
		background="#c8c8c8"
	}
	
	local layout_footerView={
		LinearLayout,
		layout_width="wrap_content",
		layout_height="wrap_content",
		orientation="horizontal",
		gravity="center_vertical",
		padding="10dp",
		white_line,
		{
			TextView,
			layout_width="wrap_content",
			layout_height="wrap_content",
			gravity="center_horizontal|center_vertical",
        	layout_gravity="center_horizontal|center_vertical",
			textSize="14sp",
			layout_marginLeft="5dp",
			layout_marginRight="5dp",
			text="显示最近十次登录记录",
			textColor=Color.Back
		},
		white_line
	}
	
	local layout_PullToRefreshListView={
				PullToRefreshListView,
				id="latestlogin_listview",
				layout_width="match_parent",
				background=Color.WhiteSmoke,
				layout_height="match_parent",
				cacheColorHint="#cbcbcb",
				divider="#cbcbcb",
				dividerHeight="4dp",
				duplicateParentState=false,
				footerDividersEnabled="false",
				headerDividersEnabled="false",
				scrollbars="none",
				ptrAnimationStyle="rotate",
				ptrHeaderBackground="#f3f3f6",
				ptrHeaderTextColor="#c9c9c9"
			}
	
	local layout={
		LinearLayout,
		layout_width="match_parent",
		layout_height="match_parent",
		background=Color.WhiteSmoke,
		orientation="vertical",
		layout_header.layout,
		{
			LocalHoveringScrollView,
			paddingLeft="5dp",
			background=Color.WhiteSmoke,
			layout_width="match_parent",
			layout_height="match_parent",
			layout_PullToRefreshListView
		}
	}

	

	main={}
	
	this.setContentView(loadlayout(layout,main))
	layout_header:initComponent(main.iBtn_title_back,main.iBtn_title_right)
	
	
	local adapter=LatestLoginAdapter()
	local pullToRefresh=main.latestlogin_listview
	
	local function loadNetData(refreshView)
		
			local function update(n)
				os.execute("sleep 0.1")
			end
			
			--封装获取一个item数据
			local function getListItem(json)
				t_latest_login=cjson.decode(json)
				local json_latest_time=LocalUtils.getFormatDateTime(t_latest_login.latest_time)
				local latest_time=cjson.decode(json_latest_time)
				latest_time.location=t_latest_login.location
				return cjson.encode(latest_time)
			end
			--加载获取
			local function RefreshData(body)
				println(body)
				--{"latest_time":"2016-05-11 10:23:23","location":"广东省深圳市南山区白石路8号与海园二路交界"}
				list.clear()
				local array=cjson.decode(body)
				for i = 1, #array do
					local json=cjson.encode(array[i])
					list.add(getListItem(json))
				end
				refreshView.onRefreshComplete()
				adapter.setList(list)
				adapter.notifyDataSetChanged()
			end
			--回调函数
			local function callback()
				getHttp(URL_Latest_Login(),RefreshData)
			end
			task(update,1000,callback)
	end
	
	local onRefreshListener2=luajava.createProxy("com.sharemob.tinchat.lib.refreashtabview.refreash.PullToRefreshBase$OnRefreshListener2",{
		onPullDownToRefresh=function(refreshView)
			println("onPullDownToRefresh")
			
			loadNetData(refreshView)
			
		end;
		onPullUpToRefresh=function(refreshView)
			println("onPullUpToRefresh")
		end;
		onRefresh=function(refreshView)
			println("onRefresh")
		end
	})
	

	local onRefreshListener=luajava.createProxy("com.sharemob.tinchat.lib.refreashtabview.refreash.PullToRefreshBase$OnRefreshListener",{
		onRefresh=function(refreshView)
			println("----onRefresh")
			loadNetData(refreshView)
		end
	})
	
	pullToRefresh.setOnRefreshListener(onRefreshListener)
	adapter.setList(list)
	
	local listView=pullToRefresh.getRefreshableView()
	
	listView.setVerticalScrollBarEnabled(false)
	listView.setFooterDividersEnabled(false)
	--当前正处于下拉或释放状态，要让ListView失去焦点，否则被点击的那一项会一直处于选中状态
    listView.setPressed(false)
    listView.setFocusable(false)
    listView.setFocusableInTouchMode(false)
	listView.setDividerHeight(0)
	
	--添加ListView页脚
	local footerView=loadlayout(layout_footerView)
	footerView.setLayoutParams(LocalUtils.getLayoutParams())
	listView.addFooterView(footerView)
	
	
	--初始化页头
	LocalUtils.initILoadingLayout(pullToRefresh)
	listView.setSelector(drawable.selector_list_item)
	listView.setAdapter(adapter)
	pullToRefresh.setPullToRefreshEnabled(true)
	
	loadNetData(pullToRefresh)
end

function onCreate(savedInstanceState)
	loadLayout()

end