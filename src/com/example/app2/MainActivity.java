package com.example.app2;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.Window;
import android.widget.EditText;

import com.example.app2.myview.TestView;

public class MainActivity extends Activity {

	private static final String DEBUG_TAG = "mytest";
	private int titleAndStatusHeight;
	private TestView pad;
	private EditText txt;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Log.d(DEBUG_TAG, "oncreate before setContentView");
		setContentView(R.layout.activity_main);
		Log.d(DEBUG_TAG, "oncreate after setContentView");
		pad = (TestView) findViewById(R.id.pad);
		txt = (EditText) findViewById(R.id.txt);
		pad.setOnTouchListener(new OnTouchListener() {
			public boolean onTouch(View v, MotionEvent ev) {
				int action = ev.getActionMasked();
				// the x,y value is the relative value to testview
				int x = (int) ev.getX();
				int y = (int) ev.getY();
				Log.d(DEBUG_TAG,
						"ontouch action:" + Integer.toString(action) + " x:"
								+ Integer.toString(x) + " y:"
								+ Integer.toString(y));
				float dis = pad.getDistance(x, y);
				float ang = pad.getAngle(x, y);
				int btnDown = (dis > 50 && dis < 80) ? ((int) ang / 45) : (8);
				Log.d(DEBUG_TAG,
						"ontouch dis:" + Float.toString(dis) + " ang:"
								+ Float.toString(ang) + " btnDown:"
								+ Integer.toString(btnDown));

				if (action == MotionEvent.ACTION_DOWN
						|| action == MotionEvent.ACTION_MOVE) {
					if (pad.getBtnOnTouch() != btnDown) {
						pad.setBtnOnTouch(btnDown);
						if (btnDown >= 0 && btnDown <= 7) {
							txt.append(Integer.toString(btnDown));
						}
					}
				} else if (action == MotionEvent.ACTION_UP) {
					if (pad.getBtnOnTouch() != 8) {
						pad.setBtnOnTouch(8);
					}
				}

				return true;
			}
		});
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
				"activity a:" + Integer.toString(a) + " x:"
						+ Integer.toString(x) + " y:" + Integer.toString(y));

		return true;
	}

}
