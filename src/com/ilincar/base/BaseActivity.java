package com.ilincar.base;

import com.ilincar.config.MessageConfig;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentActivity;
import android.widget.Toast;

public abstract class BaseActivity extends FragmentActivity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		ActivityList.addActiviy(this);
		super.onCreate(savedInstanceState);
	}

	public abstract void processMessage(Message message);

	private static Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			int type = msg.what;
			switch (type) {
			case MessageConfig.NETERR:
				Toast.makeText(ActivityList.getLastActivity(), "网络不给力，请检查网络。",
						0).show();
				break;
			default:
				ActivityList.getLastActivity().processMessage(msg);
				break;
			}
		}
	};

	public static void sendMsg(Message msg) {
		handler.sendMessage(msg);
	}
}
