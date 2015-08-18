package com.ilincar.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class AppSettings {
	public static final String PREFER_NAME = "CheLianLian_Setting";
	public static int MODE = 3;
	/**
	 * 获取配置文件
	 * @param context
	 * @param key  根据key获取数据
	 * @param defaultValue   如果key对应为空，则输出此默认值
	 * @return
	 */
	public static String getPrefString(Context context, String key,
			final String defaultValue) {
		final SharedPreferences settings = context.getSharedPreferences(PREFER_NAME,
				MODE);
		return settings.getString(key, defaultValue);
	}
	/**
	 * 添加一条key-value 数据
	 * @param context
	 * @param key
	 * @param value
	 */
	public static void setPrefString(Context context, final String key,
			final String value) {
//		final SharedPreferences settings = PreferenceManager
//				.getDefaultSharedPreferences(context);
		final SharedPreferences settings = context.getSharedPreferences(PREFER_NAME,
				MODE);
		settings.edit().putString(key, value).commit();
	}
	/**
	 * 清空配置文件所有数据
	 * @param context
	 * @param p
	 */
	public static void clearPreference(Context context,
			final SharedPreferences p) {
		
		final Editor editor = p.edit();

		editor.clear();
		editor.commit();
	}
	/**
	 * 移除相关key的数据
	 * @param context
	 * @param key
	 */
	public static void removePreference(Context context,final String key){
		final SharedPreferences settings = context.getSharedPreferences(PREFER_NAME,
				MODE);
		settings.edit().remove(key).commit();
	}
}


