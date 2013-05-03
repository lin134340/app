package slide;

import java.io.IOException;
import java.io.InputStream;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import slide.view.PadView;
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
import android.widget.TextView;

import com.slide.R;

public class MainActivity extends Activity {

	private static final String DEBUG_TAG = "mytest";
	private int titleAndStatusHeight;
	private PadView pad;
	private EditText txt;
	private TextView py;
	private TextView selchar;
	private Button btn1;
	private Button btn2;
	private TouchState touchState;
	private JSONObject py2u;
	//private String tempstr;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Log.d(DEBUG_TAG, "oncreate before setContentView");
		setContentView(R.layout.activity_main);
		Log.d(DEBUG_TAG, "oncreate after setContentView");

		try {
			py2u = new JSONObject(loadJSONFromAsset());
			//tempstr = py2u.getJSONArray("a").getString(0);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		pad = (PadView) findViewById(R.id.pad);
		txt = (EditText) findViewById(R.id.txt);
		btn1 = (Button) findViewById(R.id.btn1);
		btn2 = (Button) findViewById(R.id.btn2);
		py = (TextView) findViewById(R.id.py);
		selchar = (TextView) findViewById(R.id.selchar);
		//btn2.setText(tempstr);
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
						insertPY(pad.tPos.btnText);
						pad.setBtnOnCast(pad.getSetN());
						setTouchState(TouchState.NOTOUCH);
					} else if (action == MotionEvent.ACTION_MOVE) {
						if (pad.tPos.dis < pad.navR) {
							insertPY(pad.ptPos.btnText);
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

	public String loadJSONFromAsset() {
		String json = null;
		try {
			InputStream is = getAssets().open("PinYinToUnicode.json");
			int size = is.available();
			byte[] buffer = new byte[size];
			is.read(buffer);
			is.close();
			json = new String(buffer, "UTF-8");
		} catch (IOException ex) {
			ex.printStackTrace();
			return null;
		}
		return json;
	}

	// if py is not empty, delete py first.
	// if py is empty, delete text.
	public void deleteChr() {
		CharSequence pySeq = py.getText();
		if (pySeq.length() != 0) {
			py.setText(pySeq.subSequence(0, pySeq.length() - 1));
			py2selchar();
		} else {
			int start = txt.getSelectionStart();
			int end = txt.getSelectionEnd();
			txt.getText().delete(
					(start == end && start != 0) ? (start - 1) : start, end);
		}
	}

	// append a char to py
	public void insertPY(String s) {
		CharSequence pySeq = py.getText() + s;
		py.setText(pySeq);
		py2selchar();
	}

	// transform py to selchar
	public void py2selchar() {
		try {
			String pySeq = (String) py.getText();
			JSONArray chrArray = py2u.getJSONArray(pySeq);
			String tempstr = "";
			for (int i = 0; i < chrArray.length(); i++) {
				tempstr += chrArray.getString(i);
			}
			selchar.setText(tempstr);
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	// insert a char to text
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
