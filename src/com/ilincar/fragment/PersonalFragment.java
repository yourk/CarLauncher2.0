package com.ilincar.fragment;

import com.ilincar.carlauncher.R;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * 账号交互界面
 * 
 * @author liujh
 * 
 */
public class PersonalFragment extends Fragment {
	private View view;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.personal_fragment, null);
		init();
		return view;
	}

	/**
	 * 初始化控件
	 */
	private void init() {

	}
}
