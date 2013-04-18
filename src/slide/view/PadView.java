package slide.view;

import java.util.Vector;

import slide.data.BtnDefine;
import slide.data.BtnDisplay;
import slide.data.CornerBtn;
import slide.data.Display;
import slide.data.SetDisplay;
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
	private float corPad = 30.0f;
	private Paint btnPaintNormal = new Paint();
	private Paint btnPaintDown = new Paint();
	private Paint btnPaintCast = new Paint();
	private Paint textPaint = new Paint();
	private Paint cornerEnablePaint = new Paint();
	private Paint cornerUnenablePaint = new Paint();
	public Display disp = new Display();
	public TouchPosition ptPos;
	public TouchPosition tPos;
	private int setN;
	// private boolean chineseEnalbe;
	// private boolean charEnalbe;
	private boolean upperEnable;
	private boolean numberEnable;
	private int btnOnCast;
	private int btnOnTouch;

	public PadView(Context context, AttributeSet attrs) {
		super(context, attrs);
		Log.d(DEBUG_TAG, "padview constructor start");
		TypedArray a = context.getTheme().obtainStyledAttributes(attrs,
				R.styleable.PadView, 0, 0);
		try {
			boardR = a.getFloat(R.styleable.PadView_boardR, 80.0f);
			navR = a.getFloat(R.styleable.PadView_navR, 50.0f);
			btnPad = a.getFloat(R.styleable.PadView_btnPad, 1.0f);
			textSize = a.getFloat(R.styleable.PadView_textSize, 8.0f);
			corPad = a.getFloat(R.styleable.PadView_corPad, 30.0f);
		} finally {
			a.recycle();
		}
		init();
		Log.d(DEBUG_TAG, "padview constructor end");
	}

	public int getSetN() {
		return setN;
	}

	public void setUpperEnalbe(boolean b) {
		upperEnable = b;
		resetDisplayGroup();
	}

	public boolean getUpperEnalbe() {
		return upperEnable;
	}

	public void setNumerEnalbe(boolean b) {
		numberEnable = b;
		resetDisplayGroup();
	}

	public boolean getNumberEnalbe() {
		return numberEnable;
	}

	// If btnOnTouch is between 0 to setDisN-1, one of the setN buttons is being
	// pressed down.
	// If btnOnTouch is setDisN, none button is being pressed down.
	public void setBtnOnTouch(int b) {
		btnOnTouch = b;
		invalidate();
	}

	public int getBtnOnTouch() {
		return btnOnTouch;
	}

	// If btnOnCast is between 0 to setDisN-1, one of the setN buttons is
	// casting.
	// If btnOnCast is setDisN, none button is casting.
	public void setBtnOnCast(int b) {
		btnOnCast = b;
		if (btnOnCast == setN) {
			invalidate();
		}
	}

	public int getBtnOnCast() {
		return btnOnCast;
	}

	// get the distance between point1 and point2
	public float getDistance(float x1, float y1, float x2, float y2) {
		return (float) Math.sqrt((x1 - x2) * (x1 - x2) + (y1 - y2) * (y1 - y2));
	}

	// get the angle from point1 to point2
	public float getAngle(float x1, float y1, float x2, float y2) {
		// float toPoint = toCenter ? boardR : 0;
		float angle = (float) Math.toDegrees(Math.atan2(y1 - y2, x1 - x2));
		if (angle < 0) {
			angle += 360;
		}
		return angle;
	}

	// get the direction from ptPos to tPos
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

	public enum MoveDirection {
		LEFT, UP, RIGHT, DOWN
	}

	public class TouchPosition {
		public float x;
		public float y;
		public float dis;
		public float ang;
		public int btnId;
		public String btnText;
		public int corner;

		public TouchPosition(float x, float y) {
			this.x = x;
			this.y = y;
			dis = getDistance(x, y, boardR, boardR);
			ang = getAngle(x, y, boardR, boardR);
			float btnAng = (float) 360 / setN;
			float offset = (float) ((setN % 2 == 0) ? (-90)
					: (-90 - btnAng / 2));
			btnId = (dis > navR && dis < boardR) ? (((int) ((ang - offset) / btnAng)) % setN)
					: setN;
			if (disp.disGroup.equals(Display.DisGroup.LetterL)) {
				btnText = disp.letterLowerDisplay.sets.elementAt(btnOnCast)
						.getTextById(btnId);
			} else if (disp.disGroup.equals(Display.DisGroup.Number)) {
				btnText = disp.numberDisplay.sets.elementAt(0).getTextById(
						btnId);
			} else if (disp.disGroup.equals(Display.DisGroup.LetterU)) {
				btnText = disp.letterUpperDisplay.sets.elementAt(btnOnCast)
						.getTextById(btnId);
			} else {
				btnText = "";
			}
			corner = (dis > boardR && (ang % 90) > corPad && (ang % 90) < (90 - corPad)) ? ((int) ang / 90 + 1)
					: 0;
		}
	}

	//
	private void setDisplayGroup() {
		if (numberEnable == true) {
			disp.disGroup = Display.DisGroup.Number;
			setN = disp.numberDisplay.setN;
		} else if (upperEnable == true) {
			disp.disGroup = Display.DisGroup.LetterU;
			setN = disp.letterUpperDisplay.setN;
		} else {
			disp.disGroup = Display.DisGroup.LetterL;
			setN = disp.letterLowerDisplay.setN;
		}
	}

	private void resetDisplayGroup() {
		setDisplayGroup();
		btnOnCast = setN;
		btnOnTouch = setN;
		invalidate();
	}

	// btnN must >= 1
	private int calId(int sn, int bn, int setN, int btnN) {
		return (sn + (bn - (btnN - 1) / 2) + setN) % setN;
	}

	private Path calPath(int id, float btnAng, float offset) {
		Path p = new Path();
		RectF rb = new RectF(0, 0, 2 * boardR, 2 * boardR);
		RectF rv = new RectF(boardR - navR, boardR - navR, boardR + navR,
				boardR + navR);
		p.arcTo(rb, id * btnAng + offset + btnPad, btnAng - 2 * btnPad, true);
		p.arcTo(rv, (id + 1) * btnAng + offset - btnPad, -btnAng + 2 * btnPad);
		p.close();
		return p;
	}

	private float[] calTextPosition(int id, float btnAng, float offset) {
		float[] pos = new float[2];
		double angle = ((double) id + 0.5) * btnAng + offset;
		double phi = Math.toRadians(angle);
		pos[0] = (float) Math.cos(phi) * (boardR + navR) / 2 + boardR;
		pos[1] = (float) Math.sin(phi) * (boardR + navR) / 2 + boardR
				+ textSize * 0.37f;
		return pos;
	}

	// setInit constructs a BtnDisplay-type Vector used in a setDisplay class
	// this Vector contains buttons showed in a cast view
	private Vector<BtnDisplay> setInit(int sn, int setN, int btnN, String[] btn) {
		Vector<BtnDisplay> btns = new Vector<BtnDisplay>();
		for (int bn = 0; bn < btnN; bn++) {
			int id = calId(sn, bn, setN, btnN);
			float btnAng = (float) 360 / setN;
			float offset = (float) ((setN % 2 == 0) ? (-90)
					: (-90 - btnAng / 2));
			btns.add(new BtnDisplay(id, calPath(id, btnAng, offset), btn[bn],
					calTextPosition(id, btnAng, offset)));
		}
		return btns;
	}

	// setInit constructs a BtnDisplay-type Vector used in a setDisplay class
	// this Vector contains buttons showed in a non-cast view
	private Vector<BtnDisplay> groupSetInit(int setN, int[] btnN, String[][] btn) {
		Vector<BtnDisplay> btns = new Vector<BtnDisplay>();
		for (int sn = 0; sn < setN; sn++) {
			float btnAng = (float) 360 / setN;
			float offset = (float) ((setN % 2 == 0) ? (-90)
					: (-90 - btnAng / 2));
			String s = "";
			for (int bn = 0; bn < btnN[sn]; bn++) {
				s += btn[sn][bn];
			}
			btns.add(new BtnDisplay(sn, calPath(sn, btnAng, offset), s,
					calTextPosition(sn, btnAng, offset)));
		}
		return btns;
	}

	private float[] calCornerBtnPosition(float a) {
		float[] pos = new float[2];
		double phi = Math.toRadians(a);
		pos[0] = (float) Math.cos(phi) * boardR * 1.21f + boardR;
		pos[1] = (float) Math.sin(phi) * boardR * 1.21f + boardR + textSize
				* 0.37f * ((a < 180) ? 1 : -1);
		// Log.d(DEBUG_TAG, "x:" + pos[0] + " y:" + pos[1]);
		return pos;
	}

	private void DisplayInit() {
		// lower letter group
		for (int sn = 0; sn < BtnDefine.LetterLowerGroup.setN; sn++) {
			disp.letterLowerDisplay.sets.add(new SetDisplay(setInit(sn,
					BtnDefine.LetterLowerGroup.setN,
					BtnDefine.LetterLowerGroup.btnN[sn],
					BtnDefine.LetterLowerGroup.btn[sn])));
		}
		disp.letterLowerDisplay.sets.add(new SetDisplay(
				groupSetInit(BtnDefine.LetterLowerGroup.setN,
						BtnDefine.LetterLowerGroup.btnN,
						BtnDefine.LetterLowerGroup.btn)));
		disp.letterLowerDisplay.setN = BtnDefine.LetterLowerGroup.setN;

		// upper letter group
		for (int sn = 0; sn < BtnDefine.LetterUpperGroup.setN; sn++) {
			disp.letterUpperDisplay.sets.add(new SetDisplay(setInit(sn,
					BtnDefine.LetterUpperGroup.setN,
					BtnDefine.LetterUpperGroup.btnN[sn],
					BtnDefine.LetterUpperGroup.btn[sn])));
		}
		disp.letterUpperDisplay.sets.add(new SetDisplay(
				groupSetInit(BtnDefine.LetterUpperGroup.setN,
						BtnDefine.LetterUpperGroup.btnN,
						BtnDefine.LetterUpperGroup.btn)));
		disp.letterUpperDisplay.setN = BtnDefine.LetterUpperGroup.setN;

		// number group
		disp.numberDisplay.sets.add(new SetDisplay(groupSetInit(
				BtnDefine.NumberGroup.setN, BtnDefine.NumberGroup.btnN,
				BtnDefine.NumberGroup.btn)));
		disp.numberDisplay.setN = BtnDefine.NumberGroup.setN;

		disp.cornerBtnRD = new CornerBtn("123", calCornerBtnPosition(45));
		disp.cornerBtnLD = new CornerBtn("S", calCornerBtnPosition(135));
		disp.cornerBtnRU = new CornerBtn("¡û", calCornerBtnPosition(315));
		setDisplayGroup();
	}

	private void init() {
		setBackgroundColor(0xffcccccc);
		btnPaintNormal.setColor(0xff888888);
		btnPaintDown.setColor(0xffff0000);
		btnPaintCast.setColor(0xffaaaaaa);
		textPaint.setColor(0xffffffff);
		textPaint.setTextAlign(Paint.Align.CENTER);
		textPaint.setTextSize(textSize);
		cornerEnablePaint.setColor(0xff000000);
		cornerEnablePaint.setTextAlign(Paint.Align.CENTER);
		cornerEnablePaint.setTextSize(textSize);
		cornerUnenablePaint.setColor(0xff999999);
		cornerUnenablePaint.setTextAlign(Paint.Align.CENTER);
		cornerUnenablePaint.setTextSize(textSize);
		upperEnable = false;
		numberEnable = false;
		DisplayInit();
		btnOnCast = setN;
		btnOnTouch = setN;
	}

	private void drawSet(Vector<SetDisplay> sets, Canvas c) {
		BtnDisplay btn;
		int setId = disp.disGroup.equals(Display.DisGroup.Number) ? 0
				: btnOnCast;
		for (int i = 0; i < sets.elementAt(setId).btnN; i++) {
			btn = sets.elementAt(setId).btns.elementAt(i);
			c.drawPath(
					btn.btnPath,
					(btnOnCast >= 0 && btnOnCast <= (setN - 1) && btnOnTouch == btn.btnId) ? btnPaintDown
							: btnPaintNormal);
			c.drawText(btn.btnText, btn.textPos.x, btn.textPos.y, textPaint);
		}
	}

	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		Log.d(DEBUG_TAG, "padview ondraw start");
		Log.d(DEBUG_TAG, "btnoncast:" + getBtnOnCast() + " btnontouch:"
				+ getBtnOnTouch());
		if (disp.disGroup.equals(Display.DisGroup.Number)) {
			drawSet(disp.numberDisplay.sets, canvas);
		} else if (disp.disGroup.equals(Display.DisGroup.LetterL)) {
			drawSet(disp.letterLowerDisplay.sets, canvas);
		} else if (disp.disGroup.equals(Display.DisGroup.LetterU)) {
			drawSet(disp.letterUpperDisplay.sets, canvas);
		}
		canvas.drawText(disp.cornerBtnRD.btnText, disp.cornerBtnRD.textPos.x,
				disp.cornerBtnRD.textPos.y,
				(numberEnable == true) ? cornerEnablePaint
						: cornerUnenablePaint);
		canvas.drawText(disp.cornerBtnLD.btnText, disp.cornerBtnLD.textPos.x,
				disp.cornerBtnLD.textPos.y,
				(upperEnable == true) ? cornerEnablePaint : cornerUnenablePaint);
		canvas.drawText(disp.cornerBtnRU.btnText, disp.cornerBtnRU.textPos.x,
				disp.cornerBtnRU.textPos.y, cornerEnablePaint);
		Log.d(DEBUG_TAG, "padview ondraw end");
	}

	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		Log.d(DEBUG_TAG, "padview onmeasure start");
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

		Log.d(DEBUG_TAG, "padview onmeasure end");
	}

}
