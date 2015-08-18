package com.ilincar.base;

import com.ilincar.util.L;

import android.app.Application;

public class BaseApplication extends Application {
	@Override
	public void onCreate() {
		super.onCreate();
		//打开BUG调试信息
		L.isDebug=true;
	}
}
