package com.ilincar.carlauncher;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.DecelerateInterpolator;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;

import com.amap.api.location.AMapLocation;
import com.amap.api.maps.AMap;
import com.amap.api.maps.AMapOptions;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.LocationSource;
import com.amap.api.maps.MapView;
import com.amap.api.maps.model.LatLng;
import com.ilincar.anim.RotateAnimation;
import com.ilincar.base.BaseActivity;
import com.ilincar.config.BroadCastConfig;
import com.ilincar.service.AMapLocationService;
import com.ilincar.util.AppSettings;
import com.ilincar.util.L;
import com.ilincar.view.TasksCompletedView;

public class MainActivity extends BaseActivity implements LocationSource {
	private TasksCompletedView detection_animation;
	private int mTotalProgress = 0;
	private int mCurrentProgress = 100;
	private ViewGroup mContainer;
	private RelativeLayout rl_layout01;
	private RelativeLayout rl_layout02;
	private OnLocationChangedListener mListener;
	private LinearLayout map, navigation;
	private MapView mapView;
	private AMap aMap;
	private BroadCastData broadCastData;
	private String TAG = "MainActivity";
	public static AMapLocation aMapLocation;
	private Intent service;
	private TextView data;
	public final static String[] strActions = { "��", "�Գ�", "��ת", "��ת", "��ǰ����ʻ",
			"��ǰ����ʻ", "�����ʻ", "�Һ���ʻ", "��ת��ͷ", "ֱ��", "����;����", "���뻷��", "ʻ������",
			"���������", "�����շ�վ", "����Ŀ�ĵ�", "�������", "����", "����", "ͨ�����к��", "ͨ����������",
			"ͨ������ͨ��", "ͨ���㳡", "����·б����" };

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		initView(savedInstanceState);
		broadCastData = new BroadCastData();
		IntentFilter intentFilter = new IntentFilter();
		intentFilter.addAction(BroadCastConfig.LOCATION);
		intentFilter.addAction(BroadCastConfig.NAVIGATION);
		intentFilter.addAction(BroadCastConfig.STARTNAVIG);
		intentFilter.addAction(BroadCastConfig.STOPNAVIG);
		registerReceiver(broadCastData, intentFilter);
		new Thread(new Runnable() {

			@Override
			public void run() {
				while (mTotalProgress < mCurrentProgress) {
					mTotalProgress += 1;
					detection_animation.setProgress(mTotalProgress);
					try {
						Thread.sleep(100);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
				handler.sendEmptyMessage(0);
			}
		}).start();
	}

	@Override
	protected void onStart() {
		super.onStart();
		service = new Intent(this, AMapLocationService.class);
		startService(service);
		AMapLocationService.isSendData=true;
	}

	/**
	 * ��ʼ���ؼ�
	 */
	private void initView(Bundle savedInstanceState) {
		detection_animation = (TasksCompletedView) findViewById(R.id.detection_animation);
		mContainer = (ViewGroup) findViewById(R.id.container);
		mContainer
				.setPersistentDrawingCache(ViewGroup.PERSISTENT_ANIMATION_CACHE);
		rl_layout01 = (RelativeLayout) findViewById(R.id.rl_layout01);
		rl_layout02 = (RelativeLayout) findViewById(R.id.rl_layout02);
		map = (LinearLayout) findViewById(R.id.map);
		navigation = (LinearLayout) findViewById(R.id.navigation);
		data = (TextView) findViewById(R.id.data);
		mapView = new MapView(this);
		LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT,
				LayoutParams.MATCH_PARENT);
		map.addView(mapView, params);
		mapView.onResume();
		mapView.onCreate(savedInstanceState);
		if (aMap == null) {
			aMap = mapView.getMap();
			setUpMap();
		}
	}

	private void setUpMap() {
		aMap.setLocationSource(this);
		aMap.getUiSettings().setMyLocationButtonEnabled(false);// ����Ĭ�϶�λ��ť�Ƿ���ʾ
		aMap.setMyLocationEnabled(true);// ����Ϊtrue��ʾ��ʾ��λ�㲢�ɴ�����λ��false��ʾ���ض�λ�㲢���ɴ�����λ��Ĭ����false
		// ����ͨ���������ŵ�ͼ
		aMap.getUiSettings().setZoomGesturesEnabled(false);
		// ��������ƽ�ƣ���������ͼ
		aMap.getUiSettings().setScrollGesturesEnabled(false);
		// ����������ת��ͼ
		aMap.getUiSettings().setRotateGesturesEnabled(false);
		// ����������б��ͼ
		aMap.getUiSettings().setTiltGesturesEnabled(false);
		// Logo ��λ��
		aMap.getUiSettings().setLogoPosition(
				AMapOptions.LOGO_POSITION_BOTTOM_RIGHT);
		//��ͼ��ʾ����
		aMap.setMapType(AMap.MAP_TYPE_NIGHT);
		// ���ſ��Ƽ�
		aMap.getUiSettings().setZoomControlsEnabled(false);
		// ���õ�ͼ���ŵȼ�
		aMap.moveCamera(CameraUpdateFactory.zoomTo(15));
		double latitude = Double.parseDouble(AppSettings.getPrefString(this,
				"lat", "0"));
		double longitude = Double.parseDouble(AppSettings.getPrefString(this,
				"lng", "0"));
		if (latitude != 0 && longitude != 0) {
			// �趨���ĵ�����
			LatLng marker1 = new LatLng(latitude, longitude);
			aMap.moveCamera(CameraUpdateFactory.changeLatLng(marker1));
		}
	}

