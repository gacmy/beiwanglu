package com.gac.beiwanglu.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.PathEffect;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.EditText;

/**
 * Created by Administrator on 2015/12/31.
 */
public class CustomEditText extends EditText{
    private Rect mRect;
    private Paint mPaint;
    public CustomEditText(Context context) {
       this(context, null);
    }

    public CustomEditText(Context context, AttributeSet attrs) {
        super(context,attrs);
        mRect = new Rect();
        mPaint = new Paint();
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(2);
        mPaint.setColor(Color.GRAY);
        PathEffect effects = new DashPathEffect(new float[] { 5, 5, 5, 5}, 1);
        mPaint.setPathEffect(effects);


    }


    @Override
    public void setBackgroundResource(int resid) {
        super.setBackgroundResource(resid);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        // Gets the number of lines of text in the View.
        int count = getLineCount();

        // Gets the global Rect and Paint objects
        Rect r = mRect;
        Paint paint = mPaint;


        for (int i = 0; i < count; i++) {

            // Gets the baseline coordinates for the current line of text
            int baseline = getLineBounds(i, r);

                /*
                 * Draws a line in the background from the left of the rectangle to the right,
                 * at a vertical position one dip below the baseline, using the "paint" object
                 * for details.
                 */
            Log.d("gac", "baseLine:" + baseline);
            canvas.drawLine(r.left, baseline + 10, r.right, baseline + 10, paint);
        }


        super.onDraw(canvas);
    }


}
