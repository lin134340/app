package slide;

import com.slide.R;

import android.app.Activity;
import android.os.Bundle;
import android.text.Selection;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;

import slide.view.PadView;

public class MainActivity extends Activity {

	private static final String DEBUG_TAG = "mytest";
	private int titleAndStatusHeight;
	private PadView pad;
	private EditText txt;
	private Button btn1;
	private TouchState touchState;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Log.d(DEBUG_TAG, "oncreate before setContentView");
		setContentView(R.layout.activity_main);
		Log.d(DEBUG_TAG, "oncreate after setContentView");
		pad = (PadView) findViewById(R.id.pad);
		txt = (EditText) findViewById(R.id.txt);
		btn1 = (Button) findViewById(R.id.btn1);
		setTouchState(TouchState.NOTOUCH);

		btn1.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				deleteChr();
			}
		});

		pad.setOnTouchListener(new OnTouchListener() {
			public boolean onTouch(View v, MotionEvent ev) {
				int action = ev.getActionMasked();
				pad.tPos = pad.new TouchPosition(ev.getX(), ev.getY());
				if (action == MotionEvent.ACTION_DOWN) {
					pad.ptPos = pad.tPos;
				}
				int btnDown = pad.tPos.btnId;
				// int pbtnDown = pad.ptPos.btnId;
				Log.d(DEBUG_TAG,
						"ontouch action:" + Integer.toString(action) + " x:"
								+ Float.toString(pad.tPos.x) + " y:"
								+ Float.toString(pad.tPos.y) + " dis:"
								+ Float.toString(pad.tPos.dis) + " ang:"
								+ Float.toString(pad.tPos.ang));
				Log.d(DEBUG_TAG, "btnDown:" + Integer.toString(btnDown)
						+ " btnText:" + pad.tPos.btnText + " corner:"
						+ pad.tPos.corner);

				// touch event state
				Log.d(DEBUG_TAG, "ontouch touchstate_before:" + touchState);
				if (touchState.equals(TouchState.NOTOUCH)) {
					if (action == MotionEvent.ACTION_DOWN) {
						if (pad.tPos.dis < pad.navR) {
							// skip long press
							setTouchState(TouchState.NAV);
						} else if (pad.tPos.dis > pad.navR
								&& pad.tPos.dis < pad.boardR) {
							pad.setBtnOnCast(btnDown);
							pad.setBtnOnTouch(btnDown);
							setTouchState(TouchState.CAST);
						} else if (pad.tPos.corner == 1) {
							pad.setNumerEnalbe(!pad.getNumberEnalbe());
						} else if (pad.tPos.corner == 2) {
							pad.setUpperEnalbe(!pad.getUpperEnalbe());
						} else if (pad.tPos.corner == 4) {
							deleteChr();
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
						PadView.MoveDirection d = pad.getDirection();
						Log.d(DEBUG_TAG, "" + d);
						if (d == null) {
							//
						} else if (d.equals(PadView.MoveDirection.LEFT)) {
							Selection.moveLeft(txt.getText(), txt.getLayout());
						} else if (d.equals(PadView.MoveDirection.RIGHT)) {
							Selection.moveRight(txt.getText(), txt.getLayout());
						} else if (d.equals(PadView.MoveDirection.DOWN)) {
							Selection.moveDown(txt.getText(), txt.getLayout());
						} else if (d.equals(PadView.MoveDirection.UP)) {
							Selection.moveUp(txt.getText(), txt.getLayout());
						}
					} else {
						// undefined
					}
				} else if (touchState.equals(TouchState.DBCLICK)) {

				} else if (touchState.equals(TouchState.CAST)) {
					if (action == MotionEvent.ACTION_UP) {
						// Log.d(DEBUG_TAG, "123123" + pad.tPos.btnText);
						insertChr(pad.tPos.btnText);
						pad.setBtnOnCast(pad.getSetN());
						setTouchState(TouchState.NOTOUCH);
					} else if (action == MotionEvent.ACTION_MOVE) {
						if (pad.tPos.dis < pad.navR) {
							insertChr(pad.ptPos.btnText);
							pad.setBtnOnCast(pad.getSetN());
							setTouchState(TouchState.WAITNEXTBTN);
						} else if (pad.tPos.dis > pad.navR
								&& pad.tPos.dis < pad.boardR
								&& pad.tPos.btnText != null) {
							if (pad.getBtnOnTouch() != btnDown) {
								pad.setBtnOnTouch(btnDown);
							}
						} else {
							pad.setBtnOnCast(pad.getSetN());
							setTouchState(TouchState.INVALID);
						}
					}
				} else if (touchState.equals(TouchState.WAITNEXTBTN)) {
					if (action == MotionEvent.ACTION_UP) {
						setTouchState(TouchState.NOTOUCH);
					} else if (action == MotionEvent.ACTION_MOVE) {
						if (pad.tPos.dis > pad.navR
								&& pad.tPos.dis < pad.boardR) {
							pad.setBtnOnCast(btnDown);
							pad.setBtnOnTouch(btnDown);
							setTouchState(TouchState.CAST);
						}
					}
				}
				Log.d(DEBUG_TAG, "ontouch touchstate_after:" + touchState);

				if (action == MotionEvent.ACTION_MOVE) {
					pad.ptPos = pad.tPos;
				}
				return true;
			}
		});

	}

	public void deleteChr() {
		int start = txt.getSelectionStart();
		int end = txt.getSelectionEnd();
		txt.getText().delete(
				(start == end && start != 0) ? (start - 1) : start, end);
	}

	public void insertChr(String s) {
		int start = txt.getSelectionStart();
		int end = txt.getSelectionEnd();
		txt.getText().delete(start, end);
		txt.getText().insert(start, s);
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