	public void onclick(View v) {
		switch (v.getId()) {
		case R.id.detection_animation:
			break;
		default:
			break;
		}
	}

	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case 0:
				// applyRotation(0, 0, 90);
				applyRotation(0, 180, 90);
				break;

			default:
				break;
			}
		};
	};

	private void applyRotation(int position, float start, float end) {
		final float centerX = mContainer.getWidth() / 2.0f;
		final float centerY = mContainer.getHeight() / 2.0f;
		final RotateAnimation rotation = new RotateAnimation(start, end,
				centerX, centerY, 310.0f, true);
		rotation.setDuration(500);
		rotation.setFillAfter(true);
		rotation.setInterpolator(new AccelerateInterpolator());
		rotation.setAnimationListener(new DisplayNextView(position));

		mContainer.startAnimation(rotation);
	}

	private final class DisplayNextView implements Animation.AnimationListener {
		private final int mPosition;

		private DisplayNextView(int position) {
			mPosition = position;
		}

		public void onAnimationStart(Animation animation) {
		}

		public void onAnimationEnd(Animation animation) {
			mContainer.post(new SwapViews(mPosition));
		}

		public void onAnimationRepeat(Animation animation) {
		}
	}

	private final class SwapViews implements Runnable {
		private final int mPosition;

		public SwapViews(int position) {
			mPosition = position;
		}

		public void run() {
			final float centerX = mContainer.getWidth() / 2.0f;
			final float centerY = mContainer.getHeight() / 2.0f;
			RotateAnimation rotation;

			if (mPosition > -1) {
				rl_layout01.setVisibility(View.GONE);
				rl_layout02.setVisibility(View.VISIBLE);

				rotation = new RotateAnimation(90, 0, centerX, centerY, 310.0f,
						true);
			} else {
				rl_layout02.setVisibility(View.GONE);
				rl_layout01.setVisibility(View.VISIBLE);
				rotation = new RotateAnimation(90, 0, centerX, centerY, 310.0f,
						false);
			}

			rotation.setDuration(500);
			rotation.setFillAfter(true);
			rotation.setInterpolator(new DecelerateInterpolator());

			mContainer.startAnimation(rotation);
		}
	}

	@Override
	public void processMessage(Message message) {

	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		mapView.onSaveInstanceState(outState);
	}

	@Override
	public void activate(OnLocationChangedListener arg0) {
		mListener = arg0;
	}

	@Override
	public void deactivate() {
		mListener = null;
	}

	@Override
	protected void onPause() {
		super.onPause();
		unregisterReceiver(broadCastData);
		mapView.onPause();
	}

	@Override
	protected void onResume() {
		super.onResume();
		mapView.onResume();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		stopService(service);
		if (mapView != null) {
			deactivate();
			map.removeView(mapView);
			mapView.onDestroy();
			mapView = null;
		}
	}

	class BroadCastData extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			String action = intent.getAction();
			L.d(TAG, "action��" + action);
			if (BroadCastConfig.LOCATION.equals(action)) {
				if (mListener != null && aMapLocation != null) {
					mListener.onLocationChanged(aMapLocation);
					aMapLocation = null;
				}
			} else if (BroadCastConfig.NAVIGATION.equals(action)) {
				StringBuffer sbf = new StringBuffer();
				sbf.append("��ǰ·����");
				sbf.append(intent.getStringExtra("CurrentRoadName"));
				sbf.append(" ����·����");
				sbf.append(intent.getStringExtra("NextRoadName"));
				sbf.append(" ��ǰ����������");
				sbf.append(strActions[intent.getIntExtra("m_Icon", 0)]);
				sbf.append(" ��һ��������룺");
				sbf.append(intent.getIntExtra("CurStepRetainDistance",0));
				sbf.append(" ʣ���ọ́�");
				sbf.append(intent.getIntExtra("PathRetainDistance",0));
				if (intent.getIntExtra("CameraDistance", 0) != -1) {
					sbf.append(" ����ͷ���ͣ�");
					if (intent.getIntExtra("CameraType", 0) == 0) {
						sbf.append("����");
					}
					if (intent.getIntExtra("CameraType", 0) == 1) {
						sbf.append("���");
					}
					sbf.append(" ����ͷ���룺");
					sbf.append(intent.getIntExtra("CameraDistance", 0));
				}
				L.d(TAG, sbf.toString());
				data.setText(sbf);
			} else if (BroadCastConfig.STARTNAVIG.equals(action)) {
				AMapLocationService.isSendData = false;
				map.setVisibility(View.GONE);
				navigation.setVisibility(View.VISIBLE);
			} else if (BroadCastConfig.STOPNAVIG.equals(action)) {
				AMapLocationService.isSendData = true;
				map.setVisibility(View.VISIBLE);
				navigation.setVisibility(View.GONE);
			}
		}

	}

	@Override
	public void onBackPressed() {
		finish();
	}
}
