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
				int btnDown = pad.tPos.btnNum;
				int pbtnDown = pad.ptPos.btnNum;
				Log.d(DEBUG_TAG,
						"ontouch action:" + Integer.toString(action) + " x:"
								+ Float.toString(pad.tPos.x) + " y:"
								+ Float.toString(pad.tPos.y) + "dis:"
								+ Float.toString(pad.tPos.dis) + " ang:"
								+ Float.toString(pad.tPos.ang) + " btnDown:"
								+ Integer.toString(btnDown));

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
						insertChr(pad.btnGroup[pad.getBtnOnCast()]
								.getStrByNum(btnDown));
						pad.setBtnOnCast(8);
						setTouchState(TouchState.NOTOUCH);
					} else if (action == MotionEvent.ACTION_MOVE) {
						if (pad.tPos.dis < pad.navR) {
							insertChr(pad.btnGroup[pad.getBtnOnCast()]
									.getStrByNum(pbtnDown));
							pad.setBtnOnCast(8);
							setTouchState(TouchState.WAITNEXTBTN);
						} else if (pad.tPos.dis > pad.navR
								&& pad.tPos.dis < pad.boardR
								&& pad.btnGroup[pad.getBtnOnCast()]
										.getStrByNum(btnDown) != null) {
							if (pad.getBtnOnTouch() != btnDown) {
								pad.setBtnOnTouch(btnDown);
							}
						} else {
							pad.setBtnOnCast(8);
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
		txt.getText().delete((start == end) ? (start - 1) : start, end);
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
