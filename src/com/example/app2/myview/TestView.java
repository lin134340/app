package com.example.app2.myview;

import java.util.Vector;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import com.example.app2.R;

public class TestView extends View {
	private final String DEBUG_TAG = "mytest";
	private float boardR = 10.0f;
	private float navR = 7.0f;
	private float btnPad = 0.1f;
	private Paint btnPaintGray = new Paint();
	private Paint btnPaintRed = new Paint();
	// private Paint mNavPaint = new Paint();
	// private Paint mLinePaint = new Paint();
	private Vector<Path> btns = new Vector<Path>();
	private int btnOnTouch = 8;

	public TestView(Context context, AttributeSet attrs) {
		super(context, attrs);
		Log.d(DEBUG_TAG, "testview constructor start");
		TypedArray a = context.getTheme().obtainStyledAttributes(attrs,
				R.styleable.TestView, 0, 0);
		try {
			boardR = a.getFloat(R.styleable.TestView_boardR, 10.0f);
			navR = a.getFloat(R.styleable.TestView_navR, 7.0f);
			btnPad = a.getFloat(R.styleable.TestView_btnPad, 0.1f);
		} finally {
			a.recycle();
		}
		init();
		Log.d(DEBUG_TAG, "testview constructor end");
	}

	// if btnOnTouch is between 0 to 7, one of the eight buttons is being
	// pressed down
	// else none button is being pressed down
	public void setbtnOnTouch(int b) {
		btnOnTouch = b;
		invalidate();
	}

	public float getDistance(float x, float y) {
		return (float) Math.sqrt((x - boardR) * (x - boardR) + (y - boardR)
				* (y - boardR));
	}

	public float getAngle(float x, float y) {
		float angle = (float) Math
				.toDegrees(Math.atan2(y - boardR, x - boardR));
		if (angle < 0) {
			angle += 360;
		}
		return angle;
	}

	private void init() {
		setBackgroundColor(Color.LTGRAY);
		btnPaintGray.setColor(Color.GRAY);
		btnPaintRed.setColor(Color.RED);
		btnOnTouch = 8;
		// mNavPaint.setColor(Color.LTGRAY);
		// mLinePaint.setColor(Color.LTGRAY);
		// mLinePaint.setStyle(Paint.Style.STROKE);

		Path p = new Path();
		RectF r = new RectF(0, 0, 2 * boardR, 2 * boardR);
		RectF r2 = new RectF(boardR - navR, boardR - navR, boardR + navR,
				boardR + navR);
		for (int i = 0; i < 8; i++) {
			p.reset();
			p.arcTo(r, i * 45 + btnPad, 45 - 2 * btnPad, true);
			p.arcTo(r2, (i + 1) * 45 - btnPad, -45 + 2 * btnPad);
			p.close();
			btns.add(new Path(p));
		}
	}

	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		Log.d(DEBUG_TAG, "testview ondraw start");
		for (int i = 0; i < btns.size(); i++) {
			canvas.drawPath(btns.elementAt(i), (i == btnOnTouch) ? btnPaintRed
					: btnPaintGray);
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

	public boolean onTouchEvent(MotionEvent ev) {
		int a = ev.getActionMasked();
		// the x,y value is the relative value to testview
		int x = (int) ev.getX();
		int y = (int) ev.getY();
		Log.d(DEBUG_TAG,
				"testview a:" + Integer.toString(a) + " x:"
						+ Integer.toString(x) + " y:" + Integer.toString(y));
		float dis = getDistance(x, y);
		float ang = getAngle(x, y);
		int btnDown = (dis > 50 && dis < 80) ? ((int) ang / 45) : (8);
		Log.d(DEBUG_TAG, "testview dis:" + Float.toString(dis) + " ang:"
				+ Float.toString(ang) + " btnDown:" + Integer.toString(btnDown));

		if (a == MotionEvent.ACTION_DOWN || a == MotionEvent.ACTION_MOVE) {
			if (btnOnTouch != btnDown) {
				setbtnOnTouch(btnDown);
			}
		} else if (a == MotionEvent.ACTION_UP) {
			setbtnOnTouch(8);
		}

		return true;
	}
}
