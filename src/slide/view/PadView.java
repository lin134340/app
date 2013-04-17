package slide.view;

import java.util.Vector;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import com.slide.R;

public class PadView extends View {
	private final String DEBUG_TAG = "mytest";
	public float boardR = 80.0f;
	public float navR = 50.0f;
	private float btnPad = 1.0f;
	private float textSize = 8.0f;
	private Paint btnPaintNormal = new Paint();
	private Paint btnPaintDown = new Paint();
	private Paint btnPaintCast = new Paint();
	private Paint textPaint = new Paint();
	public Vector<Vector<PadButton>> padButtons = new Vector<Vector<PadButton>>();
	public TouchPosition ptPos;
	public TouchPosition tPos;
	public BtnGroup[] btnGroup = new BtnGroup[8];
	private int btnOnCast = 8;
	private int btnOnTouch = 8;

	public PadView(Context context, AttributeSet attrs) {
		super(context, attrs);
		Log.d(DEBUG_TAG, "testview constructor start");
		TypedArray a = context.getTheme().obtainStyledAttributes(attrs,
				R.styleable.PadView, 0, 0);
		try {
			boardR = a.getFloat(R.styleable.PadView_boardR, 80.0f);
			navR = a.getFloat(R.styleable.PadView_navR, 50.0f);
			btnPad = a.getFloat(R.styleable.PadView_btnPad, 1.0f);
			textSize = a.getFloat(R.styleable.PadView_textSize, 8.0f);
		} finally {
			a.recycle();
		}
		init();
		Log.d(DEBUG_TAG, "testview constructor end");
	}

	// If btnOnTouch is between 0 to 7, one of the eight buttons is being
	// pressed down.
	// Else none button is being pressed down.
	public void setBtnOnTouch(int b) {
		btnOnTouch = b;
		invalidate();
	}

	public int getBtnOnTouch() {
		return btnOnTouch;
	}

	// If btnOnCast is between 0 to 7, one of the eight buttons is casting.
	// Else none button is casting.
	public void setBtnOnCast(int b) {
		btnOnCast = b;
		if (btnOnCast == 8) {
			invalidate();
		}
	}

	public int getBtnOnCast() {
		return btnOnCast;
	}

	public float getDistance(float x1, float y1, float x2, float y2) {
		return (float) Math.sqrt((x1 - x2) * (x1 - x2) + (y1 - y2) * (y1 - y2));
	}

	// get the angle of a point from pad center
	public float getAngle(float x1, float y1, float x2, float y2) {
		// float toPoint = toCenter ? boardR : 0;
		float angle = (float) Math.toDegrees(Math.atan2(y1 - y2, x1 - x2));
		if (angle < 0) {
			angle += 360;
		}
		return angle;
	}

	public MoveDirection getDirection() {
		float ang = getAngle(tPos.y, tPos.x, ptPos.y, ptPos.x);
		float dis = getDistance(tPos.y, tPos.x, ptPos.y, ptPos.x);
		Log.d(DEBUG_TAG,
				"ang:" + Float.toString(ang) + " dis:" + Float.toString(dis));
		if (dis > 5) {
			if (ang > 0 && ang < 45 || ang > 315 && ang < 360) {
				return MoveDirection.DOWN;
			} else if (ang > 45 && ang < 135) {
				return MoveDirection.RIGHT;
			} else if (ang > 135 && ang < 225) {
				return MoveDirection.UP;
			} else if (ang > 225 && ang < 315) {
				return MoveDirection.LEFT;
			} else {
				// Log.d(DEBUG_TAG, "don't move");
				return null;
			}
		} else {
			return null;
		}
	}

	// PadButton defines one of eight buttons
	private class PadButton {
		public int num;
		public Path path;
		public TextPos textPos = new TextPos();
		public String text;

		public class TextPos {
			public float x;
			public float y;
		}

		public PadButton(Path p, int n, float tPosX, float tPosY) {
			path = new Path(p);
			num = n;
			textPos.x = tPosX;
			textPos.y = tPosY;
		}

		public void setText(String s) {
			text = s;
		}
	}

	public class BtnGroup {
		public Vector<SubBtn> subBtn;

		public BtnGroup(Object... p) {
			subBtn = new Vector<SubBtn>();
			for (int i = 0; i < p.length; i += 2) {
				subBtn.add(new SubBtn((String) p[i], (Integer) p[i + 1]));
			}
		}

		public String getStrByNum(int n) {
			for (int i = 0; i < subBtn.size(); i++) {
				if (subBtn.elementAt(i).bNum == n) {
					return subBtn.elementAt(i).bString;
				}
			}
			return null;
		}

		public class SubBtn {
			public int bNum;
			public String bString;

			public SubBtn(String s, int i) {
				bNum = i;
				bString = s;
			}
		}
	}

	public enum MoveDirection {
		LEFT, UP, RIGHT, DOWN
	}

	public class TouchPosition {
		public float x;
		public float y;
		public float dis;
		public float ang;
		public int btnNum;

		public TouchPosition(float x, float y) {
			this.x = x;
			this.y = y;
			dis = getDistance(x, y, boardR, boardR);
			ang = getAngle(x, y, boardR, boardR);
			btnNum = (dis > 50 && dis < 80) ? ((int) ang / 45) : (8);
		}
	}

