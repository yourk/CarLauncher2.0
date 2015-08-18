package com.ilincar.view;

import com.ilincar.fragment.StandbyFragment;
import com.ilincar.util.L;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

public class MyViewPage extends ViewPager {

	public MyViewPage(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public MyViewPage(Context context) {
		super(context);
	}

	@Override
	public boolean dispatchTouchEvent(MotionEvent ev) {
		if (ev.getPointerCount() == 2)
			return false;
//		if (StandbyFragment.verticalPager != null){
//			L.d(">"+StandbyFragment.verticalPager.getPageNumber());
//			if (StandbyFragment.verticalPager.getPageNumber()!=0)
//				return false;
//		}
		try {
			return super.dispatchTouchEvent(ev);
		} catch (IllegalArgumentException illegalArgumentException) {
			illegalArgumentException.printStackTrace();
		}
		return false;
	}

	@Override
	public boolean onInterceptTouchEvent(MotionEvent ev) {
		if (ev.getPointerCount() == 2)
			return false;
//		if (StandbyFragment.verticalPager != null){
//			if (StandbyFragment.verticalPager.getPageNumber()!=0)
//				return false;
//		}
		try {
			return super.onInterceptTouchEvent(ev);
		} catch (IllegalArgumentException illegalArgumentException) {
			illegalArgumentException.printStackTrace();
		}
		return false;
	}

	@Override
	public boolean onTouchEvent(MotionEvent ev) {
		if (ev.getPointerCount() == 2)
			return false;
//		if (StandbyFragment.verticalPager != null){
//			if (StandbyFragment.verticalPager.getPageNumber()!=0)
//				return false;
//		}
		try {
			return super.onTouchEvent(ev);
		} catch (IllegalArgumentException illegalArgumentException) {
			illegalArgumentException.printStackTrace();
		}
		return false;
	}
}
