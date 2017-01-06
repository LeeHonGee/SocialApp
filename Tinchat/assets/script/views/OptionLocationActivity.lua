require "init"

AMapNetwork = "lbs"
local MapView=luajava.bindClass("com.amap.api.maps.MapView")
local CameraUpdateFactory=luajava.bindClass("com.amap.api.maps.CameraUpdateFactory")
local LocationManagerProxy=luajava.bindClass("com.amap.api.location.LocationManagerProxy")
local mListener
local locationListener
local mLocationManagerProxy
local aMap
local mapView

local function registerOptionUpateUserInfo()
	local filter =IntentFilter()
	filter.addAction(ACTIONO_OPTION_UPDATE_USERINFO)
	this.registerReceiver(filter)
end

local function loadLayout(savedInstanceState)
	local layout_title_header=require "layout_title_header".new()
	layout_title_header:create({title="当前位置",leftListenerFunc=function() finish()   end,rightListenerFunc=nil})
	
	layout={
		LinearLayout,
		layout_width="fill",
		layout_height="fill",
		orientation="vertical",
		background=Color.WhiteSmoke,
		layout_title_header.layout,
		{
			MapView,
			id="mv_location",
			layout_width="match_parent",
			layout_height="wrap_content",
			layout_weight="1"
		}
	}
	main={}
	this.setContentView(loadlayout(layout,main))
	layout_title_header:initComponent(main.iBtn_title_back,main.iBtn_title_right)
	mapView=main.mv_location
	mapView.onCreate(savedInstanceState);
	locationSource=luajava.createProxy("com.amap.api.maps.LocationSource",{
	 activate=function(listener)
	 	mListener = listener
	 	if(mAMapLocationManager==nil) then
	 		mLocationManagerProxy=LocationManagerProxy.getInstance(this)
	--		mLocationManagerProxy.setGpsEnable(false)
			mLocationManagerProxy.requestLocationData(AMapNetwork, 60 * 1000, 15, locationListener);
	 	end
	 end;
	 deactivate=function()
	 	mListener = nil
	 	if (mAMapLocationManager ~= null)  then
	 		mAMapLocationManager.removeUpdates(locationListener)
	 		mAMapLocationManager.destroy()
	 	end
	 	mAMapLocationManager=nil
	 end
	 
	 })
	
	locationListener=luajava.createProxy("com.amap.api.location.AMapLocationListener",{
		onLocationChanged=function(amapLocation)
			if (amapLocation ~=nil and amapLocation.getAMapException().getErrorCode() == 0) then
--				local  address=string.format("%s%s%s",amapLocation.getCity(),amapLocation.getDistrict(),amapLocation.getPoiName() )
				mListener.onLocationChanged(amapLocation)
				
--				mLocationManagerProxy.destroy()
			end
		end;
		onProviderDisabled=function(provider)end;
		onProviderEnabled=function(provider)end;
		onStatusChanged=function(provider,status,extras) end
	})
	
	local function init() 
		if (aMap == nil) then
			aMap = mapView.getMap()
			aMap.setLocationSource(locationSource);-- 设置定位监听
			aMap.getUiSettings().setMyLocationButtonEnabled(true);--设置默认定位按钮是否显示
			aMap.getUiSettings().setScaleControlsEnabled(false);
			aMap.getUiSettings().setZoomControlsEnabled(false);
			aMap.setMyLocationEnabled(true);
			aMap.setMyLocationType(1);
			aMap.moveCamera(CameraUpdateFactory.zoomTo(1000));
		end
	end
	
	--注册更新
--	registerOptionUpateUserInfo()
end

function onPause()
	
end
function finish()
	this.finish()
end

function onKeyDown(keyCode,event)
	if(keyCode == KeyEvent.KEYCODE_BACK) then
		finish()
	end
end

function onReceive(context,intent)
	local action = intent.getAction()
	println(action)
	if (action==ACTIONO_OPTION_UPDATE_USERINFO) then
--		local bundle = intent.getExtras()
		local item_id=intent.getStringExtra(BUNDLE_ITEM_KEY).."Ofitem_right_name"
		local item_value=intent.getStringExtra(BUNDLE_ITEM_VALUE)
		
		println(item_id)
		println(item_value)
		main[item_id].setText(tostring(item_value))
	end
end

function  onResume()
	mapView.onResume()
end

function onPause()
	mapView.onPause()
end

function onSaveInstanceState(outState)
	mapView.onSaveInstanceState(outState)
end

function onDestroy()
	mapView.onDestroy()
	mLocationManagerProxy.destroy();
end

function onActivityResult(requestCode,resultCode,data) 


end
function onCreate(savedInstanceState)
	loadLayout(savedInstanceState)
end