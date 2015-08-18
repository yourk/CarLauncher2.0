package com.ilincar.util;

import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.Uri;
import android.os.Bundle;

//跟App相关的辅助类
public class AppUtils {

	private AppUtils() {
		/* cannot be instantiated */
		throw new UnsupportedOperationException("cannot be instantiated");

	}

	/**
	 * 获取应用程序名称
	 */
	public static String getAppName(Context context) {
		try {
			PackageManager packageManager = context.getPackageManager();
			PackageInfo packageInfo = packageManager.getPackageInfo(
					context.getPackageName(), 0);
			int labelRes = packageInfo.applicationInfo.labelRes;
			return context.getResources().getString(labelRes);
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * [获取应用程序版本名称信息]
	 * 
	 * @param context
	 * @return 当前应用的版本名称
	 */
	public static String getVersionName(Context context) {
		try {
			PackageManager packageManager = context.getPackageManager();
			PackageInfo packageInfo = packageManager.getPackageInfo(
					context.getPackageName(), 0);
			return packageInfo.versionName;

		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	
	/**
	 * 判断系统中是否安装某种软件
	 * */
	public static boolean CheckApp(Context context,String appString) {
		PackageManager pm = context.getPackageManager();
		List<PackageInfo> infoList = pm.getInstalledPackages(PackageManager.GET_SERVICES);
		for (PackageInfo info : infoList) {
			if (appString.equals(info.packageName)) {
				return true;
			}
		}
		return false;
	}

	
	/**
	 * 下载软件
	 * */
	public static void DownLoadApp(Context context,String appString)
	{
		Uri uri=Uri.parse("market://details?id="+appString);
		Intent intent=new Intent(Intent.ACTION_VIEW,uri);
		context.startActivity(intent);
	}
	/**
	 * 跳转界面
	 */
	public static void startActivity(Activity currentActivity,Class<?> nextActivity,Bundle bundle){
		Intent intent=new Intent(currentActivity,nextActivity);
		if(bundle!=null){
			intent.putExtra("intent_data", bundle);
		}
		currentActivity.startActivity(intent);
	}

}
