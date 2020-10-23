package com.example.colornote.View;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.widget.EditText;

public class LinedEditText extends androidx.appcompat.widget.AppCompatEditText {
    Rect rect;
    Paint paint;

    public LinedEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        rect = new Rect();
        paint = new Paint();
        paint.setStyle(Paint.Style.STROKE);
        paint.setColor(0x800000FF);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        int height = getHeight();
        int line_height = getLineHeight();
        int count = height / line_height;
        if (getLineCount() > count) {
            count = getLineCount();
        }

        Rect r = rect;
        Paint p = paint;
        int baseline = getLineBounds(0, r);
        for (int i = 0; i < count; i++) {
            canvas.drawLine(r.left, baseline + 2, r.right, baseline + 2, p);
            baseline += getLineHeight();
        }
        super.onDraw(canvas);
    }
}
