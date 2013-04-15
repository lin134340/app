package com.example.app2;

import android.app.Activity;
import android.os.Bundle;
import android.text.Layout;
import android.text.Selection;
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
	private TouchState touchState;

	// private float[] tPos = new float[2];

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Log.d(DEBUG_TAG, "oncreate before setContentView");
		setContentView(R.layout.activity_main);
		Log.d(DEBUG_TAG, "oncreate after setContentView");
		pad = (TestView) findViewById(R.id.pad);
		txt = (EditText) findViewById(R.id.txt);
		setTouchState(TouchState.NOTOUCH);
		pad.setOnTouchListener(new OnTouchListener() {
			public boolean onTouch(View v, MotionEvent ev) {
				int action = ev.getActionMasked();
				pad.tPos = pad.new TouchPosition(ev.getX(), ev.getY());
				if (action == MotionEvent.ACTION_DOWN) {
					pad.ptPos = pad.tPos;
				}
				int btnDown = (pad.tPos.dis > 50 && pad.tPos.dis < 80) ? ((int) pad.tPos.ang / 45)
						: (8);
				Log.d(DEBUG_TAG,
						"ontouch action:" + Integer.toString(action) + " x:"
								+ Float.toString(pad.tPos.x) + " y:"
								+ Float.toString(pad.tPos.y) + "dis:"
								+ Float.toString(pad.tPos.dis) + " ang:"
								+ Float.toString(pad.tPos.ang) + " btnDown:"
								+ Integer.toString(btnDown));

				Log.d(DEBUG_TAG, "ontouch touchstate_before:" + touchState);
				if (touchState.equals(TouchState.NOTOUCH)) {
					if (action == MotionEvent.ACTION_DOWN) {
						if (pad.tPos.dis < 50) {
							// skip long press
							setTouchState(TouchState.NAV);
						} else if (pad.tPos.dis > 50 && pad.tPos.dis < 80) {
							// on button process
							// cast
							// setTouchState(TouchState.CAST);
						} else {
							setTouchState(TouchState.INVALID);
						}
					}
				} else if (touchState.equals(TouchState.INVALID)) {
					if (action == MotionEvent.ACTION_UP) {
						setTouchState(TouchState.NOTOUCH);
					}
				} else if (touchState.equals(TouchState.INTONAV)) {

				} else if (touchState.equals(TouchState.LONGPRESS)) {

				} else if (touchState.equals(TouchState.NAV)) {
					if (action == MotionEvent.ACTION_UP) {
						setTouchState(TouchState.NOTOUCH);
					} else if (action == MotionEvent.ACTION_MOVE) {
						TestView.MoveDirection d = pad.getDirection();
						Log.d(DEBUG_TAG, "" + d);
						if (d == null) {
							//
						} else if (d.equals(TestView.MoveDirection.LEFT)) {
							Selection.moveLeft(txt.getText(), txt.getLayout());
						} else if (d.equals(TestView.MoveDirection.RIGHT)) {
							Selection.moveRight(txt.getText(), txt.getLayout());
						} else if (d.equals(TestView.MoveDirection.DOWN)) {
							Selection.moveDown(txt.getText(), txt.getLayout());
						} else if (d.equals(TestView.MoveDirection.UP)) {
							Selection.moveUp(txt.getText(), txt.getLayout());
						}
					} else {
						// undefined
					}
				} else if (touchState.equals(TouchState.DBCLICK)) {

				} else if (touchState.equals(TouchState.CAST)) {

				} else if (touchState.equals(TouchState.WAITNEXTBTN)) {

				}
				Log.d(DEBUG_TAG, "ontouch touchstate_after:" + touchState);

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

				if (action == MotionEvent.ACTION_MOVE) {
					pad.ptPos = pad.tPos;
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

	public TouchState getTouchState() {
		return touchState;
	}

	public void setTouchState(TouchState ts) {
		touchState = ts;
	}

	public enum TouchState {
		NOTOUCH, INVALID, INTONAV, LONGPRESS, NAV, DBCLICK, CAST, WAITNEXTBTN
	}

}
