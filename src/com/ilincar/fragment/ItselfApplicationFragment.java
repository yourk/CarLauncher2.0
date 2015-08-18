package com.ilincar.fragment;

import com.ilincar.carlauncher.R;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * 自动功能应用
 * 
 * @author liujh
 * 
 */
public class ItselfApplicationFragment extends Fragment {
	private static View view;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.itself_application_fragment, null);
		init();
		return view;
	}

	private void init() {

	}
}
