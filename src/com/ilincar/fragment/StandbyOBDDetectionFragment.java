package com.ilincar.fragment;

import com.ilincar.carlauncher.R;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * OBDºÏ≤‚“≥√Ê
 * 
 * @author liujh
 * 
 */
public class StandbyOBDDetectionFragment extends Fragment {
	private static View view;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.standby_obd_detection_fragment, null);
		init();
		return view;
	}

	private void init() {

	}
}
