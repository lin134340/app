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
import android.view.View;

import com.example.app2.R;

public class TestView extends View {
	private final String DEBUG_TAG = "mytest";
	private float boardR = 80.0f;
	private float navR = 50.0f;
	private float btnPad = 1.0f;
	private float textSize = 8.0f;
	private Paint btnPaintGray = new Paint();
	private Paint btnPaintRed = new Paint();
	private Paint textPaint = new Paint();
	private Vector<PadButton> padButton = new Vector<PadButton>();
	private int btnOnTouch = 8;

	public TestView(Context context, AttributeSet attrs) {
		super(context, attrs);
		Log.d(DEBUG_TAG, "testview constructor start");
		TypedArray a = context.getTheme().obtainStyledAttributes(attrs,
				R.styleable.TestView, 0, 0);
		try {
			boardR = a.getFloat(R.styleable.TestView_boardR, 80.0f);
			navR = a.getFloat(R.styleable.TestView_navR, 50.0f);
			btnPad = a.getFloat(R.styleable.TestView_btnPad, 1.0f);
			textSize = a.getFloat(R.styleable.TestView_textSize, 8.0f);
		} finally {
			a.recycle();
		}
		init();
		Log.d(DEBUG_TAG, "testview constructor end");
	}

	// if btnOnTouch is between 0 to 7, one of the eight buttons is being
	// pressed down
	// else none button is being pressed down
	public void setBtnOnTouch(int b) {
		btnOnTouch = b;
		invalidate();
	}

	public int getBtnOnTouch() {
		return btnOnTouch;
	}

	// get the distance between a point and the pad center
	public float getDistance(float x, float y) {
		return (float) Math.sqrt((x - boardR) * (x - boardR) + (y - boardR)
				* (y - boardR));
	}

	// get the angle of a point from pad center
	public float getAngle(float x, float y) {
		float angle = (float) Math
				.toDegrees(Math.atan2(y - boardR, x - boardR));
		if (angle < 0) {
			angle += 360;
		}
		return angle;
	}

	// PadButton defines one of eight buttons
	private class PadButton {
		public Path path;
		public TextPos textPos = new TextPos();
		public String text;

		public class TextPos {
			public float x;
			public float y;
		}

		public PadButton(Path p, float tPosX, float tPosY) {
			path = new Path(p);
			textPos.x = tPosX;
			textPos.y = tPosY;
		}

		public void setText(String s) {
			text = s;
		}
	}

	// used to get the text position
	private float[] getTextPosition(float angle) {
		float[] pos = new float[2];
		double phi = Math.toRadians(angle);
		pos[0] = (float) Math.cos(phi) * (boardR + navR) / 2 + boardR;
		pos[1] = (float) Math.sin(phi) * (boardR + navR) / 2 + boardR
				+ textSize * 0.37f;
		return pos;
	}

	private void init() {
		setBackgroundColor(Color.LTGRAY);
		btnPaintGray.setColor(Color.GRAY);
		btnPaintRed.setColor(Color.RED);
		textPaint.setColor(Color.WHITE);
		textPaint.setTextAlign(Paint.Align.CENTER);
		textPaint.setTextSize(textSize);
		btnOnTouch = 8;

		Path p = new Path();
		RectF r = new RectF(0, 0, 2 * boardR, 2 * boardR);
		RectF r2 = new RectF(boardR - navR, boardR - navR, boardR + navR,
				boardR + navR);
		float[] tPos;
		PadButton pBtn;
		for (int i = 0; i < 8; i++) {
			p.reset();
			p.arcTo(r, i * 45 + btnPad, 45 - 2 * btnPad, true);
			p.arcTo(r2, (i + 1) * 45 - btnPad, -45 + 2 * btnPad);
			p.close();
			tPos = getTextPosition((float) (22.5 + i * 45));
			pBtn = new PadButton(p, tPos[0], tPos[1]);
			pBtn.setText(Integer.toString(i));
			padButton.add(pBtn);
		}
	}

	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		Log.d(DEBUG_TAG, "testview ondraw start");
		PadButton pBtn;
		for (int i = 0; i < padButton.size(); i++) {
			pBtn = padButton.elementAt(i);
			canvas.drawPath(pBtn.path, (i == btnOnTouch) ? btnPaintRed
					: btnPaintGray);
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
