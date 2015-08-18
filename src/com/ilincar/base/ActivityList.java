package com.ilincar.base;

import java.util.LinkedList;

public class ActivityList {
	private static LinkedList<BaseActivity> list = new LinkedList<BaseActivity>();

	public static void addActiviy(BaseActivity a) {
		if (!list.contains(a)) {
			list.add(a);
		}
	}

	public static BaseActivity getLastActivity() {
		return list.getLast();
	}

	public static void removeActivity(BaseActivity a) {
		if (!list.isEmpty()) {
			list.remove(a);
		}
	}

	public static void tuichu() {
		for (BaseActivity str : list) {
			str.finish();
		}
	}

}
