require "init"

--local LocalScrollview=luajava.bindClass("com.sharemob.tinchat.component.LocalScrollview")
local LocationAdapter=luajava.bindClass("com.sharemob.tinchat.lib.adapter.LocationAdapter")
local LocalLinearLayoutManager=luajava.bindClass("com.sharemob.tinchat.lib.adapter.LocalLinearLayoutManager")
local MapView=luajava.bindClass("com.amap.api.maps.MapView")
local CameraUpdateFactory=luajava.bindClass("com.amap.api.maps.CameraUpdateFactory")

local main={}
local location_alimap
local aMap
local locationAdapter

local Top_TitleBar={
			FrameLayout,
			layout_width="match_parent",
			layout_height=dimens.dx_45,
			layout_gravity="top",
			background=Color.TitleBgColor,
			{
				TextView,
				id="tv_title_content",
				layout_width="wrap_content",
				layout_height="wrap_content",
				layout_gravity="left|center_vertical",
				layout_marginLeft="60dp",
				text="你所在位置",
				textStyle="bold",
				textColor=Color.TitleTextColor,
				textSize="16sp"
			},
			{
				FrameLayout,
				layout_width="50dp",
				layout_height="fill",
				layout_gravity="left|center_vertical",
				{
					ImageView,
					layout_width="25dp",
					layout_height="25dp",
					layout_gravity="center_vertical|center_horizontal",
					background="drawable/abc_ic_ab_back_mtrl_am_alpha.png"
				},
				{
					ColorButton,
					alpha="0.3",
					id="iBtn_title_back",
					layout_width="match_parent",
					layout_height="match_parent",
					gravity="vertical"
				}
			},
			{	
				FrameLayout,
				layout_width="wrap",
				layout_height="fill",
				layout_marginRight="10dp",
				layout_gravity="right|center_vertical",
				{
					ColorButton,
					id="iBtn_title_right",
					text="发送",
					paddingLeft="17dp",
					paddingRight="17dp",
					paddingTop="6dp",
					paddingBottom="6dp",
					layout_width="match_parent",
					layout_height="wrap",
					textColor=Color.TitleTextColor,
					layout_gravity="center_vertical",
					textSize="15sp"
				}
			}
}
local loadLocationPosiList=function(longitude,latitude)
		local locationUrl=URL_Location_regeo(longitude,latitude)
		println(locationUrl)
		local list = ArrayList()
		AsyncHttp(locationUrl,function(body)  
--			println(body)
			local entity=cjson.decode(body)
			local pois_array=entity["regeocode"].pois
			local position={
				name="位置",
				address=entity["regeocode"].formatted_address,
				location=string.format("%s,%s",latitude,longitude)
			}
			list.add(cjson.encode(position))
		
			for i=1,#pois_array,1 do
	--			println(cjson.encode(pois_array[i]))
				list.add(cjson.encode(pois_array[i]))
			end
			locationAdapter.setAdapter(list)
			locationAdapter.notifyDataSetChanged();
		end)
end

local locationSource=luajava.createProxy("com.amap.api.maps.LocationSource",{
	 activate=function(listener)
	 	local localLocationMapListener=luajava.createProxy("com.sharemob.tinchat.lib.LocalUtils$LocalLocationMapListener",{
	 		onLocationChanged=function(amapLocation)
	 			listener.onLocationChanged(amapLocation)
				local longitude = amapLocation.getLongitude();
				local altitude = amapLocation.getLatitude();
	 			loadLocationPosiList(longitude,altitude)
	 		end
	 	});
	 	LocalUtils.LocalLocationMap(this,localLocationMapListener)
	 end;
	 deactivate=function()
	 	
	 end
})

function loadLayout(savedInstanceState)
	layout={
		LinearLayout,
		layout_width="fill",
		layout_height="match_parent",
		orientation="vertical",
		Top_TitleBar,
		{
			MapView,	
			id="location_alimap",
			layout_width="match_parent",
			layout_height="300dp"
		},
		{
			RecyclerView,
			id="location_list",
			scrollbars="vertical",
			background=Color.White,
			layout_width="match_parent",
			layout_height="match_parent"
		}
	}
		
		this.setContentView(loadlayout(layout,main))
end

function onController(savedInstanceState)
	--返回按钮
	setColorButton(main.iBtn_title_back,'#e8e8e8',Color.transparent,
	function()
		println("onClickFunc")
		this.finish()
	end,0)
--	main.iBtn_title_back.setRadius(0)
	--发送按钮
	local btn_title_send=main.iBtn_title_right
	setColorButton(btn_title_send,'#989797','#e4e7e7',
	function()
		println("onClickFunc")
		locationAdapter.SendLocation()
		this.finish()
	end,10)
--	btn_title_send.setRadius(10)
	--加载附近地址名称
	locationAdapter=LocationAdapter(this)
	local location_list=main.location_list
	location_list.setHasFixedSize(true);
	local layoutManager=LinearLayoutManager(this)
	layoutManager.setStackFromEnd(false);
	location_list.setItemAnimator(DefaultItemAnimator()); 
	location_list.setLayoutManager(layoutManager);
	--绑定适配器
	location_list.setAdapter(locationAdapter);
	
	--定位地图
	location_alimap=main.location_alimap
	location_alimap.onCreate(savedInstanceState);
	aMap = location_alimap.getMap()
	aMap.setLocationSource(locationSource);-- 设置定位监听
	aMap.getUiSettings().setMyLocationButtonEnabled(true);--设置默认定位按钮是否显示
	aMap.getUiSettings().setScaleControlsEnabled(false);
	aMap.getUiSettings().setZoomControlsEnabled(false);
	aMap.setMyLocationEnabled(true);
	aMap.setMyLocationType(1);
	aMap.moveCamera(CameraUpdateFactory.zoomTo(17.5));
	LocalUtils.setUpLocationStyle(aMap)
	location_alimap.onSaveInstanceState(outState);

	

--	local latitude=113.914699
--	local longitude=22.572032
	
end

function onDestroy()
	location_alimap.onDestroy();
end

function onResume()
	location_alimap.onResume();
end

function onPause()
	location_alimap.onPause();
end

function onCreate(savedInstanceState)
	loadLayout(savedInstanceState)
	onController(savedInstanceState)
end