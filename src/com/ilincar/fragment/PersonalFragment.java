package com.ilincar.fragment;

import com.ilincar.carlauncher.R;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * �˺Ž�������
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
	 * ��ʼ���ؼ�
	 */
	private void init() {

	}
}
