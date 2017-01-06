//package com.sharemob.tinchat.lib;
//
//import android.content.Context;
//import android.location.Location;
//import android.os.Bundle;
//
//import com.amap.api.location.AMapLocation;
//import com.amap.api.location.AMapLocationListener;
//import com.amap.api.location.LocationManagerProxy;
//import com.amap.api.location.LocationProviderProxy;
//import com.sharemob.tinchat.lib.common.SMGlobal;
//import com.sharemob.tinchat.modules.dao.cache.CacheManager;
//
//public class LocalAMapLocation implements AMapLocationListener{
//
//	private LocationManagerProxy mLocationManagerProxy;
//	private Context context=SMGlobal.getInstance().context;
//	
//	public LocalAMapLocation() {
//		this.init() ;
//	}
//	
//	/**
//	 * 初始化定位
//	 */
//	private void init() {
//		// 初始化定位，只采用网络定位
//		mLocationManagerProxy = LocationManagerProxy.getInstance(context);
//		mLocationManagerProxy.setGpsEnable(false);
//		// 此方法为每隔固定时间会发起一次定位请求，为了减少电量消耗或网络流量消耗，
//		// 注意设置合适的定位时间的间隔（最小间隔支持为2000ms），并且在合适时间调用removeUpdates()方法来取消定位请求
//		// 在定位结束后，在合适的生命周期调用destroy()方法
//		// 其中如果间隔时间为-1，则定位只定一次,
//		// 在单次定位情况下，定位无论成功与否，都无需调用removeUpdates()方法移除请求，定位sdk内部会移除
//		mLocationManagerProxy.requestLocationData(LocationProviderProxy.AMapNetwork, 60 * 1000, 15, this);
//
//	}
//	
//	@Override
//	public void onLocationChanged(Location location) {
//		
//	}
//
//	@Override
//	public void onStatusChanged(String provider, int status, Bundle extras) {
//		
//	}
//
//	@Override
//	public void onProviderEnabled(String provider) {
//		
//	}
//
//	public void onProviderDisabled(String provider) {
//		
//	}
//
//	protected void destroy() {
//		// 移除定位请求
//		mLocationManagerProxy.removeUpdates(this);
//		// 销毁定位
//		mLocationManagerProxy.destroy();
//	}
//
//	public void onLocationChanged(AMapLocation amapLocation) {
//		if (amapLocation != null&& amapLocation.getAMapException().getErrorCode() == 0) {
//			// 定位成功回调信息，设置相关消息
//			String address=String.format("%s %s %s %s",amapLocation.getProvince(),amapLocation.getCity(),amapLocation.getDistrict(),amapLocation.getPoiName() );
////			CacheManager.getInstance().getAccount().setAddress(address);
//		}
//	}
//}
