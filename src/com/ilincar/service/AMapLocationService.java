package com.ilincar.service;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.location.LocationManagerProxy;
import com.amap.api.location.LocationProviderProxy;
import com.ilincar.carlauncher.MainActivity;
import com.ilincar.config.BroadCastConfig;
import com.ilincar.util.AppSettings;
import com.ilincar.util.L;

import android.app.Service;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.os.IBinder;

public class AMapLocationService extends Service implements
		AMapLocationListener {
	private LocationManagerProxy mLocationManagerProxy;
	private Intent intent;
	private String TAG = "AMapLocationService";
	/**
	 * 是否发送位置广播
	 */
	public static boolean isSendData;

	/**
	 * 初始化定位
	 */
	private void init() {
		mLocationManagerProxy = LocationManagerProxy.getInstance(this);
		// 此方法为每隔固定时间会发起一次定位请求，为了减少电量消耗或网络流量消耗，
		// 注意设置合适的定位时间的间隔，并且在合适时间调用removeUpdates()方法来取消定位请求
		// 在定位结束后，在合适的生命周期调用destroy()方法
		// 其中如果间隔时间为-1，则定位只定一次
		mLocationManagerProxy.requestLocationData(
				LocationProviderProxy.AMapNetwork, 3 * 1000, 5, this);
		mLocationManagerProxy.setGpsEnable(false);
	}

	@Override
	public IBinder onBind(Intent arg0) {
		return null;
	}

	@Override
	public void onCreate() {
		super.onCreate();
		init();
	}

	@Override
	public void onLocationChanged(Location location) {
	}

	@Override
	public void onProviderDisabled(String provider) {

	}

	@Override
	public void onProviderEnabled(String provider) {

	}

	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {

	}

	@Override
	public void onLocationChanged(AMapLocation amapLocation) {
		if (amapLocation != null
				&& amapLocation.getAMapException().getErrorCode() == 0) {
			// 获取位置信息
			Double geoLat = amapLocation.getLatitude();
			Double geoLng = amapLocation.getLongitude();
			L.d(TAG, "定位到了：" + geoLat + "," + geoLng);
			AppSettings.setPrefString(this, "lat", geoLat.toString());
			AppSettings.setPrefString(this, "lng", geoLng.toString());
			if (isSendData) {
				MainActivity.aMapLocation = amapLocation;
				intent = new Intent(BroadCastConfig.LOCATION);
				sendBroadcast(intent);
			}
		}
	}

}
