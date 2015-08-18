package com.ilincar.fragment;

import com.ilincar.carlauncher.R;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * 待机显示时间页
 * 
 * @author liujh
 * 
 */
public class StandbyTimeFragment extends Fragment {
	private View view;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.standby_time_fragment, null);
		return view;
	}

	private void init() {

	}
}