	// used to get the position of the text on padButton
	private float[] getTextPosition(float angle) {
		float[] pos = new float[2];
		double phi = Math.toRadians(angle);
		pos[0] = (float) Math.cos(phi) * (boardR + navR) / 2 + boardR;
		pos[1] = (float) Math.sin(phi) * (boardR + navR) / 2 + boardR
				+ textSize * 0.37f;
		return pos;
	}

	private void init() {
		setBackgroundColor(0xffcccccc);
		btnPaintNormal.setColor(0xff888888);
		btnPaintDown.setColor(0xffff0000);
		btnPaintCast.setColor(0xffaaaaaa);
		textPaint.setColor(0xffffffff);
		textPaint.setTextAlign(Paint.Align.CENTER);
		textPaint.setTextSize(textSize);
		btnOnCast = 8;
		btnOnTouch = 8;
		btnGroup[0] = new BtnGroup("h", 1, "i", 0, "j", 7);
		btnGroup[1] = new BtnGroup("k", 2, "l", 1, "m", 0);
		btnGroup[2] = new BtnGroup("n", 3, "o", 2, "p", 1);
		btnGroup[3] = new BtnGroup("q", 4, "r", 3, "s", 2);
		btnGroup[4] = new BtnGroup("t", 3, "u", 4, "v", 5);
		btnGroup[5] = new BtnGroup("w", 4, "x", 5, "y", 6, "z", 7);
		btnGroup[6] = new BtnGroup("a", 5, "b", 6, "c", 7, "d", 0);
		btnGroup[7] = new BtnGroup("e", 6, "f", 7, "g", 0);
		Path p = new Path();
		RectF r = new RectF(0, 0, 2 * boardR, 2 * boardR);
		RectF r2 = new RectF(boardR - navR, boardR - navR, boardR + navR,
				boardR + navR);
		float[] textPos;
		PadButton pBtn;
		for (int i = 0; i < 8; i++) {
			padButtons.add(new Vector<PadButton>());
			for (int j = 0; j < btnGroup[i].subBtn.size(); j++) {
				int bn = btnGroup[i].subBtn.elementAt(j).bNum;
				String bs = btnGroup[i].subBtn.elementAt(j).bString;
				p.reset();
				p.arcTo(r, bn * 45 + btnPad, 45 - 2 * btnPad, true);
				p.arcTo(r2, (bn + 1) * 45 - btnPad, -45 + 2 * btnPad);
				p.close();
				textPos = getTextPosition((float) (22.5 + bn * 45));
				pBtn = new PadButton(p, bn, textPos[0], textPos[1]);
				pBtn.setText(bs);
				padButtons.elementAt(i).add(pBtn);
			}
		}
		padButtons.add(new Vector<PadButton>());
		for (int i = 0; i < 8; i++) {
			p.reset();
			p.arcTo(r, i * 45 + btnPad, 45 - 2 * btnPad, true);
			p.arcTo(r2, (i + 1) * 45 - btnPad, -45 + 2 * btnPad);
			p.close();
			textPos = getTextPosition((float) (22.5 + i * 45));
			pBtn = new PadButton(p, i, textPos[0], textPos[1]);
			String bs = new String();
			for (int j = 0; j < btnGroup[i].subBtn.size(); j++) {
				bs += btnGroup[i].subBtn.elementAt(j).bString;
			}
			pBtn.setText(bs);
			padButtons.elementAt(8).add(pBtn);
		}

	}

	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		Log.d(DEBUG_TAG, "testview ondraw start");
		PadButton pBtn;
		for (int i = 0; i < padButtons.elementAt(btnOnCast).size(); i++) {
			pBtn = padButtons.elementAt(btnOnCast).elementAt(i);
			canvas.drawPath(
					pBtn.path,
					(btnOnCast >= 0 && btnOnCast <= 7 && pBtn.num == btnOnTouch) ? btnPaintDown
							: btnPaintNormal);
			canvas.drawText(pBtn.text, pBtn.textPos.x, pBtn.textPos.y,
					textPaint);
		}
		Log.d(DEBUG_TAG, "testview ondraw end");
	}

	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		Log.d(DEBUG_TAG, "testview onmeasure start");
		int desiredWidth = ((int) boardR) * 2;
		int desiredHeight = ((int) boardR) * 2;

		int widthMode = MeasureSpec.getMode(widthMeasureSpec);
		int widthSize = MeasureSpec.getSize(widthMeasureSpec);
		int heightMode = MeasureSpec.getMode(heightMeasureSpec);
		int heightSize = MeasureSpec.getSize(heightMeasureSpec);

		int width;
		int height;

		// Measure Width
		if (widthMode == MeasureSpec.EXACTLY) {
			// Must be this size
			width = widthSize;
		} else if (widthMode == MeasureSpec.AT_MOST) {
			// Can't be bigger than...
			width = Math.min(desiredWidth, widthSize);
		} else {
			// Be whatever you want
			width = desiredWidth;
		}

		// Measure Height
		if (heightMode == MeasureSpec.EXACTLY) {
			// Must be this size
			height = heightSize;
		} else if (heightMode == MeasureSpec.AT_MOST) {
			// Can't be bigger than...
			height = Math.min(desiredHeight, heightSize);
		} else {
			// Be whatever you want
			height = desiredHeight;
		}

		// MUST CALL THIS
		setMeasuredDimension(width, height);

		Log.d(DEBUG_TAG, "testview onmeasure end");
	}

}
