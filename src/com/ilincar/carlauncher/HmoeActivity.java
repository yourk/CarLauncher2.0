package com.ilincar.carlauncher;

import java.util.ArrayList;

import android.app.Service;
import android.media.AudioManager;
import android.os.Bundle;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.GestureDetector.OnGestureListener;
import android.view.GestureDetector;
import android.view.MotionEvent;

import com.ilincar.adapter.HomeFragmentAdapter;
import com.ilincar.base.BaseActivity;
import com.ilincar.fragment.ItselfApplicationFragment;
import com.ilincar.fragment.PersonalFragment;
import com.ilincar.fragment.SetStoreTrafficFragment;
import com.ilincar.fragment.StandbyFragment;
import com.ilincar.view.MyViewPage;

/**
 * 横向页面控制管理
 * 
 * @author liujh
 * 
 */
public class HmoeActivity extends BaseActivity implements OnGestureListener {
	private static MyViewPage home;
	private static ArrayList<Fragment> arrayFragment;
	private static ItselfApplicationFragment itselfApplicationFragment;
	private static PersonalFragment personalFragment;
	private static SetStoreTrafficFragment setStoreTrafficFragment;
	private static StandbyFragment standbyFragment;
	private static HomeFragmentAdapter homeFragmentAdapter;
	private static AudioManager mAudioManager;
	private static GestureDetector mGestureDetector;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.home_activity);
		init();
	}

	/**
	 * 初始化数据
	 */
	private void init() {
		home = (MyViewPage) findViewById(R.id.home);
		arrayFragment = new ArrayList<Fragment>();
		personalFragment = new PersonalFragment();
		arrayFragment.add(personalFragment);
		setStoreTrafficFragment = new SetStoreTrafficFragment();
		arrayFragment.add(setStoreTrafficFragment);
		standbyFragment = new StandbyFragment(this);
		arrayFragment.add(standbyFragment);
		itselfApplicationFragment = new ItselfApplicationFragment();
		arrayFragment.add(itselfApplicationFragment);
		homeFragmentAdapter = new HomeFragmentAdapter(
				getSupportFragmentManager(), arrayFragment);
		home.setAdapter(homeFragmentAdapter);
		home.setCurrentItem(2);
		mGestureDetector = new GestureDetector(this, this);
		mGestureDetector.setIsLongpressEnabled(true);
		mAudioManager = (AudioManager) getSystemService(Service.AUDIO_SERVICE);
	}

	@Override
	public void processMessage(Message message) {
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		if (event.getPointerCount() == 2) {
			mGestureDetector.onTouchEvent(event);
		}
		return false;
	}

	@Override
	public boolean onDown(MotionEvent e) {
		return false;
	}

	@Override
	public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
			float velocityY) {
		return false;
	}

	@Override
	public void onLongPress(MotionEvent e) {

	}

	@Override
	public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX,
			float distanceY) {
		if (distanceX > 12 && 3 > distanceY && -3 < distanceY) {
			Log.d("way", "向左" + (int) distanceX + ",distanceY=" + distanceY);
			mAudioManager.adjustStreamVolume(AudioManager.STREAM_MUSIC,
					AudioManager.ADJUST_LOWER, AudioManager.FLAG_SHOW_UI);
		} else if (distanceX < -12 && 3 > distanceY && -3 < distanceY) {
			Log.d("way", "向右" + (int) distanceX + ",distanceY=" + distanceY);
			mAudioManager.adjustStreamVolume(AudioManager.STREAM_MUSIC,
					AudioManager.ADJUST_RAISE, AudioManager.FLAG_SHOW_UI);
		}
		return false;
	}

	@Override
	public void onShowPress(MotionEvent e) {

	}

	@Override
	public boolean onSingleTapUp(MotionEvent e) {
		return false;
	}

}
