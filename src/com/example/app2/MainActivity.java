package com.example.app2;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.Rect;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.Window;
//import android.view.ViewGroup;
import com.example.app2.myview.TestView;

public class MainActivity extends Activity {

	// private static final String DEBUG_TAG = "Gestures";
	// private GestureDetectorCompat mDetector;
	private static final String DEBUG_TAG = "mytest";
	private int titleStatusHeight;
	private Rect r1, r2, r3;
	private TestView v1, v2, v3;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Log.d(DEBUG_TAG, "oncreate1");
		setContentView(R.layout.activity_main);
		Log.d(DEBUG_TAG, "oncreate2");
		// mDetector = new GestureDetectorCompat(this, this);
		// mDetector.setOnDoubleTapListener(this);
		/*
		 * r1 = new Rect(); r2 = new Rect(); r3 = new Rect(); v1 =
		 * (TestView)findViewById(R.id.s1); v2 =
		 * (TestView)findViewById(R.id.s2); v3 =
		 * (TestView)findViewById(R.id.s3);
		 */

	}

	public void onWindowFocusChanged(boolean hasFocus) {
		Log.d(DEBUG_TAG, "onWindowFocusChanged");
		titleStatusHeight = getWindow().findViewById(Window.ID_ANDROID_CONTENT)
				.getTop();
		calViewRect();
	}

	public void calViewRect() {
		int[] lo = new int[2];
		int l,r,t,b;
		v1 = (TestView) findViewById(R.id.s1);
		v1.getLocationOnScreen(lo);
		l = lo[0];
		r = l + v1.getWidth();
		t = lo[1] - titleStatusHeight;
		b = t + v1.getHeight();
		r1 = new Rect(l, t, r, b);
		Log.d(DEBUG_TAG,
				"r1L:" + Integer.toString(r1.left) + " R:"
						+ Integer.toString(r1.right) + " T:"
						+ Integer.toString(r1.top) + " B:"
						+ Integer.toString(r1.bottom));

		v2 = (TestView) findViewById(R.id.s2);
		v2.getLocationOnScreen(lo);
		l = lo[0];
		r = l + v2.getWidth();
		t = lo[1] - titleStatusHeight;
		b = t + v2.getHeight();
		r2 = new Rect(l, t, r, b);
		Log.d(DEBUG_TAG,
				"r2L:" + Integer.toString(r2.left) + " R:"
						+ Integer.toString(r2.right) + " T:"
						+ Integer.toString(r2.top) + " B:"
						+ Integer.toString(r2.bottom));

		v3 = (TestView) findViewById(R.id.s3);
		v3.getLocationOnScreen(lo);
		l = lo[0];
		r = l + v3.getWidth();
		t = lo[1] - titleStatusHeight;
		b = t + v3.getHeight();
		r3 = new Rect(l, t, r, b);
		Log.d(DEBUG_TAG,
				"r3L:" + Integer.toString(r3.left) + " R:"
						+ Integer.toString(r3.right) + " T:"
						+ Integer.toString(r3.top) + " B:"
						+ Integer.toString(r3.bottom));

	}

	public boolean onTouchEvent(MotionEvent ev) {
		int a = ev.getActionMasked();
		int x = (int) ev.getX();
		int y = (int) ev.getY() - titleStatusHeight;
		Log.d(DEBUG_TAG,
				"a:" + Integer.toString(a) + " x:" + Integer.toString(x)
						+ " y:" + Integer.toString(y));
		if (a == MotionEvent.ACTION_DOWN || a == MotionEvent.ACTION_MOVE) {
			if (r1.contains(x, y)) {
				if (v1.getColor() == Color.BLUE) {
					v1.setColor(Color.RED);
				}
			} else if (v1.getColor() == Color.RED) {
				v1.setColor(Color.BLUE);
			}
			if (r2.contains(x, y)) {
				if (v2.getColor() == Color.BLUE) {
					v2.setColor(Color.RED);
				}
			} else if (v2.getColor() == Color.RED) {
				v2.setColor(Color.BLUE);
			}
			if (r3.contains(x, y)) {
				if (v3.getColor() == Color.BLUE) {
					v3.setColor(Color.RED);
				}
			} else if (v3.getColor() == Color.RED) {
				v3.setColor(Color.BLUE);
			}
		} else if (a == MotionEvent.ACTION_UP) {
			if (v1.getColor() == Color.RED) {
				v1.setColor(Color.BLUE);
			}
			if (v2.getColor() == Color.RED) {
				v2.setColor(Color.BLUE);
			}
			if (v3.getColor() == Color.RED) {
				v3.setColor(Color.BLUE);
			}
		} else {
			Log.d(DEBUG_TAG, "else action:" + Integer.toString(a));
		}
		return true;
	}

	/*
	 * @Override public boolean onDown(MotionEvent event) { Log.d(DEBUG_TAG,
	 * "onDown: " + event.toString()); return true; }
	 * 
	 * @Override public boolean onFling(MotionEvent event1, MotionEvent event2,
	 * float velocityX, float velocityY) { Log.d(DEBUG_TAG, "onFling: " +
	 * event1.toString() + event2.toString()); return true; }
	 * 
	 * @Override public void onLongPress(MotionEvent event) { Log.d(DEBUG_TAG,
	 * "onLongPress: " + event.toString()); }
	 * 
	 * @Override public boolean onScroll(MotionEvent e1, MotionEvent e2, float
	 * distanceX, float distanceY) { Log.d(DEBUG_TAG, "onScroll: " +
	 * e1.toString() + e2.toString()); return true; }
	 * 
	 * @Override public void onShowPress(MotionEvent event) { Log.d(DEBUG_TAG,
	 * "onShowPress: " + event.toString()); }
	 * 
	 * @Override public boolean onSingleTapUp(MotionEvent event) {
	 * Log.d(DEBUG_TAG, "onSingleTapUp: " + event.toString()); return true; }
	 * 
	 * @Override public boolean onDoubleTap(MotionEvent event) {
	 * Log.d(DEBUG_TAG, "onDoubleTap: " + event.toString()); return true; }
	 * 
	 * @Override public boolean onDoubleTapEvent(MotionEvent event) {
	 * Log.d(DEBUG_TAG, "onDoubleTapEvent: " + event.toString()); return true; }
	 * 
	 * @Override public boolean onSingleTapConfirmed(MotionEvent event) {
	 * Log.d(DEBUG_TAG, "onSingleTapConfirmed: " + event.toString()); return
	 * true; }
	 */

}
