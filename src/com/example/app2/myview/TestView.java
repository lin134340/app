package com.example.app2.myview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
//import android.util.Log;
import android.view.View;

import com.example.app2.R;

public class TestView extends View {
	//private final String DEBUG_TAG = "mytest";
    private float width = 10.0f;
    private float height = 10.0f;
    private Paint mRecPaint = new Paint();
    //private Paint mRecPaintr;
    //private Paint mRecPaintb;

    public TestView(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray a = context.getTheme().obtainStyledAttributes(
                attrs,
                R.styleable.TestView,
                0, 0);
        try {
        	width = a.getFloat(R.styleable.TestView_width, 10.0f);
        	height = a.getFloat(R.styleable.TestView_height, 10.0f);
        	//width = attrs.getAttributeFloatValue("http://schemas.android.com/apk/res/android","layout_width", 80.0f);
        	//height = attrs.getAttributeFloatValue("http://schemas.android.com/apk/res/android","layout_height", 10.0f);
        } finally {
            a.recycle();
        }
        init();
    }
    
    public void setColor(int c) {
    	mRecPaint.setColor(c);
    	invalidate();
    }
    
    public int getColor() {
    	return mRecPaint.getColor();
    }

    private void init() {
    	setColor(Color.BLUE);
    	//Log.d(DEBUG_TAG, "id:"+Integer.toHexString(getId())+" h:"+Float.toString(getHeight())+" w:"+Float.toString(getWidth()));
        //mRecPaintr = new Paint();
        //mRecPaintr.setColor(Color.RED);
        //mRecPaintb = new Paint();
        //mRecPaintb.setColor(Color.BLUE);
    }

    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawRect(0, 0, width, height, mRecPaint);
    }

    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int desiredWidth = (int)width;
        int desiredHeight = (int)height;
        
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);

        int width;
        int height;

        //Measure Width
        if (widthMode == MeasureSpec.EXACTLY) {
            //Must be this size
            width = widthSize;
        } else if (widthMode == MeasureSpec.AT_MOST) {
            //Can't be bigger than...
            width = Math.min(desiredWidth, widthSize);
        } else {
            //Be whatever you want
            width = desiredWidth;
        }
        
        //Measure Height
        if (heightMode == MeasureSpec.EXACTLY) {
            //Must be this size
            height = heightSize;
        } else if (heightMode == MeasureSpec.AT_MOST) {
            //Can't be bigger than...
            height = Math.min(desiredHeight, heightSize);
        } else {
            //Be whatever you want
            height = desiredHeight;
        }
        
        //MUST CALL THIS
        setMeasuredDimension(width, height);

    }

}

