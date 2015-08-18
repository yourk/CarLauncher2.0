package com.ilincar.view;

import com.ilincar.util.L;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Interpolator;
import android.widget.Scroller;
import android.widget.Toast;

public class VerticalPager extends ViewGroup {

	private Scroller mScroller;
	private Context mContext;
	private static int deltaY;
	private VelocityTracker mVt = null;
	private float mVx = 0;
	private float mVy = 0;

	public VerticalPager(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.mContext = context;
		mScroller = new Scroller(context);
		// mScroller=new Scroller(mContext, new Interpolator() {
		//
		// @Override
		// public float getInterpolation(float input) {
		// return 300;
		// }
		// });

	}

	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
		int totalHeight = 0;
		int count = getChildCount();

		for (int i = 0; i < count; i++) {
			View childView = getChildAt(i);

			// int measureHeight=childView.getMeasuredHeight();
			// int measureWidth=childView.getMeasuredWidth();

			childView.layout(l, totalHeight, r, totalHeight + b);

			totalHeight += b;
		}
	}

	private VelocityTracker mVelocityTracker;

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		int width = MeasureSpec.getSize(widthMeasureSpec);
		int height = MeasureSpec.getSize(heightMeasureSpec);

		int count = getChildCount();
		for (int i = 0; i < count; i++) {
			getChildAt(i).measure(width, height);
		}
		setMeasuredDimension(width, height);
	}

	private int mLastMotionY;

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		if (mVelocityTracker == null) {
			mVelocityTracker = VelocityTracker.obtain();
		}
		mVelocityTracker.addMovement(event);

		int action = event.getAction();

		float y = event.getY();

		switch (action) {
		case MotionEvent.ACTION_DOWN:
			L.d("getScrollY:"+getScrollY());
			if (getPageNumber() != 0) {
				getParent().requestDisallowInterceptTouchEvent(true);
			}
			if (!mScroller.isFinished()) {
				mScroller.abortAnimation();
			}
			mLastMotionY = (int) y;
			mVt = VelocityTracker.obtain(); // 建立一个新的对象
			mVt.addMovement(event); // 增加一个采样点
			Log.d("montion", "" + getScrollY());
			break;
		case MotionEvent.ACTION_MOVE:
			if (getPageNumber() != 0) {
				getParent().requestDisallowInterceptTouchEvent(true);
			}
			deltaY = (int) (mLastMotionY - y);
			if (getScrollY() >= -1 && getScrollY() <= 1 && deltaY < 0)
				break;
			if (getScrollY() >= getHeight() * (getChildCount() - 1) - 1
					&& getScrollY() <= getHeight() * (getChildCount() - 1) + 1
					&& deltaY > 0)
				break;
			if (Math.abs(deltaY) > 2)
				scrollBy(0, deltaY);
			invalidate();
			mLastMotionY = (int) y;
			// 添加采样点
			mVt.addMovement(event);
			break;
		case MotionEvent.ACTION_UP:
			if (getPageNumber() != 0) {
				getParent().requestDisallowInterceptTouchEvent(true);
			}
			if (mVelocityTracker != null) {
				mVelocityTracker.recycle();
				mVelocityTracker = null;
			}
			mVt.computeCurrentVelocity(1000);
			mVx = mVt.getXVelocity();// 得到当前手指在这一点移动的速度
			mVy = mVt.getYVelocity();// 速度分为了x和y轴两个方向的速度
			L.d("速度:" + mVx + "," + mVy);
			if (getScrollY() < 3 && getScrollY() > -3)
				break;
			if (getScrollY() > (getHeight() - 3)
					&& getScrollY() < (getHeight() + 3))
				break;
			if (getScrollY() > (getHeight() * 2 - 3)
					&& getScrollY() < (getHeight() * 2 + 3))
				break;
			if (getScrollY() < 0) {
				mScroller.startScroll(0, getScrollY(), 0, -getScrollY());
			} else if (getScrollY() > (getHeight() * (getChildCount() - 1))) {
				View lastView = getChildAt(getChildCount() - 1);
				mScroller.startScroll(0, lastView.getTop() + 300, 0, -300);
			} else {
				int position = getScrollY() / getHeight();
				int mod = getScrollY() % getHeight();
				L.d("mod=" + mod + "," + (getHeight() / 3) + "," + deltaY + ","
						+ getScrollY());
				if (deltaY > 0) {
					if (mod > getHeight() / 3) {
						View positionView = getChildAt(position + 1);
						mScroller.startScroll(0, positionView.getTop() - 300,
								0, +300);
					} else {
						View positionView = getChildAt(position);
						mScroller.startScroll(0, positionView.getTop() + 300,
								0, -300);
					}
				} else {
					if ((getHeight() - mod) < getHeight() / 3) {
						View positionView = getChildAt(position + 1);
						mScroller.startScroll(0, positionView.getTop() - 300,
								0, +300);
					} else {
						View positionView = getChildAt(position);
						mScroller.startScroll(0, positionView.getTop() + 300,
								0, -300);
					}
				}
			}
			invalidate();
			break;
		}

		return true;
	}

	@Override
	public void computeScroll() {
		super.computeScroll();

		if (mScroller.computeScrollOffset()) {
			scrollTo(0, mScroller.getCurrY());
		} else {

		}
	}

	public int getPageNumber() {
		return getScrollY() / getHeight();
	}

	public void setPage(int page) {
		if (getChildCount() > page) {
			View positionView = getChildAt(page);
			mScroller.startScroll(0, positionView.getTop() - 720, 0, +720);
		}
	}
}
