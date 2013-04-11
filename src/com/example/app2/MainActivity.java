package com.example.app2;

import com.example.app2.myview.TestView;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.Window;

public class MainActivity extends Activity {

	private static final String DEBUG_TAG = "mytest";
	private int titleAndStatusHeight;
	private TestView pad;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Log.d(DEBUG_TAG, "oncreate1");
		setContentView(R.layout.activity_main);
		Log.d(DEBUG_TAG, "oncreate2");
		pad = (TestView)findViewById(R.id.pad);
	}

	public void onWindowFocusChanged(boolean hasFocus) {
		Log.d(DEBUG_TAG, "onWindowFocusChanged");
		titleAndStatusHeight = getWindow().findViewById(
				Window.ID_ANDROID_CONTENT).getTop();
		
	}

	public boolean onTouchEvent(MotionEvent ev) {
		int a = ev.getActionMasked();
		int x = (int) ev.getX();
		int y = (int) ev.getY() - titleAndStatusHeight;
		Log.d(DEBUG_TAG,
				"activity a:" + Integer.toString(a) + " x:" + Integer.toString(x)
						+ " y:" + Integer.toString(y));

		return true;
	}

}
