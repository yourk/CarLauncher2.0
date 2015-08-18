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
	 * �Ƿ���λ�ù㲥
	 */
	public static boolean isSendData;

	/**
	 * ��ʼ����λ
	 */
	private void init() {
		mLocationManagerProxy = LocationManagerProxy.getInstance(this);
		// �˷���Ϊÿ���̶�ʱ��ᷢ��һ�ζ�λ����Ϊ�˼��ٵ������Ļ������������ģ�
		// ע�����ú��ʵĶ�λʱ��ļ���������ں���ʱ�����removeUpdates()������ȡ����λ����
		// �ڶ�λ�������ں��ʵ��������ڵ���destroy()����
		// ����������ʱ��Ϊ-1����λֻ��һ��
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
			// ��ȡλ����Ϣ
			Double geoLat = amapLocation.getLatitude();
			Double geoLng = amapLocation.getLongitude();
			L.d(TAG, "��λ���ˣ�" + geoLat + "," + geoLng);
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
