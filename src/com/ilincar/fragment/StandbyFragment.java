package com.ilincar.fragment;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.amap.api.location.AMapLocalWeatherForecast;
import com.amap.api.location.AMapLocalWeatherListener;
import com.amap.api.location.AMapLocalWeatherLive;
import com.amap.api.location.LocationManagerProxy;
import com.ilincar.carlauncher.R;
import com.ilincar.util.AppSettings;
import com.ilincar.util.Util;
import com.ilincar.view.VerticalPager;

/**
 * ������ҳ����ҳ��������
 * 
 * @author liujh
 * 
 */
@SuppressLint("ValidFragment")
public class StandbyFragment extends Fragment {
	private static View view;
	private static TextView time, date, city, temperature, weather;
	private static final int TIME_UPDATE = 0;
	private static final int WEATHER_UPDATE = 1;
	private static IntentFilter intentFilter;
	private static LocationManagerProxy mLocationManagerProxy;
	private static Context context;
	// ����ˢ�¼��ʱ�䣬��λ����
	private static final int interval_time = 10;
	// ����ˢ�¼�ʱ�ֶ�
	private static int timing;
	public static VerticalPager verticalPager;

	public StandbyFragment(Context context) {
		this.context = context;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.standby_fragment, null);
		init();
		return view;
	}

	private void init() {
		time = (TextView) view.findViewById(R.id.time);
		date = (TextView) view.findViewById(R.id.date);
		city = (TextView) view.findViewById(R.id.city);
		weather = (TextView) view.findViewById(R.id.weather);
		temperature = (TextView) view.findViewById(R.id.temperature);
		verticalPager=(VerticalPager) view.findViewById(R.id.verticalPager);
		handler.sendEmptyMessage(TIME_UPDATE);
		intentFilter = new IntentFilter();
		intentFilter.addAction(Intent.ACTION_TIME_TICK);
		intentFilter.addAction(Intent.ACTION_TIME_CHANGED);
		getActivity().registerReceiver(timeDateBrod, intentFilter);
		mLocationManagerProxy = LocationManagerProxy.getInstance(getActivity());
		handler.sendEmptyMessage(WEATHER_UPDATE);
	}

	private static Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case TIME_UPDATE:
				time.setText(Util.timeString());
				date.setText(Util.dayString() + " " + Util.stringData());
				if (timing <= interval_time) {
					handler.sendEmptyMessage(WEATHER_UPDATE);
					timing = 0;
				} else {
					timing++;
				}
				break;
			case WEATHER_UPDATE:
				city.setText(AppSettings.getPrefString(context, "Weather_city",
						"����"));
				temperature.setText(AppSettings.getPrefString(context,
						"Weather_temperature", "27��")+"��");
				weather.setText(AppSettings.getPrefString(context,
						"Weather_weather", "��"));
				mLocationManagerProxy.requestWeatherUpdates(
						LocationManagerProxy.WEATHER_TYPE_LIVE,
						aMapLocalWeatherListener);
				break;
			default:
				break;
			}
		};
	};
	private static BroadcastReceiver timeDateBrod = new BroadcastReceiver() {

		@Override
		public void onReceive(Context context, Intent intent) {
			String action = intent.getAction();
			if (Intent.ACTION_TIME_TICK.equals(action)
					|| Intent.ACTION_TIME_CHANGED.equals(action)) {
				handler.sendEmptyMessage(TIME_UPDATE);
			}
		}
	};
	private static AMapLocalWeatherListener aMapLocalWeatherListener = new AMapLocalWeatherListener() {

		@Override
		public void onWeatherLiveSearched(
				AMapLocalWeatherLive aMapLocalWeatherLive) {
			if (aMapLocalWeatherLive != null
					&& aMapLocalWeatherLive.getAMapException().getErrorCode() == 0) {
				String city = aMapLocalWeatherLive.getCity();// ����
				String weather = aMapLocalWeatherLive.getWeather();// �������
				String windDir = aMapLocalWeatherLive.getWindDir();// ����
				String windPower = aMapLocalWeatherLive.getWindPower();// ����
				String humidity = aMapLocalWeatherLive.getHumidity();// ����ʪ��
				String reportTime = aMapLocalWeatherLive.getReportTime();// ���ݷ���ʱ��
				String temperature = aMapLocalWeatherLive.getTemperature();// �¶�
				StandbyFragment.city.setText(city);
				StandbyFragment.temperature.setText(temperature+"��");
				StandbyFragment.weather.setText(weather);
				AppSettings.setPrefString(context, "Weather_city", city);
				AppSettings.setPrefString(context, "Weather_weather", weather);
				AppSettings.setPrefString(context, "Weather_windDir", windDir);
				AppSettings.setPrefString(context, "Weather_windPower",
						windPower);
				AppSettings
						.setPrefString(context, "Weather_humidity", humidity);
				AppSettings.setPrefString(context, "Weather_reportTime",
						reportTime);
				AppSettings.setPrefString(context, "Weather_temperature",
						temperature);
			} else {
				// ��ȡ����Ԥ��ʧ��
				Toast.makeText(
						context,
						"��ȡ����Ԥ��ʧ��:"
								+ aMapLocalWeatherLive.getAMapException()
										.getErrorMessage(), Toast.LENGTH_SHORT)
						.show();
			}
		}

		@Override
		public void onWeatherForecaseSearched(AMapLocalWeatherForecast arg0) {

		}
	};

	public void onDestroy() {
		mLocationManagerProxy.destroy();
	};
}
