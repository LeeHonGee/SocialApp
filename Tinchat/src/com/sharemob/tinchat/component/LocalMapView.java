//package com.sharemob.tinchat.component;
//
//import android.app.Activity;
//import android.content.Context;
//import android.location.Location;
//import android.os.Bundle;
//import android.util.AttributeSet;
//import android.util.Log;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.widget.TextView;
//
//import com.amap.api.location.AMapLocation;
//import com.amap.api.location.AMapLocationListener;
//import com.amap.api.location.LocationManagerProxy;
//import com.amap.api.location.LocationProviderProxy;
//import com.amap.api.maps.AMap;
//import com.amap.api.maps.CameraUpdateFactory;
//import com.amap.api.maps.LocationSource;
//import com.amap.api.maps.LocationSource.OnLocationChangedListener;
//import com.amap.api.maps.MapView;
//import com.sharemob.tinchat.R;
////com.sharemob.tinchat.component.LocalMapView
//public class LocalMapView extends View {
//	private AMap aMap;
//	private MapView mapView;
//	private TextView chating_tv_location;
//	private OnLocationChangedListener mListener;
//	private LocationManagerProxy mAMapLocationManager;
//	private Context context;
//	private LocationSource locationSource;
//	private AMapLocationListener aMapLocationListener;
//	public LocalMapView(Context context) {
//		this(context, null);
//		
//		System.out.println("LocalMapView1");
//	}
//
//
//	public LocalMapView(Context context, AttributeSet attrs) {
//		super(context, attrs);
//		System.out.println("LocalMapView3");
//		
//		
//	}
//	
//	public LocalMapView builder(Context context,Bundle savedInstanceState) {
//		this.context=context;
//		View view = LayoutInflater.from(context).inflate(R.layout.local_mapview, null);
//		mapView = (MapView) view.findViewById(R.id.mv_location);
//		mapView.onCreate(savedInstanceState);
//		chating_tv_location = (TextView) view.findViewById(R.id.tv_location);
//		init(savedInstanceState);
//		mapView.onSaveInstanceState(savedInstanceState);
//		return this;
//	}
//
//	private void initListener() {
//		aMapLocationListener = new AMapLocationListener() {
//			public void onStatusChanged(String provider, int status,
//					Bundle extras) {
//			}
//
//			public void onProviderEnabled(String provider) {
//			}
//
//			public void onProviderDisabled(String provider) {
//			}
//
//			public void onLocationChanged(Location location) {
//			}
//
//			/**
//			 * 定位成功后回调函数
//			 */
//			public void onLocationChanged(AMapLocation amapLocation) {
//				if (mListener != null && amapLocation != null) {
//					if (amapLocation != null
//							&& amapLocation.getAMapException().getErrorCode() == 0) {
//						mListener.onLocationChanged(amapLocation);// 显示系统小蓝点
//						chating_tv_location.setText(String.format(
//								"%s",
//								amapLocation.getCity()
//										+ amapLocation.getDistrict()
//										+ amapLocation.getStreet()));
//						locationSource.deactivate();
//						// amapLocation.getProvince(),amapLocation.getCity(),amapLocation.getDistrict(),
//					} else {
//						Log.e("AmapErr", "Location ERR:"
//								+ amapLocation.getAMapException()
//										.getErrorCode());
//					}
//				}
//			}
//		};
//
//		locationSource = new LocationSource() {
//			/**
//			 * 停止定位
//			 */
//			public void deactivate() {
//				mListener = null;
//				if (mAMapLocationManager != null) {
//					mAMapLocationManager.removeUpdates(aMapLocationListener);
//					mAMapLocationManager.destroy();
//				}
//				mAMapLocationManager = null;
//			}
//
//			/**
//			 * 激活定位
//			 */
//			public void activate(OnLocationChangedListener listener) {
//				mListener = listener;
//				if (mAMapLocationManager == null) {
//					mAMapLocationManager = LocationManagerProxy.getInstance(context);
//					// 此方法为每隔固定时间会发起一次定位请求，为了减少电量消耗或网络流量消耗，
//					// 注意设置合适的定位时间的间隔（最小间隔支持为2000ms），并且在合适时间调用removeUpdates()方法来取消定位请求
//					// 在定位结束后，在合适的生命周期调用destroy()方法
//					// 其中如果间隔时间为-1，则定位只定一次
//					// 在单次定位情况下，定位无论成功与否，都无需调用removeUpdates()方法移除请求，定位sdk内部会移除
//					mAMapLocationManager.requestLocationData(
//							LocationProviderProxy.AMapNetwork, 60 * 1000, 10,
//							aMapLocationListener);
//				}
//			}
//		};
//	}
//	
//	/**
//	 * 初始化
//	 */
//	private void init(Bundle savedInstanceState) {
//		initListener();
//		if (aMap == null) {
//			aMap = mapView.getMap();
//			aMap.setLocationSource(locationSource);// 设置定位监听
//			aMap.getUiSettings().setMyLocationButtonEnabled(false);// 设置默认定位按钮是否显示
//			aMap.getUiSettings().setScaleControlsEnabled(false);
//			aMap.getUiSettings().setZoomControlsEnabled(false);
//			aMap.setMyLocationEnabled(true);// 设置为true表示显示定位层并可触发定位，false表示隐藏定位层并不可触发定位，默认是false
//			// 设置定位的类型为定位模式 ，可以由定位、跟随或地图根据面向方向旋转几种
//			aMap.setMyLocationType(AMap.MAP_TYPE_SATELLITE);
//			aMap.moveCamera(CameraUpdateFactory.zoomTo(100));
//		}
//	}
//	
//	public void onPause() {
//		locationSource.deactivate();
//	}
//	
//	public void onDestroy() {
//		mapView.onDestroy();
//	}
//	
//	public void onResume() {
//		mapView.onResume();
//	}
//}
