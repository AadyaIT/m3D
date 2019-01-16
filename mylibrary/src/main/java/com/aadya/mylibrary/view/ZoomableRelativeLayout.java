package com.aadya.mylibrary.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.widget.RelativeLayout;

public class ZoomableRelativeLayout extends RelativeLayout {
    float mScaleFactor = 1;
    float mPivotX;
    float mPivotY;

    public ZoomableRelativeLayout(Context context) {
        super(context);
        // TODO Auto-generated constructor stub
    }

    public ZoomableRelativeLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        // TODO Auto-generated constructor stub
    }

    public ZoomableRelativeLayout(Context context, AttributeSet attrs,
                                  int defStyle) {
        super(context, attrs, defStyle);
        // TODO Auto-generated constructor stub
    }


    @SuppressLint("WrongConstant")
    protected void dispatchDraw(Canvas canvas) {
        int flags = Canvas.MATRIX_SAVE_FLAG | Canvas.CLIP_SAVE_FLAG | Canvas.HAS_ALPHA_LAYER_SAVE_FLAG | Canvas.FULL_COLOR_LAYER_SAVE_FLAG | Canvas.CLIP_TO_LAYER_SAVE_FLAG;
        canvas.save(flags);//Canvas.MATRIX_SAVE_FLAG
        canvas.scale(mScaleFactor, mScaleFactor, mPivotX, mPivotY);
        super.dispatchDraw(canvas);
        canvas.restore();
    }

    public void scale(float scaleFactor, float pivotX, float pivotY) {
        mScaleFactor = scaleFactor;
        mPivotX = pivotX;
        mPivotY = pivotY;
        this.invalidate();
    }

    public void restore() {
        mScaleFactor = 1;
        this.invalidate();
    }

}
