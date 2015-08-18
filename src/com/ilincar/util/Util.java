package com.ilincar.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.Uri;
import android.os.Environment;

public class Util {
	// 获取时间
	public static String timeString() {
		SimpleDateFormat formatter = new SimpleDateFormat("HH:mm");
		Date curDate = new Date(System.currentTimeMillis());
		return formatter.format(curDate);
	}

	// 获取日期
	public static String dayString() {
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy.MM.dd");
		Date curDate = new Date(System.currentTimeMillis());
		return formatter.format(curDate);
	}

	// 获取星期几
	public static String stringData() {
		Calendar c = Calendar.getInstance();
		c.setTimeZone(TimeZone.getTimeZone("GMT+8:00"));
		// mYear = String.valueOf(c.get(Calendar.YEAR)); // 获取当前年份
		// mMonth = String.valueOf(c.get(Calendar.MONTH) + 1);// 获取当前月份
		// mDay = String.valueOf(c.get(Calendar.DAY_OF_MONTH));// 获取当前月份的日期号码
		String mWay = String.valueOf(c.get(Calendar.DAY_OF_WEEK));
		if ("1".equals(mWay)) {
			mWay = "日";
		} else if ("2".equals(mWay)) {
			mWay = "一";
		} else if ("3".equals(mWay)) {
			mWay = "二";
		} else if ("4".equals(mWay)) {
			mWay = "三";
		} else if ("5".equals(mWay)) {
			mWay = "四";
		} else if ("6".equals(mWay)) {
			mWay = "五";
		} else if ("7".equals(mWay)) {
			mWay = "六";
		}
		return "星期" + mWay;
	}

	/**
	 * 判断是否安装目标应用
	 * 
	 * @param packageName
	 *            目标应用安装后的包名
	 * @return 是否已安装目标应用
	 * @author zuolongsnail
	 * @throws NameNotFoundException
	 */
	public static boolean isInstallByread(Context context, String packageName) {
		PackageInfo packageInfo;
		try {
			packageInfo = context.getPackageManager().getPackageInfo(
					packageName, 0);
		} catch (NameNotFoundException e) {
			packageInfo = null;
			e.printStackTrace();
		}
		if (packageInfo == null) {
			// System.out.println("没有安装");
			return false;
		} else {
			// System.out.println("已经安装");
			return true;
		}
	}

	/**
	 * 先判断是否安装，已安装则启动目标应用程序，否则先安装
	 * 
	 * @param packageName
	 *            目标应用安装后的包名
	 * @param appPath
	 *            目标应用apk安装文件所在的路径
	 * @author zuolongsnail
	 */
	public static void launchApp(String packageName, String appPath,
			Context context, String name) {
		// 启动目标应用
		if (isInstallByread(context, packageName)) {
			// 获取目标应用安装包的Intent
			Intent intent = context.getPackageManager()
					.getLaunchIntentForPackage(packageName);
			context.startActivity(intent);
		}
		// 安装目标应用
		else {
			if (copyApkFromAssets(context, name, appPath)) {
				installApk(appPath, name, context);
			}
		}
	}

	public static boolean copyApkFromAssets(Context context, String fileName,
			String path) {
		boolean copyIsFinish = false;
		try {
			InputStream is = context.getAssets().open(fileName);
			File file = new File(path, fileName);
			file.createNewFile();
			FileOutputStream fos = new FileOutputStream(file);
			byte[] temp = new byte[1024];
			int i = 0;
			while ((i = is.read(temp)) > 0) {
				fos.write(temp, 0, i);
			}
			fos.flush();
			fos.close();
			is.close();
			copyIsFinish = true;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return copyIsFinish;
	}

	/**
	 * 安装APK文件
	 */
	public static void installApk(String mSavePath, String name, Context context) {
		File apkfile = new File(mSavePath, name);
		if (!apkfile.exists()) {
			return;
		}
		// 通过Intent安装APK文件
		Intent i = new Intent(Intent.ACTION_VIEW);
		i.setDataAndType(Uri.parse("file://" + apkfile.toString()),
				"application/vnd.android.package-archive");
		context.startActivity(i);
	}
}
